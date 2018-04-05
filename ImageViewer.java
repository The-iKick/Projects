import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class ImageViewer extends StackPane {
	private ImageView image = new ImageView();
	private VBox overlay = new VBox(3);
	private ImageData data;
	private Timeline timeline;
	
	public boolean opened = false;
	
	ImageViewer (ImageData imageInfo, int width, int height) {
		this.data = imageInfo;
		
		image.setImage(new Image(data.getUrl()));
		image.setFitWidth(width);
		image.setFitHeight(height);
		
		this.setOverlay();
		this.setOnMouseEntered((e) -> {
			fadeIn((event) -> {
				opened = true;
			}, 200);
		});
		
		this.setOnMouseExited((e) -> {
			timeline.stop(); // make sure we stop the fadeIn timeline so it does not get stuck
			fadeOut((event) -> {
				opened = false;
			}, 100);
		});
		this.getChildren().addAll(image, overlay);
	}
	
	/**
	 * Shorten a string, adding ellipses if they needed to be cut
	 * @param toShorten String to make shorter
	 * @param max Max length that a string is allowed to be
	 * @return shortened string
	 */
	private String shortenString (String toShorten, int max) {
		return toShorten.substring(0, Math.min(max, toShorten.length())) + (toShorten.length() > max ? ".." : "");
	}
	
	/**
	 * Sets up the overlay for the image
	 */
	private void setOverlay () {
		String artist = shortenString(data.getArtist(), 15);
		String album = shortenString(data.getAlbum(), 15);
		String genre = shortenString(data.getGenre(), 15);
		
		
		Text artistText = new Text(artist);
		Text albumText = new Text(album);
		Text genreText = new Text(genre);
		Text trackLengthText = new Text("# of Tracks: " + data.getTrackLength());
		
		overlay.getChildren().addAll(artistText, albumText, genreText, trackLengthText);
		overlay.setOpacity(0);
	}
	
	/**
	 * Get the ImageData instance
	 * @return ImageData instance
	 */
	public ImageData getImage () {
		return data;
	}
	
	/**
	 * Nicely fades out the overlay
	 * 
	 * @param event The event to call when the animation has completed
	 * @param time The time (in milliseconds) that the fade out takes
	 */
	private void fadeOut (EventHandler<ActionEvent> event, int time) {
		Timeline timeline = new Timeline();
		KeyValue transparent = new KeyValue(overlay.opacityProperty(), 0.0);
		KeyValue opaque = new KeyValue(overlay.opacityProperty(), 1.0);
		
		KeyFrame keyFrame = new KeyFrame(Duration.millis(time), event, opaque, transparent);
		timeline.getKeyFrames().add(keyFrame);
		timeline.play();
	}
	
	
	/**
	 * Nicely fades in the overlay
	 * 
	 * @param event The event to call when the animation has completed
	 * @param time The time (in milliseconds) that the fade out takes
	 */
	private void fadeIn (EventHandler<ActionEvent> event, int time) {
		timeline = new Timeline();
		KeyValue transparent = new KeyValue(overlay.opacityProperty(), 0.0);
		KeyValue opaque = new KeyValue(overlay.opacityProperty(), 1.0);
		
		KeyFrame keyFrame = new KeyFrame(Duration.millis(time), event, transparent, opaque);
		timeline.getKeyFrames().add(keyFrame);
		timeline.play();
	}
}
