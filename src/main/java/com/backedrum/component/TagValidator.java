package com.backedrum.component;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

public class TagValidator implements IValidator<String> {

    @Override
    public void validate(IValidatable<String> validatable) {
        String tagEntered = validatable.getValue();

        if (tagEntered.matches(".*\\s+.*")) {
            ValidationError error = new ValidationError(this);
            validatable.error(error);
        }
    }
}