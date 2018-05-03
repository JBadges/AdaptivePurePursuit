package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import util.Point3;

public class PathCreation {

    private static List<Point3> waypoints;
    private static List<Circle> waypointCircles;

    public static Scene getScene() {
        waypoints = new ArrayList<>();
        waypointCircles = new ArrayList<>();
        Pane sp = new Pane();
        Button finishPath = new Button();
        finishPath.setText("Finish Path");
        finishPath.setPrefWidth(150);
        finishPath.setPrefHeight(20);
        finishPath.setLayoutX(250 - 150/2.0);
        finishPath.setLayoutY(20);
        finishPath.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(waypoints.size() >= 2)  {
                    for(int i = 0; i < waypoints.size()-1; i++) {
                        sp.getChildren().add(new Line(waypoints.get(i).getX(), waypoints.get(i).getY(), waypoints.get(i+1).getX(), waypoints.get(i+1).getY()));
                    }
                    Main.changeScene(SimulationScene.getScene()); 
                }
            }
        });
        sp.getChildren().add(finishPath);
        Scene scene = new Scene(sp, 500,500);
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(!(
                    (mouseEvent.getX() > finishPath.getLayoutX()-2 && mouseEvent.getX() < finishPath.getLayoutX() + 150 + 2) &&
                    (mouseEvent.getY() > 20-2 && mouseEvent.getY() < finishPath.getLayoutY() + 20 + 2))
                    ) {
                    StackPane stackPane = new StackPane();

                    Circle circ = new Circle(mouseEvent.getX(), mouseEvent.getY(), 5);
                    waypoints.add(new Point3(mouseEvent.getX(), mouseEvent.getY()));
                    waypointCircles.add(circ);

                    Label circleLabel = new Label(""+waypointCircles.size());
                    circleLabel.setTextAlignment(TextAlignment.CENTER);
                    circ.radiusProperty().bind(circleLabel.widthProperty());

                    //Changing the color based on number of waypoints
                    for(int i = 0; i < waypointCircles.size(); i++) {
                        double scalar = 1.0/waypointCircles.size();
                        Circle c = waypointCircles.get(i);
                        c.setFill(new Color(i*scalar, 1-(i*scalar), 0, 1));
                    }

                    stackPane.getChildren().addAll(circ, circleLabel);
                    
                    stackPane.setLayoutX(circ.getCenterX() - 5);
                    stackPane.setLayoutY(circ.getCenterY() - 10);
                    sp.getChildren().add(stackPane);
                }
            }
        });
        return scene;
    }
    
}