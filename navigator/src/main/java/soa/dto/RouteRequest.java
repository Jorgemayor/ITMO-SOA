package soa.dto;

import soa.model.Coordinates;
import soa.model.LocationFrom;
import soa.model.LocationTo;

/**
 * DTO for creating/updating routes.
 */
public class RouteRequest {
    
    private String name;
    private Coordinates coordinates;
    private LocationFrom from;
    private LocationTo to;
    private Long distance;
    
    public RouteRequest() {
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Coordinates getCoordinates() {
        return coordinates;
    }
    
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
    
    public LocationFrom getFrom() {
        return from;
    }
    
    public void setFrom(LocationFrom from) {
        this.from = from;
    }
    
    public LocationTo getTo() {
        return to;
    }
    
    public void setTo(LocationTo to) {
        this.to = to;
    }
    
    public Long getDistance() {
        return distance;
    }
    
    public void setDistance(Long distance) {
        this.distance = distance;
    }
}
