package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
 
public class Main extends Application {

    enum Scenes {
        MainMenu,
        PathCreation,
        SimulationScreen
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
    }

    public static void changeScene(Scenes scene) {
        switch(scene) {
            case MainMenu:
                primaryStage.setScene(MainMenu.getScene());
                break;
            case PathCreation:
                primaryStage.setScene(PathCreation.getScene());
                break;
            case SimulationScreen:
                primaryStage.setScene(SimulationScene.getScene());
                break;
        }
    }
}