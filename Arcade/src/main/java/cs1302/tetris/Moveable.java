package cs1302.tetris;

public interface Moveable {
	/**
	 * Moves a Drawable object horizontally
	 * @param a the number and director to move the 
	 */
	public void moveHori(double a);
	
	/**
	 * Moves a Drawable object vertically
	 * @param a the number and director to move the 
	 */
	public void moveVerti(double a);
	
	/**
	 * Moves a Drawable object down
	 */
	public void moveDown();
	
	/**
	 * Moves a Drawable object left
	 */
	public void moveLeft();
	
	/**
	 * Moves a Drawable object right
	 */
	public void moveRight();
}
