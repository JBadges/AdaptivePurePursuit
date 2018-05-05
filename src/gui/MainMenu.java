package gui;

import gui.Main.Scenes;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MainMenu {

    public static Scene getScene() {
        StackPane p = new StackPane();
        VBox verticalBox = new VBox();

        //Logo
        Text titleLogo = new Text("Pure Pursuit SimGen");
        titleLogo.setStyle("-fx-font: 32 sans-serif;");
        verticalBox.getChildren().add(titleLogo);

        //Create Path Button
        Button btn_createPath = new Button("Create a new path");
        btn_createPath.setStyle("-fx-font: 24 sans-serif;");
        verticalBox.getChildren().add(btn_createPath);

        //Load Path Button
        Button btn_loadPath = new Button("Load a saved path");
        btn_loadPath.setStyle("-fx-font: 24 sans-serif;");
        verticalBox.getChildren().add(btn_loadPath);

        //Links & Exit
        HBox hor_linksAndExit = new HBox(10);
        Button btn_github = new Button("github");
        btn_github.setStyle("-fx-font: 18 sans-serif;");
        Button btn_exit = new Button("exit");
        btn_exit.setStyle("-fx-font: 12 sans-serif;");
        hor_linksAndExit.getChildren().addAll(btn_github, btn_exit);
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

        btn_exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });

        Scene scene = new Scene(p, 800, 600);
        return scene;
    }
    
}