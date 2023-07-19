package com.request.burst.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfig {
    private final String THREAD_NAME_PREFIX = "burst-thread-";

    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor()  {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int availableThreads = Runtime.getRuntime().availableProcessors();
        executor.setCorePoolSize(availableThreads);
        executor.setMaxPoolSize(availableThreads);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
        executor.initialize();
        return executor;
    }
}
