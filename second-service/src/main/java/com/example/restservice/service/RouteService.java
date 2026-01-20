package com.example.restservice.service;

import com.example.restservice.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;

@Service
public class RouteService {

    private static final Logger logger = LoggerFactory.getLogger(RouteService.class);

    private final RestTemplate restTemplate;

    @Value("${external.api.base-url}")
    private String baseUrl;

    public RouteService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Finds routes between two locations with sorting
     * 
     * @param idFrom The starting location ID
     * @param idTo The destination location ID
     * @param orderBy Comma-separated list of fields to sort by
     * @param sortDirection The sort direction (ASC or DESC)
     * @return RouteResponse with the filtered and sorted routes
     */
    public RouteResponse findRoutesBetweenLocations(Long idFrom, Long idTo, String orderBy, String sortDirection) {
        try {
            // Build the URL with query parameters
            // Add filters for from.x (assuming it's the ID) and to.x (assuming it's the ID)
            // Based on the MainService.yaml, we can filter by from.x and to.x
            // We use .build(false) to avoid double encoding of special characters like [ ] and =
            // which are part of our filter syntax but might be mis-encoded by RestTemplate or UriComponentsBuilder
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + "/routes")
                    .queryParam("filter", "from.x[gte]=" + idFrom)
                    .queryParam("filter", "to.x[lte]=" + idTo);

            // Add sorting parameters
            String[] sortFields = orderBy.split(",");
            for (String field : sortFields) {
                builder.queryParam("sort", field.trim());
            }
            builder.queryParam("sortDirection", sortDirection);

            String finalUrl = builder.build(false).toUriString();
            logger.info("Making GET request to external API: {}", finalUrl);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<RouteResponse> response = restTemplate.exchange(
                finalUrl,
                HttpMethod.GET,
                entity,
                RouteResponse.class
            );
            
            logger.info("Received response with status: {}", response.getStatusCode());
            RouteResponse routeResponse = response.getBody();
            
            // Return an empty response if body is null
            if (routeResponse == null) {
                routeResponse = new RouteResponse(new ArrayList<>(), 0, 0, 0, 0, 0);
            }
            
            return routeResponse;
            
        } catch (RestClientException e) {
            logger.error("Error calling external API: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch routes from external API: " + e.getMessage());
        }
    }

    /**
     * Adds a new route between two locations
     * 
     * @param idFrom The starting location ID
     * @param idTo The destination location ID
     * @param distance The distance of the route
     * @param request The navigator post request with name and coordinates
     * @return The created Route
     */
    public Route addRoute(Long idFrom, Long idTo, Long distance, NavigatorPostRequest request) {
        try {
            String url = baseUrl + "/route";
            logger.info("Making POST request to external API: {}", url);
            
            // Build the full route request
            // We need to fetch the LocationFrom and LocationTo from the external API first
            // Or we can construct them with the IDs
            
            // For simplicity, we'll construct LocationFrom and LocationTo with the IDs
            // Assuming the IDs correspond to the x coordinates as per the swagger spec
            LocationFrom from = new LocationFrom(idFrom, 0.0, "Location " + idFrom);
            LocationTo to = new LocationTo(idTo.floatValue(), 0.0, 0);
            
            RouteRequest routeRequest = new RouteRequest(
                request.getName(),
                request.getCoordinates(),
                from,
                to,
                distance
            );
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<RouteRequest> entity = new HttpEntity<>(routeRequest, headers);
            
            ResponseEntity<Route> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                Route.class
            );
            
            logger.info("Received response with status: {}", response.getStatusCode());
            return response.getBody();
            
        } catch (RestClientException e) {
            logger.error("Error calling external API: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to add route to external API: " + e.getMessage());
        }
    }
}

