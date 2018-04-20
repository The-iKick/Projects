package cs1302.tetris;

import java.util.Arrays;

enum Rotations {
	Clockwise,
	Counterclockwise
}

abstract public class Shape {
	protected Box[] shapeBoxes = new Box[4]; // all shapes are four
	protected Box pivot; // the pivot of each shape
	
	public Box[] getBoxes () {
		return shapeBoxes;
	}
	
	public void moveHori(int a) {
		for (Box box: shapeBoxes) box.moveHori(a);
	}
	
	public void moveDown() {
		for (Box box: shapeBoxes) box.moveDown();
	}
	
	public void moveLeft() {
		for (Box box: shapeBoxes) box.moveLeft();
	}
	
	public void moveRight() {
		for (Box box: shapeBoxes) box.moveRight();
	}
	
	public void rotateRight() {
		rotateShape(Rotations.Counterclockwise);
	}
	
	public void rotateLeft() {
		rotateShape(Rotations.Clockwise);
	}
	
	public void draw() {
		for(Box box: shapeBoxes) {
			box.draw();
		}
	}
	
	public Box[] getBounds(int checkX, int checkY) {
		Box[] returnArray = new Box[4];
		int i = 0;
		
		for(Box box: shapeBoxes) {
			boolean add = true;
			for(Box comparisonBox: shapeBoxes) {
				if(comparisonBox.getX() == box.getX()+checkX && comparisonBox.getY() == box.getY()+checkY) {
					add = false;
				}
			}
			if(add) {
				returnArray[i] = box;
				i++;
			}
		}
		
		returnArray = Arrays.stream(returnArray)
				.filter(e -> e != null)
				.toArray(Box[]::new);
		
		return returnArray;
	}

	private void rotateShape(Rotations direction) {
		double angle = Math.PI/2 * (direction == Rotations.Clockwise ? 1 : -1);
		int s = (int) Math.round(Math.sin(angle));
		int c = (int) Math.round(Math.cos(angle));
		
		int pivotX = pivot.getX(); // x value to rotate around 
		int pivotY = pivot.getY(); // y value to rotate around
		
		int leftMost = Driver.GRID_WIDTH;
		int rightMost = 0;
		for (Box box: shapeBoxes) {
			int pointX = box.getX(); // x value of point to rotate
			int pointY = box.getY(); // y value of point to rotate
			
			// translate point back to origin:
			box.setX(pointX - pivotX);
			box.setY(pointY - pivotY);
			
			// rotate point
			int xnew = box.getX() * c - box.getY() * s;
			int ynew = box.getX() * s + box.getY() * c;

			// translate point back:
			box.setX(xnew + pivotX);
			box.setY(ynew + pivotY);
			
			if ((xnew + pivotX) < leftMost) {
				leftMost = (xnew + pivotX);
			} else if ((xnew + pivotX) > rightMost) {
				rightMost = (xnew + pivotX);
			}
		}
		if (leftMost < 0) {
			this.moveHori(-leftMost);
		} else if (rightMost > 14) {
			this.moveHori(14 - rightMost);
		}
		
	}
}
