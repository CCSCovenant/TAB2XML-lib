package visualizer;

import com.google.gson.Gson;
import javafx.scene.image.Image;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ImageResourceHandler {
	private static ImageResourceHandler imageResourceHandler = new ImageResourceHandler();
	String imageList = "graphic/imageList.json";
	private Gson gson = new Gson();
	public HashMap<String,Image> imageMap = new HashMap<>();
	public HashMap<String,String> ImageResources;


	private ImageResourceHandler(){
		ImageResources = new HashMap<>();
		readResources();
	}
	private void readResources(){
		System.out.println("reading json");
		try {
			InputStreamReader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream(imageList));
			ImageResource[] resources = gson.fromJson(reader, ImageResource[].class);
			for (ImageResource imageResource:resources){
				ImageResources.put(imageResource.getId(),imageResource.getUrl());
				InputStream inputStream = getClass().getClassLoader().getResourceAsStream(imageResource.getUrl());
				imageMap.put(imageResource.getId(),new Image(inputStream));
			}
			System.out.println("resources have been read");
		} catch (IllegalStateException e){
			System.out.println("wrong type of resource");
		}
	}
	public static ImageResourceHandler getInstance(){
		if (imageResourceHandler==null){
			imageResourceHandler = new ImageResourceHandler();
		}
		return imageResourceHandler;
	}

	public Image getImage(String id) {
		try {
			return imageMap.get(id);
		}catch (Exception e){
			return null;
		}
	}
}
