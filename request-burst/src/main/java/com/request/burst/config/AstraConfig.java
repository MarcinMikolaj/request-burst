package com.request.burst.config;

import com.request.burst.infrastructure.profiles.ProdProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.nio.file.Paths;

@ProdProfile
@Configuration
public class AstraConfig {
    @Value("${astra.cql.download-scb.path}")
    private File secureConnectBundle;

    @Bean
    public CqlSessionBuilderCustomizer sessionBuilderCustomizer(){
        return builder -> builder.withCloudSecureConnectBundle(Paths.get(secureConnectBundle.getPath()));
    }

}
