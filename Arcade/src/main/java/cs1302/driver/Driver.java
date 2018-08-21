package cs1302.driver;

import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

import cs1302.minesweeper.Minesweeper;
import cs1302.minesweeper.Minesweeper.Difficulty;
import cs1302.tetris.Tetris;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Driver extends Application{
	
	final Image tetrisImage = new Image(Files.image.TETRIS_ICON);
	final Image minesweeperImage = new Image(Files.image.MINESWEEPER_ICON);
	final Image titleImage = new Image(Files.image.MAIN_TITLE);
	final Image backgroundImage = new Image(Files.image.BACKGROUND);
	
	private static Stage stage;
	private static Scene scene;
	
	Pane tetrisButton, minesweeperButton;
	
	public void start(Stage stage) {
		//Sets the stage as a global variable
		this.stage = stage;
				
		//Creating the game selection panel
		StackPane root = new StackPane();
		BorderPane main = new BorderPane();
		main.setPadding(new Insets(40,40,40,40));
		
		generateTetrisButton();
		generateMinesweeperButton();
		ImageView title = new ImageView(titleImage);

		//
		BorderPane.setAlignment(title, Pos.TOP_CENTER);
		BorderPane.setMargin(title, new Insets(0,0, 15,0));
		main.setTop(title);
		main.setLeft(tetrisButton);
		main.setRight(minesweeperButton);
		
		main.setStyle("-fx-background-color: black");
		//main.setBackground(background);
		root.getChildren().addAll(main, animatedOverlay());
		//Starting the game
		scene = new Scene(root);
		scene.getStylesheets().addAll(Files.css.MAIN);
		stage.setScene(scene);
		stage.setTitle("ARCADE");
		stage.setResizable(false);
		stage.show();
	}
	
	private int i = 0;
	private VBox animatedOverlay () {
		VBox intro = new VBox();
		ImageView title = new ImageView(titleImage);
		title.setOpacity(0);
		Text teamName = new Text("Teamname: IllegalSkillsException");
		Text names = new Text("Shawn Holman, Hazim Mohammad");
		
		teamName.getStyleClass().add("text");
		names.getStyleClass().add("text");
		intro.setStyle("-fx-background-color: rgba(0, 0, 0, 1); -fx-alignment: center;");
		
		Timeline flicker = new Timeline(new KeyFrame(Duration.seconds(0.1), (e) -> {
			if (i == 1) {
				fadeIn(title, 100);
			}
			if (i == 3) {
				fadeOut(title, 50);
			}
			if (i == 4) {
				fadeIn(title, 25);
			}
			if (i == 6) {
				fadeOut(title, 50);
			}
			if (i == 7) {
				fadeIn(title, 250);
			}
			if (i == 10) {
				TranslateTransition tt = new TranslateTransition(Duration.millis(1000), title);
			     tt.setByY(-97);
			     tt.setCycleCount(1);
			     tt.play();
			}
			if (i == 16) {
				fadeIn(teamName, 250);
			}
			if (i == 17) {
				fadeIn(names, 250);
			}
			if (i == 40) {
				fadeOut(intro, 300);
			}
			if (i == 44) {
				StackPane p = (StackPane) intro.getParent();
				p.getChildren().remove(intro);
			}
			i++;
		}));
		intro.setOnMouseClicked((e) -> {
			flicker.stop();
			fadeOut(intro, 300);
			new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                		Platform.runLater(() -> {
                			StackPane p = (StackPane) intro.getParent();
                			p.getChildren().remove(intro);
                		});
                }
            }, 400); 
		});
		flicker.setCycleCount(45);
		flicker.play();
		intro.getChildren().addAll(title, teamName, names);
		return intro;
	}
	
	private void generateTetrisButton() {
		ImageView img = new ImageView(tetrisImage);
		img.setFitWidth(250);
		img.setFitHeight(250);
		tetrisButton = new Pane(img);
		tetrisButton.getStyleClass().add("pane");
		tetrisButton.setOnMouseClicked(e -> {
			try {
				startTetris();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		});
	}	
	
	private void generateMinesweeperButton(){
		ImageView img = new ImageView(minesweeperImage);
		img.setFitWidth(250);
		img.setFitHeight(250);
		minesweeperButton = new Pane(img);
		minesweeperButton.getStyleClass().add("pane");
		minesweeperButton.setOnMouseClicked(e -> {
			try {
				startMinesweeper();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
	}
	
	public static void fadeIn (Node el, int delay) {
		FadeTransition ft = new FadeTransition(Duration.millis(delay), el);
	     ft.setFromValue(0);
	     ft.setToValue(1);
	     ft.setCycleCount(1);
	     ft.play();
	}
	
	public static void fadeOut (Node el, int delay) {
		FadeTransition ft = new FadeTransition(Duration.millis(delay), el);
	     ft.setFromValue(1);
	     ft.setToValue(0);
	     ft.setCycleCount(1);
	     ft.play();
	}
	
	private void startTetris() throws FileNotFoundException {
		stage.hide();
		Tetris tetris = new Tetris();
		stage.setScene(tetris.getScene());
		stage.sizeToScene();
		stage.show();
	}
	
	private void startMinesweeper() throws Exception {
		stage.hide();
		Minesweeper minesweeper =  new Minesweeper(Difficulty.INTERMEDIATE, stage);
		stage.setScene(minesweeper.getScene());
		stage.sizeToScene();
		stage.show();
	}
	
	public static void returnToStart () {
		stage.hide();
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}
	
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}
