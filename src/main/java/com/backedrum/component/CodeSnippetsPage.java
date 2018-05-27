package com.backedrum.component;

import com.backedrum.component.util.UiUtils;
import com.backedrum.model.BaseEntity;
import com.backedrum.model.SourceCodeSnippet;
import com.backedrum.service.ItemsService;
import com.googlecode.wicket.jquery.ui.widget.tooltip.TooltipBehavior;
import lombok.val;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
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

        add(UiUtils.constructCodeSnippetsList(form, snippetService, snippets)).setVersioned(false);
        add(new TooltipBehavior());
    }

    public final class SourceCodeSnippetForm extends Form<ValueMap> {

        SourceCodeSnippetForm(String id) {
            super(id, new CompoundPropertyModel<>(new ValueMap()));

            setMarkupId("snippetsForm");

            add(new TextField<String>("title").setType(String.class).setRequired(true));
            add(new TextField<String>("tag").setType(String.class).add(new TagValidator()));
            add(new TextArea<String>("sourceCode").setType(String.class).setRequired(true));
        }

        @Override
        protected void onSubmit() {
            ValueMap values = getModelObject();

            val snippet = SourceCodeSnippet.builder()
                    .dateTime(LocalDateTime.now())
                    .title((String) values.get("title"))
                    .tag(values.get("tag") != null ? (String) values.get("tag") : null)
                    .sourceCode((String) values.get("sourceCode")).build();
            snippetService.saveItem(snippet);

            values.put("title", "");
            values.put("tag", "");
            values.put("sourceCode", "");

            setResponsePage(CodeSnippetsPage.class);
        }
    }
}
