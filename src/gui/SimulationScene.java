package gui;

import control.PathSegment;
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
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import util.Point3;

public class SimulationScene {

    //Time seconds
    private static double currentTime = 0.0;
    private static double lastUpdate;

    public static Scene getScene() {
        Pane sp = new Pane();
        
        //Draw Lines between points before the waypoints so they are beneath
        for(int i = 0; i < PathCreation.getWaypoints().size()-1; i++) {
            Point3 a = PathCreation.getWaypoints().get(i);
            Point3 b = PathCreation.getWaypoints().get(i+1);
            sp.getChildren().add(new Line(a.getX(), a.getY(), b.getX(), b.getY()));
        }
        //Draw waypoints from PathCreation
        PathCreation.circleColors(PathCreation.getCircles());
        sp.getChildren().addAll(PathCreation.getCircles());

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
                    final Rectangle robotRect = new Rectangle(-100, -100, 25, 25);
                    class SkidDriveRobot {
                        public Point3 position;
                        //In m/s
                        public double leftVelocity;
                        public double rightVelocity;

                        //In Kg
                        public double mass;
                        //In m
                        public double wheelDistance;

                        public void updateLeftAcceleration(double voltage) {
                            
                        }

                        public void updateRightAcceleration(double voltage) {
                            
                        }

                        public void updatePos(double dt) {
                            
                        }
                    }

                    //Robot information setup
                    SkidDriveRobot robot = new SkidDriveRobot();
                    robot.wheelDistance = Double.parseDouble(txtf_wheelDist.getText());
                    robot.mass = Double.parseDouble(txtf_robotMassKg.getText());
                    Point3 starting = PathCreation.getWaypoints().get(0);
                    Point3 second = PathCreation.getWaypoints().get(1);
                    PathSegment startToSecond = new PathSegment(starting, second);
                    robot.position = new Point3(starting.getX(), starting.getY(), Math.atan(startToSecond.getSlope()));

                    AnimationTimer loop = new AnimationTimer(){
                        @Override
                        public void handle(long now) {
                            if(currentTime > 15) {
                                this.stop();
                            }
                            final double dt = (now-lastUpdate) / 1000000000.0;
                            //TODO: get voltage for left and right sides based on robot position and goal position
                            double voltageLeft = 0;
                            double voltageRight = 0;

                            robot.updateLeftAcceleration(voltageLeft);
                            robot.updateRightAcceleration(voltageRight);

                            robot.updatePos(dt);
                            currentTime += dt;

                            robotRect.setX(robot.position.getX()-25/2.0);
                            robotRect.setY(robot.position.getY()-25/2.0);
                            
                            robotRect.setFill(new Color(0, 1, 0, 0.3));
                            robotRect.setRotate(Math.toDegrees(robot.position.getTheta()));

                            try {
								Thread.sleep(5);
							} catch (InterruptedException e) {
								e.printStackTrace();
                            }
                            lastUpdate = now;
                        }
                    };

                    lastUpdate = System.nanoTime();
                    loop.start();

                    sp.getChildren().add(robotRect);
                }
            }
        });

        vb_input.getChildren().addAll(vb_wheelDist, vb_robotMassKg, btn_simulateRobot);

        sp.getChildren().add(vb_input);

        Scene scene = new Scene(sp, 500, 500);
        return scene;
    }
    
}