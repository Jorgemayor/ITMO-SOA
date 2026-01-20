package soa.model;

import java.time.LocalDate;

/**
 * Route entity representing a route with coordinates and locations.
 */
public class Route {
    
    private long id;
    private String name;
    private Coordinates coordinates;
    private LocalDate creationDate;
    private LocationFrom from;
    private LocationTo to;
    private long distance;
    
    public Route() {
    }
    
    public Route(long id, String name, Coordinates coordinates, LocalDate creationDate, 
                 LocationFrom from, LocationTo to, long distance) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.from = from;
        this.to = to;
        this.distance = distance;
    }
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
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
    
    public LocalDate getCreationDate() {
        return creationDate;
    }
    
    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
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
    
    public long getDistance() {
        return distance;
    }
    
    public void setDistance(long distance) {
        this.distance = distance;
    }
}
