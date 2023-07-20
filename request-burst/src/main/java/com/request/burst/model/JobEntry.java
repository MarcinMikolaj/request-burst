package com.request.burst.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@ToString
@Table
public class JobEntry {
    @PrimaryKey
    private UUID id;
    private String url;
    private String method;
    private String result;
    private int currentRequestCounter;
    private int totalRequestLimit;
    private LocalDateTime start;
    private LocalDateTime stop;
    private Long duration;
}
