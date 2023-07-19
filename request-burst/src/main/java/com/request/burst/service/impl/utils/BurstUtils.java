package com.request.burst.service.impl.utils;

import com.request.burst.model.JobEntry;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.MessageFormat;
import java.time.LocalDateTime;

public class BurstUtils {

    public static String callSync(HttpMethod httpMethod, String uri){
        return WebClient.create()
                .method(httpMethod)
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

        public static JobEntry prepareJobEntry(String url, String method, int totalRequestLimit, int currentRequestCounter, LocalDateTime start){
        return JobEntry.builder()
                .url(url)
                .method(method)
                .currentRequestCounter(currentRequestCounter)
                .totalRequestLimit(totalRequestLimit)
                .start(start)
                .build();
    }

    public static HttpMethod resolveHttpMethod(String method){
        return switch (method) {
            case "get" -> HttpMethod.GET;
            case "post" -> HttpMethod.POST;
            case "put" -> HttpMethod.PUT;
            case "delete" -> HttpMethod.DELETE;
            default -> throw new IllegalArgumentException();
        };
    }

    public static String getFormatMessage(Long start, Long stop){
        return MessageFormat.format("Start: {0}, Stop: {1}, Duration: {2}", start, stop, stop-start);
    }

}
