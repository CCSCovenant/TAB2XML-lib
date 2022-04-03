package visualizer;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ImageResourceHandler {
	private static ImageResourceHandler imageResourceHandler = new ImageResourceHandler();
	String imageList = "./graphic/imageList.json";
	private Gson gson = new Gson();
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

	public InputStream getImage(String id) {
		try {
			String s = ImageResources.get(id);
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(s);
			return inputStream;
		}catch (Exception e){
			return null;
		}
	}
}
