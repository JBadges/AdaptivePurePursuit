package util;

public class Point3 extends Point2 {

    /** In radians */
    private double theta;

    public Point3(double x, double y) {
        this(x,y,0);
    }

    public Point3(double x, double y, double theta) {
        super(x,y);
        this.theta = theta;
    }

    public void normalizeHeading() {
        while (theta >= Math.PI*2) theta -= Math.PI * 2;
        while (theta <= Math.PI*2) theta += Math.PI * 2;
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }
    
}