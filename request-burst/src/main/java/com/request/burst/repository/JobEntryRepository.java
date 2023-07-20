package com.request.burst.repository;

import com.request.burst.model.JobEntry;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JobEntryRepository extends CassandraRepository<JobEntry, UUID> {
}
