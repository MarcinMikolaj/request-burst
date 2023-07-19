package com.request.burst.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
public class JobEntry {
    private String url;
    private String method;
    private String result;
    private int currentRequestCounter;
    private int totalRequestLimit;
    private LocalDateTime start;
    private LocalDateTime stop;
    private Long duration;
}
