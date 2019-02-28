package hu.bsido.rovas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ResourceBundle;

import com.google.common.eventbus.EventBus;

import hu.bsido.rovas.common.LRResources;
import hu.bsido.rovas.view.FxmlView;
import hu.bsido.rovas.view.LRFXMLLoader;
import hu.bsido.rovas.view.StageManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	
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
    	stageManager = new StageManager(new LRFXMLLoader(resourceBundle()), stage);
    	displayInitialScene();
    }
    	
    protected void displayInitialScene() {
        stageManager.initScene(FxmlView.MAIN);
    }
	
//    	AnchorPane root = new AnchorPane();        //Group root = new Group();
//        HBox buttonbox = new HBox(10);
//        Label draghere = new Label("Drag & Drop here");
//        Label log = new Label();
//        
//        AnchorPane.setTopAnchor(buttonbox, 10.0);
//        AnchorPane.setRightAnchor(buttonbox, 10.0);
//        AnchorPane.setLeftAnchor(buttonbox, 10.0);
//        
//        AnchorPane.setTopAnchor(draghere, 50.0);
//        AnchorPane.setLeftAnchor(draghere, 50.0);
//        AnchorPane.setBottomAnchor(log, 2.0);
//        AnchorPane.setLeftAnchor(log, 2.0);
//        
//        Button c2normal = new Button("to Normal");
//        Button c2extented = new Button("to Extented");
//        
//        c2normal.setOnAction(e -> {
//        	convert2normal(input);
//        	log.setText("Converted!");
//        });
//        
//        
//        buttonbox.getChildren().addAll(c2normal, c2extented);
//        
//        Scene scene = new Scene(root, 200, 100);
//        
//        scene.setOnDragOver(new EventHandler<DragEvent>() {
//            @Override
//            public void handle(DragEvent event) {
//                Dragboard db = event.getDragboard();
//                if (db.hasFiles()) {
//                    event.acceptTransferModes(TransferMode.COPY);
//                } else {
//                    event.consume();
//                }
//            }
//        });
//        
//        // Dropping over surface
//        scene.setOnDragDropped(new EventHandler<DragEvent>() {
//            @Override
//            public void handle(DragEvent event) {
//                Dragboard db = event.getDragboard();
//                boolean success = false;
//                if (db.hasFiles()) {
//                    success = true;
//                    //String filePath = null;
//                    
//                    for (File file:db.getFiles()) {
//                        input = file;
//                    	//filePath = file.getAbsolutePath();
//                        //System.out.println(filePath);
//                    }
//                }
//                event.setDropCompleted(success);
//                event.consume();
//                
//                log.setText(input.getName());
//            }
//            
//        });
//
//        root.getChildren().addAll(buttonbox, draghere, log);
        
        
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
    
    private static void convert2normal(File input){
    	String line = null;
		String rovas = null;
		
		try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(input.getAbsolutePath());

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            StringBuilder sb = new StringBuilder();
            
            while((line = bufferedReader.readLine()) != null) {
                rovas = sb.append(simarovas(line.toLowerCase()) + "\r\n").toString();
            }  
            //System.out.println(rovas);
            // Always close files.
            bufferedReader.close(); 
            
            write(rovas, input.getAbsolutePath(), "sima");
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                input.getName() + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + input.getName() + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
    }
		
	private static String simarovas(String line) {
		String rovas = line;
		for (int i = 0; i < LRResources.ROVASOK.length; i++) {
			rovas = rovas.replaceAll(LRResources.ROVASOK[i][0], LRResources.ROVASOK[i][1]);			
		}
		return rovas;
	}
    
	private static void write(String output, String path, String type){
		// The name of the file to open.
        String fileName =  path.substring(0, path.length() - 4) + "_" + type + ".txt";
        try {
            // Assume default encoding.
            FileWriter fileWriter = new FileWriter(fileName);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(output);
            
            // Always close files.
            bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println(
                "Error writing to file '"
                + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
    }
    
	public static ResourceBundle resourceBundle() {
		return ResourceBundle.getBundle("Bundle");
	}
}