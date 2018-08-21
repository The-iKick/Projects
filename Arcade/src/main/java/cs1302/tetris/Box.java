package cs1302.tetris;

import javafx.scene.canvas.GraphicsContext;
/// new branch comment
public class Box implements Drawable, Moveable {              
	private GraphicsContext gc;
	
	private double x = 0;  // keep track of the x positioning in terms of "Boxes"
	private double y = 0;  // keep track of the x positioning in the terms of "Boxes"
	
	private int iconOffsetX = 0;
	private int iconOffsetY = 0;
	
	Box (GraphicsContext gc, int x, int y) {
		this.x = x;
		this.y = y;
		this.gc = gc;
	}
	
	/**
	 * Set the color of the box
	 * @param color Color to set the box to
	 */
	public void setColor (String color) {
		switch(color) {
			case "teal": case "blue": case "white":
				iconOffsetX = 1;
				break;
			case "red": case "orange": case "black":
				iconOffsetX = 429;
				break;
			case "lightgreen": case "pink": case "brown":
				iconOffsetX = 858;
				break;
			case "yellow": case "purple": case "green":
				iconOffsetX = 1286;
				break;
		}
		switch (color) {
			case "teal": case "red": case "lightgreen": case "yellow":
				iconOffsetY = 1;
				break;
			case "blue": case "orange": case "pink": case "purple":
				iconOffsetY = 429;
				break;
			case "white": case "black": case "brown": case "green":
				iconOffsetY = 858;
		}
	}
	
	/**
	 * Sets the x position based on a grid
	 * @param x column
	 */
	public void setX (double x) {
		this.x = x;
	}
	
	/**
	 * Sets the y position based on a grid
	 * @param y row
	 */
	public void setY (double y) {
		this.y = y;
	}
	
	/**
	 * Gets the x position based on a grid
	 * @return column
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Gets the y position based on a grid
	 * @param row
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Changes the x position based on a grid
	 * @param dir the distance plus direction of the movement
	 */
	public void moveHori (double dir) {
		x += dir;
	}
	
	/**
	 * Changes the y position based on a grid
	 * @param dir the distance plus direction of the movement
	 */
	public void moveVerti (double dir) {
		y += dir;
	}
	
	/**
	 * Increase the y position based on a grid
	 */
	public void moveDown() {
		y++;
	}

	/**
	 * Increase the x position based on a grid
	 */
	public void moveRight() {
		x++;
	}

	/**
	 * Decreases the x position based on a grid
	 */
	public void moveLeft() {
		x--;
	}
	
	/**
	 * Draws the box on a given GraphicsContext
	 */
	public void draw () {
		gc.drawImage(Tetris.icons, iconOffsetX, iconOffsetY, 306, 306, x * Tetris.CELL_SIZE, y * Tetris.CELL_SIZE, Tetris.CELL_SIZE, Tetris.CELL_SIZE);
	}
	
	/**
	 * Updates the drawing context
	 * @param gc
	 */
	public void setGC(GraphicsContext gc) {
		this.gc = gc;
	}
}
