package visualizer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ImageResourceHandler {
	private static ImageResourceHandler imageResourceHandler = new ImageResourceHandler("Resources.json");
	private HashMap<String,ImageResource> ImageResources;
	private Gson gson = new Gson();
	private File file;
	public ImageResourceHandler(String filename){

	}
	private void readResources(){
		try(InputStreamReader reader = new InputStreamReader(new FileInputStream(file))){
			JsonArray jsonElements = JsonParser.parseReader(reader).getAsJsonArray();
			for (JsonElement imageResource:jsonElements){
				ImageResource imageResource1 = gson.fromJson(imageResource,ImageResource.class);
				ImageResources.put(imageResource1.id,imageResource1);
			}
		}catch (Exception e){

		}
	}
	public static ImageResourceHandler getInstance(){
		if (imageResourceHandler==null){
			imageResourceHandler = new ImageResourceHandler("Resources.json");
		}
		return imageResourceHandler;
	}

	public Image getImage(String id) throws IOException {
			File imageFile = new File(ImageResources.get(id).url);
			Image image = ImageIO.read(imageFile);
			return image;
	}
}
