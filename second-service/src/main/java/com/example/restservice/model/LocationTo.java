package com.example.restservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LocationTo {
    
    @JsonProperty("x")
    private Float x;
    
    @JsonProperty("y")
    private Double y;
    
    @JsonProperty("z")
    private Integer z;

    public LocationTo() {
    }

    public LocationTo(Float x, Double y, Integer z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Integer getZ() {
        return z;
    }

    public void setZ(Integer z) {
        this.z = z;
    }
}

