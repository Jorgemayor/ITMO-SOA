package soa.service;

import soa.model.Route;
import soa.model.Coordinates;
import soa.model.LocationFrom;
import soa.model.LocationTo;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Service class for managing routes with advanced filtering, sorting, and pagination.
 */
@ApplicationScoped
public class RouteService {
    
    private final Map<Long, Route> routes = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    public RouteService() {
        addRoute(createSampleRoute(null, "Route to City Center", 
                new Coordinates(10L, 20.3),
                new LocationFrom(5L, 10.0f, "Starting Point A"),
                new LocationTo(15.0f, 25.0, 5.0f),
                150L));
        
        addRoute(createSampleRoute(null, "Mountain Pass Route",
                new Coordinates(30L, 45.8),
                new LocationFrom(25L, 40.0f, "Base Camp"),
                new LocationTo(35.0f, 50.0, 1200.0f),
                500L));
        
        addRoute(createSampleRoute(null, "Coastal Highway",
                new Coordinates(60L, 12.4),
                new LocationFrom(55L, 10.0f, "Port City"),
                new LocationTo(70.0f, 15.0, 10.0f),
                320L));
    }
    
    private Route createSampleRoute(Long id, String name, Coordinates coordinates, 
                                   LocationFrom from, LocationTo to, long distance) {
        Route route = new Route();
        if (id != null) {
            route.setId(id);
        }
        route.setName(name);
        route.setCoordinates(coordinates);
        route.setCreationDate(LocalDate.now());
        route.setFrom(from);
        route.setTo(to);
        route.setDistance(distance);
        return route;
    }
    
    /**
     * Get all routes with filtering, sorting and pagination
     */
    public List<Route> getRoutes(Integer page, Integer pageSize, Integer limit, Integer offset,
                                  List<String> sortFields, String sortDirection, List<String> filters) {
        Stream<Route> stream = routes.values().stream();
        
        if (filters != null && !filters.isEmpty()) {
            stream = applyFilters(stream, filters);
        }
        
        if (sortFields != null && !sortFields.isEmpty()) {
            stream = applySorting(stream, sortFields, sortDirection);
        }
        
        // pagination (offset/limit takes precedence)
        if (offset != null && limit != null) {
            return stream.skip(offset).limit(limit).collect(Collectors.toList());
        } else if (page != null && pageSize != null) {
            return stream.skip((long) page * pageSize).limit(pageSize).collect(Collectors.toList());
        }
        
        return stream.collect(Collectors.toList());
    }
    
    /**
     * Get total count of routes matching filters
     */
    public int getTotalCount(List<String> filters) {
        Stream<Route> stream = routes.values().stream();
        if (filters != null && !filters.isEmpty()) {
            stream = applyFilters(stream, filters);
        }
        return (int) stream.count();
    }
    
    /**
     * Apply filters to the stream
     * Filter format: field[operator]=value
     * Operators: eq, ne, gt, lt, gte, lte
     */
    private Stream<Route> applyFilters(Stream<Route> stream, List<String> filters) {
        for (String filter : filters) {
            Pattern pattern = Pattern.compile("^([^\\[]+)\\[([^\\]]+)\\]=(.+)$");
            Matcher matcher = pattern.matcher(filter);
            
            if (matcher.matches()) {
                String field = matcher.group(1);
                String operator = matcher.group(2);
                String value = matcher.group(3);
                
                stream = stream.filter(route -> matchesFilter(route, field, operator, value));
            }
        }
        return stream;
    }

    /**
     * Check if a route matches a filter condition
     */
    private boolean matchesFilter(Route route, String field, String operator, String value) {
        try {
            switch (field) {
                case "id":
                    return compareNumbers(route.getId(), operator, Long.parseLong(value));
                case "name":
                    return compareStrings(route.getName(), operator, value);
                case "coordinates.x":
                    return compareNumbers(route.getCoordinates().getX(), operator, Long.parseLong(value));
                case "coordinates.y":
                    return compareNumbers(route.getCoordinates().getY(), operator, Double.parseDouble(value));
                case "creationDate":
                    return compareDates(route.getCreationDate(), operator, LocalDate.parse(value));
                case "from.x":
                    return route.getFrom() != null && compareNumbers(route.getFrom().getX(), operator, Long.parseLong(value));
                case "from.y":
                    return route.getFrom() != null && compareNumbers(route.getFrom().getY(), operator, Float.parseFloat(value));
                case "from.name":
                    return route.getFrom() != null && compareStrings(route.getFrom().getName(), operator, value);
                case "to.x":
                    return route.getTo() != null && compareNumbers(route.getTo().getX(), operator, Float.parseFloat(value));
                case "to.y":
                    return route.getTo() != null && compareNumbers(route.getTo().getY(), operator, Double.parseDouble(value));
                case "to.z":
                    return route.getTo() != null && compareNumbers(route.getTo().getZ(), operator, Float.parseFloat(value));
                case "distance":
                    return compareNumbers(route.getDistance(), operator, Long.parseLong(value));
                default:
                    return true;
            }
        } catch (NumberFormatException | java.time.format.DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid value format for field '" + field + "': " + value);
        } catch (Exception e) {
            return true;
        }
    }
    
