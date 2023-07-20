package com.request.burst.infrastructure.exception.handler;

import com.request.burst.infrastructure.exception.dto.ExceptionDto;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex){
        log.error(ex.getMessage());
        return new ResponseEntity<>(prepareResponseMap(ex, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<?> handleWebClientResponseException(WebClientResponseException ex, WebRequest request){
        log.error(ex.getMessage());
        return new ResponseEntity<>(prepareExceptionDto(ex, HttpStatus.BAD_REQUEST, request.getDescription(false),
                ((ServletWebRequest) request).getHttpMethod().toString(), ex.getClass().getName(), Collections.singletonList(ex.getMessage())),
                HttpStatus.BAD_REQUEST);
    }

    private Map prepareResponseMap(ConstraintViolationException e, HttpStatus httpStatus){
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", httpStatus.value());
        List<FieldError> errors = new ArrayList<>();
        if(e instanceof ConstraintViolationException){
            errors = e.getConstraintViolations().stream()
                    .map(constraintViolation -> new FieldError(constraintViolation.getRootBeanClass().getName()
                            , constraintViolation.getPropertyPath().toString()
                            , constraintViolation.getMessage()))
                    .collect(Collectors.toList());
        }
        body.put("errors", errors);
        return body;
    }

    private ExceptionDto prepareExceptionDto(Exception ex, HttpStatus status, String method, String path,
                                             String exception, List<String> messages){
        return ExceptionDto.builder()
                .timestamp(new Date())
                .method(method)
                .status(status.value())
                .path(path)
                .exception(exception)
                .messages(messages)
                .build();
    }

}
