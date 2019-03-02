package hu.bsido.rovas;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.ResourceBundle;

import com.google.common.eventbus.EventBus;

import hu.bsido.rovas.view.FxmlView;
import hu.bsido.rovas.view.LRFXMLLoader;
import hu.bsido.rovas.view.StageManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static Main INSTANCE;
	private StageManager stageManager;
	
	public static LinkedList<Object> controllers = new LinkedList<>(); 
	private static final EventBus eventBus = new EventBus();

	public static void register(Object controller) {
		eventBus.register(controller);
		controllers.add(controller);
	}
	
	// unregister all controllers except the most recent one
	public static void unregisterEvents() {
		Object lastController = Main.controllers.removeLast();
		
		for (Object c : controllers) {
			eventBus.unregister(c);
		}
		controllers.clear();
		controllers.add(lastController);
	}
	
	public static void unregisterLast() {
		eventBus.unregister(controllers.removeLast());
	}
	
	public static void postEvent(Object event) {
		eventBus.post(event);
	}
	
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
    	INSTANCE = this;

    	stageManager = new StageManager(new LRFXMLLoader(resourceBundle()), stage);
    	displayInitialScene();
    }
    
    protected void displayInitialScene() {
        stageManager.initScene(FxmlView.MAIN);
    }
	
	public static ResourceBundle resourceBundle() {
		return ResourceBundle.getBundle("Bundle");
	}
}