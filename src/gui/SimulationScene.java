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
        
        //Draw Lines between points before the waypoints so they are beneath
        for(int i = 0; i < PathCreation.getWaypoints().size()-1; i++) {
            Point3 a = PathCreation.getWaypoints().get(i);
            Point3 b = PathCreation.getWaypoints().get(i+1);
            sp.getChildren().add(new Line(a.getX(), a.getY(), b.getX(), b.getY()));
        }
        //Draw waypoints from PathCreation
        PathCreation.circleColors(PathCreation.getCircles());
        sp.getChildren().addAll(PathCreation.getCircles());

        Scene scene = new Scene(sp, 500, 500);
        return scene;
    }
    
}