package com.jobboard.data;

import com.jobboard.models.dto.JobListing;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobListingMongoRepo extends MongoRepository<JobListing, String> {
    @Override
    JobListing save(JobListing entity);
}
