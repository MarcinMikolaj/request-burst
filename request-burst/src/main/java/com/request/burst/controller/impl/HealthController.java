package com.request.burst.controller.impl;

import com.request.burst.controller.HealthControllerApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController implements HealthControllerApi {

    @Override
    public ResponseEntity<?> get() {
        return new ResponseEntity<>("UP", HttpStatus.OK);
    }
}
