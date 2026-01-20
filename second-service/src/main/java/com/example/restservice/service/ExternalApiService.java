package com.example.restservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalApiService {

    private static final Logger logger = LoggerFactory.getLogger(ExternalApiService.class);

    private final RestTemplate restTemplate;

    @Value("${external.api.base-url}")
    private String baseUrl;

    public ExternalApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Makes a GET request to the external API
     * 
     * @param endpoint The specific endpoint path
     * @return Response from the external API
     */
    public Object getFromExternalApi(String endpoint) {
        try {
            String url = baseUrl + endpoint;
            logger.info("Making GET request to external API: {}", url);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Object> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Object.class
            );
            
            logger.info("Received response with status: {}", response.getStatusCode());
            return response.getBody();
            
        } catch (RestClientException e) {
            logger.error("Error calling external API: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to call external API: " + e.getMessage());
        }
    }

    /**
     * Makes a POST request to the external API
     * 
     * @param endpoint The specific endpoint path
     * @param requestBody The request body to send
     * @return Response from the external API
     */
    public Object postToExternalApi(String endpoint, Object requestBody) {
        try {
            String url = baseUrl + endpoint;
            logger.info("Making POST request to external API: {}", url);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> entity = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<Object> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                Object.class
            );
            
            logger.info("Received response with status: {}", response.getStatusCode());
            return response.getBody();
            
        } catch (RestClientException e) {
            logger.error("Error calling external API: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to call external API: " + e.getMessage());
        }
    }
}

