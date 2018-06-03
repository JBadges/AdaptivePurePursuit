package control;

import util.Point2;

public class PathSegment {

	private Point2 start;
	private Point2 end;

	/**
	 * Calls the two Point2 constructor
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public PathSegment(int x1, int y1, int x2, int y2) {
		this(new Point2(x1, y1), new Point2(x2, y2));
	}

	/**
	 * Sets both parameters to instance variables
	 * @param start
	 * @param end
	 */
	public PathSegment(Point2 start, Point2 end) {
		this.start = start;
		this.end = end;
	}

	/**
	 * Uses dot and cross product with distance to solve
	 * @param point
	 * @return - if the point is contained within the path segment
	 */
	public boolean isPointContained(Point2 point) {
		double crossProduct = (point.getY() - start.getY()) * (end.getX() - start.getX()) - (point.getX() - start.getX()) * (end.getY() - start.getY());
		if (Math.abs(crossProduct) > 1e-5) {
			return false;
		}

		double dotProduct = (point.getX() - start.getX()) * (end.getX() - start.getX()) + (point.getY() - start.getY()) * (end.getY() - start.getY());
		if (dotProduct < 0) {
			return false;
		}

		double squaredLengthBA = (end.getX() - start.getX()) * (end.getX() - start.getX()) + (end.getY() - start.getY()) * (end.getY() - start.getY());
		if (dotProduct > squaredLengthBA) {
			return false;
		}

		return true;
	}

	public Point2 getStart() {
		return this.start;
	}

	public Point2 getEnd() {
		return this.end;
	}

	/**
	 * Rise over Run
	 * @return the slope value as a decimal
	 */
	public double getSlope() {
		return (this.end.getY() - this.start.getY()) / (this.end.getX() - this.start.getX());
	}
	
	/**
	 * 
	 * @return the y intercept of the line if infinitely extended
	 */
	public double getYInt() {
		return start.getY() - getSlope() * start.getX();
	}

	public double getLength() {
		return start.distanceTo(end);
	}
	
	/**
	 * 
	 * @return the intersection of two lines if both are infinitely extended
	 */
	public Point2 intersection(PathSegment line) {
		double x = (line.getYInt() - this.getYInt()) / (this.getSlope() - line.getSlope());
		double y = this.getSlope() * x + this.getYInt();
		return new Point2(x, y);
	}

}
