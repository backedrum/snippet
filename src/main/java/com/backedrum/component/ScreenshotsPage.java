package com.backedrum.component;

import com.backedrum.component.util.Utils;
import com.backedrum.model.BaseEntity;
import com.backedrum.model.Image;
import com.backedrum.model.Screenshot;
import com.backedrum.service.ItemsService;
import com.googlecode.wicket.jquery.ui.widget.tooltip.TooltipBehavior;
import lombok.val;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.value.ValueMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ScreenshotsPage extends BasePage implements AuthenticatedPage {
    public static final int MAX_FILE_SIZE = 3072;
    public static final int MAX_UPLOAD_SIZE = 20480;

    public static final int RESIZE_TO_WIDTH = 640;
    public static final int RESIZE_TO_HEIGHT = 480;

    @SpringBean(name = "screenshotService")
    private ItemsService<Screenshot> screenshotService;

    /**
     * Constructor that is invoked when page is invoked without a session.
     */
    public ScreenshotsPage() {
        val form = new ScreenshotForm("screenshotsForm");
        add(form);

        IModel<List<Screenshot>> screenshots = (IModel<List<Screenshot>>) () -> screenshotService.retrieveAllItems()
                .stream().sorted(Comparator.comparing(BaseEntity::getTitle)).collect(Collectors.toList());

        val listContainer = new WebMarkupContainer("screenshotsContainer");
        listContainer.setOutputMarkupId(true);
        listContainer.add(new PropertyListView<>("screenshots", screenshots) {
            @Override
            protected void populateItem(ListItem<Screenshot> listItem) {
                listItem.add(new Label("dateTime"));

                val removeLink = new AjaxSubmitLink("removeScreenshot", form) {
                    @Override
                    public void onSubmit(AjaxRequestTarget target) {
                        screenshotService.removeItem(listItem.getModelObject().getId());
                        target.add(listContainer);
                    }
                };
                removeLink.setDefaultFormProcessing(false);
                listItem.add(removeLink);

                listItem.add(Utils.constructTitle(screenshotService, listItem.getModelObject(), listContainer));

                listItem.add(new PropertyListView<>("images", listItem.getModelObject().getImages()) {
                    @Override
                    protected void populateItem(ListItem<Image> image) {
                        image.add(new NonCachingImage("screenshotImage", new DynamicImageResource() {
                            @Override
                            protected byte[] getImageData(Attributes attributes) {
                                return image.getModelObject().getImage();
                            }
                        }));
                    }
                });

            }
        });

        add(new TooltipBehavior());

        add(listContainer).setVersioned(false);
    }

    public class ScreenshotForm extends Form<ValueMap> {
        private FileUploadField fileUploadField;
        private CheckBox keepOriginal;

        ScreenshotForm(String id) {
            super(id, new CompoundPropertyModel<>(new ValueMap()));

            setMarkupId("screenshotsForm");

            add(new TextField<>("title").setType(String.class).setRequired(true));

            setMultiPart(true);

            add(fileUploadField = new FileUploadField("screenshotUpload"));

            add(keepOriginal = new CheckBox("keepOriginal", Model.of(Boolean.FALSE)));

            setMaxSize(Bytes.kilobytes(MAX_UPLOAD_SIZE));
            setFileMaxSize(Bytes.kilobytes(MAX_FILE_SIZE));
        }

        private byte[] resizeToByteArray(BufferedImage originalImage) throws IOException {
            BufferedImage resizedImage = new BufferedImage(RESIZE_TO_WIDTH, RESIZE_TO_HEIGHT, originalImage.getType());
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, RESIZE_TO_WIDTH, RESIZE_TO_HEIGHT, null);
            g.dispose();

            ByteArrayOutputStream o = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, "jpg", o);

            return o.toByteArray();
        }

        @Override
        protected void onSubmit() {
            ValueMap values = getModelObject();

            val scale = !keepOriginal.getModelObject();

            val images = fileUploadField.getFileUploads().stream()
                    .map(u -> {
                        if (scale && !u.getClientFileName().endsWith(".ico")) {
                            try {
                                BufferedImage original = ImageIO.read(u.getInputStream());
                                return Image.builder().image(resizeToByteArray(original)).build();
                            } catch (IOException e) {
                                ScreenshotsPage.this.error("An exception has been occurred:" + e.getMessage());
                            }
                        }

                        return Image.builder().image(u.getBytes()).build();
                    }).collect(Collectors.toList());

            val screenshot = Screenshot.builder()
                    .dateTime(LocalDateTime.now())
                    .title((String) values.get("title"))
                    .images(images).build();
            screenshotService.saveItem(screenshot);

            values.put("title", "");

            setResponsePage(ScreenshotsPage.class);
        }
    }

}
