package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
 
public class Main extends Application {

    private static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage ps) {
        this.primaryStage = ps;
        primaryStage.setTitle("Pure Pursuit SimGen");
        primaryStage.setScene(MainMenu.getScene());
        primaryStage.show();
    }

    public static void changeScene(Scene scene) {
        primaryStage.setScene(scene);
    }
}