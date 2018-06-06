package gui;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Main extends Application {

	enum Scenes {
		MainMenu, PathCreation, SimulateRobot, Settings
	}

	private static Stage primaryStage;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage ps) {
		primaryStage = ps;
		primaryStage.setTitle("Pure Pursuit SimGen");
		changeScene(Scenes.MainMenu);
		primaryStage.show();
		new Alert(AlertType.INFORMATION, "The pure pursuit algorithm is not implemented to end at a specific location. It is not guaranteed to end at the last specified location. Hitting ESC will allow you to go back one screen.").showAndWait();
	}

	public static void changeScene(Scenes scene) {
		switch (scene) {
		case MainMenu:
			primaryStage.setScene(MainMenu.getInstance().getScene());
			break;
		case PathCreation:
			primaryStage.setScene(PathCreation.getInstance().getScene());
			break;
		case SimulateRobot:
			primaryStage.setScene(SimulateRobot.getInstance().getScene());
			break;
		case Settings:
			primaryStage.setScene(Settings.getInstance().getScene());
			break;
		}
	}

	public static Stage getStage() {
		return primaryStage;
	}
}