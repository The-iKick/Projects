import java.io.IOException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Node;

import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Represents a class that manages ImageGrid classes by loading, resetting, and switching images in the grid
 * 
 * @author Shawn Holman
 * @version 0.1.0
 */
public class ImageGridManager {
	/**
	 * Represents a function that takes no arguments and returns no value meant to perform a 
	 * constant task.
	 */
	public interface Completion {
		void call ();
	}
	
	ImageGrid currentGrid;
	
	private final int IMAGE_WIDTH = 100;
	private final int IMAGE_HEIGHT = 100;
	private final int GRID_COL_COUNT = 5; 
	private final int GRID_ROW_COUNT = 4;
	private final int GRID_COUNT = GRID_COL_COUNT * GRID_ROW_COUNT;
	
	Timer timer;
	String[] imageRotation;
	
	int atCol = 0; // for the sake of adding the images, we keep track of the columns
	int atRow = 0; // and the rows in order to index them correctly
	
	ImageGridManager () { // each grid manager has its own image grid
		currentGrid = new ImageGrid();
	}
	
	/**
	 * Get the current image grid
	 * 
	 * @return current image grid
	 */
	ImageGrid getGrid() { 
		return currentGrid;
	}
	
	/**
	 * Creates a new task meant to keep track of the progress of the images that are loading into the 
	 * new image grid. This allows us to show a progress bar that is accurate to what is happening
	 * behind the scene
	 * 
	 * @param urls an array of urls that will be used to fetch the images
	 * @param onComplete a function to be called when all images have loaded
	 * @return the task
	 */
	Task loadGrid (String[] urls, Completion onComplete) {
		return new Task<Void>() {
		    @Override 
		    public Void call() throws IOException {
				for (int i = 0; i < GRID_COUNT; i++) {
					ImageViewer image = new ImageViewer(urls[i], IMAGE_WIDTH, IMAGE_HEIGHT);
					
					Platform.runLater(() -> {
						if (atCol == GRID_COL_COUNT) { // if we are at the end of the columns then go back
							atCol =  0;
							atRow += 1;
						}
						image.setId("gridItem-" + (atCol++) + "-" + atRow);
						currentGrid.add(image, atCol-1, atRow);
					});
					if (i == GRID_COUNT-1) { // if we are at the last image
						Platform.runLater(() -> {
							onComplete.call();
							currentGrid.setId("grid");
						});
					}
					updateProgress(i, 19);
				}
				// the remaining images will go into rotation
				imageRotation = Arrays.copyOfRange(urls, GRID_COUNT, urls.length);
				
		        return null;
		    }
		};
	}
	
	/**
	 * Stop the Gallery and remove the timer thread
	 */
	public void stopGallery () {
		if (timer != null) {
			timer.cancel();
			timer.purge();
		}
	}
	
	/**
	 * Creates a new timer thread that randomly replaces an image in the image grid every 2 seconds
	 */
	public void startGallery () {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask(){
		    @Override
		    public void run(){
		    		Random rand = new Random();
		    		int randIndex = rand.nextInt(imageRotation.length);
		    		ImageViewer image = new ImageViewer(imageRotation[randIndex], IMAGE_WIDTH, IMAGE_HEIGHT);

		    		Platform.runLater(() -> {
		    			int randCol = rand.nextInt(GRID_COL_COUNT);
		    			int randRow = rand.nextInt(GRID_ROW_COUNT);
		    			ImageViewer currentImage = (ImageViewer) currentGrid.lookup("#gridItem-" + randCol + "-" + randRow);
		    			
		    			imageRotation[randIndex] = currentImage.getUrl();
		    			currentGrid.getChildren().remove(currentGrid.lookup("#gridItem-" + randCol + "-" + randRow));
		    			image.setId("gridItem-" + randCol + "-" + randRow);
		    			
		    			currentGrid.add(image, randCol, randRow);
		    		});
		    }
		}, 0, 2000);
	}
}
