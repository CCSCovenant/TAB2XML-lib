package system;

import GUI.PreviewViewController;
import GUI.Sidebar;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import visualElements.GUISelector;
import visualElements.HighLight;
import visualElements.PlayingSelector;
import visualElements.VDot;

public class SelectorTest extends ApplicationTest {
	static VDot dot_1 = new VDot();
	static VDot dot_2 = new VDot();
	static GUISelector guiSelector = GUISelector.getInstance();
	static PlayingSelector playingSelector = PlayingSelector.getInstance();
	public void start(Stage stage){
		guiSelector.setSidebar(new Sidebar(new PreviewViewController()));
	}
	@BeforeEach
	void clear(){
		guiSelector.setSElement(null);
		playingSelector.setPlayingElement(null);
	}
	@Test
	void SelectGUITEST(){
		Assertions.assertNull(guiSelector.getSElement());
		guiSelector.setSElement(dot_1);
		Assertions.assertEquals(dot_1,guiSelector.getSElement());
		Assertions.assertEquals(HighLight.SELECTED,dot_1.highLight);
		guiSelector.setSElement(dot_1);
		Assertions.assertNull(guiSelector.getSElement());
		Assertions.assertEquals(HighLight.NULL,dot_1.highLight);
		guiSelector.setSElement(dot_1);
		guiSelector.setSElement(dot_2);
		Assertions.assertEquals(dot_2,guiSelector.getSElement());
		Assertions.assertEquals(HighLight.SELECTED,dot_2.highLight);
		Assertions.assertEquals(HighLight.NULL,dot_1.highLight);
	}
	@Test
	void SelectPlayingTEST(){
		Assertions.assertNull(playingSelector.getSElement());
		playingSelector.setPlayingElement(dot_1);
		Assertions.assertEquals(dot_1,playingSelector.getSElement());
		Assertions.assertEquals(HighLight.PLAY,dot_1.highLight);
		playingSelector.setPlayingElement(dot_1);
		Assertions.assertNull(playingSelector.getSElement());
		Assertions.assertEquals(HighLight.NULL,dot_1.highLight);
		playingSelector.setPlayingElement(dot_1);
		playingSelector.setPlayingElement(dot_2);
		Assertions.assertEquals(dot_2,playingSelector.getSElement());
		Assertions.assertEquals(HighLight.PLAY,dot_2.highLight);
		Assertions.assertEquals(HighLight.NULL,dot_1.highLight);
	}
	@Test
	void CrossSelectTest1(){
		Assertions.assertNull(playingSelector.getSElement());
		Assertions.assertNull(guiSelector.getSElement());

		guiSelector.setSElement(dot_1);

		Assertions.assertEquals(HighLight.SELECTED,dot_1.highLight);

		playingSelector.setPlayingElement(dot_1);

		Assertions.assertEquals(HighLight.PLAY,dot_1.highLight);

		Assertions.assertEquals(dot_1,playingSelector.getSElement());
		Assertions.assertEquals(dot_1,guiSelector.getSElement());

		playingSelector.setPlayingElement(dot_2);

		Assertions.assertEquals(dot_2,playingSelector.getSElement());
		Assertions.assertEquals(HighLight.SELECTED,dot_1.highLight);

	}
	@Test
	void CrossSelectTest2(){
		Assertions.assertNull(playingSelector.getSElement());
		Assertions.assertNull(guiSelector.getSElement());

		guiSelector.setSElement(dot_1);

		Assertions.assertEquals(HighLight.SELECTED,dot_1.highLight);

		playingSelector.setPlayingElement(dot_1);


		Assertions.assertEquals(HighLight.PLAY,dot_1.highLight);
		guiSelector.setSElement(dot_1);

		Assertions.assertEquals(dot_1,playingSelector.getSElement());
		Assertions.assertNull(guiSelector.getSElement());
		Assertions.assertEquals(HighLight.PLAY,dot_1.highLight);

	}
}

