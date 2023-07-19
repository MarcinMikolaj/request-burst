package com.request.burst.service.impl;

import com.request.burst.model.JobEntry;
import com.request.burst.service.impl.utils.BurstUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;

@Service
public class AsyncOperation {
    @Async
    public CompletableFuture<JobEntry> callAsync(String method, String url, Integer count) {
        JobEntry jobEntry = BurstUtils.prepareJobEntry(url, method, count, 0, LocalDateTime.now());
        System.out.println("THREAD: " + Thread.currentThread().getName());
        jobEntry.setResult(BurstUtils.callSync(BurstUtils.resolveHttpMethod(method), url));
        jobEntry.setCurrentRequestCounter(jobEntry.getCurrentRequestCounter() + 1);
        jobEntry.setStop(LocalDateTime.now());
        jobEntry.setDuration(ChronoUnit.MILLIS.between(jobEntry.getStart(), jobEntry.getStop()));
        return CompletableFuture.completedFuture(jobEntry);
    }

}
