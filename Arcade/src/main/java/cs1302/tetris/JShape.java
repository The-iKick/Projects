package cs1302.tetris;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class JShape extends Shape {
	public static final Color c = Color.BLACK;
	
	JShape (GraphicsContext gc) {
		shapeBoxes[0] = new Box(gc, 1, 0, c); 
		shapeBoxes[1] = new Box(gc, 1, 1, c);
		shapeBoxes[2] = new Box(gc, 1, 2, c);
		shapeBoxes[3] = new Box(gc, 2, 0, c);
		
		pivot = shapeBoxes[1];
	}
	
}
