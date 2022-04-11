package GUI;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import converter.Score;
import custom_exceptions.TXMLException;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import javafx.util.Pair;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import player.LinkedPlayer;
import utility.SwingFXUtils;
import visualElements.*;
import visualizer.ImageResourceHandler;
import visualizer.Visualizer;

import javax.imageio.ImageIO;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

public class PreviewViewController extends Application {
	@FXML Spinner<Integer> pageSpinner;
	@FXML Spinner<Integer> measureSpinner;
	@FXML ScrollPane scrollView;
	@FXML Button refresh;
	@FXML Button saveMXLButton;
	@FXML private JFXHamburger hamburger;
	@FXML private JFXDrawer drawer;
	@FXML ToggleButton playButton;
	@FXML ToggleButton repeatButton;
	@FXML Spinner tempoAdjust;
	Sidebar sidebar;
	private static Window convertWindow = new Stage();

	Score score;
	private double scale = 1.0;
	private int pageNumber = 0;
	private Visualizer visualizer;
	HashMap<Integer, Pair<Integer,Integer>> measureMapping;
	public boolean isPlaying;
	public LinkedPlayer linkedPlayer;
	public Scene scene;
	public Stage stage;
	public ArrayList<Group> groups;

	public void updateScore(Score score){
		this.score = score;
	}

	public void setSceneAndStage(Scene scene,Stage stage){
		this.scene = scene;
		this.stage =stage;
	}

	public LinkedPlayer getLinkedPlayer() {
		return linkedPlayer;
	}

