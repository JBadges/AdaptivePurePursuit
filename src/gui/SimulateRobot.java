package gui;

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

public class SimulateRobot implements GUI {

    //Time seconds
    private static double currentTime;
    private static double lastUpdate;
    private static AnimationTimer loop;

    public static Scene getScene() {
        currentTime = 0;
        lastUpdate = 0;
        loop = null;
        currentTime = 0.0;

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
                    SkidRobot robot = new SkidRobot();
                    robot.robotWheelDistance = Double.parseDouble(txtf_wheelDist.getText());
                    robot.mass = Double.parseDouble(txtf_robotMassKg.getText());
                    Point2 starting = PathCreation.getWaypoints().size() < 1 ? new Point2(0,0) : PathCreation.getWaypoints().get(0);
                    Point2 second = PathCreation.getWaypoints().size() < 2 ? new Point2(0,0) : PathCreation.getWaypoints().get(1);
                    PathSegment startToSecond = new PathSegment(starting, second);
                    robot.position = new Point3(starting.getX(), starting.getY(), Math.atan(startToSecond.getSlope()));
                    //Robot design setup
                    final Circle robotDebugBase = new Circle(-100, -100, 10);
                    final Line robotHeadingLine = new Line(robotDebugBase.getCenterX(), robotDebugBase.getCenterY(), 5*Math.cos(robot.position.getTheta()), 5*Math.sin(robot.position.getTheta()));

                    loop = new AnimationTimer(){
                        @Override
                        public void handle(long now) {
                            btn_simulateRobot.setText("Start a new path");
                            if(currentTime > 15) {
                                this.stop();
                            }
                            final double dt = (now-lastUpdate) / 1000000000.0;
                            //TODO: get voltage for left and right sides based on robot position and goal position
                            double voltageLeft = 12;
                            double voltageRight = 12;                            

                            robot.updatePos(dt);
                            robot.updateLeftVelocity(dt, voltageLeft);
                            robot.updateRightVelcoty(dt, voltageRight);

                            currentTime += dt;

                            //Update display
                            robotDebugBase.setCenterX(robot.position.getX());
                            robotDebugBase.setCenterY(robot.position.getY());
                            robotHeadingLine.setStartX(robotDebugBase.getCenterX());
                            robotHeadingLine.setStartY(robotDebugBase.getCenterY());
                            robotHeadingLine.setEndX(robotHeadingLine.getStartX() + robotDebugBase.getRadius()*Math.cos(robot.position.getTheta()));
                            robotHeadingLine.setEndY(robotHeadingLine.getStartY() + robotDebugBase.getRadius()*Math.sin(robot.position.getTheta()));
                            robotDebugBase.setFill(new Color(0, 1, 0, 0.3));
                            robotDebugBase.setRotate(Math.toDegrees(robot.position.getTheta()));
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

                    sp.getChildren().addAll(robotDebugBase, robotHeadingLine);
                }
            }
        });

        vb_input.getChildren().addAll(vb_wheelDist, vb_robotMassKg, btn_simulateRobot);

        sp.getChildren().add(vb_input);

        Scene scene = new Scene(sp, 800, 600);
        return scene;
    }
    
}