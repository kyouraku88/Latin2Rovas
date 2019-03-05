package hu.bsido.rovas.controller;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.rtfparserkit.converter.text.StringTextConverter;
import com.rtfparserkit.parser.RtfStreamSource;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;

public class LearnController implements Initializable {

	@FXML private JFXTextArea taRovas;
	@FXML private JFXTextField tbLatin;
	
	private String toTranscribe;
	private ArrayList<String> words = new ArrayList<>();
	private Random rand = new Random();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		taRovas.setEditable(false);
		taRovas.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		Font rovasFont = Font.loadFont(getClass().getClassLoader().getResourceAsStream("hu/bsido/rovas/common/rov_fsjb.ttf"), 40);
		taRovas.setFont(rovasFont);
		
		loadWords();
		tbLatin.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.ENTER){
			    checkCorrect();
			}
		});
		
		newWord();
//		toTranscribe = "balazs";
//		taRovas.setText("\u202E" + ConvertController.simarovas(toTranscribe));
	}

	private void checkCorrect() {
		if (toTranscribe.equals(tbLatin.getText())) {
			newWord();
			tbLatin.setText("");
		}
	}

	private void loadWords() {
		String filePath = "/Users/kyouraku/Develop/develop/maven/Latin2Rovas/src/main/resources/Eredet.rtf";
		StringTextConverter converter = new StringTextConverter();
		try {
			InputStream is = new FileInputStream(filePath);
			converter.convert(new RtfStreamSource(is));
		} catch (Exception e) {
			e.printStackTrace();
		}
		String extractedText = converter.getText();
		HashSet<String> wordSet = new HashSet<>(Arrays.asList(extractedText.split(" ")));
		words.addAll(wordSet);
	}
	
	private void newWord() {
		Integer randInt = rand.nextInt(words.size());
		toTranscribe = words.get(randInt).toLowerCase().trim();
		System.out.println("'" + toTranscribe + "'");
		taRovas.setText("\u202E" + ConvertController.simarovas(toTranscribe));
	}

}
