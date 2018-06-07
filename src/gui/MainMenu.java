package gui;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import control.Path;
import gui.Main.Scenes;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import util.Point2;

public class MainMenu implements GUI {

	private static MainMenu instance;

	public static MainMenu getInstance() {
		if (instance == null) {
			instance = new MainMenu();
		}
		return instance;
	}

	public Scene getScene() {
		StackPane p = new StackPane();
		VBox verticalBox = new VBox();

		//Logo
		Text titleLogo = new Text("Pure Pursuit SimGen");
		titleLogo.setStyle("-fx-font: 32 sans-serif;");
		verticalBox.getChildren().add(titleLogo);

		//Create Path Button
		Button btn_createPath = new Button("Create a new path");
		btn_createPath.setStyle(Styles.getButtonDefault());
		verticalBox.getChildren().add(btn_createPath);

		//Load Path Button
		Button btn_loadPath = new Button("Load a saved path");
		btn_loadPath.setStyle(Styles.getButtonDefault());
		verticalBox.getChildren().add(btn_loadPath);

		//Links & Exit
		HBox hor_linksAndExit = new HBox(10);
		Button btn_github = new Button("github");
		btn_github.setStyle(Styles.getButtonDefault());
		Button btn_exit = new Button("exit");
		btn_exit.setStyle(Styles.getButtonDefault());
		Button btn_settings = new Button("settings");
		btn_settings.setStyle(Styles.getButtonDefault());
		hor_linksAndExit.getChildren().addAll(btn_settings, btn_github, btn_exit);
		hor_linksAndExit.setAlignment(Pos.CENTER);
		verticalBox.getChildren().add(hor_linksAndExit);

		//Allignment and Spacing
		p.getChildren().add(verticalBox);
		verticalBox.setAlignment(Pos.CENTER);
		verticalBox.setSpacing(50);
		StackPane.setAlignment(verticalBox, Pos.CENTER);

		//Button Functionality
		btn_createPath.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Main.changeScene(Scenes.PathCreation);
			}
		});

		btn_loadPath.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Path File");
				File pathFile = fileChooser.showOpenDialog(null);
				if (pathFile != null) {
					//Find extension type
					String extension = "";
					int i = pathFile.getName().lastIndexOf('.');
					if (i > 0) {
						extension = pathFile.getName().substring(i + 1);
					}
					if (extension.toLowerCase().equals("json")) {
						Gson gson = new Gson();
						JsonReader reader = null;
						try {
							reader = new JsonReader(new FileReader(pathFile));
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						JSONWaypoints waypoints = gson.fromJson(reader, JSONWaypoints.class);
						PathCreation.getInstance().getWaypoints().clear();
						PathCreation.getInstance().getCircles().clear();
						PathCreation.getInstance().getWaypoints().addAll(waypoints.waypoints);

						for (int j = 0; j < PathCreation.getInstance().getWaypoints().size(); j++) {
							StackPane stackPane = new StackPane();

							Circle circ = new Circle(PathCreation.getInstance().getWaypoints().get(j).getX(), PathCreation.getInstance().getWaypoints().get(j).getY(), 5);
							Label circleLabel = new Label("" + PathCreation.getInstance().getCircles().size());
							circleLabel.setTextAlignment(TextAlignment.CENTER);
							circ.radiusProperty().bind(circleLabel.widthProperty());

							stackPane.getChildren().addAll(circ, circleLabel);
							PathCreation.getInstance().getCircles().add(stackPane);

							//Changing the color based on number of waypoints
							PathCreation.getInstance().circleColors(PathCreation.getInstance().getCircles());

							stackPane.setLayoutX(circ.getCenterX() - 5);
							stackPane.setLayoutY(circ.getCenterY() - 5);
						}
						Main.changeScene(Scenes.SimulateRobot);
					} else {
						new Alert(AlertType.ERROR, "Please choose a .JSON file type", ButtonType.OK).showAndWait();
					}
				}
			}
		});

		btn_github.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					Desktop.getDesktop().browse(new URI("https://github.com/JBadges/AdaptivePurePursuit"));
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});

		btn_exit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.exit(0);
			}
		});

		btn_settings.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Main.changeScene(Scenes.Settings);
			}
		});

		Scene scene = new Scene(p, 800, 600);
		return scene;
	}

}

/**
 * 
 * Data class used for GSON to convert Json to and from text to objects
 */
class JSONWaypoints {
	public List<Point2> waypoints;

	public JSONWaypoints() {
		waypoints = new ArrayList<>();
	}

	public Path getPath() {
		return new Path((Point2[]) waypoints.toArray());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Point2 p : waypoints) {
			sb.append(p);
		}
		return sb.toString();
	}

}