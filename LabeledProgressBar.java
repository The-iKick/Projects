import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;

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
}
