package control;

import java.util.ArrayList;
import java.util.List;

import util.Point2;
import util.Point3;

public class Path {

    private List<PathSegment> segments;

    public Path(Point2... waypoints) {
        if(waypoints.length < 2) {
            throw new Error("Must have at least 2 waypoints to create a path");
        }
        segments = new ArrayList<>();

        //Create a line between all the points
        for(int i = 0; i < waypoints.length-1; i++) {
            segments.add(new PathSegment(waypoints[i], waypoints[i+1]));
        }
    }
}