package GUI;

import custom_exceptions.TXMLException;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import player.MXLPlayer;
import player.ThreadPlayer;
import visualizer.Visualizer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class PreviewViewController extends Application {
	@FXML ImageView pdfViewer;
	@FXML TextField gotoPageField;
	@FXML ChoiceBox configs;
	@FXML ChoiceBox values;
	@FXML ScrollPane scrollView;
	private static Window convertWindow = new Stage();

	private double scale = 1.0;
	private String selected = "";
	private PreviewConfig c = PreviewConfig.getInstance();
	private MainViewController mvc;
	private int pageNumber = 0;
	private Visualizer visualizer;
	private MXLPlayer player;
	public static ThreadPlayer thp;
	public Scene scene;
	public Node currentSelected;
	public ArrayList<Group> groups;
	public void setMainViewController(MainViewController mvcInput) {
		mvc = mvcInput;
	}
	public void setScene(Scene scene){
		this.scene = scene;
	}
	public void update() throws TXMLException, FileNotFoundException, URISyntaxException {
		this.visualizer = new Visualizer(mvc.converter.getScore());
		groups = visualizer.getElementGroups();
		AnchorPane anchorPane = new AnchorPane();
		anchorPane.getChildren().add(groups.get(0));
		initEvents(anchorPane);
		scrollView.setContent(anchorPane);


	}
	private void initEvents(AnchorPane anchorPane){

		KeyCombination zoomOut = new KeyCodeCombination(KeyCode.PAGE_DOWN,KeyCombination.CONTROL_DOWN);
		KeyCombination zoomIn = new KeyCodeCombination(KeyCode.PAGE_UP,KeyCombination.CONTROL_DOWN);
		scene.getAccelerators().put(zoomIn,()->{
			scale = scale+scale*0.05;
			anchorPane.setScaleX(scale);
			anchorPane.setScaleY(scale);
		});

		scene.getAccelerators().put(zoomOut,()->{
			scale = scale-scale*0.05;
			anchorPane.setScaleX(scale);
			anchorPane.setScaleY(scale);
		});
		scrollView.setOnDragDropped(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {

			}
		});
	}
	@FXML
	private void exportPDFHandler(){
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showSaveDialog(convertWindow);
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("pdf files", "*.pdf");
		fileChooser.getExtensionFilters().add(extFilter);

		if (file!=null){
			try {

			}catch (Exception e){

			}
		}

	}
	@FXML
	private void LastPageHandler(){
		//goToPage(pageNumber-1);

	}
	@FXML
	private void NextPageHandler(){
		//goToPage(pageNumber+1);
	}
	@FXML
	private void goToPageHandler(){
		//int pageNumber = Integer.parseInt(gotoPageField.getText());
		//goToPage(pageNumber);
	}
	@FXML
	private void apply(){
		//refreshPDF();
	}
	@FXML
	private void playHandler(){
		String s = player.getString(-1,-1,-1);
		thp = new ThreadPlayer("music-thread");
		thp.start(s);
	}
	private void goToPage(int page)  {

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

