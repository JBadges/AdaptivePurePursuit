package control;

import java.util.ArrayList;
import java.util.List;
import util.Point3;

public class Path {

    private List<PathSegment> segments;

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
        Point3 minPoint = null;

        for(int i=this.segments.size()-1; i>=0; i--) {
            PathSegment curSegment = this.segments.get(i);

            double relStartX = curSegment.getStart().getX() - position.getX();
            double relStartY = curSegment.getStart().getY() - position.getY();
            double relEndX = curSegment.getEnd().getX() - position.getX();
            double relEndY = curSegment.getEnd().getY() - position.getY();
            double dX = relEndX-relStartX;
            double dY = relEndY-relStartY;
            double dR = Math.sqrt(dX*dX+dY*dY);

            double D = relStartX*relEndY-relEndX*relStartY;
            double discrim = lookahead*lookahead*dR*dR-D*D;

            if(Math.abs(discrim) < 1e-5) {
                double x = (D*dY)/(dR*dR) + position.getX();
                double y = (-1*D*dX)/(dR*dR) + position.getY();
                minPoint = new Point3(x, y, Math.atan(curSegment.getSlope()));
            } else if (discrim>0) {
                double x1 = (D*dY+(dY<0?-1:1)*dX*Math.sqrt(discrim))/(dR*dR)+position.getX();
                double y1 = (-1*D*dX+Math.abs(dY)*Math.sqrt(discrim))/(dR*dR)+position.getY();
                double distEnd1 = Math.sqrt(Math.pow(curSegment.getEnd().getX()-x1, 2)+Math.pow(curSegment.getEnd().getY()-y1,2));
                boolean oneContained = curSegment.isPointContained(new Point3(x1,y1,0));

                double x2 = (D*dY-(dY<0?-1:1)*dX*Math.sqrt(discrim))/(dR*dR)+position.getX();
                double y2 = (-1*D*dX-Math.abs(dY)*Math.sqrt(discrim))/(dR*dR)+position.getY();
                double distEnd2 = Math.sqrt(Math.pow(curSegment.getEnd().getX()-x2, 2)+Math.pow(curSegment.getEnd().getY()-y2,2));
                boolean twoContained = curSegment.isPointContained(new Point3(x2,y2,0));

                if(oneContained&&twoContained) {
                    if(distEnd1<distEnd2) {
                        minPoint = new Point3(x1,y1,Math.atan(curSegment.getSlope()));
                    } else {
                        minPoint = new Point3(x2,y2,Math.atan(curSegment.getSlope()));
                    }
                } else if (oneContained) {
                    minPoint = new Point3(x1,y1,Math.atan(curSegment.getSlope()));
                } else if (twoContained) {
                    minPoint = new Point3(x2,y2,Math.atan(curSegment.getSlope()));
                }
            } else {
                continue;
            }
        }
        
        return minPoint;
    }

}