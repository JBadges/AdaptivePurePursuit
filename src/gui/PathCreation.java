package gui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
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
        btn_finishPath.setPrefHeight(50);
        btn_finishPath.setLayoutX(400 - btn_finishPath.getPrefWidth() - 10);
        pane.getChildren().add(btn_finishPath);

        //Save Path Button
        Button btn_savePath = new Button("Save Path");
        btn_savePath.setStyle("-fx-font: 24 sans-serif;");
        btn_savePath.setPrefWidth(200);
        btn_savePath.setLayoutY(20);
        btn_savePath.setPrefHeight(50);
        btn_savePath.setLayoutX(400 + 10);
        pane.getChildren().add(btn_savePath);

        //Button Functionality
        btn_finishPath.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (waypoints.size() >= 2) {
                    Main.changeScene(Scenes.SimulateRobot);
                }
            }
        });

        //Button Functionality
        btn_savePath.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (waypoints.size() >= 2) {
                    FileChooser chooser = new FileChooser();
                    chooser.setTitle("Choose location To Save Path");
                    chooser.setInitialFileName("Path.json");
                    ExtensionFilter jsonExtFilter = new ExtensionFilter("JSON files (*.json)", "*.json");
                    ExtensionFilter allExtFilter = new ExtensionFilter("All files (*.)", "*.");
                    chooser.getExtensionFilters().addAll(jsonExtFilter, allExtFilter);
                    File selectedFile = chooser.showSaveDialog(null);
                    Gson gson = new Gson();
                    JSONWaypoints javaObjectToJson = new JSONWaypoints();
                    javaObjectToJson.waypoints.clear();
                    javaObjectToJson.waypoints.addAll(waypoints);
                    String strJson = gson.toJson(javaObjectToJson);
                    FileWriter writer = null;
                    try {
                        writer = new FileWriter(selectedFile);
                        writer.write(strJson);
                    } catch (NullPointerException e)  {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (writer != null) {
                            try {
                                writer.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });

        Scene scene = new Scene(pane, 800, 600);
        //Screen waypoint clicking
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getY() > 70) {
                    StackPane stackPane = new StackPane();

                    Circle circ = new Circle(mouseEvent.getX(), mouseEvent.getY(), 5);
                    waypoints.add(new Point3(mouseEvent.getX(), mouseEvent.getY()));

                    Label circleLabel = new Label("" + waypointCircles.size());
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