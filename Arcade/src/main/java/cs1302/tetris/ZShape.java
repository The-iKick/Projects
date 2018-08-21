package cs1302.tetris;

import javafx.scene.canvas.GraphicsContext;
public class ZShape extends Shape {
	ZShape (GraphicsContext gc) {
		
		shapeBoxes[0] = new Box(gc, 1, 0); 
		shapeBoxes[1] = new Box(gc, 0, 1);
		shapeBoxes[2] = new Box(gc, 0, 2);
		shapeBoxes[3] = new Box(gc, 1, 1);
		
		setPivot(shapeBoxes[3]);
		setColor("green");
	}
	
}
