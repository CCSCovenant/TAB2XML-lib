package visualizer;

import com.google.gson.Gson;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ImageResourceHandler {
	private static ImageResourceHandler imageResourceHandler = new ImageResourceHandler("./graphic/imageList.json");
	private Gson gson = new Gson();
	private File file;

	public HashMap<String,String> ImageResources;

	private ImageResourceHandler(String filename){
		try {
			this.file = new File(filename);

		}catch (Exception e){
			System.out.println("wrong URL");
		}
		ImageResources = new HashMap<>();
		readResources();
	}
	private void readResources(){
		System.out.println("reading json");
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
		try {
			String s = ImageResources.get(id);
			ImageData imageData = ImageDataFactory.create(ImageResources.get(id));
			return imageData;
		}catch (Exception e){
			System.out.println(ImageResources.get(id));
			System.out.println("can't solve resources");
			return null;
		}
	}
}
