package cs1302.tetris;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.util.Arrays;

import cs1302.driver.Files;

/**
 * Our GridManager is the main backend driver and relates the moving shape to a static board. 
 */
public class GridManager implements Drawable {
	private Box[][] boxes;
	private ScoreBoard scoreBoard;
	
	private boolean isPlayable = true;
	private Media rotateMedia; 
	
	GridManager (ScoreBoard scoreBoard) {
		boxes = new Box[Tetris.GRID_HEIGHT][Tetris.GRID_WIDTH];
		this.scoreBoard = scoreBoard;
		
		try {
			rotateMedia = new Media(Files.audio.ROTATE);
		} catch(Exception e) {
			//Do nothing
		}
		
	}
	
	/**
	 * Check left bounds 
	 * @param shape
	 * @return if you can move left or not
	 */
	public boolean canMoveLeft(Shape shape) {
		Box[] leftBounds = shape.getBounds(-1,0); 
		
		for (Box bounds: leftBounds) {
			int x = (int) bounds.getX();
			int y = (int) bounds.getY();
			// checks to see if there is a block next to it
			// but if x is less than 0 we also do not want to move left
			if (x <= 0 || (y > 0 && boxes[y][x - 1] != null)) { // x would be going out of bounds or through another block
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Check right bounds 
	 * @param shape
	 * @return if you can move right or not
	 */
	public boolean canMoveRight(Shape shape) {
		Box[] rightBounds = shape.getBounds(1,0);
		
		for (Box bounds: rightBounds) { 
			int x = (int) bounds.getX();
			int y = (int) bounds.getY();
			// checks to see if there is a block next to it
			// and we also want to check the boundary wall
			if (x >= Tetris.GRID_WIDTH - 1 || (y > 0 && boxes[y][x + 1] != null)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Check bottom bounds 
	 * @param shape
	 * @return if you can move down or not
	 */
	public boolean canMoveDown(Shape shape) {
		Box[] bottomBounds = shape.getBounds(0,1);
		
		for (Box bounds: bottomBounds) {
			int x = (int) bounds.getX();
			int y = (int) bounds.getY();
			if (y > 0) { // we are going to start the shape outside of the field but y can't be negative
				if (y >= Tetris.GRID_HEIGHT - 1 || boxes[y + 1][x] != null) {
					this.setShape(shape);
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * If the shape can rotate within the grid, this method will rotate it
	 * @param direction 
	 * @param shape
	 */
	public void rotateIfCan (Rotations direction, Shape shape) {
		boolean rotateBack = false;
		
		// try rotating the shape
		if (direction == Rotations.Clockwise) shape.rotateRight();
		if (direction == Rotations.Counterclockwise) shape.rotateLeft();
		
		// check to make sure that the shape is not invading an already set space
		Box[] newBoxes = shape.getBoxes();
		for (Box box: newBoxes) {
			int x = (int) box.getX();
			int y = (int) box.getY();
			// the first condition makes sure that we can not rotate the shape outside the bottom boundary
			// the second conditions checks for in bound positions and the current space its trying to occupy
			if (y >= Tetris.GRID_HEIGHT || (y >= 0 && x >= 0 && x < Tetris.GRID_WIDTH && boxes[y][x] != null)) {
				rotateBack = true;
			}
		}
		
		if (rotateBack) { // we need to rotate the shape back if it ended up invading a space
			if (direction == Rotations.Clockwise) shape.rotateLeft();
			if (direction == Rotations.Counterclockwise) shape.rotateRight();
		} else {                
			try {
				if (!Tetris.pausedMusic) new MediaPlayer(rotateMedia).play();  
			} catch(Exception e) {
				//Do nothing
			}
		}
	}
	
	/**
	 * Finds the Coordinates of the Predicated Landing Position
	 * @param shape The shape to check the position of
	 * @return
	 */
	public int[][] findPredictedLanding (Shape shape) {
		Box[] bottomBounds = shape.getBounds(0,1);
		int[][] predictedBoxes = new int[4][2];
		
		// closest distance will start out as a number bigger than the grid
		// because we want to show the predicition before the shape even shows up on the screen
		int closestDistance = Tetris.GRID_HEIGHT + 10; 
		int i = 0;
		for (Box bounds: bottomBounds) {
			int x = (int) bounds.getX();
			int y = (int) bounds.getY();
			int closestBoxY = findClosestBoxY(x, y);
			int distance = closestBoxY - y; // distance is defined as the {y of closest box} - {current y}
				
			if (distance <= closestDistance) {
				int[] coord = {x, closestBoxY - 1}; // go one above the closest box for predicition
				if (distance == closestDistance) { // if more than one box have same distances we add them to the array
					i++;
				} else { // if closer distance is found we scratch all that
					predictedBoxes = new int[4][2];
					closestDistance = distance;
					i = 0;
				}
				predictedBoxes[i] = coord;
			}
		}
		
		return Arrays.stream(predictedBoxes).filter(e -> { // remove all "null" items, they look like {0, 0}
			return !(e[0] == 0 && e[1] == 0);
		}).toArray(int[][]::new);
	}
	
	private int findClosestBoxY (int x, int y) {
		for (int i = Math.max(y, 0); i < Tetris.GRID_HEIGHT; i++) {
			// loop down the column x and if we find an item return that index
			if (boxes[i][x] != null) return i;
		}
		// else, there is no box at the bottom
		return Tetris.GRID_HEIGHT; 
	}
	
	/**
	 * Disassemble the shape and set it onto the grid.
	 * @param shape
	 */
	private void setShape (Shape shape) {
		Box[] shapeBoxes = shape.getBoxes();
		for (Box box: shapeBoxes) { // disasseble the shape into the grid
			int x = (int) box.getX();
			int y = (int) box.getY();
			if (y >= 0) {
				boxes[y][x] = box;
			}
		}
		
		// if the first row contains just one element, the game is done for.
		if (isRowOccupied(0)) isPlayable = false; 
		for(int i = 1, height = Tetris.GRID_HEIGHT; i < height; i++){ // delete full rows
			if (isRowFilled(i)) { 
				deleteRow(i);
				scoreBoard.incrementLinesDeleted();
				scoreBoard.incrementScore(Tetris.SCORE_PER_ROW);
			}
		}
	}
	
	/**
	 * Delete a row in the array, which effectly deletes a row in canvas
	 * @param row
	 */
	private void deleteRow (int row) {
		Box[][] newArray = new Box[Tetris.GRID_HEIGHT][Tetris.GRID_WIDTH];
		
		for(int i = 0, height = boxes.length; i < height; i++){
			if (i >= row) { // all rows after the deleted row
				if (i == row) continue; // skip the row we are trying to delete
				newArray[i] = boxes[i]; // continue adding the rest of the rows like normal
			} else { // all rows before the delete row
				newArray[i + 1] = boxes[i]; // moves all of the rows down one if they were above the delete row
				for (Box box: boxes[i]) {
					if (box != null) box.moveDown();
				}
			}
		}
		boxes = newArray;
	}
	
	/**
	 * @return isPlayable
	 */
	public boolean isPlayable() {
		return isPlayable;
	}
	
	/**
	 * Checks to see if a row is occupied (one or more objects)
	 * @param row
	 * @return whether the row is occupied or not
	 */
	public boolean isRowOccupied (int row) {
		for (Box box: boxes[row]) {
			if (box != null) return true;
		}
		return false;
	}
	
	/**
	 * Checks to see if a row is full 
	 * @param row
	 * @return whether the row is full or not
	 */
	public boolean isRowFilled (int row) {
		for (Box box: boxes[row]) {
			if (box == null) return false;
		}
		return true;
	}
	
	/**
	 * Draws the grid of set boxes
	 */
	public void draw() {
		for(int i = 0, height = boxes.length; i < height; i++){
			for(int j = 0, width = boxes[i].length; j < width; j++) {
				if(boxes[i][j] != null) {
					boxes[i][j].draw();
				}
			}
		}
	}
}
