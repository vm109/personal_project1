package com.jobboard.schedulers;

import com.jobboard.data.JobListingMongoRepo;
import com.jobboard.models.dto.JobListing;
import com.jobboard.services.JobListingService;
import jakarta.annotation.Resource;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
public class ListJobsInMongo {

    @Resource
    private JobListingService externalAPIPolling;

    @Resource
    private JobListingMongoRepo jobListingMongoRepo;

    @Value("${jobsearch.api.url}")
    private String jobSearchApiUrl;

    private static final Logger LOG = LoggerFactory.getLogger(ListJobsInMongo.class);

    @Scheduled(initialDelay = 0, fixedRate = 900000) // Every 15 minutes
    public void runJobListingsIntoMongoEvery15Minutes() {
        LOG.info("Starting scheduled task to list job listings into MongoDB.");
        // Implementation to list job listings into MongoDB every 15 minutes
        try {
            List<JobListing> jobList = externalAPIPolling.fetchJobListings(jobSearchApiUrl, "java", "hyderabad", 8, 1, 50);
            jobListingMongoRepo.upsertIgnoreDuplicates(jobList);
        } catch (Exception e) {
            LOG.error("Error while fetching or saving job listings: {}", e.getMessage());
        }
    }
}
