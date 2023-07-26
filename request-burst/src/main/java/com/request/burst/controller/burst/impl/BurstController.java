package com.request.burst.controller.burst.impl;

import com.request.burst.controller.burst.BurstControllerApi;
import com.request.burst.model.JobEntry;
import com.request.burst.service.burst.BurstService;
import com.request.burst.service.entry.JobEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BurstController implements BurstControllerApi {
    private final BurstService burstService;
    private final JobEntryService jobEntryService;

    @Override
    public ResponseEntity<?> getSync(String method, String url, Integer count, Boolean storage) {
        List<JobEntry> jobEntries = burstService.callSync(method, url, count);
        if(storage)
            jobEntryService.saveAll(jobEntries).subscribe();
        return new ResponseEntity<>(jobEntries, HttpStatus.OK);
    }

    @Override
    public Flux<JobEntry> getAsync(String method, String url, Integer count, Boolean storage) {
        Flux<JobEntry> jobEntries = burstService.callAsync(method, url, count);
        if(storage)
            jobEntryService.saveAll(jobEntries);
       return jobEntries;
    }

}
