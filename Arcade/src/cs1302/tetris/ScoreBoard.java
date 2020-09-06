package cs1302.tetris;

import cs1302.driver.HighScoreTable;
import cs1302.driver.HighScoreTable.Callee;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 * Holds the score for our game
 * @author Shawn
 *
 */
public class ScoreBoard extends VBox{
	HBox scoreText, linesDeletedText, levelText;
	private int score, level, linesDeleted;
	
	ScoreBoard(){
		Label levelLabel = new Label("Level");
		Label linesDeletedLabel = new Label("Lines Deleted");
		Label scoreLabel = new Label("Score");

		score = 0;
		linesDeleted = 0;
		level = 1;
		
		scoreText = new HBox(new Text(Integer.toString(this.score)));
		linesDeletedText = new HBox(new Text(Integer.toString(this.linesDeleted)));
		levelText = new HBox(new Text(Integer.toString(this.level)));
		
		// adds a style class for the purpose of manipulating these in the css
		scoreText.getStyleClass().add("scoreboard-number");
		linesDeletedText.getStyleClass().add("scoreboard-number");
		levelText.getStyleClass().add("scoreboard-number");
		
		this.setStyle("-fx-padding: 20 0 0 0");
		this.getChildren().addAll(scoreLabel,scoreText,
								linesDeletedLabel,linesDeletedText,
								levelLabel,levelText);
	}
	
	/**
	 * Increases the score by n
	 * @param n
	 */
	public void incrementScore(int n) {
		score += n * level;
		((Text) scoreText.getChildren().get(0)).setText(Integer.toString(this.score));
	}
	
	/**
	 * Increases the level by 1
	 */
	public void incrementLevel() {
		level++;
		((Text) levelText.getChildren().get(0)).setText(Integer.toString(this.level));
	}
	
	/**
	 * Increases the number of lines deleted by one
	 */
	public void incrementLinesDeleted() {
		linesDeleted++;
		((Text) linesDeletedText.getChildren().get(0)).setText(Integer.toString(this.linesDeleted));
	}
	
	/**
	 * Get the current level
	 * @return current level
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * Get the current score
	 * @return current score
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * Resets all statistics
	 */
	public void reset () {
		score = 0;
		linesDeleted = 0;
		level = 1;
		((Text) scoreText.getChildren().get(0)).setText(Integer.toString(this.score));
		((Text) levelText.getChildren().get(0)).setText(Integer.toString(this.level));
		((Text) linesDeletedText.getChildren().get(0)).setText(Integer.toString(this.linesDeleted));
	}
	
	/**
	 * Creates the visual aspect of adding a new high score entry
	 * @param file Absolute path of the highscore file
	 * @param score The high score
	 * @param onNewEntry A function that is executed after you add the score
	 * @return the component
	 */
	public static VBox newHighScoreEntry (String file, int score, Callee onNewEntry) {
		VBox addRecord = new VBox();
		addRecord.setStyle("-fx-background-color: rgba(0, 0, 0, 1); -fx-alignment: center;");
		
		Label new_high_score = new Label("New High Score!");
		new_high_score.setStyle(" -fx-font-size: 24; -fx-text-fill:white;");
		TextField init = new TextField();
		Button addRecordButton = new Button("Add Score!");
		
		addRecordButton.setOnAction((e) -> {
			HighScoreTable.addEntry(file, init.getText(), score);
			onNewEntry.call();
		});
		init.setAlignment(Pos.CENTER);
		VBox.setMargin(init, new Insets(10, 0, 10, 0));
		addRecord.getChildren().addAll(new_high_score, init, addRecordButton);
		init.textProperty().addListener((ov, oldValue, newValue) -> {
            if (init.getText().length() > 3) {
                String s = init.getText().substring(0, 3);
                init.setText(s);
            }
        });
		
		return addRecord;
	}
}
