package control;

import java.util.List;

import util.Point3;

public class Path {

    List<PathSegment> segments;

    public Path(Point3... waypoints) {
        if(waypoints.length < 2) {
            throw new Error("Must have at least 2 waypoints to create a path");
        }
        segments = new ArrayList<>();
        
        //Create a line between all the points
        for(int i = 0; i < waypoints.length-1; i++) {
            segments.add(new PathSegment(waypoints[i], waypoints[i+1]));
        }
    }

    public Point3 getGoalPoint(Point3 position, double lookahead) {
        return new Point3(0, 0);
    }

}