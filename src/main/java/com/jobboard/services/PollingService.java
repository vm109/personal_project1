package com.jobboard.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


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
        HttpHeaders headers = new HttpHeaders();
        headers.set("Appid", Appid);
        headers.set("Systemid", Systemid);
        headers.set("Nkparam", Nkparam);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

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
}