    private boolean compareNumbers(Number actual, String operator, Number expected) {
        double a = actual.doubleValue();
        double e = expected.doubleValue();
        
        switch (operator) {
            case "eq": return a == e;
            case "ne": return a != e;
            case "gt": return a > e;
            case "lt": return a < e;
            case "gte": return a >= e;
            case "lte": return a <= e;
            default: return true;
        }
    }
    
    private boolean compareStrings(String actual, String operator, String expected) {
        if (actual == null) return false;
        
        switch (operator) {
            case "eq": return actual.equals(expected);
            case "ne": return !actual.equals(expected);
            default: return true;
        }
    }
    
    private boolean compareDates(LocalDate actual, String operator, LocalDate expected) {
        if (actual == null) return false;
        
        switch (operator) {
            case "eq": return actual.isEqual(expected);
            case "ne": return !actual.isEqual(expected);
            case "gt": return actual.isAfter(expected);
            case "lt": return actual.isBefore(expected);
            case "gte": return actual.isAfter(expected) || actual.isEqual(expected);
            case "lte": return actual.isBefore(expected) || actual.isEqual(expected);
            default: return true;
        }
    }
    
    /**
     * Apply sorting to the stream
     */
    private Stream<Route> applySorting(Stream<Route> stream, List<String> sortFields, String sortDirection) {
        Comparator<Route> comparator = null;
        
        for (String field : sortFields) {
            Comparator<Route> fieldComparator = getComparatorForField(field);
            if (fieldComparator != null) {
                comparator = (comparator == null) ? fieldComparator : comparator.thenComparing(fieldComparator);
            }
        }
        
        if (comparator != null) {
            if ("DESC".equalsIgnoreCase(sortDirection)) {
                comparator = comparator.reversed();
            }
            stream = stream.sorted(comparator);
        }
        
        return stream;
    }
    
    /**
     * Get comparator for a specific field
     */
    private Comparator<Route> getComparatorForField(String field) {
        switch (field) {
            case "id":
                return Comparator.comparing(Route::getId);
            case "name":
                return Comparator.comparing(Route::getName);
            case "creationDate":
                return Comparator.comparing(Route::getCreationDate);
            case "distance":
                return Comparator.comparing(Route::getDistance);
            case "coordinates":
                return Comparator.<Route, Long>comparing(r -> r.getCoordinates().getX())
                        .thenComparing(r -> r.getCoordinates().getY());
            case "from":
                return Comparator.<Route, Long>comparing(r -> r.getFrom() != null ? r.getFrom().getX() : 0L);
            case "to":
                return Comparator.<Route, Float>comparing(r -> r.getTo() != null ? r.getTo().getX() : 0.0f);
            default:
                return null;
        }
    }
    
    /**
     * Get a route by ID
     */
    public Optional<Route> getRouteById(long id) {
        return Optional.ofNullable(routes.get(id));
    }
    
    /**
     * Add a new route
     */
    public Route addRoute(Route route) {
        long id = idGenerator.getAndIncrement();
        route.setId(id);
        if (route.getCreationDate() == null) {
            route.setCreationDate(LocalDate.now());
        }
        routes.put(id, route);
        return route;
    }
    
    /**
     * Update an existing route
     */
    public Optional<Route> updateRoute(long id, Route updatedRoute) {
        if (!routes.containsKey(id)) {
            return Optional.empty();
        }
        updatedRoute.setId(id);
        // Preserve original creation date
        Route existingRoute = routes.get(id);
        updatedRoute.setCreationDate(existingRoute.getCreationDate());
        routes.put(id, updatedRoute);
        return Optional.of(updatedRoute);
    }
    
    /**
     * Delete a route
     */
    public boolean deleteRoute(long id) {
        return routes.remove(id) != null;
    }
    
    /**
     * Get the route with the minimum (first alphabetically) name
     */
    public Optional<Route> getRouteWithMinimumName() {
        return routes.values().stream()
                .min(Comparator.comparing(Route::getName));
    }
    
    /**
     * Get routes with exact distance
     */
    public List<Route> getRoutesWithEqualDistance(long distance) {
        return routes.values().stream()
                .filter(r -> r.getDistance() == distance)
                .collect(Collectors.toList());
    }
    
    /**
     * Get routes with distance less than specified
     */
    public List<Route> getRoutesWithLessDistance(long distance) {
        return routes.values().stream()
                .filter(r -> r.getDistance() < distance)
                .collect(Collectors.toList());
    }
}
