package hu.bsido.rovas.view;

import java.io.IOException;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class LRFXMLLoader {

	public static ClassLoader cachingClassLoader = new FXMLClassLoader(FXMLLoader.getDefaultClassLoader()); 
	private final ResourceBundle resourceBundle;
	
	public LRFXMLLoader (ResourceBundle resourceBundle) {
		this.resourceBundle = resourceBundle;
	}
	
	public Parent load(String fxmlPath) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setClassLoader(cachingClassLoader); 
		loader.setResources(resourceBundle);
		loader.setLocation(getClass().getResource(fxmlPath));
		return loader.load();
	}
}
