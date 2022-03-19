package visualizer;

import com.google.gson.Gson;

import java.io.*;
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
			imageResourceHandler = new ImageResourceHandler("./graphic/imageList.json");
		}
		return imageResourceHandler;
	}

	public InputStream getImage(String id) {
		try {
			String s = ImageResources.get(id);
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(s);
			return inputStream;
		}catch (Exception e){
			System.out.println(ImageResources.get(id));
			System.out.println("can't solve resources");
			return null;
		}
	}
}
