package com.request.burst.controller.health.impl;

import com.request.burst.controller.health.HealthControllerApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;

@RestController
public class HealthController implements HealthControllerApi {

    @Value("${spring.profiles.active:unknown}")
    private String activeProfile;

    @Override
    public ResponseEntity<?> get() {
        return new ResponseEntity<>(MessageFormat.format("Status: {0}, Profile: {1}", "UP", activeProfile),
                HttpStatus.OK);
    }
}
