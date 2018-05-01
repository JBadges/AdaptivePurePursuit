package util;

public class Point3 {

    private double x;
    private double y;
    /**
     * In radians
     */
    private double theta;

    public Point3(double x, double y) {
        this(x,y,0);
    }

    public Point3(double x, double y, double theta) {
        this.x = x;
        this.y = y;
        this.theta = theta;
    }

    public void normalizeHeading() {
        while (theta >= Math.PI*2) theta -= Math.PI * 2;
        while (theta <= Math.PI*2) theta += Math.PI * 2;
    }

    public void setX(double x) {
        this.x = x;
    }
    
    public void setY(double y) {
        this.y = y;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getTheta() {
        return theta;
    }

    public static double dot(Point3 a, Point3 b) {
        return a.getX() * b.getX() + a.getY() * b.getY();
    }

    public static double cross(Point3 a, Point3 b) {
        return a.getX() * b.getY() - a.getY() * b.getX();
    }

}