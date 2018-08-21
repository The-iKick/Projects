package cs1302.tetris;

import javafx.scene.canvas.GraphicsContext;

public class Square extends Shape {
	Square (GraphicsContext gc) {
		shapeBoxes[0] = new Box(gc, 0, 0); 
		shapeBoxes[1] = new Box(gc, 0, 1);
		shapeBoxes[2] = new Box(gc, 1, 0);
		shapeBoxes[3] = new Box(gc, 1, 1);
		
		this.setColor("teal");
	}
	
	@Override
	public void rotateRight() {
		//Do nothing
	}
	
	@Override
	public void rotateLeft() {
		//Do nothing
	}
	
	
}
