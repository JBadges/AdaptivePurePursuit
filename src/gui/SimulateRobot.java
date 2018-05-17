package gui;

import control.AdaptivePurePursuit;
import control.PathSegment;
import control.SkidRobot;
import gui.Main.Scenes;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import util.Point2;
import util.Point3;
import util.SpeedPoint;

public class SimulateRobot implements GUI {

    //Time seconds
    private static double currentTime;
    private static double lastUpdate;
    private static AnimationTimer loop;
    private static boolean stopLoop;

    public static Scene getScene() {
        currentTime = 0;
        lastUpdate = 0;
        loop = null;
        stopLoop = false;

        Pane sp = new Pane();
        
        //Draw Lines between points before the waypoints so they are beneath
        for(int i = 0; i < PathCreation.getWaypoints().size()-1; i++) {
            Point2 a = PathCreation.getWaypoints().get(i);
            Point2 b = PathCreation.getWaypoints().get(i+1);
            sp.getChildren().add(new Line(a.getX(), a.getY(), b.getX(), b.getY()));
        }
        //Draw waypoints from PathCreation
        PathCreation.circleColors(PathCreation.getCircles());
        sp.getChildren().addAll(PathCreation.getCircles());

        //Input
        VBox vb_input = new VBox();
        VBox vb_wheelDist = new VBox();
        TextField txtf_wheelDist = new TextField();
        Label lbl_wheelDist = new Label("Robot Wheel Distance");
        vb_wheelDist.getChildren().addAll(lbl_wheelDist, txtf_wheelDist);
        VBox vb_robotMassKg = new VBox();
        TextField txtf_robotMassKg = new TextField();
        Label lbl_robotMassKg = new Label("Robot Mass Kg");
        vb_robotMassKg.getChildren().addAll(lbl_robotMassKg, txtf_robotMassKg);
        Button btn_simulateRobot = new Button("Simulate Robot Path");
        btn_simulateRobot.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(btn_simulateRobot.getText().equals("Start a new path")) {
                    stopLoop = true;
                    loop.stop();
                    Main.changeScene(Scenes.PathCreation);
                }
                //Check user input and show if invalid
                boolean isValidInput = true;
                if (!txtf_wheelDist.getText().matches("-?\\d+(\\.\\d+)?")) {
                    txtf_wheelDist.setText("Must be numeric");
                    isValidInput = false;
                }
                if (!txtf_robotMassKg.getText().matches("-?\\d+(\\.\\d+)?")) {
                    txtf_robotMassKg.setText("Must be numeric");
                    isValidInput = false;
                }
                //Input is valid - Commence robot simulation
                if (isValidInput) {
                    //Robot information setup
                    SkidRobot robot = new SkidRobot(Double.parseDouble(txtf_wheelDist.getText()), 0.1016, 3, Double.parseDouble(txtf_robotMassKg.getText()), 50);
                    Point2 starting = PathCreation.getWaypoints().size() < 1 ? new Point2(0,0) : PathCreation.getWaypoints().get(0);
                    Point2 second = PathCreation.getWaypoints().size() < 2 ? new Point2(0,0) : PathCreation.getWaypoints().get(1);
                    PathSegment startToSecond = new PathSegment(starting, second);
                    robot.setPosition(new Point3(starting.getX(), starting.getY(), second.getX()<starting.getX()?Math.atan(startToSecond.getSlope())+Math.PI:Math.atan(startToSecond.getSlope())));
                    //Robot design setup
                    final double lookahead = 100;
                    final Circle robotDebugBase = new Circle(-100, -100, 10);
                    final Circle robotLookahead = new Circle(-100, -100, lookahead);
                    final Circle goalPointDebug = new Circle(-100, -100, 5);
                    final Line robotHeadingLine = new Line(robotDebugBase.getCenterX(), robotDebugBase.getCenterY(), 5*Math.cos(robot.getPosition().getTheta()), 5*Math.sin(robot.getPosition().getTheta()));
                    AdaptivePurePursuit app = new AdaptivePurePursuit(PathCreation.getPath(), lookahead);
                    loop = new AnimationTimer(){
                        @Override
                        public void handle(long now) {
                            btn_simulateRobot.setText("Start a new path");
                            if(currentTime > 60 || stopLoop) {
                                this.stop();
                            }
                            final double dt = (now-lastUpdate) / 1000000000.0;
                            
                            SpeedPoint twist = app.update(robot.getPosition());
                            double[] voltages = app.getVoltageFromTwist(twist, robot);
                            double voltageLeft = voltages[0];
                            double voltageRight = voltages[1];
                            System.out.printf("Left %.6f | Right %.6f\n", voltageLeft, voltageRight);

                            robot.updatePos(dt, voltageLeft, voltageRight);

                            currentTime += dt;

                            //Update display
                            Point3 goalP = app.getGoalPoint(robot.getPosition(), lookahead);
                            goalPointDebug.setCenterX(goalP.getX());
                            goalPointDebug.setCenterY(goalP.getY());
                            goalPointDebug.setFill(new Color(1,0,0,0.5));
                            robotDebugBase.setCenterX(robot.getPosition().getX());
                            robotDebugBase.setCenterY(robot.getPosition().getY());
                            robotLookahead.setCenterX(robot.getPosition().getX());
                            robotLookahead.setCenterY(robot.getPosition().getY());
                            robotHeadingLine.setStartX(robotDebugBase.getCenterX());
                            robotHeadingLine.setStartY(robotDebugBase.getCenterY());
                            robotHeadingLine.setEndX(robotHeadingLine.getStartX() + robotDebugBase.getRadius()*Math.cos(robot.getPosition().getTheta()));
                            robotHeadingLine.setEndY(robotHeadingLine.getStartY() + robotDebugBase.getRadius()*Math.sin(robot.getPosition().getTheta()));
                            robotLookahead.setFill(new Color(0, 1, 0.4, 0.1));
                            robotDebugBase.setFill(new Color(0, 1, 0, 0.3));
                            robotDebugBase.setRotate(Math.toDegrees(robot.getPosition().getTheta()));
                            robotHeadingLine.setFill(new Color(1,1,1,1));

                            //Delay 10ms
                            try {
								Thread.sleep(10);
							} catch (InterruptedException e) {
								e.printStackTrace();
                            }
                            //To calculate change in time
                            lastUpdate = now;
                        }
                    };

                    lastUpdate = System.nanoTime();
                    loop.start();

                    sp.getChildren().addAll(robotDebugBase, robotHeadingLine, robotLookahead, goalPointDebug);
                }
            }
        });

        vb_input.getChildren().addAll(vb_wheelDist, vb_robotMassKg, btn_simulateRobot);

        sp.getChildren().add(vb_input);

        Scene scene = new Scene(sp, 800, 600);
        return scene;
    }
    
}