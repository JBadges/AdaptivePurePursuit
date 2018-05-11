package gui;

import javafx.scene.Scene;

public interface GUI {

    /** 
     * returns null if not overriden
     */
    public static Scene getScene() {return null;}

}