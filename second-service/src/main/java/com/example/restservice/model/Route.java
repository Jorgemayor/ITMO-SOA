package com.example.restservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public class Route {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("coordinates")
    private Coordinates coordinates;
    
    @JsonProperty("creationDate")
    private LocalDate creationDate;
    
    @JsonProperty("from")
    private LocationFrom from;
    
    @JsonProperty("to")
    private LocationTo to;
    
    @JsonProperty("distance")
    private Long distance;

    public Route() {
    }

    public Route(Long id, String name, Coordinates coordinates, LocalDate creationDate, 
                 LocationFrom from, LocationTo to, Long distance) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }
}

