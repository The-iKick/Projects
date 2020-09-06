package cs1302.driver;

import java.io.File;

/**
 * Holds the Absolute Path of several files used throughout the game
 * @author Shawn
 *
 */
public class Files {
	private static String CSS = "resources/css/";
	private static String ASSETS = "resources/assets/";

	public static class css {
		public static String TETRIS = fullName(CSS + "tetris.css");
		public static String MINESWEEPER = fullName(CSS + "minesweeper.css");
		public static String MAIN = fullName(CSS + "main.css");
	}
	
	public static class image {
		public final static String TETRIS_SPRITE = fullName(ASSETS + "tetris/spritesheet.png");
		public final static String TETRIS_LOGO = fullName(ASSETS + "tetris/tetris-logo.png");
		public final static String TETRIS_ICON = fullName(ASSETS + "tetris-icon.png");
		public final static String MINESWEEPER_ICON = fullName(ASSETS + "minesweeper-icon.png");
		public final static String MAIN_TITLE = fullName(ASSETS + "Title.png");
		public final static String BACKGROUND = fullName(ASSETS + "BackgroundImage.png");
		
		public final static String HELP = fullName(ASSETS + "tetris/icons/help.png");
		public final static String HOME = fullName(ASSETS + "tetris/icons/home.png");
		public final static String HIGH_SCORE = fullName(ASSETS + "tetris/icons/highscore.png");
		public final static String MUTED = fullName(ASSETS + "tetris/icons/muted.png");
		public final static String UNMUTED = fullName(ASSETS + "tetris/icons/unmuted.png");
		public final static String RESTART = fullName(ASSETS + "tetris/icons/restart.png");
		
		public final static String DOWN_ARROW = fullName(ASSETS + "tetris/computer_key_down_arrow.png");
		public final static String UP_ARROW = fullName(ASSETS + "tetris/computer_key_up_arrow.png");
		public final static String LEFT_ARROW = fullName(ASSETS + "tetris/computer_key_left_arrow.png");
		public final static String RIGHT_ARROW = fullName(ASSETS + "tetris/computer_key_right_arrow.png");
		public final static String P_KEY = fullName(ASSETS + "tetris/computer_key_P.png");
		public final static String SPACEBAR = fullName(ASSETS + "tetris/computer_key_spacebar.png");
		
	}
	
	public static class audio {
		public final static String THEME = fullName(ASSETS + "tetris/tetris-music.mp3");
		public final static String ROTATE = fullName(ASSETS + "tetris/rotatepiece.mp3");
	}
	
	private static String fullName (String file) {
		return "file:///" + new File(file).getAbsolutePath().replace("\\", "/");
	}
}
