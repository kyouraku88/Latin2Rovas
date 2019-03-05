package hu.bsido.rovas.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import hu.bsido.rovas.Main;
import hu.bsido.rovas.common.Events.AddSceneEvent;
import hu.bsido.rovas.common.Events.FillConvertEvent;
import hu.bsido.rovas.common.LRResources.AppColor;
import hu.bsido.rovas.view.FxmlView;
import hu.bsido.rovas.view.LRFXMLLoader;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainController implements Initializable {

	@FXML private StackPane stackPane;
	@FXML private AnchorPane anchorPane;
	@FXML private Label lblDragDrop;
	@FXML private JFXButton btnChooseFile;
	@FXML private JFXButton btnLearn;

	public MainController() {
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		anchorPane.setOnDragEntered(e -> {
			Dragboard db = e.getDragboard();
			File file = getTxtFile(db);
			if (file != null) {
				lblDragDrop.setTextFill(AppColor.GREEN.paint());
			} else {
				lblDragDrop.setTextFill(AppColor.RED.paint());
			}
		});

		anchorPane.setOnDragExited(e -> {
			lblDragDrop.setTextFill(AppColor.BLACK.paint());
		});

		anchorPane.setOnDragOver(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				Dragboard db = event.getDragboard();
				if (db.hasFiles()) {
					event.acceptTransferModes(TransferMode.COPY);
				} else {
					event.consume();
				}
			}
		});

		// Dropping over surface
		anchorPane.setOnDragDropped(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				Dragboard db = event.getDragboard();
				File file = getTxtFile(db);

				if (file != null) {
					Main.postEvent(new AddSceneEvent(FxmlView.CONVERT, null));
					Main.postEvent(new FillConvertEvent(file));
				}

				event.setDropCompleted(file != null);
				event.consume();
			}
		});

		btnChooseFile.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Choose a txt file");
			fileChooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt", "*.rtf"));

			File selectedFile = fileChooser.showOpenDialog(stackPane.getScene().getWindow());

			if (selectedFile != null) {
				Main.postEvent(new AddSceneEvent(FxmlView.CONVERT, null));
				Main.postEvent(new FillConvertEvent(selectedFile));
			}
		});
		
		btnLearn.setOnAction(e -> {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(FxmlView.LEARN.getFxmlFile()));
			loader.setClassLoader(LRFXMLLoader.cachingClassLoader); 
			try {
				Parent root = loader.load();
				
				Stage stage = new Stage();
				stage.setTitle("Learn");
				stage.setScene(new Scene(root));
				stage.show();
				
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});
	}

	private static File getTxtFile(Dragboard db) {
		File res = null;
		if (db.hasFiles()) {
			File file = db.getFiles().iterator().next();
			if (file.getName().endsWith(".txt") || file.getName().endsWith(".rtf")) {
				res = file;
			}
		}
		return res;
	}
}
