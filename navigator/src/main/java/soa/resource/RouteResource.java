package soa.resource;

import soa.model.Route;
import soa.dto.RouteRequest;
import soa.dto.RouteResponse;
import soa.dto.ErrorResponse;
import soa.service.RouteService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

/**
 * REST API endpoints for Route management
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RouteResource {
    
    @Inject
    private RouteService routeService;
    
    /**
     * GET /routes - Get a list of routes with pagination, sorting and filtering
     */
    @GET
    @Path("/routes")
    public Response getRoutes(
            @QueryParam("page") @DefaultValue("0") Integer page,
            @QueryParam("pageSize") @DefaultValue("10") Integer pageSize,
            @QueryParam("limit") @DefaultValue("10") Integer limit,
            @QueryParam("offset") Integer offset,
            @QueryParam("sort") List<String> sort,
            @QueryParam("sortDirection") @DefaultValue("ASC") String sortDirection,
            @QueryParam("filter") List<String> filter) {
        
        try {
            if (page < 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("BadRequest", "Page must be >= 0"))
                        .build();
            }
            if (pageSize < 1) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("BadRequest", "PageSize must be >= 1"))
                        .build();
            }
            
            List<Route> routes = routeService.getRoutes(page, pageSize, limit, offset, sort, sortDirection, filter);
            int total = routeService.getTotalCount(filter);
            RouteResponse response = new RouteResponse(routes, total, page, pageSize, limit, offset);
            
            return Response.ok(response).build();
            
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("InternalServerError", "An error occurred while processing the request"))
                    .build();
        }
    }
    
    /**
     * POST /route - Create a new route
     */
    @POST
    @Path("/route")
    public Response createRoute(RouteRequest routeRequest) {
        try {
            if (routeRequest.getName() == null || routeRequest.getName().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("BadRequest", "Route name is required and cannot be empty"))
                        .build();
            }
            
            if (routeRequest.getCoordinates() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("BadRequest", "Coordinates are required"))
                        .build();
            }
            
            if (routeRequest.getCoordinates().getX() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("BadRequest", "Coordinates x is required"))
                        .build();
            }
            
            if (routeRequest.getCoordinates().getX() <= -617) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("BadRequest", "Coordinates x must be > -617"))
                        .build();
            }
            
            if (routeRequest.getDistance() == null || routeRequest.getDistance() <= 1) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("BadRequest", "Distance is required and must be > 1"))
                        .build();
            }
            
            // Validate LocationTo.x if LocationTo is provided
            if (routeRequest.getTo() != null && routeRequest.getTo().getX() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("BadRequest", "LocationTo x cannot be null"))
                        .build();
            }
            
            Route route = new Route();
            route.setName(routeRequest.getName());
            route.setCoordinates(routeRequest.getCoordinates());
            route.setFrom(routeRequest.getFrom());
            route.setTo(routeRequest.getTo());
            route.setDistance(routeRequest.getDistance());
            
            Route createdRoute = routeService.addRoute(route);
            
            return Response.ok(createdRoute).build();
            
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("InternalServerError", "An error occurred while creating the route"))
                    .build();
        }
    }
    
    /**
     * GET /routes/{id} - Get route by ID
     */
    @GET
    @Path("/routes/{id}")
    public Response getRouteById(@PathParam("id") long id) {
        try {
            if (id < 1) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("BadRequest", "ID must be greater than 0"))
                        .build();
            }
            
            Optional<Route> route = routeService.getRouteById(id);
            
            if (route.isPresent()) {
                return Response.ok(route.get()).build();
            }
            
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("NotFound", "Route not found with id: " + id))
                    .build();
                    
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("InternalServerError", "An error occurred while retrieving the route"))
                    .build();
        }
    }
    
    /**
     * PUT /routes/{id} - Update route by ID (supports partial updates)
     * Only provided fields will be updated, others remain unchanged
     */
    @PUT
    @Path("/routes/{id}")
    public Response updateRoute(@PathParam("id") long id, RouteRequest routeRequest) {
        try {
            if (id < 1) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("BadRequest", "ID must be greater than 0"))
                        .build();
            }
            
            // Get existing route
            Optional<Route> existingRouteOpt = routeService.getRouteById(id);
            if (!existingRouteOpt.isPresent()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("NotFound", "Route not found with id: " + id))
                        .build();
            }
            
            Route existingRoute = existingRouteOpt.get();
            
            // Validate provided fields
            if (routeRequest.getName() != null && routeRequest.getName().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("BadRequest", "Route name cannot be empty"))
                        .build();
            }
            
            if (routeRequest.getCoordinates() != null) {
                if (routeRequest.getCoordinates().getX() == null) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity(new ErrorResponse("BadRequest", "Coordinates x is required"))
                            .build();
                }
                
                if (routeRequest.getCoordinates().getX() <= -617) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity(new ErrorResponse("BadRequest", "Coordinates x must be > -617"))
                            .build();
                }
            }
            
            if (routeRequest.getDistance() != null && routeRequest.getDistance() <= 1) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("BadRequest", "Distance must be > 1"))
                        .build();
            }
            
            if (routeRequest.getTo() != null && routeRequest.getTo().getX() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("BadRequest", "LocationTo x cannot be null"))
                        .build();
            }
            
            // Update only provided fields (partial update)
            if (routeRequest.getName() != null) {
                existingRoute.setName(routeRequest.getName());
            }
            if (routeRequest.getCoordinates() != null) {
                existingRoute.setCoordinates(routeRequest.getCoordinates());
            }
            if (routeRequest.getFrom() != null) {
                existingRoute.setFrom(routeRequest.getFrom());
            }
            if (routeRequest.getTo() != null) {
                existingRoute.setTo(routeRequest.getTo());
            }
            if (routeRequest.getDistance() != null) {
                existingRoute.setDistance(routeRequest.getDistance());
            }
            
            Optional<Route> updatedRoute = routeService.updateRoute(id, existingRoute);
            
            if (updatedRoute.isPresent()) {
                return Response.ok(updatedRoute.get()).build();
            }
            
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("NotFound", "Route not found with id: " + id))
                    .build();
                    
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("InternalServerError", "An error occurred while updating the route"))
                    .build();
        }
    }
    
    /**
     * DELETE /routes/{id} - Delete route by ID
     */
    @DELETE
    @Path("/routes/{id}")
    public Response deleteRoute(@PathParam("id") long id) {
        try {
            if (id < 1) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("BadRequest", "ID must be greater than 0"))
                        .build();
            }
            
            boolean deleted = routeService.deleteRoute(id);
            
            if (deleted) {
                return Response.noContent().build();
            }
            
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("NotFound", "Route not found with id: " + id))
                    .build();
                    
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("InternalServerError", "An error occurred while deleting the route"))
                    .build();
        }
    }
    
    /**
     * GET /route/minimum-name - Get the route with the first name alphabetically
     */
    @GET
    @Path("/route/minimum-name")
    public Response getRouteWithMinimumName() {
        try {
            Optional<Route> route = routeService.getRouteWithMinimumName();
            
            if (route.isPresent()) {
                return Response.ok(route.get()).build();
            }
            
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("NotFound", "No routes found"))
                    .build();
                    
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("InternalServerError", "An error occurred while retrieving the route"))
                    .build();
        }
    }
    
    /**
     * GET /routes/equal-distance/{distance} - Get routes with exact distance
     */
    @GET
    @Path("/routes/equal-distance/{distance}")
    public Response getRoutesWithEqualDistance(@PathParam("distance") long distance) {
        try {
            if (distance <= 1) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("BadRequest", "Distance must be > 1"))
                        .build();
            }
            
            List<Route> routes = routeService.getRoutesWithEqualDistance(distance);
            
            RouteResponse response = new RouteResponse();
            response.setRoutes(routes);
            
            return Response.ok(response).build();
            
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("InternalServerError", "An error occurred while retrieving routes"))
                    .build();
        }
    }
    
    /**
     * GET /routes/less-distance/{distance} - Get routes with distance less than specified
     */
    @GET
    @Path("/routes/less-distance/{distance}")
    public Response getRoutesWithLessDistance(@PathParam("distance") long distance) {
        try {
            if (distance <= 1) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("BadRequest", "Distance must be > 1"))
                        .build();
            }
            
            List<Route> routes = routeService.getRoutesWithLessDistance(distance);
            
            RouteResponse response = new RouteResponse();
            response.setRoutes(routes);
            
            return Response.ok(response).build();
            
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("InternalServerError", "An error occurred while retrieving routes"))
                    .build();
        }
    }
}
