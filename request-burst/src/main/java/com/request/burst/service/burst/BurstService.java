package com.request.burst.service.burst;

import com.request.burst.model.JobResult;

import java.util.concurrent.ExecutionException;

public interface BurstService {
    JobResult callSync(String method, String url, Integer count);
    JobResult callAsync(String method, String url, Integer count) throws ExecutionException, InterruptedException;

}
