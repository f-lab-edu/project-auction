package com.auction.flab.application.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = DateTimeOrderValidator.class)
@Target(TYPE)
@Retention(RUNTIME)
public @interface DateTimeOrder {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default { };

    String previousDateTime() default "";

    String laterDateTime() default "";

}
