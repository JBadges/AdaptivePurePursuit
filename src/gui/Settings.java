package gui;

import control.SkidRobot;
import gui.Main.Scenes;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Settings implements GUI {

	public static KeyCode backButton = KeyCode.ESCAPE;

	private static Settings instance;

	public static Settings getInstance() {
		if (instance == null) {
			instance = new Settings();
		}
		return instance;
	}

	public Scene getScene() {

		StackPane p = new StackPane();
		Scene scene = new Scene(p, 800, 600);
		VBox verticalBox = new VBox();

		//Logo
		Text titleLogo = new Text("Settings");
		titleLogo.setStyle("-fx-font: 32 sans-serif;");
		verticalBox.getChildren().add(titleLogo);

		//Create Path Button
		HBox backButtonGroup = new HBox();
		Label lbl_backButton = new Label("Back Button");
		Button btn_setBackButton = new Button(backButton.getName());
		btn_setBackButton.setStyle(Styles.getButtonDefault(20));

		btn_setBackButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
					public void handle(KeyEvent keyEvent) {
						backButton = keyEvent.getCode();
						btn_setBackButton.setText(backButton.getName());
						scene.removeEventHandler(KeyEvent.KEY_PRESSED, this);
					}
				});
			}
		});

		backButtonGroup.setAlignment(Pos.CENTER);
		backButtonGroup.setSpacing(25);
		backButtonGroup.getChildren().addAll(lbl_backButton, btn_setBackButton);

		verticalBox.getChildren().add(backButtonGroup);

		HBox speedMultiplierGroup = new HBox();
		Label lbl_speedMultiplier = new Label("Speed Multiplier");
		TextField txtFld_speedMultiplier = new TextField(SkidRobot.speedModifier + "");
		speedMultiplierGroup.setAlignment(Pos.CENTER);
		speedMultiplierGroup.setSpacing(25);
		speedMultiplierGroup.getChildren().addAll(lbl_speedMultiplier, txtFld_speedMultiplier);

		verticalBox.getChildren().add(speedMultiplierGroup);

		Button btn_saveAndExit = new Button("Back to Menu");
		btn_saveAndExit.setStyle(Styles.getButtonDefault(20));
		btn_saveAndExit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (txtFld_speedMultiplier.getText().matches("^[+]?([.]\\d+|\\d+[.]?\\d*)$")) {
					SkidRobot.speedModifier = Double.parseDouble(txtFld_speedMultiplier.getText());
				}
				Main.changeScene(Scenes.MainMenu);
			}
		});

		verticalBox.getChildren().add(btn_saveAndExit);

		verticalBox.setSpacing(25);

		p.getChildren().add(verticalBox);
		verticalBox.setAlignment(Pos.CENTER);

		return scene;
	}

}
