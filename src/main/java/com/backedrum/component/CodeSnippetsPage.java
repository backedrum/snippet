package com.backedrum.component;

import com.backedrum.component.util.Utils;
import com.backedrum.model.BaseEntity;
import com.backedrum.model.SourceCodeSnippet;
import com.backedrum.service.ItemsService;
import com.googlecode.wicket.jquery.ui.widget.tooltip.TooltipBehavior;
import lombok.val;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.value.ValueMap;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CodeSnippetsPage extends BasePage implements AuthenticatedPage {

    private static final long serialVersionUID = 1L;

    @SpringBean(name = "snippetService")
    private ItemsService<SourceCodeSnippet> snippetService;

    /**
     * Constructor that is invoked when page is invoked without a session.
     */
    public CodeSnippetsPage() {
        val form = new SourceCodeSnippetForm("snippetsForm");
        add(form);

        IModel<List<SourceCodeSnippet>> snippets = (IModel<List<SourceCodeSnippet>>) () -> snippetService.retrieveAllItems()
                .stream().sorted(Comparator.comparing(BaseEntity::getTitle)).collect(Collectors.toList());

        val listContainer = new WebMarkupContainer("snippetsContainer");
        listContainer.setOutputMarkupId(true);
        listContainer.add(new PropertyListView<>("snippets", snippets) {
            @Override
            protected void populateItem(ListItem<SourceCodeSnippet> listItem) {
                listItem.add(new Label("dateTime"));

                val removeLink = new AjaxSubmitLink("removeSnippet", form) {
                    @Override
                    public void onSubmit(AjaxRequestTarget target) {
                        snippetService.removeItem(listItem.getModelObject().getId());
                        target.add(listContainer);
                    }
                };
                removeLink.setDefaultFormProcessing(false);
                listItem.add(removeLink);

                listItem.add(Utils.constructTitle(snippetService, listItem.getModelObject(), listContainer));

                listItem.add(new MultiLineLabel("sourceCode"));
            }
        });

        add(new TooltipBehavior());

        add(listContainer).setVersioned(false);
    }

    public final class SourceCodeSnippetForm extends Form<ValueMap> {

        SourceCodeSnippetForm(String id) {
            super(id, new CompoundPropertyModel<>(new ValueMap()));

            setMarkupId("snippetsForm");

            add(new TextField<>("title").setType(String.class).setRequired(true));
            add(new TextArea<>("sourceCode").setType(String.class).setRequired(true));
        }

        @Override
        protected void onSubmit() {
            ValueMap values = getModelObject();

            val snippet = SourceCodeSnippet.builder()
                    .dateTime(LocalDateTime.now())
                    .title((String) values.get("title"))
                    .sourceCode((String) values.get("sourceCode")).build();
            snippetService.saveItem(snippet);

            values.put("title", "");
            values.put("sourceCode", "");

            setResponsePage(CodeSnippetsPage.class);
        }
    }
}
