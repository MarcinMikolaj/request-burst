package com.request.burst.service.entry;

import com.request.burst.model.JobEntry;
import reactor.core.publisher.Flux;

import java.util.List;

public interface JobEntryService {
    Flux<JobEntry> saveAll(List<JobEntry> jobEntries);
    void saveAll(Flux<JobEntry> jobEntries);

}
