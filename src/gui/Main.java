package gui;

import javafx.application.Application;
import javafx.stage.Stage;
 
public class Main extends Application {

    enum Scenes {
        MainMenu,
        PathCreation,
        SimulateRobot
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
            case SimulateRobot:
                primaryStage.setScene(SimulateRobot.getScene());
                break;
        }
    }

    public static Stage getStage() {
        return primaryStage;
    }
}