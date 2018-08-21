package cs1302.tetris;

import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

import cs1302.driver.Driver;
import cs1302.driver.Files;
import cs1302.driver.HighScoreTable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * The GUI of the Tetris application. This class puts together all components into one seamless application
 */
public class Tetris{
	static final int GRID_WIDTH = 15;
	static final int GRID_HEIGHT = 25;
	static final int CELL_SIZE = 20; // the size of each cell of the grid
	
	public static final int SCORE_PER_DOWN = 1; // determines how quickly the score increase from hitting space
	public static final int SCORE_PER_ROW = 100; // determines how much the score increases per row deleted
	public static final int SHAPES_PER_LEVEL = 14; // determines how many shapes have to fall for the level to increase

	static final int CANVAS_WIDTH = GRID_WIDTH * CELL_SIZE;
	static final int CANVAS_HEIGHT = GRID_HEIGHT * CELL_SIZE;

	public static boolean pausedMusic = false; // determines if the music is paused
	public static boolean highscoreShown = false; // determines if the highscore box is shown for purpose of toggling
	
	// load assets into usable formats
	private MediaPlayer thememusic;
	static final Image icons = new Image(Files.image.TETRIS_SPRITE);
	private ImageView tetrisLogo = new ImageView(new Image(Files.image.TETRIS_LOGO, CANVAS_WIDTH, 70, false, true));
    
	private GameManager gameManager;
	private Timeline gameLoop;
	private StackPane mainGame = new StackPane();
	private Canvas gameCanvas, nextShapeCanvas;
	private ScoreBoard scoreBoard;
	
	private boolean paused = true; // determines if the game is paused
	private boolean gameover = false; // determines if the game is over

	Scene scene;
	
