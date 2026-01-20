package soa.model;

/**
 * Represents 2D coordinates (x, y).
 */
public class Coordinates {
    
    private Long x;
    private double y;
    
    public Coordinates() {
    }
    
    public Coordinates(Long x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public Long getX() {
        return x;
    }
    
    public void setX(Long x) {
        this.x = x;
    }
    
    public double getY() {
        return y;
    }
    
    public void setY(double y) {
        this.y = y;
    }
}
