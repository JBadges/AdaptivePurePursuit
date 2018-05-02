package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class MainMenu {

    public static Scene getScene() {
        StackPane sp = new StackPane();
        Button createNewPath = new Button();
        createNewPath.setText("Create a new Path");
        createNewPath.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.changeScene(PathCreation.getScene());
            }
        });
        sp.getChildren().add(createNewPath);
        Scene scene = new Scene(sp, 500,500);
        return scene;
    }
    
}