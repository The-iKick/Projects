import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.input.KeyCode;
import javafx.geometry.Insets;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Represents the Controls that allow us to Pause/Play the gallery and search a query
 * 
 * @author Shawn Holman
 * @version 0.1.0
 */
public class Controls extends HBox {
	boolean playing = true; // is the gallery playing?
	boolean loading = false; // are we loading a new query?
	
	TextField queryField;
	ImageGridManager gridManager;
	
	Controls(StackPane root, ImageGridManager grid) {
		super();
		
		this.gridManager = grid;
		// center text field
		queryField = new TextField();
		queryField.setPromptText("Try searching for \"Pop\"");
		queryField.setAlignment(Pos.CENTER); // center the text
		queryField.setOnKeyReleased((event) -> { // allows us to query using the enter butter
			if(!loading && event.getCode().equals(KeyCode.ENTER)) { 
				queryImages(root);
			}
		});
		
		// spacers for alignment
		final Pane spacer = new Pane();
	    HBox.setHgrow(spacer, Priority.ALWAYS);
	    final Pane spacer2 = new Pane();
	    HBox.setHgrow(spacer2, Priority.ALWAYS);
	    
	    // create an HBox for the query field so that we can vertically center the input
	    HBox input = new HBox();
	    input.setAlignment(Pos.CENTER_LEFT);
	    input.getChildren().addAll(queryField);
		
		this.setId("controls");
		this.getChildren().addAll(getPlayToggle(), spacer, input, spacer2, getUpdateButton(root));						
	}
	
	/**
	 * Creates a Button that when clicked toggles between "Pause" and "Play" text and respectively
	 * performing these actions on the gallery.
	 * 
	 * @return modified button object
	 */
	Button getPlayToggle () {
		// button menu
		Button playToggle = new Button("Pause");
		playToggle.setOnAction((e) -> {
			if (playing) {
				playToggle.setText("Play");
				gridManager.stopGallery();
				playing = false;
			} else {
				playToggle.setText("Pause");
				gridManager.startGallery();
				playing = true;
			}
		});
		return playToggle;
	}
	
	/**
	 * Creates a button that when clicked will query for new images
	 * 
	 * @param root Our root is being passed along as it will be used for look ups
	 * @return modified button
	 */
	Button getUpdateButton (StackPane root) {
		Button update = new Button("Update Images");
		
		// our button had a weird offset issue which caused a small space to the right of it
		// so I just used some negated margins to fix the issue
		HBox.setMargin(update, new Insets(0,-1,0, 0)); 
		update.setOnAction(e -> {
			 queryImages(root);
		});
		return update;
	}
	
	private void queryImages (StackPane root) {
		String query = queryField.getText().trim();
		
		loading = true;
		if (query.length() > 0) {
			// append loader
			LabeledProgressBar loadingOverlay = new LabeledProgressBar();
			root.getChildren().add(loadingOverlay);
			
			String[] urls = JSONParser.generateURLS(query, 50); // generates up to 50 images
			if (urls.length >= 20) { // we need at least 20 URLS to display them
				gridManager.stopGallery();
				gridManager = new ImageGridManager(); // reset image manager
				
				Task task = gridManager.loadGrid(urls, () -> {
					VBox mainContent = (VBox) root.lookup("#main-content");
					MenuBar menu = (MenuBar) root.lookup("#menu");
					ImageGrid images = (ImageGrid) root.lookup("#grid");
					
					int menuIndex = mainContent.getChildren().indexOf(menu);
					
					// reset
					mainContent.getChildren().remove(images); // remove the original grid
					mainContent.getChildren().add(menuIndex+1, gridManager.getGrid()); // add the new grid
					
					queryField.setText("");
					if (playing && urls.length > 25) gridManager.startGallery();
					root.getChildren().remove(loadingOverlay);
					
					loading = false;
				});
				loadingOverlay.getBar().progressProperty().bind(task.progressProperty());
				Thread thread = new Thread(task);
				thread.setDaemon(true);
				thread.start();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Unsuffiecient Results");
				alert.setHeaderText("Unsuffiecient Results");
				alert.setContentText("Your query does not provide us with enough images to build a gallery.");
				
				root.getChildren().remove(loadingOverlay);
				alert.showAndWait();
				
				loading = false;
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Blank Query");
			alert.setHeaderText("Blank Query");
			alert.setContentText("Your query can not be blank!");
			
			alert.showAndWait();
			
			loading = false;
		}
	}
	
	public ImageGridManager getCurrentImageGridManager () {
		return gridManager;
	}
}
