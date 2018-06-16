package com.backedrum.component;

import com.backedrum.component.util.UiUtils;
import com.backedrum.model.BaseEntity;
import com.backedrum.model.HowTo;
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

        add(UiUtils.constructHowToList(form, howtoService, howToList, null)).setVersioned(false);
        add(new TooltipBehavior());
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
