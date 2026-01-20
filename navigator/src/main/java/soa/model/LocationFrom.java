package soa.model;

/**
 * Represents the starting location.
 */
public class LocationFrom {
    
    private long x;
    private float y;
    private String name;
    
    public LocationFrom() {
    }
    
    public LocationFrom(long x, float y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }
    
    public long getX() {
        return x;
    }
    
    public void setX(long x) {
        this.x = x;
    }
    
    public float getY() {
        return y;
    }
    
    public void setY(float y) {
        this.y = y;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
