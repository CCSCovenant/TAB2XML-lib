package visualizer;

public class ImageResource {
	public String name; // id for human to read
	public String url; // file url
	public String id; // id for program to read
	public ImageResource(){

	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
