package com.example.restservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RouteRequest {
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("coordinates")
    private Coordinates coordinates;
    
    @JsonProperty("from")
    private LocationFrom from;
    
    @JsonProperty("to")
    private LocationTo to;
    
    @JsonProperty("distance")
    private Long distance;

    public RouteRequest() {
    }

    public RouteRequest(String name, Coordinates coordinates, LocationFrom from, 
                       LocationTo to, Long distance) {
        this.name = name;
        this.coordinates = coordinates;
        this.from = from;
        this.to = to;
        this.distance = distance;
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

