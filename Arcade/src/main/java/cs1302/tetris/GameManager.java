package cs1302.tetris;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;


public class GameManager implements EventHandler<ActionEvent>{
	public int score;
	
	Shape currentShape;
	GridManager gridManager;
	GraphicsContext gc;
	Canvas canvas;
	Timer timer = new Timer();
	Random rand = new Random();
	
	boolean ready = false;
	
	GameManager (Canvas canvas) {
		gridManager = new GridManager();
		this.canvas = canvas;
		this.gc = canvas.getGraphicsContext2D();
		
		generateNewShape();
		defineHandlers ();
		
		ready = true;
	}
	
	public void defineHandlers () {
		canvas.setOnKeyPressed((e) -> {
			if (e.getCode() == KeyCode.LEFT) {
				if (gridManager.canMoveLeft(currentShape)) {
					currentShape.moveLeft();
				}
			}
			if (e.getCode() == KeyCode.RIGHT) {
				if (gridManager.canMoveRight(currentShape)) {
					currentShape.moveRight();
				}
			}
			if (e.getCode() == KeyCode.SPACE) {
				if (gridManager.canMoveDown(currentShape)) {
	    				currentShape.moveDown();
		    		} else {
		    			generateRandomShape ();
		    		}
			}
			if (e.getCode() == KeyCode.UP) {
				currentShape.rotateLeft();
			}
			if (e.getCode() == KeyCode.DOWN) {
				currentShape.rotateRight();
			}
		});
	}
	
	public void generateRandomShape () {
		int randomInt = rand.nextInt(8);
		switch(randomInt) {
			case 0:
				currentShape = new Line(gc);
			break;
			case 1:
				currentShape = new LShape(gc);
			break;
			case 2:
				currentShape = new Square(gc);
			break;
			case 3:
				currentShape = new SShape(gc);
			break;
			case 4:
				currentShape = new TShape(gc);
			break;
			case 5:
				currentShape = new ZShape(gc);
			break;
			case 6:
				currentShape = new JShape(gc);
			break;
		}
	}
	
	public void generateNewShape () {
		generateRandomShape ();
		
		timer.scheduleAtFixedRate(new TimerTask(){
		    @Override
		    public void run(){
		    		if (gridManager.canMoveDown(currentShape)) {
		    			currentShape.moveDown();
		    		} else {
		    			generateRandomShape ();
		    		}
		    }
		}, 0, 500);
	}
	
	/**
	 * Ensure that the game is prepared to close but canceling the timer
	 */
	public void closeGame () {
		timer.cancel();
		timer.purge();
	}
	
	@Override
	public void handle(ActionEvent actionEvent) {
		if (ready) {
			gc.clearRect(0, 0, Driver.CANVAS_WIDTH, Driver.CANVAS_HEIGHT);
			currentShape.draw();
			gridManager.draw();
		}
	}
}
