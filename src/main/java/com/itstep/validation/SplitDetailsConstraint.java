package com.itstep.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SplitDetailsValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SplitDetailsConstraint {
    String message() default "Invalid split details for the selected split type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
