package util;

public class Twist {

    /**
     * Velocity in meters/seconds
     */
    private double velocity; 
    /**
     * Curvature in radians/seconds
     */
    private double omega;
    
    public Twist(double v, double w) {
        this.velocity = v;
        this.omega = w;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public void setCurvature(double omega) {
        this.omega = omega;
    }

    public double getVelocity() {
        return this.velocity;
    }

    public double getCurvature() {
        return this.omega;
    }

}