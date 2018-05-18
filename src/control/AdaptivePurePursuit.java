package control;

import util.Point3;
import util.SpeedPoint;

public class AdaptivePurePursuit {

    private Path path;
    private double lookahead;

    public AdaptivePurePursuit (Path path, double lookahead) {
        this.path = path;
        this.lookahead = lookahead;
    }

    public double[] getVoltageFromTwist(SpeedPoint twist, SkidRobot robot) {
        double leftCommand = (twist.getVelocity() - twist.getCurvature() * robot.getWheelDistance()/2.0)/robot.getWheelRadius();
        double rightCommand = (twist.getVelocity() + twist.getCurvature() * robot.getWheelDistance()/2.0)/robot.getWheelRadius();
        return new double[] {leftCommand, rightCommand};
    }

    public SpeedPoint update(Point3 pose) {
    	double curX = pose.getX();
    	double curY = pose.getY();
    	
        Point3 goalPoint = getGoalPoint(pose, lookahead);
        if (goalPoint == null) {
            throw new Error("Goal Point is null");
        }
        double mMirror = (curX-goalPoint.getX())/(goalPoint.getY()-curY);
        double midX = (curX+goalPoint.getX())/2;
        double midY = (curY+goalPoint.getY())/2;
        double bMirror = midY-midX*mMirror;
        double mPerp = -1/(Math.atan(pose.getTheta()));
        double bPerp = curY-mPerp*curX;
        double centreX = (bMirror-bPerp)/(mPerp-mMirror);
        double centreY = centreX*mPerp+bPerp;
        double radius = Math.sqrt(Math.pow(curX-centreX,2)+Math.pow(curY-centreY,2));
        double updateX = curX+10;
        double updateY = curY+10*Math.atan(pose.getTheta());
        
        boolean isRightTurn = (updateX-curX)*(goalPoint.getY()-curY)-(goalPoint.getX()-curX)*(updateY-curY)>0;
        
        double velocity = 1;
        double omega = velocity/radius*(isRightTurn?1:-1);
        
        return new SpeedPoint(velocity, omega);
    }
    
    public Point3 getGoalPoint(Point3 position, double lookahead) {
        Point3 minPoint = null;

        // for(int i = path.getSegments().size()-1; i >= 0; i--) {
        for(int i = 0; i < path.getSegments().size(); i++) {
            PathSegment curSegment = path.getSegments().get(i);

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
            }
        }
        
        return minPoint;
    }
}