	public Tetris() throws FileNotFoundException {
		HBox columnContainer = new HBox();
		StackPane root = new StackPane(columnContainer);
        VBox left = new VBox(); // this is the left side of the tetris game
		VBox right = new VBox(); // this is the right side of the tetris game
		
		// create the scene with out root
		this.scene = new Scene(root);

		// SET UP EACH COMPONENT
		left.setId("left");
		right.setId("right");
		
		try{
	        thememusic.setVolume(0.7);
	        thememusic.setCycleCount(MediaPlayer.INDEFINITE);
		} catch(Exception e) {
			//Do nothing
		}
		scene.getStylesheets().addAll(Files.css.TETRIS);
	        
		
		// LEFT COLUMN COMPONENTS
		gameCanvas = new Canvas(CANVAS_WIDTH , CANVAS_HEIGHT);
		mainGame.getChildren().add(gameCanvas); // the tetris game itself is a stackpane to add a menu			
				
		// RIGHT COLUMN COMPONSENTS
		// home button
		IconButton home = new IconButton(Files.image.HOME);
		home.setPrefHeight(70);
		home.setOnMousePressed((e) -> {
			try {
				gameManager.closeGame();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			try {
				thememusic.pause();
			} catch (Exception exc) {
				//Do nothing
			}
			Driver.returnToStart ();
		});
		
		// next shape
		HBox next_shape_title = new HBox(new Text("Next Shape"));
		next_shape_title.setStyle("-fx-alignment:center; font-size: 24px");
		nextShapeCanvas = new Canvas(CELL_SIZE*7, CELL_SIZE*5);
		HBox nextGroup = new HBox(nextShapeCanvas);
		nextGroup.setId("next-shape");
        scoreBoard = new ScoreBoard();
        
        // setup menu
        GridPane menu = SquareMenu ();
        
        // Set necessary margins
		VBox.setMargin(tetrisLogo, new Insets(10));
		VBox.setMargin(home, new Insets(0, 0, 7, 0));
		VBox.setMargin(scoreBoard, new Insets(20, 0, -20, 0));
		
        // Add everything to its parent
		left.getChildren().addAll(tetrisLogo, mainGame);
		right.getChildren().addAll(home, next_shape_title, nextGroup, scoreBoard, menu);
		columnContainer.getChildren().addAll(left, right);
        showInstructions(true);
        
        try {
        	thememusic = new MediaPlayer(new Media(Files.audio.THEME)); 
        } catch (Exception e) {
        	//Do nothing
        }
	}
	
	/**
	 * Displays the instructions 
	 * @param start if true, we signify that this is the first run of the application
	 */
	public void showInstructions(boolean start) {
		Timeline flicker = new Timeline();
		VBox instructions = new VBox();
		GridPane keys = new GridPane();
		ImageView leftArrow = new ImageView(new Image(Files.image.LEFT_ARROW));
		ImageView rightArrow = new ImageView(new Image(Files.image.RIGHT_ARROW));
		ImageView upArrow = new ImageView(new Image(Files.image.UP_ARROW));
		ImageView downArrow = new ImageView(new Image(Files.image.DOWN_ARROW));
		ImageView pKey = new ImageView(new Image(Files.image.P_KEY));
		ImageView spacebar = new ImageView(new Image(Files.image.SPACEBAR));
		Label spacebarLabel = new Label("Speed Up");
		Label mainLabel = new Label("Click to " + (start ? "start!" : "continue!"));
		
		leftArrow.setFitHeight(80);
		leftArrow.setFitWidth(80);
		
		rightArrow.setFitHeight(80);
		rightArrow.setFitWidth(80);
		
		upArrow.setFitHeight(80);
		upArrow.setFitWidth(80);
		
		downArrow.setFitHeight(80);
		downArrow.setFitWidth(80);
		
		pKey.setFitHeight(80);
		pKey.setFitWidth(80);
		
		spacebar.setFitHeight(60);
		spacebar.setFitWidth(320);
		keys.add(new Label("Move Left  "), 0, 3);
		keys.add(new Label("  Move Right"), 4, 3);
		keys.add(new Label("Rotate Right"), 2, 1);
		keys.add(new Label("\n\n  Rotate Left"), 2, 4);
		keys.add(new Label("Pause"), 4, 0);
		keys.add(leftArrow, 1, 3);
		keys.add(rightArrow, 3, 3);
		keys.add(downArrow, 2, 3);
		keys.add(upArrow, 2, 2);
		keys.add(pKey, 4, 1);
		keys.setHgap(3); 
		keys.setVgap(3);

		// make the start lable flicker
		flicker.getKeyFrames().add(new KeyFrame(Duration.seconds(0.1), (e) -> {
			if (mainLabel.getOpacity() == 0) mainLabel.setOpacity(1);
			else mainLabel.setOpacity(0);
		}));
		flicker.setCycleCount(Timeline.INDEFINITE);
		flicker.play();
		
		instructions.setOnMouseClicked((e) -> { // start the game when you click the instructions
			if (start) startGame();
			else play();
			flicker.stop();
			Driver.fadeOut(instructions, 400);
			new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                		Platform.runLater(() -> {
                    		((StackPane) scene.getRoot()).getChildren().remove(instructions);
                		});
                }
            }, 400);    
		});
		
		VBox.setMargin(spacebar, new Insets(30, 0, 0, -120));
		VBox.setMargin(spacebarLabel, new Insets(0, 0, 0, -120));
		VBox.setMargin(mainLabel, new Insets(-40, 0, 0, -120));

		instructions.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-alignment: center;");
		keys.setStyle("-fx-alignment: center;");
		mainLabel.setId("start-label");
		mainLabel.setStyle("-fx-font-size: 40;");
		instructions.setId("instructions");
		instructions.getChildren().addAll(mainLabel, keys, spacebar, spacebarLabel);
		((StackPane) scene.getRoot()).getChildren().add(instructions);
	}
	
	/**
	 * Finishes a tetris game by checking the highscore and displaying it
	 * @param transparent Defines the transparency of the high score table background
	 * @throws FileNotFoundException
	 */
	public void gameover (boolean transparent) throws FileNotFoundException {
		if (HighScoreTable.isNewHighScore("src/main/java/cs1302/tetris/highscores.txt", scoreBoard.getScore(), 1)) {
			VBox entry = ScoreBoard.newHighScoreEntry("src/main/java/cs1302/tetris/highscores.txt", scoreBoard.getScore(), () -> {
				mainGame.getChildren().remove(mainGame.lookup("VBox"));
				try {
					highScoreTable(transparent);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			});
			mainGame.getChildren().addAll(entry);
		} else {
			highScoreTable(transparent);
		}
		pause ();
		gameover = true;
	}
	
	/**
	 * Defines the Square Menu component
	 * @return
	 */
	private GridPane SquareMenu () {
		GridPane menu = new GridPane();
		IconButton help = new IconButton(Files.image.HELP);
		IconButton unmute = new IconButton(Files.image.UNMUTED);
		IconButton restart = new IconButton(Files.image.RESTART);
		IconButton highscore= new IconButton(Files.image.HIGH_SCORE);
		
		// add the buttons in a square fashion
		menu.add(help, 0, 0);
		menu.add(unmute, 0, 1);
		menu.add(restart, 1, 0);
		menu.add(highscore, 1, 1);
		menu.setHgap(6); 
		menu.setVgap(6);
		menu.setPadding(new Insets(65, 7, 0, 7));
		
        restart.setOnMousePressed((e) -> {
        	try {
    			thememusic.seek(Duration.ZERO);
        	} catch (Exception exc) {
        		//Do nothing
        	}
        	// restart playback
        	try {
        		gameLoop.stop();
				gameManager.closeGame ();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
        		scoreBoard.reset();
            startGame();
        });
        unmute.setOnMousePressed((e) -> {
        		if (!paused) {
        			try {
        				unmute.changeIcon(pausedMusic ? Files.image.UNMUTED : Files.image.MUTED);
        				pausedMusic = !pausedMusic;
        				thememusic.setMute(pausedMusic);
        			} catch(Exception exc) {
        				//Do nothing
        			}
        		}
	    });
        help.setOnMousePressed((e) -> {
        		pause();
	    		showInstructions(false);
	    });
        highscore.setOnMousePressed((e) -> {
	    		if (!gameover) {
	        		if (highscoreShown) { // if the highscore box is already visible
		    			play(); // then start the game again
		    		} else {
		    			try {
		    				highScoreTable(false);
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
		    		}
	        		highscoreShown = !highscoreShown;
	    		}
	    });
        
        return menu;
	}
	
	/**
	 * Defines the highScoreTable component
	 * @param transparent
	 * @throws FileNotFoundException
	 */
	private void highScoreTable (boolean transparent) throws FileNotFoundException {
		VBox highscoreOverlay = new VBox();
		Label highscore_label = new Label("High Scores!");
		HighScoreTable highscore_table = new HighScoreTable("src/main/java/cs1302/tetris/highscores.txt", 1);
	
		VBox.setMargin(highscore_table, new Insets(10, 0, 10, 0));

		highscoreOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, " + (transparent ? 0.5 : 1) + "); -fx-alignment: center;");	
		highscore_label.setStyle(" -fx-font-size: 30;");
		
		highscoreOverlay.getChildren().addAll(highscore_label, highscore_table);
		mainGame.getChildren().addAll(highscoreOverlay);
	}
	
	/**
	 * Starts a new game
	 */
	private void startGame() {
		//A class that does all the stuff that happens everytime a frame is updated
        gameManager = new GameManager(this);
        //The game loop
        gameLoop = new Timeline(new KeyFrame(Duration.seconds(1f/60f), gameManager));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        play();    
        
        gameCanvas.setFocusTraversable(true);
	}
	
	/**
	 * Pauses the Game
	 */
	public void pause () {
		// pause the game
		gameManager.pause();
		gameLoop.pause();
		try {
			thememusic.pause();
		} catch(Exception exc) {
			//Do nothing
		}
		paused = true;
	}
	
	/**
	 * Adds the Pause Overlay to the game
	 */
	public void pauseOverlay () {
		VBox pauseOverlay = new VBox();
		Label pauseText = new Label("PAUSED");
		
		pauseText.setStyle(" -fx-font-size: 20;");
		pauseOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 1); -fx-alignment: center;");
		pauseOverlay.getChildren().add(pauseText);
		mainGame.getChildren().add(pauseOverlay);
		
		pause ();
	}
	
	/**
	 * Plays the game
	 */
	public void play () {
		mainGame.getChildren().remove(mainGame.lookup("VBox")); // remove the highscore box
		mainGame.getChildren().remove(mainGame.lookup("VBox")); // also remove the paused overlay if availiable
		
		gameManager.play();
		try {
			thememusic.play();
		} catch(Exception exc) {
			//Do nothing
		}
		gameLoop.play();
		paused = false;
        gameover = false; 
	}
	
	/**
	 * Checks to see if the game is paused
	 */
	public boolean isPaused () {
		return paused;
	}
	
	/**
	 * Gets the Score Board instance 
	 * @return
	 */
	public ScoreBoard getScoreBoard() {
		return scoreBoard;
	}

	/**
	 * Gets the canvas instance for the game
	 * @return
	 */
	public Canvas getGameCanvas() {
		return gameCanvas;
	}
	
	/**
	 * Gets the canvas instance for the next shape
	 * @return next shape canvas
	 */
	public Canvas getNextShapeCanvas() {
		return nextShapeCanvas;
	}
	
	/**
	 * Gets the Scene instance 
	 * @return scene
	 */
	public Scene getScene () {
		return scene;
	}
	
	/**
	 * Defines a custom button that has an icon rather than text
	 * 
	 */
	static class IconButton extends HBox {
		ImageView icon;
		
		IconButton (String imageFileURL) {
			Image image = new Image(imageFileURL);
			icon = new ImageView(image);
			
			icon.setFitWidth(24);
			icon.setFitHeight(24);
			this.getChildren().add(icon);
			this.getStyleClass().add("icons");
		}
		
		public void changeIcon (String newImageFileURL) {
			Image image = new Image(newImageFileURL);
			icon.setImage(image);
		}
	}
}
