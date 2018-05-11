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

    public SpeedPoint update(Point3 pose) {
    	double curX = pose.getX();
    	double curY = pose.getY();
    	
        Point3 goalPoint = path.getGoalPoint(pose, lookahead);
        double mMirror = (curX-goalPoint.getX())/(goalPoint.getY()-curY);
        double midX = (curX+goalPoint.getX())/2;
        double midY = (curY+goalPoint.getY())/2;
        double bMirror = midY-midX*mMirror;
        double mPerp = -1/(Math.atan(pose.getHeading()));
        double bPerp = curY-mPerp*curX;
        double centreX = (bMirror-bPerp)/(mPerp-mMirror);
        double centreY = centreX*mPerp+bPerp;
        double radius = Math.sqrt(Math.pow(curX-centreX,2)+Math.pow(curY-centreY,2));
        double updateX = curX+10;
        double updateY = curY+10*Math.atan(pose.getHeading());
        
        boolean isRightTurn = (updateX-curX)*(goalPoint.getY()-curY)-(goalPoint.getX()-curX)*(updateY-curY)>0;
        
        double velocity = 1;
        double omega = velocity/radius*isRightTurn?1:-1;
        
        return new SpeedPoint(velocity, omega);
    }
}