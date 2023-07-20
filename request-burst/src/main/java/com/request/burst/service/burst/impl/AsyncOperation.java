package com.request.burst.service.burst.impl;

import com.request.burst.model.JobEntry;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;

@Service
public class AsyncOperation {

    @Async
    public CompletableFuture<JobEntry> callAsync(String method, String url, Integer count) {
        JobEntry jobEntry = prepareJobEntry(url, method, count, 0, LocalDateTime.now());
        //System.out.println("THREAD: " + Thread.currentThread().getName());
        jobEntry.setResult(invokeSync(resolveHttpMethod(method), url));
        jobEntry.setCurrentRequestCounter(jobEntry.getCurrentRequestCounter() + 1);
        jobEntry.setStop(LocalDateTime.now());
        jobEntry.setDuration(ChronoUnit.MILLIS.between(jobEntry.getStart(), jobEntry.getStop()));
        return CompletableFuture.completedFuture(jobEntry);
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

    protected String invokeSync(HttpMethod httpMethod, String uri){
        return WebClient.create()
                .method(httpMethod)
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .block();
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

}
