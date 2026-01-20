package com.example.restservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Coordinates {
    
    @JsonProperty("x")
    private Integer x;
    
    @JsonProperty("y")
    private Double y;

    public Coordinates() {
    }

    public Coordinates(Integer x, Double y) {
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }
}

