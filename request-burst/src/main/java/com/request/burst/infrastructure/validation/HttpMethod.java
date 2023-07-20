package com.request.burst.infrastructure.validation;

import com.request.burst.infrastructure.validation.validator.HttpMethodValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HttpMethodValidator.class)
public @interface HttpMethod {
    String message() default "Incorrect http method ${validatedValue} (acceptable: get, post, put, delete)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default{};
}
