package com.request.burst.controller.burst.impl;

import com.request.burst.controller.burst.BurstControllerApi;
import com.request.burst.service.BurstService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class BurstController implements BurstControllerApi {
    private final BurstService burstService;

    @Override
    public ResponseEntity<?> getSync(String method, String url, Integer count) {
        return new ResponseEntity<>(burstService.callSync(method, url, count), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAsync(String method, String url, Integer count) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(burstService.callAsync(method, url, count), HttpStatus.OK);
    }
}
