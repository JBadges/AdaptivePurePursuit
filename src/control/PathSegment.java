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
}