package cs1302.tetris;

import java.util.Arrays;
import java.util.Random;

import javafx.scene.canvas.GraphicsContext;

enum Rotations {
	Clockwise,
	Counterclockwise
}

/**
 * This abstract class defines what it means to be a shape without actually defining what a shape looks like.
 */
abstract public class Shape implements Drawable, Moveable {
	protected Box[] shapeBoxes = new Box[4]; // all shapes are four
	protected Box pivot; // the pivot of each shape
	private final Random rand = new Random();
	
	public Box[] getBoxes () {
		return shapeBoxes;
	}
	
	/**
	 * Stabilizes the shape by rounding off the X and Y value and moves it off the screen
	 */
	public void moveOutContext () {
		// boxes move in a double for the same of being able to centering in the Next Shape Box
		// When moving to the game field we have to ensure it starts off rounded as to match exactly
		// with each shape
		for (Box box: shapeBoxes) {
			box.setX(Math.round(box.getX()));
			box.setY(Math.round(box.getY()));
		}
		moveHori(rand.nextInt(Tetris.GRID_WIDTH - 4)); // center the piece
		moveVerti(-6); 
	}
	
	/**
	 * Moves the shape into the center of the context
	 */
	public void moveToContextCenter () {
		this.moveHori(7.0/2 - this.getWidth()/2);
		this.moveVerti(5.0/2 - this.getHeight()/2);
		this.draw();
	}
	
	/**
	 * Sets the color of the shpae
	 * @param color color to set the shape to
	 */
	public void setColor (String color) {
		for (Box box: shapeBoxes) box.setColor(color);
	}
	
	/**
	 * Sets the Pivot of the shape for sake of rotations
	 * @param pivot
	 */
	protected void setPivot (Box pivot) {
		this.pivot = pivot;
	}
	
	public void moveHori(double a) {
		for (Box box: shapeBoxes) box.moveHori(a);
	}
	
	public void moveVerti(double a) {
		for (Box box: shapeBoxes) box.moveVerti(a);
	}
	
	public void moveDown() {
		for (Box box: shapeBoxes) box.moveDown();
	}
	
	public void moveLeft() {
		for (Box box: shapeBoxes) box.moveLeft();
	}
	
	public void moveRight() {
		for (Box box: shapeBoxes) box.moveRight();
	}

	public void setGC(GraphicsContext gc) {
		for(Box box: shapeBoxes) box.setGC(gc);
	}
	
	/**
	 * Rotates the Shape to the right
	 */
	public void rotateRight() {
		rotateShape(Rotations.Counterclockwise);
	}
	
	/**
	 * Rotates the Shape to the left
	 */
	public void rotateLeft() {
		rotateShape(Rotations.Clockwise);
	}
	
	public void draw() {
		for(Box box: shapeBoxes) box.draw();
	}
	
	/**
	 * Get the outer boxes that determine the bounds of the shape
	 * @param checkX the x offset
	 * @param checkY the y offset
	 * @return an arraw of the bound boxes
	 */
	public Box[] getBounds(int checkX, int checkY) {
		Box[] returnArray = new Box[4];
		int i = 0;
		
		outer: for(Box box: shapeBoxes) {
			for(Box comparisonBox: shapeBoxes) {
				// if the current boxes we are checking has another box next to it,
				// then do not add this as a boundary 
				if(comparisonBox.getX() == box.getX()+checkX && comparisonBox.getY() == box.getY()+checkY) {
					continue outer;
				}
			}
			returnArray[i] = box;
			i++;
		}
		
		// return an array without null
		return Arrays.stream(returnArray).filter(e -> e != null).toArray(Box[]::new);
	}
	
	/**
	 * The general definition for what it means for a shape to be rotated
	 * @param direction the angle of rotation
	 */
	private void rotateShape(Rotations direction) {
		double angle = Math.PI/2 * (direction == Rotations.Clockwise ? 1 : -1);
		int s = (int) Math.round(Math.sin(angle));
		int c = (int) Math.round(Math.cos(angle));
		
		double pivotX = pivot.getX(); // x value to rotate around 
		double pivotY = pivot.getY(); // y value to rotate around
		
		double leftMost = Tetris.GRID_WIDTH;
		double rightMost = 0;
		for (Box box: shapeBoxes) {
			double pointX = box.getX(); // x value of point to rotate
			double pointY = box.getY(); // y value of point to rotate
			
			// translate point back to origin:
			box.setX(pointX - pivotX);
			box.setY(pointY - pivotY);
			
			// rotate point
			double xnew = box.getX() * c - box.getY() * s;
			double ynew = box.getX() * s + box.getY() * c;

			// translate point back:
			box.setX(xnew + pivotX);
			box.setY(ynew + pivotY);
			
			// if the shape goes outside of the grid then we need to keep track of how much
			if ((xnew + pivotX) < leftMost) { 
				leftMost = (xnew + pivotX);
			} else if ((xnew + pivotX) > rightMost) {
				rightMost = (xnew + pivotX);
			}
		}
		if (leftMost < 0) { // move back over by however far the left most box got
			this.moveHori(-leftMost);
		} else if (rightMost > 14) { // move back to the right but however much the box got
			this.moveHori(Tetris.GRID_WIDTH - rightMost - 1);
		}
	}
	
	/**
	 * Get the height of the shape
	 * @return height 
	 */
	public double getHeight () {
		double topMost = Tetris.GRID_HEIGHT;
		double bottomMost = 0;
		
		for (Box box: shapeBoxes) { // narrows it on the top and bottom most y
			double y = box.getY();
			if (y > bottomMost) bottomMost = y;
			if (y < topMost) topMost = y; 
		}
		return bottomMost - topMost + 1;
	}
	
	/**
	 * Get the width of the shape
	 * @return height 
	 */
	public double getWidth () {
		double leftMost = Tetris.GRID_WIDTH;
		double rightMost = 0;
		
		for (Box box: shapeBoxes) { // narrows in on the left and right most y
			double x = box.getX();
			if (x < leftMost) leftMost = x;
			if (x > rightMost) rightMost = x;
		}
		
		return rightMost - leftMost + 1;
	}
}
