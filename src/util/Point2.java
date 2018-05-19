package util;

public class Point2 {

	private double x;
	private double y;

	public Point2(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Point2(Point2 point) {
		this.x = point.getX();
		this.y = point.getY();
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double distanceTo(Point2 point) {
		return Math.sqrt((point.getX() - this.getX()) * (point.getX() - this.getX()) + (point.getY() - this.getY()) * (point.getY() - this.getY()));
	}

	public static double dot(Point3 a, Point3 b) {
		return a.getX() * b.getX() + a.getY() * b.getY();
	}

	public static double cross(Point3 a, Point3 b) {
		return a.getX() * b.getY() - a.getY() * b.getX();
	}

	@Override
	public String toString() {
		return "x: " + x + " y: " + y;
	}

}