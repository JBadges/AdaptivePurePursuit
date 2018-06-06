package util;

public class Point3 extends Point2 {

	/** In radians */
	private double theta;

	public Point3(Point2 point, double theta) {
		this(point.getX(), point.getY(), theta);
	}

	public Point3(double x, double y) {
		this(x, y, 0);
	}

	public Point3(double x, double y, double theta) {
		super(x, y);
		this.theta = theta;
	}

	/**
	 * Adds or substracts 2Pi until the theta is between 0 <= x < 2Pi
	 */
	public void normalizeTheta() {
		while (theta >= Math.PI * 2) {
			theta -= Math.PI * 2;
		}
		while (theta < 0) {
			theta += Math.PI * 2;
		}
	}

	public double getTheta() {
		return theta;
	}

	public void setTheta(double theta) {
		this.theta = theta;
	}

}