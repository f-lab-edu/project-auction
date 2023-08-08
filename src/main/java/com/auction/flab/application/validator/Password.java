package com.auction.flab.application.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = PasswordValidator.class)
@Target(FIELD)
@Retention(RUNTIME)
public @interface Password {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default { };

}
