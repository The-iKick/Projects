package cs1302.driver;

import java.io.File;

/**
 * Holds the Absolute Path of several files used throughout the game
 * @author Shawn
 *
 */
public class Files {
	public static class css {
		public static String TETRIS = fullName(new File("css/tetris.css"));
		public static String MINESWEEPER = fullName(new File("css/minesweeper.css"));
		public static String MAIN = fullName(new File("css/main.css"));
	}
	
	public static class image {
		public final static String TETRIS_SPRITE = fullName(new File("assets/tetris/spritesheet.png"));
		public final static String TETRIS_LOGO = fullName(new File("assets/tetris/tetris-logo.png"));
		public final static String TETRIS_ICON = fullName(new File("assets/tetris-icon.png"));
		public final static String MINESWEEPER_ICON = fullName(new File("assets/minesweeper-icon.png"));
		public final static String MAIN_TITLE = fullName(new File("assets/Title.png"));
		public final static String BACKGROUND = fullName(new File("assets/BackgroundImage.png"));
		
		public final static String HELP = fullName(new File("assets/tetris/icons/help.png"));
		public final static String HOME = fullName(new File("assets/tetris/icons/home.png"));
		public final static String HIGH_SCORE = fullName(new File("assets/tetris/icons/highscore.png"));
		public final static String MUTED = fullName(new File("assets/tetris/icons/muted.png"));
		public final static String UNMUTED = fullName(new File("assets/tetris/icons/unmuted.png"));
		public final static String RESTART = fullName(new File("assets/tetris/icons/restart.png"));
		
		public final static String DOWN_ARROW = fullName(new File("assets/tetris/computer_key_down_arrow.png"));
		public final static String UP_ARROW = fullName(new File("assets/tetris/computer_key_up_arrow.png"));
		public final static String LEFT_ARROW = fullName(new File("assets/tetris/computer_key_left_arrow.png"));
		public final static String RIGHT_ARROW = fullName(new File("assets/tetris/computer_key_right_arrow.png"));
		public final static String P_KEY = fullName(new File("assets/tetris/computer_key_P.png"));
		public final static String SPACEBAR = fullName(new File("assets/tetris/computer_key_spacebar.png"));
		
	}
	
	public static class audio {
		public final static String THEME = fullName(new File("assets/tetris/tetris-music.mp3"));
		public final static String ROTATE = fullName(new File("assets/tetris/rotatepiece.mp3"));
	}
	
	private static String fullName (File file) {
		return "file:///" + file.getAbsolutePath().replace("\\", "/");
	}
}
