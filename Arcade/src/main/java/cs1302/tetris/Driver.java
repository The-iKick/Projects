package cs1302.tetris;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import javafx.util.Duration;
 
public class Driver extends Application {
	static final int GRID_WIDTH = 15;
	static final int GRID_HEIGHT = 22;
	static final int CELL_SIZE = 20;
	static final int CANVAS_WIDTH = GRID_WIDTH * CELL_SIZE;
	static final int CANVAS_HEIGHT = GRID_HEIGHT * CELL_SIZE;
	GameManager gameManager;
	
    public static void main(String[] args) {
        launch(args);
    }
 
    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Canvas canvas = new Canvas(CANVAS_WIDTH , CANVAS_HEIGHT);
        Scene scene = new Scene(root);
        root.getChildren().add(canvas);
                
        //A class that does all the stuff that happens everytime a frame is updated
        gameManager = new GameManager(canvas);
        
        //The game loop
        Timeline gameLoop = new Timeline(new KeyFrame(Duration.seconds(1f/60f), gameManager));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
        
        stage.setScene(scene);
        stage.setTitle("Drawing Operations Test");
        stage.show();
        
        canvas.setFocusTraversable(true);
    }
    
    public void stop () {
    		gameManager.closeGame();
    }
}