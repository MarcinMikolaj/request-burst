package com.request.burst.controller.burst;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.ExecutionException;

@RequestMapping(path = "/v1/api/burst", produces = MediaType.APPLICATION_JSON_VALUE)
public interface BurstControllerApi {
    @PostMapping(path = "/sync")
    ResponseEntity<?> getSync(@RequestParam String method, @RequestParam String url, @RequestParam Integer count);

    @PostMapping(path = "/async")
    ResponseEntity<?> getAsync(@RequestParam String method, @RequestParam String url, @RequestParam Integer count)
            throws ExecutionException, InterruptedException;

}
