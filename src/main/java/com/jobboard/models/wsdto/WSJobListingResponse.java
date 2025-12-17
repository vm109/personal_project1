package com.jobboard.models.wsdto;

import com.jobboard.models.dto.JobListing;

import java.util.List;

public class WSJobListingResponse {
    private List<JobListing> jobListings;
    private WSError error;

    public List<JobListing> getJobListings() {
        return jobListings;
    }
    public void setJobListings(List<JobListing> jobListings) {
        this.jobListings = jobListings;
    }

    public WSError getError() {
        return error;
    }
    public void setError(WSError error) {
        this.error = error;
    }
}
