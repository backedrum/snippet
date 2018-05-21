package com.backedrum.component.util;

import com.backedrum.model.BaseEntity;
import com.backedrum.service.ItemsService;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableMultiLineLabel;
import org.apache.wicket.model.PropertyModel;

import java.io.Serializable;
import java.util.function.Consumer;

@UtilityClass
public class UiUtils {

    public interface WicketConsumer<T> extends Consumer<T>, Serializable {}

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
}