	public void update() throws TXMLException, FileNotFoundException, URISyntaxException {
		reset();
		sidebar = new Sidebar(this);
		sidebar.initialize(drawer, hamburger);
		GUISelector.getInstance().setSidebar(sidebar);
		GUISelector.getInstance().setSElement(null);
		goToPage(0);
		initPageHandler(groups.size());
		initMeasureHandler(visualizer.getMeasureCounter());
		initRefresh();
		initToggleIcon();
		initTempoSpinner();
		//goToPage(pageNumber);
	}
	private void initRefresh(){
		ImageView refreshView = new ImageView(ImageResourceHandler.getInstance().getImage("refresh"));
		refreshView.setFitWidth(40);
		refreshView.setFitHeight(40);
		refresh.setGraphic(refreshView);
		RotateTransition rotate = new RotateTransition(Duration.seconds(2),refresh);
		rotate.setCycleCount(Animation.INDEFINITE);
		rotate.setByAngle(360);

		FadeTransition fade = new FadeTransition(Duration.seconds(0.2),refresh);
		fade.setFromValue(1.0);
		fade.setToValue(0.2);
		fade.setCycleCount(2);
		fade.setAutoReverse(true);
		refresh.setOnMouseEntered(e->rotate.play());
		refresh.setOnMouseExited(e->rotate.pause());
		refresh.setOnMouseClicked(e->fade.play());
	}
	private void initToggleIcon(){
		ImageView playIcon = new ImageView(ImageResourceHandler.getInstance().getImage("play"));
		playIcon.setFitWidth(30);
		playIcon.setFitHeight(30);
		Tooltip playTooltip = new Tooltip("Select to play music, unselect to stop music");
		ImageView repeatIcon = new ImageView(ImageResourceHandler.getInstance().getImage("enableRepeat"));
		repeatIcon.setFitWidth(30);
		repeatIcon.setFitHeight(30);
		Tooltip repeatTooltip = new Tooltip("Select to enable repeat, unselect to disable repeat");

		ImageView exportIcon = new ImageView(ImageResourceHandler.getInstance().getImage("saveIcon"));
		exportIcon.setFitWidth(30);
		exportIcon.setFitHeight(30);
		Tooltip exportTooltip = new Tooltip("Select to save file");
		saveMXLButton.setGraphic(exportIcon);
		saveMXLButton.setTooltip(exportTooltip);
		playButton.setGraphic(playIcon);
		playButton.setTooltip(playTooltip);
		repeatButton.setGraphic(repeatIcon);
		repeatButton.setTooltip(repeatTooltip);
	}
	private void initTempoSpinner(){
		tempoAdjust.setEditable(true);
		tempoAdjust.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 240, 120));
		tempoAdjust.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				VConfig.getInstance().updateConfig("tempo",newValue);
			}
		});

	}
	private void initPageHandler(int max_page){
		pageSpinner.setEditable(true);
		pageSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, max_page, 1));
		pageSpinner.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				goToPage(newValue-1);
			}
		});

	}
	private void initMeasureHandler(int max_measures){
		measureSpinner.setEditable(true);
		measureSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, max_measures, 1));
		measureSpinner.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				GUISelector.getInstance().setSElement(goToMeasure(newValue));
			}
		});
	}
	public VMeasure goToMeasure(int measureNumber){
		Pair<Integer,Integer> localPart = measureMapping.get(measureNumber);
		VMeasure measure = visualizer.getVMeasures().get(measureNumber-1);
		double YPos = measure.getVLine().getShapeGroups().getLayoutY() - VConfig.getInstance().getGlobalConfig("MeasureDistance");
		double XPos = measure.getShapeGroups().getLayoutX();
		double PageX = VConfig.getInstance().getGlobalConfig("PageX");
		double PageY = VConfig.getInstance().getGlobalConfig("PageY");
		double factorY = 1;
		double factorX = 1;
		Bounds bounds = scrollView.getViewportBounds();
		double diffY = PageY-bounds.getHeight();
		double diffX = PageX-bounds.getWidth();
		if (XPos<diffX) {
			factorX = XPos/diffX;
		}
		if (YPos<diffY) {
			factorY = YPos/diffY;
		}

		int pageNumber = localPart.getKey();
		goToPage(pageNumber);
		scrollView.setVvalue(scrollView.getVmax()*factorY);
		scrollView.setHvalue(scrollView.getHmax()*factorX);
		return measure;
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
	public void reset() throws TXMLException {
		if (linkedPlayer!=null){
			linkedPlayer.stop();
		}
		playButton.setSelected(false);
		VConfig.getInstance().initDefaultConfig();
		this.visualizer = new Visualizer(score);
		this.linkedPlayer = new LinkedPlayer(score);
		linkedPlayer.setController(this);
		groups = visualizer.getElementGroups();
		measureMapping = visualizer.getMeasureMapping();
		linkedPlayer.setVMeasures(visualizer.getVMeasures());
		goToPage(pageNumber);
		initPageHandler(groups.size());
	}
	public void RestPlayButton(){
		playButton.setSelected(false);
		isPlaying = false;
		Color color = VConfig.getInstance().defaultColor;
		Blend blend = new Blend();
		Bounds bounds = ((ImageView)playButton.getGraphic()).getBoundsInLocal();
		ColorInput colorinput = new ColorInput(bounds.getMinX(),bounds.getMinY(),bounds.getWidth(),bounds.getHeight(),color);
		blend.setTopInput(colorinput);
		blend.setMode(BlendMode.SRC_ATOP);
		playButton.getGraphic().setEffect(blend);
	}
	public void apply() {
		try {
			visualizer.alignment();
			groups = visualizer.getElementGroups();
			measureMapping = visualizer.getMeasureMapping();
			double vv = scrollView.getVvalue();
			double hv = scrollView.getHvalue();
			goToPage(pageNumber);
			scrollView.setVvalue(vv);
			scrollView.setHvalue(hv);
			initPageHandler(groups.size());
			pageSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, groups.size(), 1));

		}catch (Exception e){

		}
	}
	@FXML
	private void setRepeat(){
		VConfig.getInstance().setEnableRepeat(repeatButton.isSelected());
		Color color = VConfig.getInstance().defaultColor;
		if (repeatButton.isSelected()){
			color = VConfig.getInstance().getHighLightColor();
		}
		Blend blend = new Blend();
		Bounds bounds = ((ImageView)repeatButton.getGraphic()).getBoundsInLocal();
		ColorInput colorinput = new ColorInput(bounds.getMinX(),bounds.getMinY(),bounds.getWidth(),bounds.getHeight(),color);
		blend.setTopInput(colorinput);
		blend.setMode(BlendMode.SRC_ATOP);
		repeatButton.getGraphic().setEffect(blend);
	}
	@FXML
	private void playHandler() throws InvalidMidiDataException, MidiUnavailableException, InterruptedException {
		Color color = VConfig.getInstance().defaultColor;
		if (playButton.isSelected()){
			color = VConfig.getInstance().getHighLightColor();
		}
		Blend blend = new Blend();
		Bounds bounds = ((ImageView)playButton.getGraphic()).getBoundsInLocal();
		ColorInput colorinput = new ColorInput(bounds.getMinX(),bounds.getMinY(),bounds.getWidth(),bounds.getHeight(),color);
		blend.setTopInput(colorinput);
		blend.setMode(BlendMode.SRC_ATOP);
		playButton.getGraphic().setEffect(blend);

		if (playButton.isSelected()){
			if (GUISelector.getInstance().getSElement()==null){
				isPlaying = true;
				linkedPlayer.play(0);
			}else {
				VElement vElement = GUISelector.getInstance().getSElement();
				int measureNumber = 0;
				if (vElement instanceof VMeasure){
					measureNumber = ((VMeasure) vElement).getNumber()-1;
				}else if (vElement instanceof VNote){
					measureNumber = ((VNote) vElement).getParentMeasure().getNumber()-1;
				}else if (vElement instanceof VNoteHead){
					measureNumber = ((VNoteHead) vElement).getParentNote().getParentMeasure().getNumber()-1;
				}
				isPlaying = true;
				linkedPlayer.play(measureNumber);
			}
		}else {
			isPlaying = false;
			linkedPlayer.stop();
		}
	}
	private void goToPage(int page)  {
		if (0<=page&&page<groups.size()){
			pageNumber = page;
			pageSpinner.getEditor().setText((pageNumber+1)+"");
			pageSpinner.commitValue();
			AnchorPane anchorPane = new AnchorPane();
			anchorPane.getChildren().add(groups.get(page));
			Group group = new Group(anchorPane);
			initEvents(anchorPane);
			scrollView.setContent(group);
			anchorPane.setScaleX(scale);
			anchorPane.setScaleY(scale);
		}else {

		}

	}

	@Override
	public void start(Stage primaryStage) throws Exception {}

	public void expendRight(){
		stage.setResizable(false);
		double W = stage.getWidth();
		stage.setWidth(W+150);
		stage.setResizable(true);
	}
	public void reduceLeft(){
		stage.setResizable(false);
		double W = stage.getWidth();
		stage.setWidth(W-150);
		stage.setResizable(true);
	}
	public void showURL(String url){
		getHostServices().showDocument(url);
	}

}

