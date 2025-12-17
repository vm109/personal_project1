package com.jobboard.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobboard.models.dto.JobListing;
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
public class PollingService implements ExternalAPIPolling{

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
            HttpEntity entity = createHeaders();
            String urlWithParams = UriComponentsBuilder.fromHttpUrl(externalApiUrl)
                    .queryParam("noOfResults", noOfResults)
                    .queryParam("urlType", "search_by_key_loc")
                    .queryParam("searchType", "adv")
                    .queryParam("location",location)
                    .queryParam("keyword", keyword)
                    .queryParam("experience",experience)
                    .queryParam("pageNo", pageNo)
                    .toUriString();

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(urlWithParams, HttpMethod.GET, entity, String.class);

            if(Objects.nonNull(response.getBody())) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());
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
                }
                return jobListings; // Replace with actual parsing logic
            }

        } catch (Exception e) {
            e.printStackTrace();
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
}
