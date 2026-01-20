package soa.model;

/**
 * Represents the destination location.
 */
public class LocationTo {
    
    private Float x;
    private double y;
    private float z;
    
    public LocationTo() {
    }
    
    public LocationTo(Float x, double y, float z) {
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
    
    public double getY() {
        return y;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public float getZ() {
        return z;
    }
    
    public void setZ(float z) {
        this.z = z;
    }
}
