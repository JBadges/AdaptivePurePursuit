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

	/**
	 * The distance from this point to the parameter point
	 * 
	 * @param point
	 * @return sqrt(x^2 + y^2)
	 */
	public double distanceTo(Point2 point) {
		return Math.sqrt((point.getX() - this.getX()) * (point.getX() - this.getX()) + (point.getY() - this.getY()) * (point.getY() - this.getY()));
	}

	/**
	 * The dot product of the x,y values of the points
	 * 
	 * @param a
	 * @return
	 */
	public double dot(Point2 a) {
		return getX() * a.getX() + getY() * a.getY();
	}

	/**
	 * The cross product of the x,y values of the points
	 * 
	 * @param a
	 * @return
	 */
	public double cross(Point2 a) {
		return getX() * a.getY() - getY() * a.getX();
	}

	@Override
	public String toString() {
		return "x: " + x + " y: " + y;
	}

	@Override
	public boolean equals(Object obj) {
		return Math.abs(((Point2) obj).getX() - this.getX()) < 1e-5 && Math.abs(((Point2) obj).getY() - this.getY()) < 1e-5;
	}

}