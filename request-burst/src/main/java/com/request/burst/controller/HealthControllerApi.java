package com.request.burst.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(path = "/v1/api/health")
public interface HealthControllerApi {

    @GetMapping
    ResponseEntity<?> get();
}
