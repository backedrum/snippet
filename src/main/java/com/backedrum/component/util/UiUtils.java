package com.backedrum.component.util;

import com.backedrum.model.BaseEntity;
import com.backedrum.model.HowTo;
import com.backedrum.model.Image;
import com.backedrum.model.Screenshot;
import com.backedrum.model.SourceCodeSnippet;
import com.backedrum.service.ItemsService;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableMultiLineLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.util.value.ValueMap;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

@UtilityClass
public class UiUtils {

    public interface WicketConsumer<T> extends Consumer<T>, Serializable {
    }

    public static <T extends BaseEntity> AjaxEditableMultiLineLabel constructEditableLabel(ItemsService<T> service, T entity, String id,
                                                                                           Component container) {
        val label = new AjaxEditableMultiLineLabel<>(id, new PropertyModel<>(entity, id)) {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                service.saveItem(entity);

                super.onSubmit(target);
                target.add(container);
            }
        };

        label.setRows(1);
        label.setCols(128);

        return label;
    }

    /**
     * Constructs howto list of items with editing and removal capabilities.
     *
     * @param form      Main form on the page
     * @param service   Howto service
     * @param howToList List to render
     * @return constructed list of howtos that is ready to be used in UI.
     */
    public static WebMarkupContainer constructHowToList(Form<ValueMap> form, ItemsService<HowTo> service, IModel<List<HowTo>> howToList) {
        val listContainer = new WebMarkupContainer("howtosContainer");
        listContainer.setOutputMarkupId(true);
        listContainer.add(new PropertyListView<>("howtos", howToList) {
            @Override
            protected void populateItem(ListItem<HowTo> listItem) {
                listItem.add(new Label("dateTime"));

                val removeLink = new AjaxSubmitLink("removeHowTo", form) {
                    @Override
                    public void onSubmit(AjaxRequestTarget target) {
                        service.removeItem(listItem.getModelObject().getId());
                        target.add(listContainer);
                    }
                };
                removeLink.setDefaultFormProcessing(false);
                listItem.add(removeLink);

                listItem.add(UiUtils.constructEditableLabel(service, listItem.getModelObject(), "title", listContainer));
                listItem.add(UiUtils.constructEditableLabel(service, listItem.getModelObject(), "tag", listContainer));

                val howToText = new AjaxEditableMultiLineLabel<>("text", new PropertyModel<>(listItem.getModelObject(), "text")) {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target) {
                        val howTo = listItem.getModelObject();
                        howTo.setText(getEditor().getValue());
                        service.saveItem(howTo);

                        super.onSubmit(target);
                        target.add(listContainer);
                    }
                };

                listItem.add(howToText);
            }
        });

        return listContainer;
    }

    /**
     * Constructs list of code snippets to be rendered in UI.
     *
     * @param form     Main form on the page
     * @param service  Source code snippets service
     * @param snippets Snippets to render in the list
     * @return List of source code snippets ready to be used in UI
     */
    public static WebMarkupContainer constructCodeSnippetsList(Form<ValueMap> form,
                                                               ItemsService<SourceCodeSnippet> service,
                                                               IModel<List<SourceCodeSnippet>> snippets) {
        val listContainer = new WebMarkupContainer("snippetsContainer");
        listContainer.setOutputMarkupId(true);
        listContainer.add(new PropertyListView<>("snippets", snippets) {
            @Override
            protected void populateItem(ListItem<SourceCodeSnippet> listItem) {
                listItem.add(new Label("dateTime"));

                val removeLink = new AjaxSubmitLink("removeSnippet", form) {
                    @Override
                    public void onSubmit(AjaxRequestTarget target) {
                        service.removeItem(listItem.getModelObject().getId());
                        target.add(listContainer);
                    }
                };
                removeLink.setDefaultFormProcessing(false);
                listItem.add(removeLink);

                listItem.add(UiUtils.constructEditableLabel(service, listItem.getModelObject(), "title", listContainer));
                listItem.add(UiUtils.constructEditableLabel(service, listItem.getModelObject(), "tag", listContainer));

                listItem.add(new MultiLineLabel("sourceCode"));
            }
        });

        return listContainer;
    }

    public static WebMarkupContainer constructScreenshotsList(Form<ValueMap> form, ItemsService<Screenshot> service, IModel<List<Screenshot>> screenshots) {
        val listContainer = new WebMarkupContainer("screenshotsContainer");
        listContainer.setOutputMarkupId(true);
        listContainer.add(new PropertyListView<>("screenshots", screenshots) {
            @Override
            protected void populateItem(ListItem<Screenshot> listItem) {
                listItem.add(new Label("dateTime"));

                val removeLink = new AjaxSubmitLink("removeScreenshot", form) {
                    @Override
                    public void onSubmit(AjaxRequestTarget target) {
                        service.removeItem(listItem.getModelObject().getId());
                        target.add(listContainer);
                    }
                };
                removeLink.setDefaultFormProcessing(false);
                listItem.add(removeLink);

                listItem.add(UiUtils.constructEditableLabel(service, listItem.getModelObject(), "title", listContainer));
                listItem.add(UiUtils.constructEditableLabel(service, listItem.getModelObject(), "tag", listContainer));

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

        return listContainer;
    }

}
