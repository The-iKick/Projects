package cs1302.minesweeper;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Rectangle2D;

import java.io.File;
import java.lang.IllegalArgumentException;

enum Borders {
	TOP_LEFT,
	TOP_RIGHT,
	BOTTOM_LEFT,
	BOTTOM_RIGHT,
	HORIZONTAL,
	VERTICAL_SHORT,
	VERTICAL_LONG,
	JOINT_LEFT,
	JOINT_RIGHT
}
enum Reaction {
	HAPPY,
	HAPPY_PRESSED,
	ANXIOUS,
	DEAD,
	COOL
}

public class SpriteManager {
	private final static File spriteAsset = new File("assets/minesweeper.gif");
	public final static Image sprite = new Image("file:///" + spriteAsset.getAbsolutePath().replace("\\", "/"));
	
	private static ImageView createClip(int x, int y, int width, int height) {
		ImageView spriteImage = new ImageView(sprite);
		spriteImage.setViewport(new Rectangle2D(x, y, width, height));
		return spriteImage;
	}
	
	/**
	 * Gets the unopened image 
	 * @return The unopened face
	 */
	public static ImageView unopened() {
		return createClip(0, 39, 16, 16);
	}
	
	/**
	 * Gets the flagged image
	 * @return The flagged face
	 */
	public static ImageView flagged() {
		return createClip(16, 39, 16, 16);
	}
	
	/**
	 * Gets the triggered bomb image
	 * @return The triggered bomb image
	 */
	public static ImageView triggeredBomb() {
		return createClip(32, 39, 16, 16);
	}
	
	/**
	 * Gets the not bomb image
	 * @return The not bomb image
	 */
	public static ImageView notBomb() {
		return createClip(48, 39, 16, 16);
	}
	
	/**
	 * Gets the bomb image
	 * @return The bomb image
	 */
	public static ImageView bomb() {
		return createClip(64, 39, 16, 16);
	}
	
	/**
	 * Returns the guess face image
	 * @return The guess face image
	 */
	public static ImageView guess() {
		return createClip(80, 39, 16, 16);
	}
	
	/**
	 * Returned the uncovered guess image
	 * @return The uncovered guess image
	 */
	public static ImageView uncoveredGuess() {
		return createClip(96, 39, 16, 16);
	}
	
	/**
	 * A convinence method to grab any of the face images based on a {@link Reaction}
	 * @param react
	 * @return A face image
	 */
	public static ImageView face (Reaction react) {
		switch(react) {
		case HAPPY:
			return createClip(0, 55, 26, 26);
		case HAPPY_PRESSED:
			return createClip(26, 55, 26, 26);
		case ANXIOUS:
			return createClip(52, 55, 26, 26);
		case DEAD:
			return createClip(78, 55, 26, 26);
		case COOL:
			return createClip(104, 55, 26, 26);
		default:
			break;
		}
		return null;
	}
	
	/**
	 * Returns the happy face image
	 * @return The happy face image
	 */
	public static ImageView happyFace() {
		return createClip(0, 55, 26, 26);
	}
	
	/**
	 * Returns the pressed happy face image
	 * @return The pressed happy face image
	 */
	public static ImageView happyFacePressed() {
		return createClip(26, 55, 26, 26);
	}
	
	/**
	 * Returns the pressed anxious face image
	 * @return The anxious face image
	 */
	public static ImageView anxiousFace() {
		return createClip(52, 55, 26, 26);
	}
	
	/**
	 * Returns the dead face image
	 * @return The dead face image
	 */
	public static ImageView deadFace() {
		return createClip(78, 55, 26, 26);
	}
	
	/**
	 * Returns the cool face image
	 * @return The cool face image
	 */
	public static ImageView coolFace() {
		return createClip(104, 55, 26, 26);
	}
	
	/**
	 * Gets the opened number based on a number n
	 * @param n The number ou want to display
	 * @return An image of an opened n
	 */
	public static ImageView opened(int n) throws IllegalArgumentException {
		if (n < 0 || n > 8) throw new IllegalArgumentException();
		int pos = n * 16;
		return createClip(pos, 23, 16, 16);
	}
	
	/**
	 * Gets the scoreboard numbers based on the parameter n
	 * @param n The number to get
	 * @return A scoreboard image of the number n
	 * @throws IllegalArgumentException 
	 */
	public static ImageView numbers (int n) throws IllegalArgumentException { // n of 10 is a dash
		if (n < 0 || n > 10) throw new IllegalArgumentException();
		int pos = n * 13;
		return createClip(pos, 0, 13, 23);
	}
	
	/**
	 * Gets the border based on an enum border type
	 * @param borderType The bordertype to get
	 * @return An image of a border
	 */
	public static ImageView border (Borders borderType) {
		switch (borderType) {
		     case TOP_LEFT:
		    	 	return createClip(0, 81, 10, 10);
		     case TOP_RIGHT:
		    	 	return createClip(10, 81, 10, 10);
		     case BOTTOM_LEFT:
		    	 	return createClip(20, 81, 10, 10);
		     case BOTTOM_RIGHT:
		    	 	return createClip(30, 81, 10, 10);
		     case JOINT_LEFT:
		    	 	return createClip(56, 81, 10, 10);
		     case JOINT_RIGHT:
		    	 	return createClip(66, 81, 10, 10);
		     case HORIZONTAL:
		    	 	return createClip(40, 81, 16, 10);
		     case VERTICAL_SHORT:
		    	 	return createClip(134, 39, 10, 16);
		     case VERTICAL_LONG:
		    	 	return createClip(134, 39, 10, 32);
		}
		return null;
	}
	
	/**
	 * A convinence method that draws a horizontal line
	 * @param n The length of the line
	 * @return An {@link HBox} containing the items
	 */
	public static HBox horizontal (int n) {
		HBox line = new HBox();
		for (int i = 0; i < n; i++) {
			line.getChildren().add(SpriteManager.border(Borders.HORIZONTAL));
		}
		return line;
	}
	
	/**
	 * A covinence methood that draws a vertical line
	 * @param n The length of the line
	 * @return An {@link HBox} containing the items
	 */
	public static VBox vertical (int n) {
		VBox line = new VBox();
		for (int i = 0; i < n; i++) {
			line.getChildren().add(SpriteManager.border(Borders.VERTICAL_SHORT));
		}
		return line;
	}
}
