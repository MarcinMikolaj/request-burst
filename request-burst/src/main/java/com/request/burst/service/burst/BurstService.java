package com.request.burst.service.burst;

import com.request.burst.model.JobEntry;
import reactor.core.publisher.Flux;

import java.util.List;

public interface BurstService {
    List<JobEntry> callSync(String method, String url, Integer count);
    Flux<JobEntry> callAsync(String method, String url, Integer count);

}
