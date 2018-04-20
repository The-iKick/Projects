package cs1302.tetris;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/// new branch comment
public class Box {
	private GraphicsContext gc;
	
	private int x = 0;  // keep track of the x positioning in terms of "Boxes"
	private int y = 0;  // keep track of the x positioning in the terms of "Boxes"
	
	private Color color;
	
	Box (GraphicsContext gc, int x, int y, Color c) {
		this.x = x + 7;
		this.y = y;
		this.gc = gc;
		this.color = c;
	}
	
	public void setX (int x) {
		this.x = x;
	}
	
	public void setY (int y) {
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void draw () {
		gc.setFill(color);
		gc.fillRect(x * Driver.CELL_SIZE, y * Driver.CELL_SIZE, Driver.CELL_SIZE, Driver.CELL_SIZE);
	}

	public void moveHori (int dir) {
		x += dir;
	}
	
	public void moveDown() {
		y++;
	}

	public void moveRight() {
		x++;
	}

	public void moveLeft() {
		x--;
	}
}
