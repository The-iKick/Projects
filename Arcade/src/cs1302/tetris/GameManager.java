package cs1302.tetris;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;

/**
 * The GameManager is the main frontend logic driver which manages game movement and score
 */
public class GameManager implements EventHandler<ActionEvent> {
	public int shapesDroppedThisLevel;

	Tetris game;
	Shape currentShape;
	Shape nextShape;
	GridManager gridManager;
	GraphicsContext gc, nextShapeGC;
	Canvas canvas;
	Timeline moveDownShape = new Timeline();
	KeyListener keyManager = new KeyListener();
	ScoreBoard scoreBoard = new ScoreBoard();

	Random rand = new Random();
	
	public GameManager(Tetris tetris) {
		this.game = tetris;
		
		this.gridManager = new GridManager(tetris.getScoreBoard());
		this.canvas = tetris.getGameCanvas();
		this.gc = canvas.getGraphicsContext2D();
		this.nextShapeGC = tetris.getNextShapeCanvas().getGraphicsContext2D();
		this.scoreBoard = tetris.getScoreBoard();
		
		// set our first two shapes
		currentShape = generateRandomShape(gc);
		nextShape = generateRandomShape(nextShapeGC);
		currentShape.moveOutContext();
		nextShape.moveToContextCenter();
		
		defineHandlers ();
		startMovingShapes ();
		
		shapesDroppedThisLevel = 0;
	}
	
	/**
	 * Defines a set of keyhandlers
	 */
	public void defineHandlers () {
		canvas.setOnKeyPressed((e) -> {	
			keyManager.register(e.getCode());
			if (e.getCode() == KeyCode.UP) gridManager.rotateIfCan(Rotations.Clockwise, currentShape);
			if (e.getCode() == KeyCode.DOWN) gridManager.rotateIfCan(Rotations.Counterclockwise, currentShape);
			if (e.getCode() == KeyCode.P) {
				if (game.isPaused()) {
					game.play();
					moveDownShape.play();
				}
				else {
					game.pauseOverlay();
					moveDownShape.pause();
				}
			}
		});
		canvas.setOnKeyReleased((e) -> keyManager.clear(e.getCode()));
		keyManager.define(KeyCode.SPACE, () -> {
			if (gridManager.canMoveDown(currentShape)) {
				currentShape.moveDown();
				scoreBoard.incrementScore(Tetris.SCORE_PER_DOWN);
	    		} else {
	    			swapShapes();
	    		}
		});
		keyManager.define(KeyCode.LEFT, () -> {
			if (gridManager.canMoveLeft(currentShape)) {
				currentShape.moveLeft();
			}
		});
		keyManager.define(KeyCode.RIGHT, () -> {
			if (gridManager.canMoveRight(currentShape)) {
				currentShape.moveRight();
			}
		});
	}
	
	/**
	 * Generates a new shape in a random fashion
	 * @param gc Graphics Context to draw the shape in
	 * @return next shape
	 */
	public Shape generateRandomShape(GraphicsContext gc) {
		int randomInt = rand.nextInt(7);
		
		switch(randomInt) {
			case 0:
				return new Line(gc);
			case 1:
				return new LShape(gc);
			case 2:
				return new Square(gc);
			case 3:
				return new SShape(gc);
			case 4:
				return new TShape(gc);
			case 5:
				return new ZShape(gc);
			case 6:
				return new JShape(gc);
		}

		return null;
	}
	
	/**
	 * Start a timeline that moves the shape down every so often
	 */
	public void startMovingShapes () {
		// defines how fast the shapes fall
		Duration downDelay = Duration.millis(600 - (scoreBoard.getLevel()*scoreBoard.getLevel())/3);
		moveDownShape.stop();
		moveDownShape.getKeyFrames().clear();
		
		moveDownShape.getKeyFrames().add(new KeyFrame(downDelay, e -> {
			if (gridManager.canMoveDown(currentShape)) {
		    		currentShape.moveDown();
		    	} else {
		    		swapShapes();
		    	}
		}));
		moveDownShape.setCycleCount(Timeline.INDEFINITE);
		moveDownShape.play();
	}
	
	/**
	 * Ensure that the game is prepared to close but canceling the timer
	 * @throws FileNotFoundException 
	 */
	public void closeGame () throws FileNotFoundException {
		nextShapeGC.clearRect(0, 0, Tetris.CELL_SIZE * 7, Tetris.CELL_SIZE * 5);
		moveDownShape.stop();
		game.gameover(true);
	}
	
	/**
	 * Pauses the game manager
	 */
	public void pause () {
		moveDownShape.pause();
	}
	
	/**
	 * Plays the game manager
	 */
	public void play () {
		moveDownShape.play();
	}
	
	@Override
	public void handle(ActionEvent actionEvent) {
		if (gridManager.isPlayable()) {
			gc.clearRect(0, 0, Tetris.CANVAS_WIDTH, Tetris.CANVAS_HEIGHT);
			
			int[][] shapePredicitons = gridManager.findPredictedLanding (currentShape);
			for (int[] coords: shapePredicitons) {
				Box a = new Box(gc, coords[0], coords[1]);
				a.setColor("black");
				a.draw();
			}
			
			keyManager.checkDefined();
			currentShape.draw();
			gridManager.draw();
		} else {
			try {
				closeGame();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Change the level, which adjusts the falling speed
	 */
	private void nextLevel() {
		if (scoreBoard.getLevel() < 38) {
			scoreBoard.incrementLevel();
			
			startMovingShapes ();
			shapesDroppedThisLevel = 0;
		}
	}
	
	/**
	 * Swaps the current and next shape and generates a new next shape
	 */
	private void swapShapes () {
		nextShapeGC.clearRect(0, 0, Tetris.CELL_SIZE * 7, Tetris.CELL_SIZE * 5);
		
		nextShape.setGC(gc); // move the next shape to the new canvas
		currentShape = nextShape; // swap the shapes in memory
		nextShape = generateRandomShape(nextShapeGC); // generate a new next shape
		currentShape.moveOutContext(); // prepare the currentShape so it can fall
		nextShape.moveToContextCenter(); // prepare the next shape so that it can be displayed
		shapesDroppedThisLevel++;
		
		if(shapesDroppedThisLevel >= Tetris.SHAPES_PER_LEVEL) {
			nextLevel();
		}
	}
}
