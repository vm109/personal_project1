package com.jobboard.data;

import com.jobboard.models.dto.JobListing;

import java.util.List;

public interface JobListingCustomMongoRepo {
    void upsertIgnoreDuplicates(List<JobListing> jobs);
}
