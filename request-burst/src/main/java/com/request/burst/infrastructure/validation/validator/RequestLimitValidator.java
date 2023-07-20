package com.request.burst.infrastructure.validation.validator;

import com.request.burst.infrastructure.validation.RequestLimit;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

public class RequestLimitValidator implements ConstraintValidator<RequestLimit, Integer> {
    @Value("${application.request.settings.request-limit}")
    private Integer requestLimit;

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        return value <= requestLimit && value >= 1;
    }
}
