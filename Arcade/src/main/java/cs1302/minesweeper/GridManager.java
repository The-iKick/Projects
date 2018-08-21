package cs1302.minesweeper;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

import cs1302.driver.HighScoreTable;
import cs1302.minesweeper.Box.State;
import javafx.scene.control.TextInputDialog;

public class GridManager {
	GridView gridView;
	Minesweeper minesweeper;
	
	int width, height, numberOfMines; // hold the settings
	boolean firstClickImmunity;  
	
	boolean[][] bombLocations; // hold the bombs
	Box[][] boxArray;  // hold the actual boxes
	
	
	GridManager(Minesweeper minesweeper) {
		this.minesweeper = minesweeper;
		width = minesweeper.getGridWidth();
		height = minesweeper.getGridHeight();
		numberOfMines = minesweeper.getNumberOfMines();
		
		bombLocations =  new boolean[height][width];
		boxArray = new Box[height][width];
		
		for(int y = 0; y < minesweeper.getGridHeight(); y++) {
			for(int x = 0; x < minesweeper.getGridWidth(); x++) { // cover all of the boxes
				boxArray[y][x] = new Box(this,x,y);
				boxArray[y][x].setState(State.COVERED);
				bombLocations[y][x] = false;
			}
		}
		
		minesweeper.getScoreBoard().setOnClick(() -> { // set up the reset
			try {
				resetGame();
			} catch (Exception e) {}
		});
		generateMines();
		
		firstClickImmunity = true;
	}
	
	/**
	 * Get a Box at a column X and row Y
	 * @param x 
	 * @param y
	 * @return current box
	 */
	Box getBox(int x, int y) {
		if(x < 0 || x > width-1) throw new IllegalArgumentException("X is out of range");
		if(y < 0 || x > height-1) throw new IllegalArgumentException("Y is out of range");
		return boxArray[y][x];
	}
	
	/**
	 * Represents a box being clicked!
	 * @param x 
	 * @param y
	 * @throws FileNotFoundException
	 */
	void clicked(int x, int y) throws FileNotFoundException { 
		if(firstClickImmunity) { // on the first click we need to move a mine to the top left corner
			for(int i = -1; i <= 1; i++) { 
				for(int j = -1; j <= 1; j++) {
					try {
						if(bombLocations[y+j][x+i]) {
							moveMine(x+i,y+j);
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						continue;
					}
				}
			}
			firstClickImmunity = false;
			clicked(x,y);
			
			minesweeper.getScoreBoard().startTimer();
		}
		firstClickImmunity = false;
		if (bombLocations[y][x]) { // if we hit a bomb
			boxArray[y][x].setState(State.UNCOVERED_BOMB_TRIGGERED); 
			minesweeper.getScoreBoard().setFace(Reaction.DEAD);
			minesweeper.getScoreBoard().stopTimer();
			endGame(x, y);
		} else { // if not a bomb
			if (boxArray[y][x].getState() != State.FLAGGED) { // if it is not flagged
				boxArray[y][x].setState(State.UNCOVERED_NUMBER); // uncover the box
				if(getNumberOfMinesAround(x, y) == 0) { // if there are blank boxes around
					Box[] boxes = getBoxesAround(x, y);
					
					for (Box box: boxes) { // click the boxes around
						if (box != null && box.getState() != State.UNCOVERED_NUMBER) {
							clicked(box.getXPosition(), box.getYPosition());
						}
					}
				}
			}
		}
		if (won()) { // check if the game is completed
			String level = minesweeper.getDifficulty().name().toLowerCase(); 
			String Tablefile = "src/main/java/cs1302/minesweeper/highscores-"+level+".txt";
			minesweeper.getScoreBoard().stopTimer(); 
			minesweeper.getScoreBoard().setFace(Reaction.COOL);
			setAllDisabled(true);
			if (HighScoreTable.isNewHighScore(Tablefile, minesweeper.getScoreBoard().getTime(), -1)) { // if new highscore
				highscoreAlert ();
			}
		}
	}
	
	/**
	 * Right click to flag
	 * @param x
	 * @param y
	 * @throws Exception
	 */
	void rightClicked (int x, int y) throws Exception {
		Box box = boxArray[y][x];
		if (box.getState() == State.FLAGGED) { // increase bomb count
			minesweeper.getScoreBoard().incrementBombCount();
			box.setState(State.COVERED); 
		} else { // if not flagged
			if (box.getState() == State.COVERED) { 
				minesweeper.getScoreBoard().decrementBombCount();
				box.setState(State.FLAGGED); 
			}
		}
		if (won()) {
			minesweeper.getScoreBoard().stopTimer();
			minesweeper.getScoreBoard().setFace(Reaction.COOL);
			setAllDisabled(true);
		}
	}
	
	/**
	 * Get all of the boxes that are around the coordinate
	 * @param x
	 * @param y
	 * @return
	 */
	public Box[] getBoxesAround (int x, int y) {
		Box[] boxes = new Box[8];
		
		int count = 0; 
		for(int i = -1; i <= 1; i++) { // loop in a circle around the coordinate
			for(int j = -1; j <= 1; j++) {
				try {
					if(!(i == 0 && j == 0)) { // if the box is not itself
						boxes[count] = boxArray[i+y][j+x];
						count++;
					}
				} catch (ArrayIndexOutOfBoundsException e) { // make sure we do not count boxes outside of the grid
					continue;
				}
			}
		}
		return boxes;
	}
	
	/**
	 * Get the number of mines around the a box
	 * @param x
	 * @param y
	 * @return
	 */
	public int getNumberOfMinesAround(int x, int y){
		int num = 0;
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 1; j++) {
				try {
					if(bombLocations[y+j][x+i]) {
						num++;
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					continue;
				}
			}
		}
		return num;
	}
	
