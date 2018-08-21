package cs1302.tetris;

import javafx.scene.canvas.GraphicsContext;

public class TShape extends Shape {
	TShape (GraphicsContext gc) {
		shapeBoxes[0] = new Box(gc, 0, 0); 
		shapeBoxes[1] = new Box(gc, 1, 0);
		shapeBoxes[2] = new Box(gc, 2, 0);
		shapeBoxes[3] = new Box(gc, 1, 1);
		
		setPivot(shapeBoxes[1]);
		setColor("purple");
	}
	
}
