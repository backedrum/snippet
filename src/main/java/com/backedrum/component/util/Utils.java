package com.backedrum.component.util;

import com.backedrum.model.BaseEntity;
import com.backedrum.service.ItemsService;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableMultiLineLabel;
import org.apache.wicket.model.PropertyModel;

@UtilityClass
public class Utils {

    public static <T extends BaseEntity> AjaxEditableMultiLineLabel constructTitle(ItemsService<T> service, T entity, Component container) {
        val title = new AjaxEditableMultiLineLabel<>("title", new PropertyModel<>(entity, "title")) {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                entity.setTitle(getEditor().getValue());
                service.saveItem(entity);

                super.onSubmit(target);
                target.add(container);
            }
        };

        title.setRows(1);
        title.setCols(128);

        return title;
    }
}
