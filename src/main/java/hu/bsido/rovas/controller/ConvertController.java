package hu.bsido.rovas.controller;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import com.google.common.eventbus.Subscribe;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.rtfparserkit.converter.text.AbstractTextConverter;
import com.rtfparserkit.parser.IRtfParser;
import com.rtfparserkit.parser.IRtfSource;
import com.rtfparserkit.parser.RtfStreamSource;
import com.rtfparserkit.parser.standard.StandardRtfParser;

import hu.bsido.rovas.Main;
import hu.bsido.rovas.common.Events.FillConvertEvent;
import hu.bsido.rovas.common.Events.RemoveLastSceneEvent;
import hu.bsido.rovas.common.LRResources;
import hu.bsido.rovas.common.LRResources.SlideDirection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class ConvertController implements Initializable {

	@FXML private JFXButton btnBack;
	@FXML private JFXButton btnConvert;
	@FXML private JFXSpinner spinner;

	private File toConvert;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Main.register(this);
		spinner.setVisible(false);

		btnBack.setOnAction(e -> {
			Main.postEvent(new RemoveLastSceneEvent(this, SlideDirection.LEFT));
		});

		btnConvert.setOnAction(e -> {
			convert2normal();
		});
	}

	@Subscribe
	public void fill(FillConvertEvent e) {
		toConvert = e.file;
	}

	private void convert2normal() {
		spinner.setVisible(true);

		if (toConvert.getName().endsWith(".rtf")) {
			fromRTF();
		} else {
			fromTXT();
		}

		spinner.setVisible(false);
	}

	private void fromTXT() {
		String pdfPath = toConvert.getAbsolutePath().substring(0, toConvert.getAbsolutePath().length() - 4) + ".pdf";

		try {
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(pdfPath));

			BufferedReader br = new BufferedReader(new FileReader(toConvert.getAbsolutePath()));

			// or on mac (after adding it to FontBook):
			// /Users/<UserName>/Library/Fonts/<FontName>.ttf
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

			if (Desktop.isDesktopSupported()) {
				File myFile = new File(pdfPath);
				Desktop.getDesktop().open(myFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void fromRTF() {
		String pdfPath = toConvert.getAbsolutePath().substring(0, toConvert.getAbsolutePath().length() - 4) + ".pdf";

		try {
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(pdfPath));

			// or on mac (after adding it to FontBook):
			// /Users/<UserName>/Library/Fonts/<FontName>.ttf
			BaseFont base = BaseFont.createFont("font/rov_fsjb.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			Font rovasFont = new Font(base, 11f);
			PdfPTable table = new PdfPTable(1);
			table.setWidthPercentage(100);

			PdfPCell cell = new PdfPCell();
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

			InputStream is = new FileInputStream(toConvert.getAbsolutePath());
			IRtfSource source = new RtfStreamSource(is);
			IRtfParser parser = new StandardRtfParser();
			parser.parse(source, new AbstractTextConverter() {
				@Override
				public void processExtractedText(String text) {
					if ("\n".equals(text)) {
						return;
					}
					// add special character to make right-to-left text
					String rovas = "\u202E" + simarovas(text.toLowerCase());
					Phrase p = new Phrase(rovas, rovasFont);
					cell.addElement(p);
				}
			});

			document.open();
			table.addCell(cell);
			document.add(table);
			document.close();

			if (Desktop.isDesktopSupported()) {
				File myFile = new File(pdfPath);
				Desktop.getDesktop().open(myFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String simarovas(String line) {
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
}
