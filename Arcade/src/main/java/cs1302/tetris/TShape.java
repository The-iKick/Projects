package cs1302.tetris;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TShape extends Shape {
	public static final Color c = Color.GREEN;
	
	TShape (GraphicsContext gc) {
		shapeBoxes[0] = new Box(gc, 0, 0, c); 
		shapeBoxes[1] = new Box(gc, 1, 0, c);
		shapeBoxes[2] = new Box(gc, 2, 0, c);
		shapeBoxes[3] = new Box(gc, 1, 1, c);
		
		pivot = shapeBoxes[1];
	}
	
}