	/**
	 * Check if the board has been won!
	 * @return
	 */
	private boolean won () {
		for (int i = 0; i < boxArray.length; i++) {
			for (int j = 0; j < boxArray[i].length; j++) { 
				// a win only happens only if all boxes are uncovered that are not bombs,
				// and all bombs and only the bombs are flagged
				if (bombLocations[i][j] && boxArray[i][j].getState() != State.FLAGGED) {
					return false;
				}
				if (!bombLocations[i][j] && boxArray[i][j].getState() == State.FLAGGED) {
					return false;
				}
				if (boxArray[i][j].getState() == State.COVERED) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Reset the Game
	 * @throws Exception
	 */
	void resetGame () throws Exception {
		firstClickImmunity = true;
		bombLocations =  new boolean[width][height];
		for (int i = 0; i < boxArray.length; i++) { // reset all of the board
			for (int j = 0; j < boxArray[i].length; j++) {
				Box box = boxArray[i][j];
				box.setState(State.COVERED);
			}
		}
		minesweeper.getScoreBoard().reset();
		generateMines();
		setAllDisabled(false);
	}
	
	/**
	 * Show an alert box if there is a highscore detcted
	 */
	private void highscoreAlert () {
		TextInputDialog dialog = new TextInputDialog("AAA");
		dialog.setTitle("New High Score!");
		dialog.setHeaderText("Looks like you have made the highscore table!");
		dialog.setContentText("Please enter your initials:");

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		// The Java 8 way to get the response value (with lambda expression).
		result.ifPresent(initials -> {
			String Tablefile = "src/main/java/cs1302/minesweeper/highscores-easy.txt"; // add the entry
			HighScoreTable.addEntry(Tablefile, initials, minesweeper.getScoreBoard().getTime());
			dialog.close();
		});
	}
	
	/**
	 * If we lost the game...
	 * @param x
	 * @param y
	 * @throws FileNotFoundException
	 */
	private void endGame(int x, int y) throws FileNotFoundException {
		setAllDisabled(true);
		
		for (int i = 0; i < boxArray.length; i++) { // loop
			for (int j = 0; j < boxArray[i].length; j++) {
				Box box = boxArray[i][j];
				State state = box.getState();
				if (bombLocations[i][j] && !(i == y && j == x)) {
					if (box.getState() != State.FLAGGED) {
						box.setState(State.UNCOVERED_BOMB);
					}
				} else {
					if (state == State.FLAGGED) {
						box.setState(State.UNCOVERED_FALSE_ALARM);
					}
				}
			}
		}
	}
	
	/**
	 * Randomly generate mines
	 */
	private void generateMines() {
		Random rand = new Random();
		for(int i = 0; i < numberOfMines; i++) {
			int x = rand.nextInt(width);
			int y = rand.nextInt(height);
			if(bombLocations[y][x]) {
				i--;
				continue;
			} else {
				bombLocations[y][x] = true;
			}
		}
	}
	
	/**
	 * Move a mine
	 * @param x
	 * @param y
	 */
	private void moveMine(int x,int y) {
		bombLocations[y][x] = false;
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				if(!bombLocations[j][i]) {
					bombLocations[j][i] = true;
					return;
				}
			}
		}
	}
	
	/**
	 * Disable all of the boxes
	 * @param value
	 */
	private void setAllDisabled (boolean value) {
		for (int i = 0; i < boxArray.length; i++) {
			for (int j = 0; j < boxArray[i].length; j++) {
				Box box = boxArray[i][j];
				box.setDisable(value);
			}
		}	
	}
	
	/**
	 * Get the scoreboard
	 * @return
	 */
	public ScoreBoard getScoreBoard () {
		return this.minesweeper.getScoreBoard();
	}
}
