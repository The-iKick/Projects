package cs1302.tetris;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Line extends Shape {
	public static final Color c = Color.PINK;
	
	Line (GraphicsContext gc) {
		shapeBoxes[0] = new Box(gc, 0, 0, c); 
		shapeBoxes[1] = new Box(gc, 0, 1, c);
		shapeBoxes[2] = new Box(gc, 0, 2, c);
		shapeBoxes[3] = new Box(gc, 0, 3, c);
		
		pivot = shapeBoxes[1];
	}
	
	
}
