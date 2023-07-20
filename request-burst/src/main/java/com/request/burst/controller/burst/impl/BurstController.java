package com.request.burst.controller.burst.impl;

import com.request.burst.controller.burst.BurstControllerApi;
import com.request.burst.model.JobEntry;
import com.request.burst.model.JobResult;
import com.request.burst.service.burst.BurstService;
import com.request.burst.service.entry.JobEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class BurstController implements BurstControllerApi {
    private final BurstService burstService;
    private final JobEntryService jobEntryService;

    @Override
    public ResponseEntity<?> getSync(String method, String url, Integer count, Boolean storage) {
        JobResult jobResult = burstService.callSync(method, url, count);
//        if(storage)
//            jobEntryService.saveAll(jobResult.getJobEntries());
        return new ResponseEntity<>(jobResult, HttpStatus.OK);
    }

    @Override
    public JobResult getAsync(String method, String url, Integer count, Boolean storage)
            throws ExecutionException, InterruptedException {
        JobResult jobResult = burstService.callAsync(method, url, count);
//        if(storage)
//            jobEntryService.saveAll(jobResult.getJobEntries());
        //return new ResponseEntity<>(jobResult, HttpStatus.OK);
        return jobResult;
    }

}
