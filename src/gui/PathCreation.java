package gui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import control.Path;
import gui.Main.Scenes;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import util.Point2;

public class PathCreation implements GUI {

	private List<Point2> waypoints = new ArrayList<>();
	private List<StackPane> waypointCircles = new ArrayList<>();

	private static PathCreation instance;

	public static PathCreation getInstance() {
		if (instance == null) {
			instance = new PathCreation();
		}
		return instance;
	}

	public Scene getScene() {
		//Initialize
		waypoints.clear();
		waypointCircles.clear();

		Pane pane = new Pane();

		//Define Clickable Area
		Rectangle clickArea = new Rectangle(800, 600 - 70);
		clickArea.setFill(new Color(216 / 256.0, 216 / 256.0, 216 / 256.0, 1));
		clickArea.setY(70);
		pane.getChildren().add(clickArea);

		//Define "Finish Path" Button
		Button btn_finishPath = new Button("Finish Path");
		btn_finishPath.setStyle(Styles.getButtonDefault());
		btn_finishPath.setPrefWidth(200);
		btn_finishPath.setLayoutY(10);
		btn_finishPath.setPrefHeight(50);
		btn_finishPath.setLayoutX(400 - btn_finishPath.getPrefWidth() - 10);
		pane.getChildren().add(btn_finishPath);

		//Define "Save Path" Button
		Button btn_savePath = new Button("Save Path");
		btn_savePath.setStyle(Styles.getButtonDefault());
		btn_savePath.setPrefWidth(200);
		btn_savePath.setLayoutY(10);
		btn_savePath.setPrefHeight(50);
		btn_savePath.setLayoutX(400 + 10);
		pane.getChildren().add(btn_savePath);

		/*
		 * Handler for "Finish Path" button
		 * If there are at least two waypoints, switch to the SimulateRobot scene
		 */
		btn_finishPath.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (waypoints.size() >= 2) {
					Main.changeScene(Scenes.SimulateRobot);
				}
			}
		});

		/*
		 * Handler for "Save Path" button
		 * If there are at least two waypoints, save the waypoints into a JSON
		 * Request the user for a location to save the file
		 */
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
					} catch (NullPointerException e) {
						new Alert(AlertType.ERROR, "Please choose a valid file location", ButtonType.OK).showAndWait();
					} catch (IOException e) {
						new Alert(AlertType.ERROR, "IOException caught", ButtonType.OK).showAndWait();
					} finally {
						if (writer != null) {
							try {
								writer.close();
							} catch (IOException e) {
								new Alert(AlertType.ERROR, "IOException caught", ButtonType.OK).showAndWait();
							}
						}
					}
				}
			}
		});

		Scene scene = new Scene(pane, 800, 600);
		/*
		 * Screen waypoint clicking
		 * Mouse click handler
		 */
		scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getY() > 70) {
					StackPane stackPane = new StackPane();

					Circle circ = new Circle(mouseEvent.getX(), mouseEvent.getY(), 5);
					waypoints.add(new Point2(mouseEvent.getX(), mouseEvent.getY()));

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
		
		/*
		 * Handler for escape button to return to home screen
		 */
		scene.setOnKeyPressed(e -> {
			if (e.getCode() == Settings.backButton) {
				Main.changeScene(Scenes.MainMenu);
			}
		});

		return scene;
	}

	public void circleColors(List<StackPane> points) {
		for (int i = 0; i < waypointCircles.size(); i++) {
			double scalar = 1.0 / waypointCircles.size();
			Circle c = (Circle) waypointCircles.get(i).getChildren().get(0);
			c.setFill(new Color(i * scalar, 1 - (i * scalar), 0, 1));
		}
	}

	/*
	 * @return the path defined by the user
	 */
	public Path getPath() {
		Point2[] arr = getWaypoints().toArray(new Point2[0]);
		return new Path(arr);
	}

	/*
	 * @return the waypoints defined by the user
	 */
	public List<Point2> getWaypoints() {
		return waypoints;
	}

	/*
	 * @return the waypointCircles
	 */
	public List<StackPane> getCircles() {
		return waypointCircles;
	}

}