package com.request.burst.service.impl;

import com.request.burst.service.impl.utils.BurstUtils;
import com.request.burst.model.JobEntry;
import com.request.burst.model.JobResult;
import com.request.burst.service.BurstService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class BurstServiceImpl implements BurstService {
    private final AsyncOperation asyncOperation;

    @Override
    public JobResult callAsync(String method, String url, Integer count) throws ExecutionException, InterruptedException {
        Long start = System.currentTimeMillis() / 1000;
        List<JobEntry> jobEntries = new LinkedList<>();
        List<CompletableFuture<JobEntry>> completableFutures = IntStream.range(0, count)
                .mapToObj(i -> asyncOperation.callAsync(method, url, count))
                .toList();
        CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0])).join();
        for(CompletableFuture<JobEntry> entry: completableFutures)
            jobEntries.add(entry.get());
        log.info(BurstUtils.getFormatMessage(start, System.currentTimeMillis() / 1000));
        return new JobResult(jobEntries);
    }

    @Override
    public JobResult callSync(String method,String url, Integer count) {
        Long start = System.currentTimeMillis() / 1000;
        List<JobEntry> jobEntries = IntStream.range(0, count)
                .mapToObj(i -> BurstUtils.prepareJobEntry(url, method, count, 0, LocalDateTime.now()))
                .peek(entry -> entry.setResult(BurstUtils.callSync(BurstUtils.resolveHttpMethod(method), url)))
                .peek(entry -> entry.setCurrentRequestCounter(entry.getCurrentRequestCounter() + 1))
                .peek(entry -> entry.setStop(LocalDateTime.now()))
                .peek(entry -> entry.setDuration(ChronoUnit.MILLIS.between(entry.getStart(), entry.getStop())))
                .collect(Collectors.toList());
        log.info(BurstUtils.getFormatMessage(start, System.currentTimeMillis() / 1000));
        return new JobResult(jobEntries);
    }

}
