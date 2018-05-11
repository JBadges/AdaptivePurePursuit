package control;

public class PathSegment {

    private Point3 start;
    private Point3 end;

    public PathSegment(int x1, int y1, int x2, int y2) {
        this.start = new Point3(x1, y1);
        this.end  = new Point3(x2,y2);
    }

    public PathSegment(Point3 start, Point3 end) {
        this.start = start;
        this.end = end;
    }
    
    public Point3 getStart() {
        return this.start;
    }

    public Point3 getEnd() {
        return this.end;
    }
    
	public double getSlope() {
    	return (this.end.getY()-this.start.getY())/(this.end.getX()-this.start.getX());
	}

	public double getYInt() {
    	return start.getY() - getSlope() * start.getX();
	}
	
	public double getLength() {
		return start.distanceTo(end);
	}
    
	public Point3 intersection(PathSegment line) {
    	double x = (line.getYInt() - this.getYInt()) / (this.getSlope() - line.getSlope());
	    double y = this.getSlope() * x + this.getYInt();
	    return new Point3(x, y, 0);
	}
    
	public boolean isPointContained(Point3 point) {
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
}