package GUI;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerNextArrowBasicTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class sidebar {
	public VBox vbox;
	public ScrollPane scrollPane;
	public HBox hboxMain;
	public HBox hboxMini;
	JFXButton applyChanges;
	ArrayList<Text> labels = new ArrayList<>(18);
	ArrayList<Spinner> spinners = new ArrayList<>(18);
	@FXML public JFXDrawer d;
	@FXML Spinner noteDistance;
	private HashMap<String, Double> configMap; // size 18

	public sidebar() {
		this.hboxMini = new HBox();
		this.hboxMini.setPrefSize(38,48);
		this.hboxMini.setAlignment(Pos.BOTTOM_RIGHT);
		applyChanges = new JFXButton("Apply");
		this.hboxMini.getChildren().add(applyChanges);

		this.hboxMain = new HBox();
		this.hboxMain.setPrefSize(230, 574);

		this.vbox = new VBox();
		this.vbox.setPrefSize(210, 574);

//		configMap = VConfig.getInstance().getGlobalConfig();
		String[] str = {"Note Distance","Gap Before Measure", "Gap Between Element", "Gap Between Grace",
				"Dot size", "Dot Gap with Last Element", "Grace Offset", "Scale", "Default Size",
				"Dot Gap", "Barline Distance Between Line", "Barline Notation Height",
				"Barline Notation Size", "Guitar Notation Start Height", "Guitar Notation End Height",
				"Drum Notation Height", "Notation Gap", "Notation Thickness"};

		Font font = Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 14);

		for(int i = 0; i < 18; i++) {
			this.labels.add(new Text(str[i]));
			this.labels.get(i).setFont(font);
			this.vbox.getChildren().add(labels.get(i));
			this.spinners.add(new Spinner<>(0,20,0));
			this.vbox.getChildren().add(spinners.get(i));
		}

		this.vbox.getChildren().add(hboxMini);

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

	public void applySettings() {

	}
}
