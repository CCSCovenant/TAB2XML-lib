package GUI;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import custom_exceptions.TXMLException;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import player.MXLPlayer;
import player.ThreadPlayer;
import utility.SwingFXUtils;
import visualElements.VConfig;
import visualizer.Visualizer;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class PreviewViewController extends Application {
	@FXML ImageView pdfViewer;
	@FXML Spinner<Integer> pageSpinner;
	@FXML Spinner<Integer> measureSpinner;
	@FXML ScrollPane scrollView;
	@FXML private JFXHamburger hamburger;
	@FXML private JFXDrawer drawer;
	sidebar sb1 = new sidebar();
	private static Window convertWindow = new Stage();

	private double scale = 1.0;
	private MainViewController mvc;
	private int pageNumber = 0;
	private Visualizer visualizer;
	private MXLPlayer player;

	public static ThreadPlayer thp;
	public Scene scene;
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
		goToPage(0);

		initPageHandler(groups.size()-1);
		initMeasureHandler(visualizer.getMeasureCounter()-1);
		//goToPage(pageNumber);
	}
	private void initPageHandler(int max_page){
		pageSpinner.setEditable(true);
		pageSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, max_page, 0));
		pageSpinner.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				goToPage(newValue);
			}
		});

	}
	private void initMeasureHandler(int max_measures){
		measureSpinner.setEditable(true);
		measureSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, max_measures, 0));
		measureSpinner.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				goToMeasure(newValue);
			}
		});
	}
	private void goToMeasure(int measureNumber){
		//TODO goto measure
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
		sb1.initialize(drawer, hamburger);
	}
	@FXML
	private void exportPDFHandler() {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("pdf files (*.pdf)", "*.pdf","*.PDF");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showSaveDialog(convertWindow);

		try {
			PDDocument document = new PDDocument();
			for (Group group:groups) {
				WritableImage image = group.snapshot(new SnapshotParameters(), null);

				ByteArrayOutputStream output = new ByteArrayOutputStream();
				ImageIO.write(SwingFXUtils.fromFXImage(image,null),"png",output);
				output.close();

				PDPage page = new PDPage();
				document.addPage(page);
				PDImageXObject pdfimage = PDImageXObject.createFromByteArray(document,output.toByteArray(),"png");
				PDPageContentStream contentStream = new PDPageContentStream(document,page);

				PDRectangle box = page.getMediaBox();
				double w =VConfig.getInstance().getGlobalConfig("PageX");
				double h = VConfig.getInstance().getGlobalConfig("PageY");
				double factor = Math.min(box.getWidth() / w, box.getHeight() / h);
				contentStream.drawImage(pdfimage,  0,0,(float)(w*factor),(float)(h*factor));
				contentStream.close();

				document.save(file);
				document.close();
			}
		}catch (Exception e){
			System.out.println("E");

		}
	}
	@FXML
	private void apply() throws TXMLException {
		visualizer.alignment();
		groups = visualizer.getElementGroups();
		goToPage(pageNumber);
		initPageHandler(groups.size());
	}
	@FXML
	private void playHandler(){
		String s = player.getString(-1,-1,-1);
		thp = new ThreadPlayer("music-thread");
		thp.start(s);
	}
	private void goToPage(int page)  {
		if (0<=page&&page<groups.size()){
			AnchorPane anchorPane = new AnchorPane();
			anchorPane.getChildren().add(groups.get(page));
			Group group = new Group(anchorPane);
			initEvents(anchorPane);
			scrollView.setContent(group);
			scrollView.setVvalue(0);
			pageNumber = page;
		}else {

		}

	}

	@Override
	public void start(Stage primaryStage) throws Exception {}

}

