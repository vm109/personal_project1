package com.jobboard.services;

public interface ExternalAPIPolling {
    // return true if the external API is healthy, false otherwise
    boolean pollExternalAPIForHealth(String externalApiUrl);
}
