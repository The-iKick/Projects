package cs1302.minesweeper;

import javafx.scene.layout.GridPane;

public class GridView extends GridPane{
	int width;
	int height;
	
	GridManager gridManager;
	
	/**
	 * Makes a new {@link GridView} object for a {@link Minesweeper} game
	 * @param minesweeper The game to which the {@link GridView} belongs
	 */
	GridView(Minesweeper minesweeper) {
		width = minesweeper.getGridWidth();
		height = minesweeper.getGridHeight();
		
		gridManager = minesweeper.getGridManager();
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x ++) {
				this.add(gridManager.getBox(x, y), x ,y);
			}
		}
	}
}
