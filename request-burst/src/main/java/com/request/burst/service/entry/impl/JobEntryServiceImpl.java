package com.request.burst.service.entry.impl;

import com.request.burst.model.JobEntry;
import com.request.burst.repository.JobEntryRepository;
import com.request.burst.service.entry.JobEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobEntryServiceImpl implements JobEntryService {
    private final JobEntryRepository jobEntryRepository;

    @Override
    public Flux<JobEntry> saveAll(List<JobEntry> jobEntries) {
        List<JobEntry> entries = jobEntries.stream()
                .filter(entry -> entry.getId() == null)
                .peek(entry -> entry.setId(UUID.randomUUID()))
                .toList();
        return jobEntryRepository.saveAll(Flux.fromStream(entries.stream()));
    }

    @Override
    public void saveAll(Flux<JobEntry> jobEntries) {
        jobEntryRepository.saveAll(jobEntries.doOnNext(enr -> enr.setId(UUID.randomUUID()))).subscribe();
    }
}
