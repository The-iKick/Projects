import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
		

public class ImageViewer extends ImageView {
	private String url;
	
	ImageViewer (String url, int width, int height) {
		this.url = url;
		
		this.setImage(new Image(url));
		this.setFitWidth(width);
		this.setFitHeight(height);
	}
	
	public String getUrl () {
		return url;
	}
}
