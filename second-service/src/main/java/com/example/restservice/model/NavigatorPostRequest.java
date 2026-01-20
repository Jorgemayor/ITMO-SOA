package com.example.restservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NavigatorPostRequest {
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("coordinates")
    private Coordinates coordinates;

    public NavigatorPostRequest() {
    }

    public NavigatorPostRequest(String name, Coordinates coordinates) {
        this.name = name;
        this.coordinates = coordinates;
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
}

