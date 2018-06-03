package gui;

import javafx.scene.Scene;

public interface GUI {

	/**
	 * 
	 * @return null if not overridden
	 */
	public static Scene getScene() {
		return null;
	}

}