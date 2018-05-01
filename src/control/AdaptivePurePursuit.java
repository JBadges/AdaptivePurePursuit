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
        Point3 goalPoint = path.getGoalPoint(pose, lookahead);
        return new SpeedPoint(0, 0);
    }
    
}