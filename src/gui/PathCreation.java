package gui;

import java.util.ArrayList;
import java.util.List;

import gui.Main.Scenes;
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
import javafx.scene.text.TextAlignment;
import util.Point2;
import util.Point3;

public class PathCreation implements GUI {

    private static List<Point2> waypoints = new ArrayList<>();
    private static List<StackPane> waypointCircles = new ArrayList<>();

    public static Scene getScene() {
        //Initialize
        waypoints.clear();
        waypointCircles.clear();

        Pane pane = new Pane();

        //Finish Path Button
        Button btn_finishPath = new Button("Finish Path");
        btn_finishPath.setStyle("-fx-font: 24 sans-serif;");
        btn_finishPath.setPrefWidth(200);
        btn_finishPath.setLayoutY(20);
        btn_finishPath.setLayoutX(400 - btn_finishPath.getPrefWidth()/2.0);
        pane.getChildren().add(btn_finishPath);

        //Button Functionality
        btn_finishPath.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(waypoints.size() >= 2) {
                    Main.changeScene(Scenes.SimulateRobot);
                }
            }
        });

        Scene scene = new Scene(pane, 800, 600);
        //Screen waypoint clicking
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(!btn_finishPath.getBoundsInParent().contains(mouseEvent.getX(), mouseEvent.getY())) {
                    StackPane stackPane = new StackPane();

                    Circle circ = new Circle(mouseEvent.getX(), mouseEvent.getY(), 5);
                    waypoints.add(new Point3(mouseEvent.getX(), mouseEvent.getY()));

                    Label circleLabel = new Label(""+waypointCircles.size());
                    circleLabel.setTextAlignment(TextAlignment.CENTER);
                    circ.radiusProperty().bind(circleLabel.widthProperty());

                    stackPane.getChildren().addAll(circ, circleLabel);
                    waypointCircles.add(stackPane);
                    
                    //Changing the color based on number of waypoints
                    circleColors(waypointCircles);

                    stackPane.setLayoutX(circ.getCenterX() - 5);
                    stackPane.setLayoutY(circ.getCenterY() - 5);
                    pane.getChildren().add(stackPane);
                }
            }
        });

        return scene;
    }

    public static void circleColors(List<StackPane> points) {
        for(int i = 0; i < waypointCircles.size(); i++) {
            double scalar = 1.0/waypointCircles.size();
            Circle c = (Circle) waypointCircles.get(i).getChildren().get(0);
            c.setFill(new Color(i*scalar, 1-(i*scalar), 0, 1));
        }
    }
    
    public static List<Point2> getWaypoints() {
        return waypoints;
    }
    public static List<StackPane> getCircles() {
        return waypointCircles;
    }

}