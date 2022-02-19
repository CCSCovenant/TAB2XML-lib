package GUI;

import com.itextpdf.kernel.pdf.PdfDocument;
import custom_exceptions.TXMLException;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import player.MXLPlayer;
import visualizer.Visualizer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PreviewViewController extends Application {
	@FXML ImageView pdfViewer;
	@FXML TextField gotoPageField;

	private final String temp_dest = "tmp.pdf";
	private final int scale = 1;

	private MainViewController mvc;
	private PdfDocument pdf;
	private int pageNumber = 0;
	private Visualizer visualizer;
	private MXLPlayer player;
	private PDDocument document;
	private PDFRenderer renderer;
	public void setMainViewController(MainViewController mvcInput) {
		mvc = mvcInput;
	}
	public void update() throws TXMLException, FileNotFoundException {
		this.player = new MXLPlayer(mvc.converter.getScore());
		this.visualizer = new Visualizer(mvc.converter.getScore());
		pdf = visualizer.draw();
		pdf.close();
		try {
			 document = PDDocument.load(new File(temp_dest));
			 renderer = new PDFRenderer(document);
		}catch (IOException e){

		}
	}
	@FXML
	private void exportPDFHandler(){

	}
	@FXML
	private void LastPageHandler(){
		goToPage(pageNumber-1);

	}
	@FXML
	private void NextPageHandler(){
		goToPage(pageNumber+1);
	}
	@FXML
	private void goToPageHandler(){
		int pageNumber = Integer.parseInt(gotoPageField.getText() );
		goToPage(pageNumber);
	}
	@FXML
	private void playHandler(){
		player.play(-1,-1,-1);
	}
	private void goToPage(int page)  {
		if (page<document.getNumberOfPages()&&page>=0){
			try {
				BufferedImage img = renderer.renderImage(page,scale);
				pdfViewer.setImage(convertToFxImage(img));
				pageNumber = page;
				gotoPageField.setText(pageNumber+"");
			}catch (IOException e){

			}
		}else {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setContentText("This page number is out of range");
			alert.show();
		}
	}
	@Override
	public void start(Stage primaryStage) throws Exception {}

	/**
	 *
	 * */
	private static Image convertToFxImage(BufferedImage image) {
		WritableImage wr = null;
		if (image != null) {
			wr = new WritableImage(image.getWidth(), image.getHeight());
			PixelWriter pw = wr.getPixelWriter();
			for (int x = 0; x < image.getWidth(); x++) {
				for (int y = 0; y < image.getHeight(); y++) {
					pw.setArgb(x, y, image.getRGB(x, y));
				}
			}
		}
		return new ImageView(wr).getImage();
	}
}

