package cs1302.minesweeper;

import cs1302.driver.Driver;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Minesweeper {	
	public enum Difficulty {
		EASY, INTERMEDIATE, EXPERT, IMPOSSIBLE
	}
	private int width;
	private int height;
	private int numberOfMines;
	private Difficulty diff;
	
	private GridManager gridManager;
	private GridView gridView;
	private ScoreBoard scoreboard;
	private MenuBar menuBar;
	private Stage stage;
	private Scene scene;
	private VBox wrapper;
	
	/**
	 * Starts a Minesweeper game based on explicit width, height, and number of mines
	 * @param width
	 * @param height
	 * @param mines The number of mines to place on the field
	 * @param stage The stage that the game is to run on
	 */
	public Minesweeper(int width, int height, int mines, Stage stage) {
		//Makes the root
		wrapper = new VBox();
		
		//Sets the width, height, and number of mines
		this.width = width;
		this.height = height;
		this.numberOfMines = mines;
		
		this.stage = stage;
		
		//Creates a scoreboard
		try {
			this.scoreboard = new ScoreBoard(width, numberOfMines);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		generateMenu();
		generateMainBody();
		
		scene = new Scene(wrapper);
	}
	
	/**
	 * Starts a Minesweeper game based on difficulty
	 * @param diff The game difficulty
	 * @param stage The stage that the game is to run on
	 * @throws Exception
	 */
	
	public Minesweeper (Difficulty diff, Stage stage) throws Exception {
		wrapper = new VBox();
		switch (diff) {
			case EASY:
				this.width = 9;
				this.height = 9;
				this.numberOfMines = 10;
			break;
			case INTERMEDIATE:
				this.width = 16;
				this.height = 16;
				this.numberOfMines = 40;
			break;
			case EXPERT:
				this.width = 24;
				this.height = 24;
				this.numberOfMines = 99;
			break;
			case IMPOSSIBLE:
				this.width = 33;
				this.height = 33;
				this.numberOfMines = 220;
			break;
		}
		this.diff = diff;
		this.stage = stage;
		this.scoreboard = new ScoreBoard(width, numberOfMines); 

		generateMenu();
		generateMainBody();
		
		scene = new Scene(wrapper);
	}
	
	private void generateMenu(){
		//Creates a menu item to restart the game
		MenuItem newGame = new MenuItem("New Game");
		newGame.setOnAction(e -> {
			try {
				gridManager.resetGame();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		//Creates a menu item that starts the game as Easy
		MenuItem easy = new MenuItem("Easy");
		easy.setOnAction(e -> {
			try {
				startNewGame(Difficulty.EASY);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		//Creates a menu item that starts the game as Intermediate
		MenuItem medium = new MenuItem("Intermediate");
		medium.setOnAction(e -> {
			try {
				startNewGame(Difficulty.INTERMEDIATE);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		//Creates a menu item that starts the game as Hard
		MenuItem hard = new MenuItem("Expert");
		hard.setOnAction(e -> {
			try {
				startNewGame(Difficulty.EXPERT);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		//Creates a menu item that starts the game as Impossible
		MenuItem impossible = new MenuItem("Impossible");
		impossible.setOnAction(e -> {
			try {
				startNewGame(Difficulty.IMPOSSIBLE);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		//Creates a menu item that opens the custom size prompt box
		MenuItem custom = new MenuItem("Custom");
		custom.setOnAction(e -> {
			CustomDimensions customDimensions = new CustomDimensions(this);
			customDimensions.show();
		});
		
		//Creates a menu item that shows the high-scores window
		MenuItem highScores = new MenuItem("High Scores");
		highScores.setOnAction(new HighScoreWindow());

		//Creates a menu item that exits back to the driver
		MenuItem exit = new MenuItem("Exit");
		exit.setOnAction(e -> {
			this.scoreboard.stopTimer();
			Driver.returnToStart ();
		});
		
		//Puts all the previous menu items in the Game menu
		Menu game = new Menu("Game");
		game.getItems().addAll( 
				newGame, 
				new SeparatorMenuItem(), 
				easy, medium, hard, impossible, custom,
				new SeparatorMenuItem(),
				highScores,
				new SeparatorMenuItem(),
				exit
		);
		
		//Makes a menu item that shows the help window
		MenuItem showHelp = new MenuItem("Help");
		showHelp.setOnAction(e -> {
			HelpWindow helpWindow = new HelpWindow();
			helpWindow.show();
		});
		
		//Puts the single help menu item in the Help menu
		Menu help = new Menu("Help");
		help.getItems().add(showHelp);
		
		//sets the menuBar to a menuBar with the Game and Help menus
		menuBar = new MenuBar(game,help);
	}
	
	
	private void generateMainBody() {
		HBox gridContainer = new HBox();
		HBox bottomBorder = new HBox();
		
		ImageView bottom_left = SpriteManager.border(Borders.BOTTOM_LEFT);
		ImageView bottom_right = SpriteManager.border(Borders.BOTTOM_RIGHT);
		bottomBorder.getChildren().addAll(bottom_left, SpriteManager.horizontal(width), bottom_right);
		
		gridManager = new GridManager(this);
		gridView = new GridView(this);
		
		gridView.setPrefWidth(16*width);
		gridView.setPrefHeight(16*height);
		gridContainer.getChildren().addAll(SpriteManager.vertical(height), gridView, SpriteManager.vertical(height));
		wrapper.getChildren().addAll(menuBar,scoreboard, gridContainer, bottomBorder);
	}
	
	/**	
	 * Gets the {@link GridManager}
	 * @return The {@link GridManager}
	 */
	GridManager getGridManager() {
		return gridManager;
	}
	
	/**
	 * Gets the ScoreBoard
	 * @return The {@link ScoreBoard}
	 */
	ScoreBoard getScoreBoard () {
		return scoreboard;
	}
	
	/**
	 * Gets he {@link Difficulty}
	 * @return The {@link Difficulty}
	 */
	Difficulty getDifficulty () {
		return diff;
	}
	
	/**
	 * Gets the height of the grid
	 * @return The height of the grid
	 */
	int getGridHeight(){
		return height;
	}
	
	/**
	 * Gets the width of the grid
	 * @return The width of the grid
	 */
	int getGridWidth(){
		return width;
	}

	/**
	 * Gets the number of mines in a grid
	 * @return The number of mines in a grid
	 */
	public int getNumberOfMines() {
		return numberOfMines;
	}
	
	/**
	 * Gets the scene
	 * @return {@link Scene}
	 */
	public Scene getScene() {
		return scene;
	}
	
	/**
	 * Starts a new game based on a difficulty
	 * @param diff The {@link Difficulty} of the game
	 */
	void startNewGame(Difficulty diff) {
		stage.hide();
		try {
			 stage.setScene(new Minesweeper(diff, stage).getScene());
		} catch (Exception e) {
			e.printStackTrace();
		}
		stage.show();
	}
	
	/**
	 * Starts a new game based on explicitly defined width, height, and mines
	 * @param width
	 * @param height
	 * @param mines The number of mines on the grid
	 */
	void startNewGame(int width, int height, int mines) {
		stage.hide();
		try {
			stage.setScene(new Minesweeper(width, height, mines, stage).getScene());
		} catch (Exception e) {
			e.printStackTrace();
		}
		stage.show();
	}
}
