package GUI;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerNextArrowBasicTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Pair;
import visualElements.VElement;

import java.util.HashMap;

public class Sidebar {
	public VBox vbox;
	public ScrollPane scrollPane;
	public HBox hboxMain;
	private PreviewViewController controller;

	public Sidebar(PreviewViewController controller) {
		this.controller = controller;
		this.hboxMain = new HBox();
		this.hboxMain.setPrefSize(230, 574);
		this.scrollPane = new ScrollPane(this.vbox);
		this.scrollPane.setPrefWidth(260);
		this.hboxMain.getChildren().add(scrollPane);
	}

	public void initialize(JFXDrawer drawer, JFXHamburger hamburger) {
		drawer.setSidePane(this.hboxMain);
		HamburgerNextArrowBasicTransition burgerTask2 = new HamburgerNextArrowBasicTransition(hamburger);
		burgerTask2.setRate(-1);
		hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED,(e)->{
			burgerTask2.setRate(burgerTask2.getRate()*-1);
			burgerTask2.play();
			if(drawer.isOpened()) {
				drawer.close();
			} else {
				drawer.open();
			}
		});
	}

	public void update(VElement vElement) {
		HashMap<String,Double> configMap = vElement.getConfigAbleList();
		HashMap<String,Boolean> configAble = vElement.getConfigAble();
		HashMap<String,Pair<Double,Double>> limitMap = vElement.getLimits();
		scrollPane.setContent(null);
		if (configMap == null){
			return;
		}
		VBox vBox = new VBox();
		for (String key:configMap.keySet()){
			if (!configAble.get(key)){
				continue;
			}
			HBox hBox = new HBox();
			Text text = new Text(key);

			hBox.getChildren().add(text);
			Spinner spinner = new Spinner();
			double min = limitMap.get(key).getKey();
			double max = limitMap.get(key).getValue();
			double current = configMap.get(key);
			spinner.setEditable(true);
			spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(min,max,current));
			spinner.valueProperty().addListener(new ChangeListener<Double>() {
				@Override
				public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
					vElement.updateConfig(key,newValue);
					controller.apply();
				}
			});
			hBox.getChildren().add(spinner);
			vBox.getChildren().add(hBox);
		}
		scrollPane.setContent(vBox);
	}
}
