package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import util.Point3;

public class SimulationScene {

    public static Scene getScene() {
        Pane sp = new Pane();

        

        Scene scene = new Scene(sp, 500, 500);
        return scene;
    }
    
}