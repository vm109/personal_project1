package com.jobboard.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobboard.models.dto.JobListing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class JobListingServiceImpl implements JobListingService {

    private static final Logger LOG = LoggerFactory.getLogger(JobListingServiceImpl.class);

    @Value("${jobsearch.api.Appid}")
    private String Appid;

    @Value("${jobsearch.api.Nkparam}")
    private String Nkparam;

    @Value("${jobsearch.api.Systemid}")
    private String Systemid;

    @Override
    public boolean pollExternalAPIForHealth(final String externalApiUrl) {
        RestTemplate restTemplate = new RestTemplate();

        // Set dynamic headers
        HttpEntity entity = createHeaders();

        // Build dynamic query parameters
        String urlWithParams = UriComponentsBuilder.fromHttpUrl(externalApiUrl)
                .queryParam("noOfResults", "0")
                .queryParam("urlType", "search_by_key_loc")
                .queryParam("searchType", "adv")
                .queryParam("location","vijayawada")
                .queryParam("keyword", "java")
                .queryParam("experience","2")
                .toUriString();

        // Make the GET request
        try {
            ResponseEntity<String> response = restTemplate.exchange(urlWithParams, HttpMethod.GET, entity, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<JobListing> fetchJobListings(String externalApiUrl, String keyword, String location, int experience, int pageNo, int noOfResults) {
        List<JobListing> jobListings = new ArrayList<>();
        try {
            ResponseEntity<String> response = getJobListing(externalApiUrl, keyword, location, experience, pageNo, noOfResults);

            if(Objects.nonNull(response.getBody())) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());
                jobListings = parseJobListings(response.getBody());
                if(root.has("noOfJobs")) {
                    int numberOfResults = root.get("noOfJobs").asInt();

                    // Fetch additional pages if available
                    while (hasNextPage(numberOfResults, pageNo, noOfResults)) {
                        LOG.info("Fetching additional page: {}", pageNo + 1);
                        pageNo++;
                        ResponseEntity<String> nextPageResponse = getJobListing(externalApiUrl, keyword, location, experience, pageNo, noOfResults);
                        if(Objects.nonNull(nextPageResponse.getBody())) {
                            List<JobListing> additionalJobListings = parseJobListings(nextPageResponse.getBody());
                            jobListings.addAll(additionalJobListings);
                        }
                    }
                }
                return jobListings;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobListings;
    }


    private ResponseEntity<String> getJobListing(String externalApiUrl, String keyword, String location, int experience, int pageNo, int noOfResults) {
        LOG.info("Fetching job listings from external API: pageNo={}, noOfResults={}", pageNo, noOfResults);
        try {
            HttpEntity entity = createHeaders();
            String urlWithParams = UriComponentsBuilder.fromHttpUrl(externalApiUrl)
                    .queryParam("noOfResults", noOfResults)
                    .queryParam("urlType", "search_by_key_loc")
                    .queryParam("searchType", "adv")
                    .queryParam("location", location)
                    .queryParam("keyword", keyword)
                    .queryParam("experience", experience)
                    .queryParam("pageNo", pageNo)
                    .toUriString();

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(urlWithParams, HttpMethod.GET, entity, String.class);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private List<JobListing> parseJobListings(String responseBody) throws JsonProcessingException {
        LOG.info("Parsing job listings from response.");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(responseBody);
        List<JobListing> jobListings = new ArrayList<>();
        if(root.has("jobDetails")) {
            JsonNode jobDetailsNode = root.get("jobDetails");
            if (jobDetailsNode.isArray()) {
                for (JsonNode jobNode : jobDetailsNode) {
                    JobListing jobListing = new JobListing();
                    jobListing.setTitle(jobNode.path("title").asText());
                    jobListing.setCompanyName(jobNode.path("companyName").asText());
                    jobListing.setTagsAndSkills(jobNode.path("tagsAndSkills").asText());
                    jobListing.setJobDescription(jobNode.path("jobDescription").asText());
                    jobListing.setCreatedDate(jobNode.path("createdDate").asText());
                    jobListing.setMinimumExperience(jobNode.path("minimumExperience").asText());
                    jobListing.setMaximumExperience(jobNode.path("maximumExperience").asText());
                    jobListing.setJobId(jobNode.path("jobId").asText());
                    jobListings.add(jobListing);
                }
            }
            return jobListings;
        }
        return new ArrayList<>();
    }


    private HttpEntity createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Appid", Appid);
        headers.set("Systemid", Systemid);
        headers.set("Nkparam", Nkparam);
        return new HttpEntity<Void>(headers);
    }

    private boolean hasNextPage(int numberOfResults, int pageNo, int resultsPerPage) {
        int totalFetched = pageNo * resultsPerPage;
        return totalFetched < numberOfResults;
    }

}
