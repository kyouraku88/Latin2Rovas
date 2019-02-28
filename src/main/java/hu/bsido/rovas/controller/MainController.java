package hu.bsido.rovas.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import hu.bsido.rovas.common.LRResources.AppColor;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class MainController implements Initializable {

	@FXML private StackPane stackPane;
	@FXML private AnchorPane anchorPane;
	@FXML private Label lblDragDrop;

	private File input;

	public MainController() {
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		anchorPane.setOnDragEntered(e -> {
			Dragboard db = e.getDragboard();
			boolean isTxt = isTxtFile(db);
			if (isTxt) {
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
				boolean isTxt = isTxtFile(db);
				System.out.println("success " + isTxt);
				
				if (isTxt) {
					// add new center
				}
				
				event.setDropCompleted(isTxt);
				event.consume();
			}
		});
	}

	private boolean isTxtFile(Dragboard db) {
		boolean isTxt = false;
		if (db.hasFiles()) {
			File file = db.getFiles().iterator().next();
			isTxt = file.getName().endsWith(".txt");
		}
		return isTxt;
	}
}
