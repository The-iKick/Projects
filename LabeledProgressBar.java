import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.paint.Color;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

/**
 * Represents a progress bar that overlays over another pane using the StackPane
 * 
 * @author Shawn Holman
 * @version 0.1.0
 */
public class LabeledProgressBar extends VBox {
	ProgressBar loading;
	Text progress;
	
	LabeledProgressBar () {
		super();
		
		loading = new ProgressBar();
		progress = new Text("0%");
		progress.setFill(Color.WHITE);
		
		loading.setProgress(0);
		
		// connect the progress to the text
		loading.progressProperty().addListener((ov, oldvalue, newvalue) -> {
			int value = (int) Math.round(Double.parseDouble(newvalue.toString())*100);
			progress.setText((value < 0 ? 0 : value) + "%");
		});
		
		this.setId("overlay");
		this.setAlignment(Pos.CENTER);
		this.getChildren().addAll(loading, progress);
	}
	
	/**
	 * Set the progress of the progress bar
	 * @param percent percent of the progress bar
	 */
	public final void setProgress (double percent) {
		loading.setProgress(percent);
		progress.setText((percent * 100)  + "%");
	}
	
	/**
	 * Gets the progress bar object
	 * @return the progress abr
	 */
	public ProgressBar getBar () {
		return loading;
	}
	
	/**
	 * Nicely fades out the progress bar loader
	 * 
	 * @param event The event to call when the animation has completed
	 * @param time The time (in milliseconds) that the fade out takes
	 */
	public void fadeOut (EventHandler<ActionEvent> event, int time) {
		Timeline timeline = new Timeline();
		KeyValue transparent = new KeyValue(this.opacityProperty(), 0.0);
		KeyValue opaque = new KeyValue(this.opacityProperty(), 1.0);
		
		KeyFrame keyFrame = new KeyFrame(Duration.millis(time), event, opaque, transparent);
		timeline.getKeyFrames().add(keyFrame);
		timeline.play();
	}
}
