package com.request.burst.infrastructure.validation.validator;

import com.request.burst.infrastructure.validation.HttpMethod;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class HttpMethodValidator implements ConstraintValidator<HttpMethod, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.stream(com.request.burst.model.HttpMethod.values())
                .map(method -> method.toString().toLowerCase())
                .anyMatch(method -> method.equals(value.toLowerCase()));
    }

}
