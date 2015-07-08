package org.phantom.notificator.util;

import javax.validation.Validation;
import javax.validation.Validator;

/**
 * Created by Master Victor on 08/07/2015.
 */
public class ValidationUtil {

    private static final Validator validator = Validation
            .buildDefaultValidatorFactory()
            .getValidator();

    public static Validator getValidator() {
        return validator;
    }

}
