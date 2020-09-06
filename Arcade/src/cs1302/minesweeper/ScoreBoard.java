package cs1302.minesweeper;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class ScoreBoard extends VBox {
	interface Callee {
		void call();
	}
	
	private Timeline timer;
	private int numberOfBombs;

	private NumberImage mineCounter;
	private ImageView reaction;
	private Callee onClick = null;
	private NumberImage timeCounter = new NumberImage(0);
	
	/**
	 * Creates a new ScoreBoard object
	 * @param width
	 * @param numOfBombs The number of bombs on the grid
	 * @throws Exception
	 */
	ScoreBoard (int width, int numOfBombs) throws Exception {
		this.numberOfBombs = numOfBombs;
		mineCounter = new NumberImage(numOfBombs);
		
		///Creates the top border using the sprite manager
		HBox topBorder = new HBox();
		ImageView top_left = SpriteManager.border(Borders.TOP_LEFT);
		ImageView top_right = SpriteManager.border(Borders.TOP_RIGHT);
		topBorder.getChildren().addAll(top_left, SpriteManager.horizontal(width), top_right);
		
		//Creates the middle borders using the sprite manager
		HBox middleSection = new HBox();
		ImageView lvertical = SpriteManager.border(Borders.VERTICAL_LONG);
		ImageView rvertical = SpriteManager.border(Borders.VERTICAL_LONG);
		
		//Creates a space in the middle to space out the 
		Region spacer1 = new Region();
		Region spacer2 = new Region();
		HBox.setHgrow(spacer1, Priority.ALWAYS);
		HBox.setHgrow(spacer2, Priority.ALWAYS);
		
		//Creates the main container that the rest of the 
		HBox container = new HBox();
		container.setPrefWidth(width*16);
		
		//Creates the reaction face in the middle
		reaction = SpriteManager.happyFace();
		mineCounter.setId("mineCount");
		timeCounter.setId("timeCount");
		container.setId("scoreContainer");
		container.getChildren().addAll(mineCounter, spacer1, reaction, spacer2, timeCounter);
		container.setStyle("-fx-padding: 3 10; -fx-background-color: #c0c0c0;");
		middleSection.getChildren().addAll(lvertical, container, rvertical);
		
		//Changes the face when pressed
		reaction.setOnMousePressed((e) -> {
			setFace(Reaction.HAPPY_PRESSED);
		});
		
		//Starts a new game when the face
		reaction.setOnMouseReleased((e) -> {
			if (onClick != null) onClick.call();
			setFace(Reaction.HAPPY);
		});
		
		//Middle separator 
		HBox middleBorder = new HBox();
		ImageView joint_left = SpriteManager.border(Borders.JOINT_LEFT);
		ImageView joint_right = SpriteManager.border(Borders.JOINT_RIGHT);
		middleBorder.getChildren().addAll(joint_left, SpriteManager.horizontal(width), joint_right);
		
		//Adds all object
		this.getChildren().addAll(topBorder, middleSection, middleBorder);
	}
	
	public void setOnClick (Callee a) {
		onClick = a;
	}
	
	public void reset () throws Exception {
		timer.pause();
		timeCounter.set(0);
		mineCounter.set(numberOfBombs);
	}
	
	public void startTimer () {
		timer = new Timeline(new KeyFrame(Duration.seconds(1), (e) -> {
			try {
				timeCounter.increment();
			} catch (Exception e1) {}
		}));
		timer.setCycleCount(Timeline.INDEFINITE);
	    timer.play();
	}
	
	public void setFace (Reaction react) {
		reaction.setViewport(SpriteManager.face(react).getViewport());
	}
	
	public void stopTimer () {
		if (timer != null) timer.stop();
	}
	
	public void decrementBombCount () throws Exception {
		mineCounter.decrement();
	}
	
	public void incrementBombCount () throws Exception {
		mineCounter.increment();
	}
	
	public int getTime () {
		return timeCounter.getNumber();
	}
	
	/**
	 * 
	 * @author hazimmohamed
	 *
	 */
	class NumberImage extends HBox {
		int n = 0;
		
		NumberImage (int start) throws Exception {
			n = start;
			generate();
		}
		
		
		public void generate () throws Exception {
			this.getChildren().clear();
			this.getChildren().add(translate(n));
		}
		
		//Increments the timer
		public void increment () throws Exception {
			if (n < 999) {
				n++;
				generate();
			}
		}
		
		//decrements the timer
		public void decrement () throws Exception {
			if (n > -99) {
				n--;
				generate();
			}
		}
		
		public void set (int newNumber) throws Exception {
			n = newNumber; 
			generate();
		}
		
		public int getNumber () {
			return n;
		}
		
		private HBox translate (int number) throws Exception {
			String[] num = Integer.toString(number).split("");
			HBox numberContainer = new HBox();
			
			if (num.length == 1) {
				ImageView zero1 = SpriteManager.numbers(0);
				ImageView zero2 = SpriteManager.numbers(0);
				numberContainer.getChildren().addAll(zero1, zero2, SpriteManager.numbers(number));
			} 
			if (num.length == 2) {
				ImageView zero = SpriteManager.numbers(num[0].equals("-") ? 10 : 0);
				int one = num[0].equals("-") ? 0 : Integer.parseInt(num[0]);
				int two = Integer.parseInt(num[1]);
				numberContainer.getChildren().addAll(zero, SpriteManager.numbers(one), SpriteManager.numbers(two));
			}
			if (num.length == 3) {
				int one = num[0].equals("-") ? 10 : Integer.parseInt(num[0]);
				int two = Integer.parseInt(num[1]);
				int three = Integer.parseInt(num[2]);
				numberContainer.getChildren().addAll(SpriteManager.numbers(one), SpriteManager.numbers(two), SpriteManager.numbers(three));
			}
			return numberContainer;
		}
	}
}
