package cs1302.tetris;


public class GridManager {
	static final int width = 30;
	static final int height = 30;
	
	private Box[][] boxes;
	
	/**
	 * {
	 * 	  {},
	 *    {},
	 *    {},
	 *    {}
	 * }
	 */
	GridManager () {
		boxes = new Box[Driver.GRID_WIDTH][Driver.GRID_HEIGHT];
	}
	
	public boolean canMoveLeft(Shape shape) {
		Box[] leftBounds = shape.getBounds(-1,0);
		
		for (Box bounds: leftBounds) {
			int x = bounds.getX();
			int y = bounds.getY();
			if (x <= 0 || boxes[x - 1][y] != null) { // x would be going out of bounds or through another block
				return false;
			}
		}
		return true;
	}
	
	public boolean canMoveRight(Shape shape) {
		Box[] rightBounds = shape.getBounds(1,0);
		
		for (Box bounds: rightBounds) {
			int x = bounds.getX();
			int y = bounds.getY();
			if (x >= Driver.GRID_WIDTH - 1 || boxes[x + 1][y] != null) {
				return false;
			}
		}
		return true;
	}
	
	public boolean canMoveDown(Shape shape) {
		Box[] bottomBounds = shape.getBounds(0,1);
		
		for (Box bounds: bottomBounds) {
			int x = bounds.getX();
			int y = bounds.getY();
			if (y >= Driver.GRID_HEIGHT - 1 || boxes[x][y + 1] != null) {
				this.setShape(shape);
				return false;
			}
		}
		return true;
	}
	
	public void setShape (Shape shape) {
		Box[] shapeBoxes = shape.getBoxes();
		for (Box box: shapeBoxes) { 
			boxes[box.getX()][box.getY()] = box;
		}
		
		for(int i = 0, height = boxes.length; i < height; i++){
			if (isRowFilled(i)) {
				deleteRow(i);
			}
		}
	}
	
	public Box getBox (int x, int y) {
		return boxes[x][y];
	}
	
	private void deleteRow (int row) {
		Box[][] newArray = new Box[Driver.GRID_WIDTH][Driver.GRID_HEIGHT];
		
		for(int i = 1, height = boxes.length; i < height; i++){
			if (i >= row) {
				newArray[i + 1] = boxes[i];
			} else {
				newArray[i] = boxes[i];
			}
		}
		boxes = newArray;
	}
	
	private boolean isRowFilled (int row) {
		if (boxes[row] != null) {
			for (Box box: boxes[row]) {
				if (box == null) return false;
			}
			return true;
		}
		return false;
	}
	
	public Box[] getRow (int row) {
		return boxes[row];
	}
	
	public void draw() {
		for(int i = 0, height = boxes.length; i < height; i++){
			for(int j = 0, width = boxes[i].length; j < width; j++) {
				if(boxes[i][j] != null) {
					boxes[i][j].draw();
				}
			}
		}
	}
	
	public void printGrid() {
		for(int i = 0; i < boxes.length; i++) {
			for(int j = 0; j < boxes[i].length; j++) {
				if(boxes[i][j] == null) {
					System.out.print("   |");
				} else {
					System.out.print(" x |");
				}
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
	}
}
