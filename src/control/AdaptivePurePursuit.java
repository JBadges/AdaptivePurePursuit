package control;

import util.Point3;
import util.Twist;

public class AdaptivePurePursuit {

	private final Path path;
	private final double lookahead;

	/**
	 * 
	 * @param path
	 *            - The ordered path for the robot to follow
	 * @param lookahead
	 *            - The distance the robot looks for points in meters
	 */
	public AdaptivePurePursuit(Path path, double lookahead) {
		this.path = path;
		this.lookahead = lookahead;
	}

	/**
	 * 
	 * @param twist
	 *            - the velocities it needs to achieve
	 * @param robot
	 *            - the constants class for the conversion
	 * @return the voltage for left[0] and right[1]
	 */
	public double[] getVoltageFromTwist(Twist twist, SkidRobot robot) {
		double leftCommand = (twist.getVelocity() - twist.getCurvature() * robot.getWheelDistance() / 2.0) / robot.getWheelRadius();
		double rightCommand = (twist.getVelocity() + twist.getCurvature() * robot.getWheelDistance() / 2.0) / robot.getWheelRadius();

		double max = Math.max(Math.abs(leftCommand), Math.abs(rightCommand));
		if (max > 12) {
			leftCommand /= max;
			rightCommand /= max;
			leftCommand *= 12;
			rightCommand *= 12;
		}

		return new double[] { leftCommand, rightCommand };
	}

	/**
	 * 
	 * @param pose
	 *            - the current x,y,theta position of the robot
	 * @param robot
	 *            - the robot constants
	 * @return the twist value to go to the desired location
	 */
	public Twist update(Point3 pose, SkidRobot robot) {
		double curX = pose.getX();
		double curY = pose.getY();

		Point3 goalPoint = getGoalPoint(pose, lookahead);
		if (goalPoint == null) {
			throw new NullPointerException();
		}

		double mMirror = (curX - goalPoint.getX()) / (goalPoint.getY() - curY);
		double midX = (curX + goalPoint.getX()) / 2;
		double midY = (curY + goalPoint.getY()) / 2;
		double bMirror = midY - midX * mMirror;
		double mPerp = -1 / (Math.atan(pose.getTheta()));
		double bPerp = curY - mPerp * curX;
		double centerX = (bMirror - bPerp) / (mPerp - mMirror);
		double centerY = centerX * mPerp + bPerp;
		double radius = Math.sqrt(Math.pow(curX - centerX, 2) + Math.pow(curY - centerY, 2));
		double updateX = curX + 10 * Math.cos(pose.getTheta());
		double updateY = curY + 10 * Math.sin(pose.getTheta());

		boolean isRightTurn = (updateX - curX) * (goalPoint.getY() - curY) - (goalPoint.getX() - curX) * (updateY - curY) > 0;

		double velocity = 1;
		if (path.getSegments().get(path.getSegments().size() - 1).getEnd().equals(goalPoint)) {
			double correctedDist = pose.distanceTo(goalPoint);
			velocity = (correctedDist < 1e-2 ? 0 : correctedDist) * ((lookahead / robot.getMass()) * 0.02);
		}
		double omega = velocity / radius * (isRightTurn ? 1 : -1);

		return new Twist(velocity, omega);
	}

	/**
	 * 
	 * @param position
	 *            - the current x,y,theta position of the robot
	 * @param lookahead
	 *            - the raidus around the robot it will look for points
	 * @return the point closest to the end of the path 1 lookahead distance
	 *         away
	 */
	public Point3 getGoalPoint(Point3 position, double lookahead) {
		Point3 minPoint = null;

		if (position.distanceTo(path.getSegments().get(path.getSegments().size() - 1).getEnd()) <= lookahead) {
			return new Point3(path.getSegments().get(path.getSegments().size() - 1).getEnd(), Math.atan(path.getSegments().get(path.getSegments().size() - 1).getSlope()));
		}

		for (int i = 0; i < path.getSegments().size(); i++) {
			PathSegment curSegment = path.getSegments().get(i);

			double relStartX = curSegment.getStart().getX() - position.getX();
			double relStartY = curSegment.getStart().getY() - position.getY();
			double relEndX = curSegment.getEnd().getX() - position.getX();
			double relEndY = curSegment.getEnd().getY() - position.getY();
			double dX = relEndX - relStartX;
			double dY = relEndY - relStartY;
			double dR = Math.sqrt(dX * dX + dY * dY);

			double D = relStartX * relEndY - relEndX * relStartY;
			double discrim = lookahead * lookahead * dR * dR - D * D;

			if (Math.abs(discrim) < 1e-5) {
				double x = (D * dY) / (dR * dR) + position.getX();
				double y = (-1 * D * dX) / (dR * dR) + position.getY();
				minPoint = new Point3(x, y, curSegment.getStart().getX() > curSegment.getEnd().getX() ? Math.atan(curSegment.getSlope()) + Math.PI : Math.atan(curSegment.getSlope()));
			} else if (discrim > 0) {
				double x1 = (D * dY + (dY < 0 ? -1 : 1) * dX * Math.sqrt(discrim)) / (dR * dR) + position.getX();
				double y1 = (-1 * D * dX + Math.abs(dY) * Math.sqrt(discrim)) / (dR * dR) + position.getY();
				double distEnd1 = Math.sqrt(Math.pow(curSegment.getEnd().getX() - x1, 2) + Math.pow(curSegment.getEnd().getY() - y1, 2));
				boolean oneContained = curSegment.isPointContained(new Point3(x1, y1, 0));

				double x2 = (D * dY - (dY < 0 ? -1 : 1) * dX * Math.sqrt(discrim)) / (dR * dR) + position.getX();
				double y2 = (-1 * D * dX - Math.abs(dY) * Math.sqrt(discrim)) / (dR * dR) + position.getY();
				double distEnd2 = Math.sqrt(Math.pow(curSegment.getEnd().getX() - x2, 2) + Math.pow(curSegment.getEnd().getY() - y2, 2));
				boolean twoContained = curSegment.isPointContained(new Point3(x2, y2, 0));

				if (oneContained && twoContained) {
					if (distEnd1 < distEnd2) {
						minPoint = new Point3(x1, y1, curSegment.getStart().getX() > curSegment.getEnd().getX() ? Math.atan(curSegment.getSlope()) + Math.PI : Math.atan(curSegment.getSlope()));
					} else {
						minPoint = new Point3(x2, y2, curSegment.getStart().getX() > curSegment.getEnd().getX() ? Math.atan(curSegment.getSlope()) + Math.PI : Math.atan(curSegment.getSlope()));
					}
				} else if (oneContained) {
					minPoint = new Point3(x1, y1, curSegment.getStart().getX() > curSegment.getEnd().getX() ? Math.atan(curSegment.getSlope()) + Math.PI : Math.atan(curSegment.getSlope()));
				} else if (twoContained) {
					minPoint = new Point3(x2, y2, curSegment.getStart().getX() > curSegment.getEnd().getX() ? Math.atan(curSegment.getSlope()) + Math.PI : Math.atan(curSegment.getSlope()));
				}
			}
		}

		return minPoint;
	}

}