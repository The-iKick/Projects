package cs1302.tetris;

import javafx.scene.canvas.GraphicsContext;

public class JShape extends Shape {
	JShape (GraphicsContext gc) {
		shapeBoxes[0] = new Box(gc, 0, 0); 
		shapeBoxes[1] = new Box(gc, 0, 1);
		shapeBoxes[2] = new Box(gc, 0, 2);
		shapeBoxes[3] = new Box(gc, 1, 0);
		
		setPivot(shapeBoxes[1]);
		setColor("blue");
	}
	
}
