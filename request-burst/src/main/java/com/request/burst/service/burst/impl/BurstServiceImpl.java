package com.request.burst.service.burst.impl;

import com.request.burst.model.JobEntry;
import com.request.burst.model.JobResult;
import com.request.burst.service.burst.BurstService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.MessageFormat;
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
        log.info(getFormatMessage(start, System.currentTimeMillis() / 1000));
        return new JobResult(jobEntries);
    }

    @Override
    public JobResult callSync(String method, String url, Integer count) {
        Long start = System.currentTimeMillis() / 1000;
        List<JobEntry> jobEntries = IntStream.range(0, count)
                .mapToObj(i -> prepareJobEntry(url, method, count, 0, LocalDateTime.now()))
                .peek(entry -> entry.setResult(invokeSync(resolveHttpMethod(method), url)))
                .peek(entry -> entry.setCurrentRequestCounter(entry.getCurrentRequestCounter() + 1))
                .peek(entry -> entry.setStop(LocalDateTime.now()))
                .peek(entry -> entry.setDuration(ChronoUnit.MILLIS.between(entry.getStart(), entry.getStop())))
                .collect(Collectors.toList());
        log.info(getFormatMessage(start, System.currentTimeMillis() / 1000));
        return new JobResult(jobEntries);
    }

    protected JobEntry prepareJobEntry(String url, String method, int totalRequestLimit, int currentRequestCounter, LocalDateTime start, String result){
        return JobEntry.builder()
                .url(url)
                .method(method)
                .result(result)
                .currentRequestCounter(currentRequestCounter)
                .totalRequestLimit(totalRequestLimit)
                .start(start)
                .build();
    }

    protected String invokeSync(HttpMethod httpMethod, String uri){
        return WebClient.create()
                .method(httpMethod)
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    protected JobEntry prepareJobEntry(String url, String method, int totalRequestLimit, int currentRequestCounter, LocalDateTime start){
        return JobEntry.builder()
                .url(url)
                .method(method)
                .currentRequestCounter(currentRequestCounter)
                .totalRequestLimit(totalRequestLimit)
                .start(start)
                .build();
    }

    private HttpMethod resolveHttpMethod(String method){
        return switch (method.toLowerCase()) {
            case "get" -> HttpMethod.GET;
            case "post" -> HttpMethod.POST;
            case "put" -> HttpMethod.PUT;
            case "delete" -> HttpMethod.DELETE;
            default -> throw new IllegalArgumentException();
        };
    }

    private String getFormatMessage(Long start, Long stop){
        return MessageFormat.format("Start: {0}, Stop: {1}, Duration: {2}", start, stop, stop-start);
    }

}
