package com.backedrum.component;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.protocol.https.RequireHttps;

@RequireHttps
public abstract class BasePage extends WebPage {
    BasePage() {
        add(new BookmarkablePageLink("snippetsPage", CodeSnippetsPage.class));
        add(new BookmarkablePageLink("screenshotsPage", ScreenshotsPage.class));
        add(new BookmarkablePageLink("howtosPage", HowTosPage.class));
        add(new FeedbackPanel("feedback"));
        add(new Label("footer", "https://github.com/backedrum/snippet"));
    }
}
