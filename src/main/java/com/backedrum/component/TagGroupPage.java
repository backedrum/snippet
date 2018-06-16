package com.backedrum.component;

import com.backedrum.component.util.UiUtils;
import com.backedrum.model.HowTo;
import com.backedrum.model.Screenshot;
import com.backedrum.model.SourceCodeSnippet;
import com.backedrum.service.ItemsService;
import com.backedrum.service.TagService;
import com.googlecode.wicket.jquery.ui.widget.tooltip.TooltipBehavior;
import lombok.val;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.value.ValueMap;

import java.util.List;

public class TagGroupPage extends BasePage implements AuthenticatedPage {

    @SpringBean(name = "tagService")
    private TagService tagService;

    @SpringBean(name = "howtoService")
    private ItemsService<HowTo> howtoService;

    @SpringBean(name = "snippetService")
    private ItemsService<SourceCodeSnippet> snippetService;

    @SpringBean(name = "screenshotService")
    private ItemsService<Screenshot> screenshotService;

    public TagGroupPage(PageParameters parameters) {
        val form = new TagGroupForm("tagGroupForm");

        String tag = parameters.get("tag").toString();

        val howTos = howtoService.retrieveByTag(tag);
        Label count = new Label("howTosCount", "(" + howTos.size() + ")");
        count.setOutputMarkupId(true);
        add(count);
        add(UiUtils.constructHowToList(form, howtoService, (IModel<List<HowTo>>) () -> howTos));

        val snippets = snippetService.retrieveByTag(tag);
        count = new Label("snippetsCount", "(" + snippets.size() + ")");
        count.setOutputMarkupId(true);
        add(count);
        add(UiUtils.constructCodeSnippetsList(form, snippetService, (IModel<List<SourceCodeSnippet>>) () -> snippets));

        val screenshots = screenshotService.retrieveByTag(tag);
        count = new Label("screenshotsCount", "(" + screenshots.size() + ")");
        count.setOutputMarkupId(true);
        add(count);
        add(UiUtils.constructScreenshotsList(form, screenshotService, (IModel<List<Screenshot>>) () -> screenshots));

        add(form);
        add(new TooltipBehavior());
    }

    public final class TagGroupForm extends Form<ValueMap> {

        TagGroupForm(String id) {
            super(id, new CompoundPropertyModel<>(new ValueMap()));
            setMarkupId(id);
        }

        @Override
        protected void onSubmit() {
            setResponsePage(TagsPage.class);
        }
    }

}
