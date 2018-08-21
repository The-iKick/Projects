package cs1302.driver;

import javafx.scene.text.Text;
import javafx.scene.layout.GridPane;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Creats and manipulates a high score table
 * @author Shawn
 *
 */
public class HighScoreTable extends GridPane {
	/**
	 * This interface allows us to utilize a callback method
	 */
	public interface Callee {
		void call();
	}
	
	/**
	 * Create a highscore table with a correctly formatted highscore file
	 * @param file Absolute location of the highscore file
	 * @throws FileNotFoundException
	 */
	public HighScoreTable (String file, int direction) throws FileNotFoundException {
		Scanner output = new Scanner(new File(file)).useDelimiter("\\Z"); // read the whole file
		String scores = output.hasNext() ? output.next() : "";
		
		if (scores.length() > 0) {
			String[] players = sort(scores, direction);  // sort the score
			for (int i = 0; i < Math.min(players.length, 7); i++) { // pull 7 scores
				String[] player_details = players[i].split(" ");
				this.add(new Text(player_details[0]), 0, i+1);
				this.add(new Text(player_details[1]), 1, i+1);
			}
		}
		this.getStyleClass().add("highscore-table");
		this.setStyle("-fx-alignment: top-center;\n" + 
				"	-fx-spacing: 10px;\n" + 
				"    -fx-background-insets: 0 40 0 40;\n" + 
				"	-fx-background-color: white;");
		this.setHgap(15); 
		this.setVgap(6);
	}
	
	/**
	 * Make the scores sort from highest to lowest score
	 */
	private static String[] sort (String scores, int direction) {
		String[] players = scores.split("\n");
		Arrays.sort(players, (a, b) -> { // self explanitory
			int score1 = Integer.parseInt(a.split(" ")[1]);
			int score2 = Integer.parseInt(b.split(" ")[1]);
			
			return (score2 - score1) * direction;
		});
		
		return players;
	}
	
	/**
	 * Verifies whether a new score should be added to the list of high scores
	 * @param file Absolutely value of the highscore file to check
	 * @param score The new score that should be added
	 * @return whether we can add the new score
	 * @throws FileNotFoundException
	 */
	public static boolean isNewHighScore (String file, int score, int direction) throws FileNotFoundException {
		String scores = new Scanner(new File(file)).useDelimiter("\\Z").next();
		String[] players = scores.split("\n");
		for (int i = 0; i < Math.min(players.length, 7); i++) { // loop through the top 7 scores and see if the new one is higher
			int playerScore = Integer.parseInt(players[i].split(" ")[1]);
			if (direction == 1) if (score >= playerScore) return true;
			else if (direction == -1) if (score <= playerScore) return true;
		}
		return players.length < 7;
	}
	
	/**
	 * Check if the table is empty
	 */
	public static boolean isEmpty (String file) throws FileNotFoundException {
		try {
		String scores = new Scanner(new File(file)).useDelimiter("\\Z").next();
		String[] players = scores.split("\n");
			return players.length == 0;
		} catch (NoSuchElementException e) {
			return true;
		}
	}
	
	/**
	 * Adds a new highscore entry to a file
	 * @param file Absolute path of the high score file
	 * @param init The initals that are displayed for the score
	 * @param score The score
	 */
	public static void addEntry (String file, String init, int score) {
		try
		{
		    String filename= file;
		    FileWriter fw = new FileWriter(filename, true); //the true will append the new data
		    fw.write((isEmpty(file) ? "" : "\n") + init + " " + score);//appends the string to the file
		    fw.close();
		}
		catch(IOException ioe)
		{
		    System.err.println("IOException: " + ioe.getMessage());
		}
	}
}
