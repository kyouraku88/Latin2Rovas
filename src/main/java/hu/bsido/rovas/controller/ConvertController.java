package hu.bsido.rovas.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.google.common.eventbus.Subscribe;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import hu.bsido.rovas.Main;
import hu.bsido.rovas.common.Events.FillConvertEvent;
import hu.bsido.rovas.common.LRResources;
import hu.bsido.rovas.common.LRResources.FileOutput;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class ConvertController implements Initializable {

	@FXML private JFXComboBox<FileOutput> lbOutput;
	@FXML private JFXButton btnConvert;
	
	private File toConvert;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Main.register(this);
		
		lbOutput.getItems().addAll(FXCollections.observableArrayList(FileOutput.values()));

		btnConvert.setOnAction(e -> {
			convert2normal(toConvert);
		});
	}

	@Subscribe
	public void fill(FillConvertEvent e) {
		toConvert = e.file;
	}
	
	private static void convert2normal(File input){
    	String line = null;
		String rovas = null;
		
		try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(input.getAbsolutePath());
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            StringBuilder sb = new StringBuilder();
            
            while((line = bufferedReader.readLine()) != null) {
                rovas = sb.append(simarovas(line.toLowerCase()) + "\r\n").toString();
            }  
            bufferedReader.close(); 
            
            write(rovas, input.getAbsolutePath(), "sima");
		} catch (Exception e) {
			System.out.println("Unable to open file '" + input.getName() + "'");
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
		// The name of the file to write into.
        String fileName =  path.substring(0, path.length() - 4) + "_" + type + ".txt";
        try {
            // Assume default encoding.
            FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(output);
            
            bufferedWriter.close();
		} catch (IOException ex) {
			System.out.println("Error writing to file '" + fileName + "'");
		}
    }
	
}
