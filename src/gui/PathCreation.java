package gui;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import util.Point3;

public class PathCreation {

    private static List<Point3> waypoints;

    public static Scene getScene() {
        waypoints = new ArrayList<>();
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
                    Circle circ = new Circle(mouseEvent.getX(), mouseEvent.getY(), 5);
                    if(waypoints.size() == 0) {
                        circ.setFill(Color.GREEN);
                    } else {
                        circ.setFill(Color.RED);
                    }
                    waypoints.add(new Point3(mouseEvent.getX(), mouseEvent.getY()));
                    sp.getChildren().add(circ);
                }
            }
        });
        return scene;
    }
    
}