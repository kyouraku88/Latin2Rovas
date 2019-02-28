package hu.bsido.rovas.view;

import java.util.Objects;

import com.google.common.eventbus.Subscribe;

import hu.bsido.rovas.Main;
import hu.bsido.rovas.common.Events.AddSceneEvent;
import hu.bsido.rovas.common.Events.RemoveLastSceneEvent;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class StageManager {

	private final Stage primaryStage;
	private final LRFXMLLoader fxmlLoader;
	
	public StageManager(LRFXMLLoader fxmlLoader, Stage stage) {
//		Main.mainEventBus.register(this);
		this.fxmlLoader = fxmlLoader;
		this.primaryStage = stage;
	}

	public void initScene(final FxmlView view) {
		Parent viewRootNodeHierarchy = loadViewNode(view.getFxmlFile());
		show(viewRootNodeHierarchy, view.getTitle());
	}
	
	@Subscribe
	public void addScene(AddSceneEvent event) {
		BorderPane root = (BorderPane) primaryStage.getScene().getRoot();
		StackPane sp = ((StackPane) root.getCenter());
		Parent newCenter = loadViewNode(event.view.getFxmlFile());

		sp.getChildren().add(newCenter);
	}
	
	@Subscribe
	public void backEvent(RemoveLastSceneEvent event) {
		BorderPane root = (BorderPane) primaryStage.getScene().getRoot();
		StackPane sp = ((StackPane) root.getCenter());

		final int lastIdx = sp.getChildren().size() - 1;
		Node latestCenter = sp.getChildren().get(lastIdx);

		if (event.direction != null) {
			slideAbove(event, latestCenter, sp, lastIdx);
		}
	}

	private void slideAbove(RemoveLastSceneEvent event, Node center, StackPane sp, final int idxToRemove) {
		Scene spScene = sp.getScene();
		
		Timeline timeline = new Timeline();
		KeyValue kv = null;
		switch(event.direction) {
			case UP:
				kv = new KeyValue(center.translateYProperty(), -spScene.getHeight(), Interpolator.EASE_OUT);
				break;
			case DOWN:
				kv = new KeyValue(center.translateYProperty(), spScene.getHeight(), Interpolator.EASE_OUT);
				break;
			case LEFT:
				kv = new KeyValue(center.translateXProperty(), -spScene.getWidth(), Interpolator.EASE_OUT);
				break;
			case RIGHT:
				kv = new KeyValue(center.translateXProperty(), spScene.getWidth(), Interpolator.EASE_OUT);
				break;
			default:
				break;
		}

		KeyFrame kf = new KeyFrame(Duration.seconds(0.5), kv);
		timeline.getKeyFrames().add(kf);
		timeline.setOnFinished(e -> {
			sp.getChildren().remove(idxToRemove);
			Main.unregisterLast();
		});
		timeline.play();
	}

	private void show(final Parent rootNode, String title) {
		Scene scene = prepareScene(rootNode);
		
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.centerOnScreen();
		
		try {
			primaryStage.show();
		} catch (Exception e) {
			logAndExit("Unable to show scene for title" + title, e);
		}
	}

	private Scene prepareScene(Parent rootNode) {
		Scene scene = primaryStage.getScene();
		
		if (scene == null) {
			scene = new Scene(rootNode);
		}
		scene.setRoot(rootNode);
		return scene;
	}

	public Parent loadViewNode(String fxmlFilePath) {
		Parent rootNode = null;
		try {
			rootNode = fxmlLoader.load(fxmlFilePath);
			Objects.requireNonNull(rootNode, "A Root FXML node must not be null");
		} catch (Exception e) {
			logAndExit("Unable to load FXML view " + fxmlFilePath, e);
		}
		return rootNode;
	}

	private void logAndExit(String errorMsg, Exception e) {
		Platform.exit();
	}
}
