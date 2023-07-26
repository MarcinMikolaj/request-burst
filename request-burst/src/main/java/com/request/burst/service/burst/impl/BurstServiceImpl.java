package com.request.burst.service.burst.impl;

import com.request.burst.model.JobEntry;
import com.request.burst.repository.JobEntryRepository;
import com.request.burst.service.burst.BurstService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class BurstServiceImpl implements BurstService {
    @Value("${application.request.settings.re-query}")
    private int reQuery;
    @Value("${application.request.settings.on-4xx-response}")
    private String on4xxResponse;

    @Override
    public Flux<JobEntry> callAsync(String method, String url, Integer count) {
        return Flux.range(0, count)
                .flatMap(num -> {
                    LocalDateTime start = LocalDateTime.now();
                    return invokeAsync(url, resolveHttpMethod(method)).map(response -> prepareJobEntry(url, method, count , num, start, response));
                })
                .doOnNext(entry -> entry.setStop(LocalDateTime.now()))
                .doOnNext(entry -> entry.setDuration(ChronoUnit.MILLIS.between(entry.getStart(), entry.getStop())))
                .sort(JobEntry::compareTo);
    }

    @Override
    public List<JobEntry> callSync(String method, String url, Integer count) {
        return IntStream.range(0, count)
                .mapToObj(i -> prepareJobEntry(url, method, count, i, LocalDateTime.now()))
                .peek(entry -> entry.setResult(invokeSync(resolveHttpMethod(method), url)))
                .peek(entry -> entry.setCurrentRequestCounter(entry.getCurrentRequestCounter() + 1))
                .peek(entry -> entry.setStop(LocalDateTime.now()))
                .peek(entry -> entry.setDuration(ChronoUnit.MILLIS.between(entry.getStart(), entry.getStop())))
                .collect(Collectors.toList());
    }

    protected Mono<String> invokeAsync(String url, HttpMethod method) {
        return WebClient.create()
                .method(method)
                .uri(url)
                .exchangeToMono((clientResponse) -> clientResponse.statusCode().is2xxSuccessful()
                        ? clientResponse.bodyToMono(String.class)
                        : clientResponse.statusCode().is4xxClientError()
                              ? Mono.just(on4xxResponse)
                              : clientResponse.createException().flatMap(Mono::error))
                .retry(reQuery);
    }

    protected String invokeSync(HttpMethod httpMethod, String uri){
        return WebClient.create()
                .method(httpMethod)
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .retry(reQuery)
                .block();
    }

    private JobEntry prepareJobEntry(String url, String method, int totalRequestLimit, int currentRequestCounter, LocalDateTime start, String result){
        return JobEntry.builder()
                .url(url)
                .method(method)
                .result(result)
                .currentRequestCounter(currentRequestCounter)
                .totalRequestLimit(totalRequestLimit)
                .start(start)
                .build();
    }

    private JobEntry prepareJobEntry(String url, String method, int totalRequestLimit, int currentRequestCounter, LocalDateTime start){
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
