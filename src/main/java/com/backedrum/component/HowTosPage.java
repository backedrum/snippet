package com.backedrum.component;

import com.backedrum.component.util.UiUtils;
import com.backedrum.model.BaseEntity;
import com.backedrum.model.HowTo;
import com.backedrum.service.ItemsService;
import com.googlecode.wicket.jquery.ui.widget.tooltip.TooltipBehavior;
import lombok.val;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableMultiLineLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.value.ValueMap;
import org.apache.wicket.validation.INullAcceptingValidator;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.validator.StringValidator;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HowTosPage extends BasePage implements AuthenticatedPage {

    private static final long serialVersionUID = 1L;

    @SpringBean(name = "howtoService")
    private ItemsService<HowTo> howtoService;

    /**
     * Constructor that is invoked when page is invoked without a session.
     */
    public HowTosPage() {
        val form = new HowToForm("howtosForm");

        add(form);

        IModel<List<HowTo>> howToList = (IModel<List<HowTo>>) () -> howtoService.retrieveAllItems()
                .stream().sorted(Comparator.comparing(BaseEntity::getTitle)).collect(Collectors.toList());

        val listContainer = new WebMarkupContainer("howtosContainer");
        listContainer.setOutputMarkupId(true);
        listContainer.add(new PropertyListView<>("howtos", howToList) {
            @Override
            protected void populateItem(ListItem<HowTo> listItem) {
                listItem.add(new Label("dateTime"));

                val removeLink = new AjaxSubmitLink("removeHowTo", form) {
                    @Override
                    public void onSubmit(AjaxRequestTarget target) {
                        howtoService.removeItem(listItem.getModelObject().getId());
                        target.add(listContainer);
                    }
                };
                removeLink.setDefaultFormProcessing(false);
                listItem.add(removeLink);

                listItem.add(UiUtils.constructEditableLabel(howtoService, listItem.getModelObject(), "title", listContainer));
                listItem.add(UiUtils.constructEditableLabel(howtoService, listItem.getModelObject(), "tag", listContainer));

                val howToText = new AjaxEditableMultiLineLabel<>("text", new PropertyModel<>(listItem.getModelObject(), "text")) {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target) {
                        val howTo = listItem.getModelObject();
                        howTo.setText(getEditor().getValue());
                        howtoService.saveItem(howTo);

                        super.onSubmit(target);
                        target.add(listContainer);
                    }
                };

                listItem.add(howToText);
            }
        });

        add(new TooltipBehavior());

        add(listContainer).setVersioned(false);
    }

    public final class HowToForm extends Form<ValueMap> {

        HowToForm(String id) {
            super(id, new CompoundPropertyModel<>(new ValueMap()));

            setMarkupId("howtosForm");

            add(new TextField<String>("title").setType(String.class).setRequired(true));
            add(new TextField<String>("tag").setType(String.class).add(new TagValidator()));
            add(new TextArea<String>("text").setType(String.class).setRequired(true));
        }

        @Override
        protected void onSubmit() {
            ValueMap values = getModelObject();

            HowTo howto = HowTo.builder()
                    .dateTime(LocalDateTime.now())
                    .title((String) values.get("title"))
                    .tag(values.get("tag") != null ? (String) values.get("tag") : null)
                    .text((String) values.get("text")).build();
            howtoService.saveItem(howto);

            values.put("title", "");
            values.put("tag", "");
            values.put("text", "");

            setResponsePage(HowTosPage.class);
        }
    }
}
