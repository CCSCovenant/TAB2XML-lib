package GUI;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerNextArrowBasicTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.Pair;
import visualElements.VElement;
import visualElements.VUtility;

import java.util.HashMap;

public class Sidebar {
	public VBox vbox;
	public ScrollPane scrollPane;
	public HBox hboxMain;
	private PreviewViewController controller;
	JFXDrawer drawer;
	JFXHamburger hamburger;

	public Sidebar(PreviewViewController controller) {
		this.controller = controller;
		this.hboxMain = new HBox();
		this.hboxMain.setPrefSize(300, 574);
		this.scrollPane = new ScrollPane(this.vbox);
		this.scrollPane.setPrefWidth(300);
		this.scrollPane.setFitToHeight(true);
		this.scrollPane.setFitToWidth(true);
		this.hboxMain.getChildren().add(scrollPane);
	}

	public void initialize(JFXDrawer drawer, JFXHamburger hamburger) {
		this.drawer = drawer;
		this.hamburger = hamburger;
		drawer.setSidePane(this.hboxMain);
		drawer.setOverLayVisible(false);
		HamburgerNextArrowBasicTransition burgerTask2 = new HamburgerNextArrowBasicTransition(hamburger);
		burgerTask2.setRate(-1);
		hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED,(e)->{
			burgerTask2.setRate(burgerTask2.getRate()*-1);
			burgerTask2.play();
			if(drawer.isOpened()) {
				closeDrawer();
			} else {
				openDrawer();
			}
		});
	}
	public void openDrawer(){
		if (!drawer.isOpened()){
			controller.expendRight();
			drawer.setDisable(false);
			drawer.open();
			drawer.setMinWidth(300);
		}
	}
	public void closeDrawer(){
		if (drawer.isOpened()){
			controller.reduceLeft();
			drawer.close();
			drawer.setDisable(true);
			drawer.setMinWidth(0);
		}
	}

	public void update(VElement vElement) {
		HashMap<String,Double> configMap = vElement.getConfigAbleList();
		HashMap<String,Boolean> configAble = vElement.getConfigAble();
		HashMap<String,Pair<Double,Double>> limitMap = vElement.getLimits();
		HashMap<String,Double> stepMap = vElement.getStepMap();
		scrollPane.setContent(null);
		if (configMap == null){
			return;
		}
		VBox vBox = new VBox();
		Button button = new Button();
		Tooltip tooltip = new Tooltip("Click config option to see help docs");
		tooltip.setShowDelay(new Duration(0));
		button.setTooltip(tooltip);
		button.setText("?");
		vBox.getChildren().add(button);
		for (String key:configMap.keySet()){
			if (!configAble.get(key)){
				continue;
			}
			VBox vBoxInner = new VBox();
			Hyperlink text = new Hyperlink();
			text.setText(VUtility.getDisplayName(key));
			text.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					controller.showURL(VUtility.getHelpDocUrl());
				}
			});
			vBoxInner.getChildren().add(text);
			Spinner spinner = new Spinner();
			double min = limitMap.get(key).getKey();
			double max = limitMap.get(key).getValue();
			double current = configMap.get(key);
			double step = stepMap.get(key);
			spinner.setEditable(true);
			spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(min,max,current,step));
			spinner.valueProperty().addListener(new ChangeListener<Double>() {
				@Override
				public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
					vElement.updateConfig(key,newValue);
					controller.apply();
				}
			});
			vBoxInner.getChildren().add(spinner);
			vBox.getChildren().add(vBoxInner);
		}
		scrollPane.setContent(vBox);
	}
}
