package com.request.burst.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@ToString
@Table
public class JobEntry implements Comparable<JobEntry> {
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

    @Override
    public int compareTo(@NotNull JobEntry o) {
        return Integer.compare(o.getCurrentRequestCounter(), this.getCurrentRequestCounter());
    }
}
