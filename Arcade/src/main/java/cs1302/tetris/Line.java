package cs1302.tetris;

import javafx.scene.canvas.GraphicsContext;

public class Line extends Shape {
	Line (GraphicsContext gc) {
		shapeBoxes[0] = new Box(gc, 0, 0); 
		shapeBoxes[1] = new Box(gc, 0, 1);
		shapeBoxes[2] = new Box(gc, 0, 2);
		shapeBoxes[3] = new Box(gc, 0, 3);
		
		setPivot(shapeBoxes[1]);
		setColor("red");
	}
	
	
}
