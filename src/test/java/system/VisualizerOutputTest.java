package system;

import converter.Score;
import custom_exceptions.TXMLException;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import utility.SwingFXUtils;
import visualElements.VConfig;
import visualizer.Visualizer;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class VisualizerOutputTest extends ApplicationTest {
	@Override
	public void start(Stage stage) throws IOException, URISyntaxException, TXMLException {
		 URL outDirURL = this.getClass().getClassLoader().getResource("../../resources/test/outputs/");
		 Path outDirPath = Path.of(outDirURL.toURI());
		 File outDir= outDirPath.toFile();
		 File[] outputFiles = outDir.listFiles();
		 for (File file : outputFiles) file.delete();

		 URL inputDirURL = this.getClass().getClassLoader().getResource("../../resources/test/system/");
		 Path inputDirPath = Path.of(inputDirURL.toURI());
		 File inputDir = inputDirPath.toFile();
		 File[] inputFiles = inputDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));

		 for (File input : inputFiles) {
			 String inputText = Files.readString(input.toPath());
			 StringBuilder inputEdit = new StringBuilder(inputText);
			 for (int i = 0; i < inputEdit.length(); i++) {
				 if (inputEdit.charAt(i) == '\r') {
					 inputEdit.deleteCharAt(i);
				 }
			 }
			 Score score = new Score(inputEdit.toString());
			 Visualizer visualizer = new Visualizer(score);
			 List<Group> groups = visualizer.getElementGroups();
			 String pdfName = input.getName().substring(0,input.getName().lastIndexOf("."))+".pdf";
			 Path outFile = outDirPath.resolve(pdfName);
			 File file = outFile.toFile();
			 try {
				 PDDocument document = new PDDocument();
				 for (Group group:groups) {
					 WritableImage image = group.snapshot(new SnapshotParameters(), null);
					 System.out.println("0");
					 ByteArrayOutputStream output = new ByteArrayOutputStream();
					 ImageIO.write(SwingFXUtils.fromFXImage(image,null),"png",output);
					 output.close();

					 PDPage page = new PDPage();
					 document.addPage(page);
					 PDImageXObject pdfimage = PDImageXObject.createFromByteArray(document,output.toByteArray(),"png");
					 PDPageContentStream contentStream = new PDPageContentStream(document,page);

					 PDRectangle box = page.getMediaBox();
					 double w = VConfig.getInstance().getGlobalConfig("PageX");
					 double h = VConfig.getInstance().getGlobalConfig("PageY");
					 double factor = Math.min(box.getWidth() / w, box.getHeight() / h);
					 contentStream.drawImage(pdfimage,  0,0,(float)(w*factor),(float)(h*factor));
					 contentStream.close();
				 }
				 document.save(file);
				 document.close();
			 }catch (Exception e){
				 System.out.println(e.fillInStackTrace());
			 }
		 }

	}
	@Test
	public void forceStart(){

	}
}
