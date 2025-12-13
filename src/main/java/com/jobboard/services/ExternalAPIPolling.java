package com.jobboard.services;

import com.jobboard.modesl.dto.JobListing;

import java.util.List;

public interface ExternalAPIPolling {
    // return true if the external API is healthy, false otherwise
    boolean pollExternalAPIForHealth(String externalApiUrl);

    List<JobListing> fetchJobListings(String externalApiUrl, String keyword, String location, int experience, int pageNo, int noOfResults);
}
