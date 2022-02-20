package visualizer;

import com.google.gson.Gson;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.HashMap;

public class ImageResourceHandler {
	private static ImageResourceHandler imageResourceHandler = new ImageResourceHandler("graphic/imageList.json");
	private HashMap<String,String> ImageResources;
	private Gson gson = new Gson();
	private File file;
	private ImageResourceHandler(String filename){
		try {
			this.file = new File(getClass().getClassLoader().getResource(filename).toURI());
		}catch (URISyntaxException e){
			System.out.println("wrong URL");
		}
		ImageResources = new HashMap<>();
		readResources();
	}
	private void readResources(){
		try {
			InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
			ImageResource[] resources = gson.fromJson(reader, ImageResource[].class);
			for (ImageResource imageResource:resources){
				ImageResources.put(imageResource.getId(),imageResource.getUrl());
			}
			System.out.println("resources have been read");
		}catch (FileNotFoundException e){
			System.out.println("can not find resource");
		}catch (IllegalStateException e){
			System.out.println("wrong type of resource");
		}
	}
	public static ImageResourceHandler getInstance(){
		if (imageResourceHandler==null){
			imageResourceHandler = new ImageResourceHandler("Resources.json");
		}
		return imageResourceHandler;
	}

	public ImageData getImage(String id) {
		String s = ImageResources.get(id);
		System.out.println(s);
		ImageData imageData = ImageDataFactory.create(getClass().getClassLoader().getResource(ImageResources.get(id)));
		return imageData;
	}
}
