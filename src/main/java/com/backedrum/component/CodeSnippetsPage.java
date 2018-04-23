package com.backedrum.component;

import com.backedrum.model.Screenshot;
import com.backedrum.model.SourceCodeSnippet;
import com.backedrum.service.ItemsService;
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
import org.apache.wicket.util.value.ValueMap;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

public class CodeSnippetsPage extends BasePage {

	private static final long serialVersionUID = 1L;

	@Qualifier("snippetService")
    @Inject
    private ItemsService<SourceCodeSnippet> snippetService;

    /**
	 * Constructor that is invoked when page is invoked without a session.
	 */
    public CodeSnippetsPage() {
        val form = new SourceCodeSnippetForm("snippetsForm");
        add(form);

        IModel<List<SourceCodeSnippet>> snippets = (IModel<List<SourceCodeSnippet>>) () -> snippetService.retrieveAllItems();

        val listContainer = new WebMarkupContainer("snippetsContainer");
        listContainer.setOutputMarkupId(true);
        listContainer.add(new PropertyListView<SourceCodeSnippet>("snippets", snippets) {
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

                listItem.add(new Label("title"));
                listItem.add(new MultiLineLabel("sourceCode"));
            }
        });

        add(listContainer).setVersioned(false);
    }

    public final class SourceCodeSnippetForm extends Form<ValueMap> {

		SourceCodeSnippetForm(String id) {
			super(id, new CompoundPropertyModel<>(new ValueMap()));

			setMarkupId("snippetsForm");

			add(new TextField<>("title").setType(String.class));
			add(new TextArea<>("sourceCode").setType(String.class));
		}

		@Override
		protected void onSubmit() {
			ValueMap values = getModelObject();

			val snippet = SourceCodeSnippet.builder()
					.dateTime(LocalDateTime.now())
					.title((String) values.get("title"))
					.sourceCode((String) values.get("sourceCode")).build();
			snippetService.addItem(snippet);

			values.put("title", "");
			values.put("sourceCode", "");

			setResponsePage(CodeSnippetsPage.class);
		}
	}
}
