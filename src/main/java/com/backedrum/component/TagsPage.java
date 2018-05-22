package com.backedrum.component;

import com.backedrum.service.TagService;
import lombok.val;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class TagsPage extends BasePage implements AuthenticatedPage {

    @SpringBean(name = "tagService")
    private TagService tagService;

    public TagsPage() {
        RepeatingView list = new RepeatingView("tags");
        tagService.getAllTags().stream().sorted().forEach(t -> list.add(constructLink(list.newChildId(), t)));
        add(list);
    }

    private Link<String> constructLink(String linkName, String tag) {
        val link = new Link<String>(linkName) {
            @Override
            public void onClick() {
                PageParameters pageParameters = new PageParameters();
                pageParameters.add("tag", tag);
//                setResponsePage(TagGroupPage.class, pageParameters);
            }
        };
        link.setBody(Model.of(tag));

        return link;
    }
}
