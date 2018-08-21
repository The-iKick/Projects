package cs1302.tetris;

import javafx.scene.canvas.GraphicsContext;

public interface Drawable {
	/**
	 * Draw the drawable object onto a graphic context
	 */
	public void draw ();
	
	/**
	 * Change the drawing context 
	 * @param gc
	 * @defaulted
	 */
	default public void setGC(GraphicsContext gc) {}
}
