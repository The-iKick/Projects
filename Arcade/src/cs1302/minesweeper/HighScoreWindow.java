package cs1302.minesweeper;

import java.io.FileNotFoundException;

import cs1302.driver.Files;
import cs1302.driver.HighScoreTable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HighScoreWindow implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		Stage stage = new Stage(); // create a new window
		
		GridPane root = new GridPane();
		Scene scene = new Scene(root);
        stage.setTitle("Highscores");
        
    		try {	
    				root.add(highscoreBox("Easy"), 0, 0);
    				root.add(highscoreBox("Intermediate"), 1, 0);
    				root.add(highscoreBox("Expert"), 0, 1);
    				root.add(highscoreBox("Impossible"), 1, 1);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    
    		root.setStyle("-fx-padding: 20px");
    		root.setHgap(20);
    		root.setVgap(20);
    		// set up stage
    		stage.setScene(scene);
    		stage.sizeToScene();
	    stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
	}
	
	public VBox highscoreBox (String difficulty) throws FileNotFoundException {
		String Tablefile = "src/cs1302/minesweeper/highscores-"+(difficulty.toLowerCase())+".txt";
		VBox highscoreOverlay = new VBox();
		Label highscore_label = new Label(difficulty);
		
		

		highscoreOverlay.getChildren().add(highscore_label);

		highscoreOverlay.setStyle("-fx-alignment: top-center; -fx-min-width: 200px; -fx-min-height: 200px");	
		highscore_label.setStyle(" -fx-font-size: 20;");
		
		
		if (HighScoreTable.isEmpty(Tablefile)) {
			Label no_highscore = new Label("No High Score");
			VBox.setMargin(no_highscore, new Insets(10, 0, 10, 0));
			highscoreOverlay.getChildren().add(no_highscore);
		} else {
			HighScoreTable highscore_table = new HighScoreTable(Tablefile, -1);
			VBox.setMargin(highscore_table, new Insets(10, 0, 10, 0));
			highscoreOverlay.getChildren().add(highscore_table);
		}
		return highscoreOverlay;
	}
}
