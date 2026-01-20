package com.example.restservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LocationFrom {
    
    @JsonProperty("x")
    private Long x;
    
    @JsonProperty("y")
    private Double y;
    
    @JsonProperty("name")
    private String name;

    public LocationFrom() {
    }

    public LocationFrom(Long x, Double y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

