package control;

import util.Point3;

public class SkidRobot {

    // Stall Torque in N m
    private static double stallTorque = 2.402;
    // Stall Current in Amps
    private static double stallCurrent = 126.145;
    // Free Speed in RPM
    private static double freeSpeed = 5015.562;
    // Free Current in Amps
    private static double freeCurrent = 1.170;

    // Number of motors
    private static double numMotors = 2.0;
    // Resistance of the motor
    private static double resistance = 12.0 / stallCurrent;
    // Motor velocity constant
    private static double Kv = ((freeSpeed / 60.0 * 2.0 * Math.PI) / (12.0 - resistance * freeCurrent));
    // Torque constant
    private static double Kt = (numMotors * stallTorque) / stallCurrent;
    // Gear ratio
    private static double kG = 10.71;
    // Radius of pulley
    private static double kr = 0.065786 / 2.0;

    private static double speedModifier = 15.0;

    public Point3 position;
    //In m/s
    public double leftVelocity;
    public double rightVelocity;

    //In Kg
    public double mass;
    //In m
    public double robotWheelDistance;



    public void updateLeftVelocity(double dt, double voltage) {
        double acceleration = -Kt * kG * kG / (Kv * resistance * kr * kr * mass) * leftVelocity + kG * Kt / (resistance * kr * mass) * voltage;
        leftVelocity += dt * acceleration;
    }

    public void updateRightVelcoty(double dt, double voltage) {
        double acceleration = -Kt * kG * kG / (Kv * resistance * kr * kr * mass) * rightVelocity + kG * Kt / (resistance * kr * mass) * voltage;
        rightVelocity += dt * acceleration;
    }

    public void updatePos(double dt, double voltageLeft, double voltageRight) {
        voltageLeft *= speedModifier;
        voltageRight *= speedModifier;
        while (dt > 0) {
            double current_dt = Math.min(dt, 0.001);
            double Dl = leftVelocity * current_dt;
            double Dr = rightVelocity * current_dt;
            double radius = robotWheelDistance * (Dl + Dr) / (2 * (Dr - Dl));
            radius = Double.isNaN(radius) || Double.isInfinite(radius) ? 1e6 : radius;
            double newX;
            double newY;
            double newHeading;
            if (Math.abs(Dl - Dr) < 1.0e-6) { // basically going straight
                double dist = (Dr + Dl) / 2.0;
                newX = Math.cos(position.getTheta()) * dist + position.getX();
                newY = Math.sin(position.getTheta()) * dist + position.getY();
                newHeading = position.getTheta();
            } else {
                double headingChange = (Dr - Dl) / robotWheelDistance;
                newX = position.getX() + radius * Math.sin(headingChange + position.getTheta()) - radius * Math.sin(position.getTheta());
                newY = position.getY() - radius * Math.cos(headingChange + position.getTheta()) + radius * Math.cos(position.getTheta());
                newHeading = headingChange + position.getTheta();
            }
            position.setX(newX);
            position.setY(newY);
            position.setTheta(newHeading);

            updateLeftVelocity(current_dt, voltageLeft);
            updateRightVelcoty(current_dt, voltageRight);

            dt -= 0.001;
        }
    }
}