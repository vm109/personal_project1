package com.jobboard.controllers;

import com.jobboard.services.ExternalAPIPolling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobsearch")
public class JobsearchTestController {

    @Autowired
    private ExternalAPIPolling externalAPIPolling;

    @Value("${jobsearch.api.url}")
    private String jobSearchApiUrl;

    @GetMapping("/poll")
    public boolean poll() {
        return externalAPIPolling.pollExternalAPIForHealth(jobSearchApiUrl);
    }
}
