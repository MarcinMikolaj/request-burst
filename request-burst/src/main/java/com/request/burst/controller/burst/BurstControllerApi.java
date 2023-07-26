package com.request.burst.controller.burst;

import com.request.burst.infrastructure.validation.HttpMethod;
import com.request.burst.infrastructure.validation.RequestLimit;
import com.request.burst.model.JobEntry;
import org.hibernate.validator.constraints.URL;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

import java.util.concurrent.ExecutionException;

@Validated
@RequestMapping(path = "/v1/api/burst", produces = MediaType.APPLICATION_JSON_VALUE)
public interface BurstControllerApi {
    @PostMapping(path = "/sync")
    ResponseEntity<?> getSync(@RequestParam @HttpMethod String method,
                              @RequestParam @URL String url,
                              @RequestParam @RequestLimit Integer count,
                              @RequestParam Boolean storage);

    @PostMapping(path = "/async")
    Flux<JobEntry> getAsync(@RequestParam @HttpMethod String method,
                            @RequestParam @URL String url,
                            @RequestParam @RequestLimit Integer count,
                            @RequestParam Boolean storage) throws ExecutionException, InterruptedException;
}
