package com.request.burst.infrastructure.exception.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@Builder
public class ExceptionDto {
    private Date timestamp;
    private int status;
    private String method;
    private String path;
    private String exception;
    private List<String> messages;
}