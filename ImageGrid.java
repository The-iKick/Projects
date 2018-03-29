import javafx.scene.layout.GridPane;

/**
 * Represents our image grid
 * 
 * @author Shawn Holman
 * @version 0.1.0
 */
public class ImageGrid extends GridPane {
	private final int IMAGE_WIDTH = 100;
	private final int IMAGE_HEIGHT = 100;
	private final int GRID_COL_COUNT = 5;
	private final int GRID_ROW_COUNT = 4;
	
	ImageGrid () {
		super();
		this.setPrefSize(IMAGE_WIDTH * GRID_COL_COUNT, GRID_ROW_COUNT * IMAGE_HEIGHT);
	}
}
