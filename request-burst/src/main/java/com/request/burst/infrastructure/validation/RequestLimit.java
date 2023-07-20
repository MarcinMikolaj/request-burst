package com.request.burst.infrastructure.validation;

import com.request.burst.infrastructure.validation.validator.RequestLimitValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RequestLimitValidator.class)
public @interface RequestLimit {
    String message() default "Request limit: ${validatedValue} not allowed";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default{};
}
