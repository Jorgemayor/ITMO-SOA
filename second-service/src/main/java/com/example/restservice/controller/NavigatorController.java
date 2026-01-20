package com.example.restservice.controller;

import com.example.restservice.model.NavigatorPostRequest;
import com.example.restservice.model.Route;
import com.example.restservice.model.RouteResponse;
import com.example.restservice.service.RouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Navigator REST endpoint controller
 * Handles route navigation operations
 */
@RestController
@RequestMapping("/navigator")
public class NavigatorController {

    private static final Logger logger = LoggerFactory.getLogger(NavigatorController.class);

    private final RouteService routeService;

    public NavigatorController(RouteService routeService) {
        this.routeService = routeService;
    }

    /**
     * GET endpoint to find routes between two locations with sorting
     * 
     * Example: GET http://localhost:8080/navigator/routes/1/2/id,name?sortDirection=ASC
     */
    @GetMapping("/routes/{id-from}/{id-to}/{order-by}")
    public ResponseEntity<?> getRoutes(
            @PathVariable("id-from") Long idFrom,
            @PathVariable("id-to") Long idTo,
            @PathVariable("order-by") String orderBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortDirection) {
        
        try {
            logger.info("Received request to get routes from {} to {} with order: {} and direction: {}", 
                       idFrom, idTo, orderBy, sortDirection);

            // Validate path parameters
            if (idFrom == null || idFrom < 1) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new com.example.restservice.model.Error("BadRequest", "id-from must be greater than 0"));
            }
            
            if (idTo == null || idTo < 1) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new com.example.restservice.model.Error("BadRequest", "id-to must be greater than 0"));
            }

            // Call the route service to find routes
            RouteResponse response = routeService.findRoutesBetweenLocations(idFrom, idTo, orderBy, sortDirection);
            
            // Return 204 if no routes found
            if (response.getRoutes() == null || response.getRoutes().isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            logger.error("Validation error: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new com.example.restservice.model.Error("BadRequest", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error getting routes: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new com.example.restservice.model.Error("InternalServerError", "Failed to get routes: " + e.getMessage()));
        }
    }

    /**
     * POST endpoint to add a new route between two locations
     * 
     * Example: POST http://localhost:8080/navigator/route/add/1/2/100
     * Body: {"name": "Route A", "coordinates": {"x": 1.5, "y": 2.5}}
     */
    @PostMapping("/route/add/{id-from}/{id-to}/{distance}")
    public ResponseEntity<?> addRoute(
            @PathVariable("id-from") Long idFrom,
            @PathVariable("id-to") Long idTo,
            @PathVariable("distance") Long distance,
            @RequestBody NavigatorPostRequest request) {
        
        try {
            logger.info("Received request to add route from {} to {} with distance {} and request: {}", 
                       idFrom, idTo, distance, request);

            // Validate path parameters
            if (idFrom == null || idFrom < 1) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new com.example.restservice.model.Error("BadRequest", "id-from must be greater than 0"));
            }
            
            if (idTo == null || idTo < 1) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new com.example.restservice.model.Error("BadRequest", "id-to must be greater than 0"));
            }
            
            if (distance == null || distance <= 1) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new com.example.restservice.model.Error("BadRequest", "distance must be greater than 1"));
            }

            // Validate request body
            if (request == null || request.getName() == null || request.getName().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .body(new com.example.restservice.model.Error("ValidationException", "name is required and cannot be empty"));
            }
            
            if (request.getCoordinates() == null) {
                return ResponseEntity
                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .body(new com.example.restservice.model.Error("ValidationException", "coordinates are required"));
            }

            // Call the route service to add the route
            Route route = routeService.addRoute(idFrom, idTo, distance, request);
            
            return ResponseEntity.ok(route);
            
        } catch (IllegalArgumentException e) {
            logger.error("Validation error: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(new com.example.restservice.model.Error("ValidationException", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error adding route: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new com.example.restservice.model.Error("InternalServerError", "Failed to add route: " + e.getMessage()));
        }
    }
}

