package com.jobboard.data;

import com.jobboard.models.dto.JobListing;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobListingMongoRepo extends MongoRepository<JobListing, String>, JobListingCustomMongoRepo {
    @Override
    JobListing save(JobListing entity);

    @Override
    <S extends JobListing> List<S> saveAll(Iterable<S> entities);
}
