package hu.bsido.rovas.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.google.common.eventbus.Subscribe;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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
			convert2normal();
		});
	}

	@Subscribe
	public void fill(FillConvertEvent e) {
		toConvert = e.file;
	}
	
	private void convert2normal(){
		if (lbOutput.getValue() == null) {
			return;
		}
		
		switch (lbOutput.getValue()) {
			case pdf:
				createPDF();
				break;
			default: break;
		}
		
//		try {
//            // FileReader reads text files in the default encoding.
//            FileReader fileReader = new FileReader(toConvert.getAbsolutePath());
//            BufferedReader bufferedReader = new BufferedReader(fileReader);
//            
//            StringBuilder sb = new StringBuilder();
//            
//            while((line = bufferedReader.readLine()) != null) {
//                rovas = sb.append(simarovas(line.toLowerCase()) + "\r\n").toString();
//            }  
//            bufferedReader.close(); 
//            
//            write(rovas, toConvert.getAbsolutePath(), "sima");
//		} catch (Exception e) {
//			System.out.println("Unable to open file '" + toConvert.getName() + "'");
//        }
    }
	
	private void createPDF() {
		String pdfPath = toConvert.getAbsolutePath().substring(0, toConvert.getAbsolutePath().length() - 4) + ".pdf";
		
		try {
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
			
			BufferedReader br = new BufferedReader(new FileReader(toConvert.getAbsolutePath()));
			
			// or on mac (after adding it to FontBook): /Users/<UserName>/Library/Fonts/<FontName>.ttf
			BaseFont base = BaseFont.createFont("font/rov_fsjb.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			Font rovasFont = new Font(base, 11f);
            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(100);

            PdfPCell cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			
			String line = null;
			while ((line = br.readLine()) != null) {
				// add special character to make right-to-left text
				String rovas = "\u202E" + simarovas(line.toLowerCase());

				Phrase p = new Phrase(rovas, rovasFont);
	            cell.addElement(p);
	        }

			document.open();
            table.addCell(cell);
            document.add(table);
	        document.close();
            br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String simarovas(String line) {
		for (int i = 0; i < LRResources.ROVASOK.length; i++) {
			line = line.replaceAll(LRResources.ROVASOK[i][0], LRResources.ROVASOK[i][1]);			
		}
		return line;
	}

	public static String trueReverse(final String input) {
		final StringBuilder sb = new StringBuilder();
		sb.append(input);

		return sb.reverse().toString();
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
