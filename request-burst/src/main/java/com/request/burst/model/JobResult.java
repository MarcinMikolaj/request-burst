package com.request.burst.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class JobResult {
    private List<JobEntry> jobEntries;
}
