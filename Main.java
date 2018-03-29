import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import java.io.File;
import javafx.concurrent.Task;

/**
 * Represents the main driver class for our GUI application. It uses helper classes to put together functionality.
 * and UI representation
 * 
 * @author Shawn Holman
 * @version 0.1.0
 */
public class Main extends Application {
	Controls controls;
		
	public void start(Stage stage) {
		ImageGridManager manager = new ImageGridManager(); // hold our initial image manager
		StackPane root = new StackPane(); // this is our root so that we can stack the loader
		VBox mainContent = new VBox(); 
        Scene scene = new Scene(root);
        
        // load in the css
        File main = new File("src/main.css");
        File f = new File("src/dark.css");
        scene.getStylesheets().addAll("file:///" + main.getAbsolutePath().replace("\\", "/"), "file:///" + f.getAbsolutePath().replace("\\", "/"));
        
        // build the menu, grid, and progressbar for inital display
        FullMenu menu = new FullMenu();
		ImageGrid images = new ImageGrid();	
		controls = new Controls(root, manager);
		LabeledProgressBar loadingOverlay = new LabeledProgressBar();
		
		// load in all of the images using a task
		Task task = manager.loadGrid(JSONParser.generateURLS("rock", 50), () -> {
			int menuIndex = mainContent.getChildren().indexOf(menu);
			
			mainContent.getChildren().remove(images); // remove the original grid
			mainContent.getChildren().add(menuIndex+1, manager.getGrid()); // add the new grid
			manager.startGallery();
			
			root.getChildren().remove(loadingOverlay);
		});
		loadingOverlay.getBar().progressProperty().bind(task.progressProperty());
		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
		
		// setting and assignment
		menu.setId("menu");
		mainContent.setId("main-content");
		mainContent.getChildren().addAll(menu, images, controls);
		root.getChildren().addAll(mainContent, loadingOverlay);
		
		// window initialization
		stage.setTitle("Gallary");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
		stage.show();
	}
	
	public void stop () {
		// this should stop the gallery thread
		controls.getCurrentImageGridManager().stopGallery();
	}
	public static void main(String[] args) {
		Application.launch(args);
	}
}
