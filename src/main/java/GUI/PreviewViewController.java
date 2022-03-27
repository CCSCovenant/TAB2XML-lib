package GUI;

import com.itextpdf.kernel.pdf.PdfDocument;
import custom_exceptions.TXMLException;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
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
	private final String temp_dest = "./tmp.pdf";
	private final int scale = 2;
	private static Window convertWindow = new Stage();

	private ObservableList<String> configsList;
	private ObservableList<Double> configsValueList;
	private String selected = "";
	private PreviewConfig c = PreviewConfig.getInstance();
	private MainViewController mvc;
	private PdfDocument pdf;
	private int pageNumber = 0;
	private Visualizer visualizer;
	private MXLPlayer player;
	public static ThreadPlayer thp;

	public Node currentSelected;
	public ArrayList<Group> groups;
	boolean flag = false;
	boolean flag1 = false;
	boolean flag2 = false;
	public void setMainViewController(MainViewController mvcInput) {
		mvc = mvcInput;
	}
	public void update() throws TXMLException, FileNotFoundException, URISyntaxException {

		//this.player = new MXLPlayer(mvc.converter.getScore());
		this.visualizer = new Visualizer(mvc.converter.getScore());
		groups = visualizer.getElementGroups();
		AnchorPane anchorPane = new AnchorPane();
		anchorPane.getChildren().add(groups.get(0));
		scrollView.setContent(anchorPane);

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
	private void initChoiceBox(){
		configs.setItems(c.getConfigList());
		configsList = c.getConfigList();
		configs.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				selected = configsList.get(newValue.intValue());
				configsValueList = c.getValues(selected);
				values.setItems(c.getValues(selected));
			}
		});
		values.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue()>=0&&newValue.intValue()<configsValueList.size()){
					c.setConfig(selected,configsValueList.get(newValue.intValue()));
				}
			}
		});
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

