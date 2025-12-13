package com.jobboard.controllers;

import com.jobboard.modesl.dto.JobListing;
import com.jobboard.modesl.wsdto.WSError;
import com.jobboard.modesl.wsdto.WSJobListingResponse;
import com.jobboard.services.ExternalAPIPolling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/jobsearch")
public class JobSearchController {

    @Autowired
    private ExternalAPIPolling externalAPIPolling;

    @Value("${jobsearch.api.url}")
    private String jobSearchApiUrl;

    @GetMapping("/poll")
    public boolean poll() {
        return externalAPIPolling.pollExternalAPIForHealth(jobSearchApiUrl);
    }

    @GetMapping("/listings")
    public ResponseEntity<WSJobListingResponse> fetchJobListings(@RequestParam String keyword,
                                                                 @RequestParam String location,
                                                                 @RequestParam int experience,
                                                                 @RequestParam int pageNo,
                                                                 @RequestParam int noOfResults) {
        WSJobListingResponse wsJobListingResponse = new WSJobListingResponse();
        if(noOfResults > 50) {
            WSError wsError = new WSError();
            wsError.setErrorMessage("Requested number of results exceeds the maximum limit of 50.");
            wsJobListingResponse.setError(wsError);
            return ResponseEntity.badRequest().body(wsJobListingResponse);
        }
        if(pageNo < 0) {
            WSError wsError = new WSError();
            wsError.setErrorMessage("Page number must be at least 1.");
            wsJobListingResponse.setError(wsError);
            return ResponseEntity.badRequest().body(wsJobListingResponse);
        }
        if(experience < 0) {
            WSError wsError = new WSError();
            wsError.setErrorMessage("Experience cannot be negative.");
            wsJobListingResponse.setError(wsError);
            return ResponseEntity.badRequest().body(wsJobListingResponse);
        }
         wsJobListingResponse.setJobListings(externalAPIPolling.fetchJobListings(jobSearchApiUrl, keyword, location, experience, pageNo, noOfResults));
        return ResponseEntity.ok(wsJobListingResponse);
    }
}
