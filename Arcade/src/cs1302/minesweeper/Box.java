package cs1302.minesweeper;

import java.io.FileNotFoundException;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;

public class Box extends ImageView{
	enum State{
		COVERED, FLAGGED, QUESTIONED, UNCOVERED, UNCOVERED_NUMBER, UNCOVERED_BOMB,
		UNCOVERED_BOMB_TRIGGERED, UNCOVERED_FALSE_ALARM
	}
	
	private int x, y;
	private State state;
	private GridManager gridManager;
	private boolean disabled = false;
	
	Box(GridManager gridManager, int x, int y){
		this.x = x;
		this.y = y;
		
		this.gridManager = gridManager;
		
		this.setImage(SpriteManager.sprite);
		
		this.setOnMousePressed((e) -> {
			if (e.getButton() == MouseButton.PRIMARY && state == State.COVERED) {
				setState(State.UNCOVERED);
				this.gridManager.getScoreBoard().setFace(Reaction.ANXIOUS);
			}
		});
		this.setOnMouseReleased(e -> { // attach the button handler
			if (!disabled) {
				if (e.getButton() == MouseButton.PRIMARY) {
					if (state != State.FLAGGED) {
						try {
							gridManager.clicked(getXPosition(),getYPosition()); // click the grid
							gridManager.getScoreBoard().setFace(Reaction.HAPPY);
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				} else {
					try {
						gridManager.rightClicked(getXPosition(),getYPosition());
					} catch (Exception e1) {}
				}
			}
		});
	}
	
	/**
	 * Get State
	 * @return
	 */
	State getState() {
		return state;
	}
	
	/**
	 * Set disabled
	 */
	void setDisabled () {
		disabled = true;
	}
	
	/**
	 * Change the state of a box
	 * @param state
	 * @throws IllegalArgumentException
	 */
	void setState(State state) throws IllegalArgumentException{
		this.state = state;
		switch(state) { // switch through the states
		case COVERED:
			this.setViewport(SpriteManager.unopened().getViewport());
			break;
		case UNCOVERED:
			this.setViewport(SpriteManager.opened(0).getViewport());
			break;
		case UNCOVERED_NUMBER:
			this.setViewport(SpriteManager.opened(gridManager.getNumberOfMinesAround(x, y)).getViewport());
			break;
		case UNCOVERED_BOMB:
			this.setViewport(SpriteManager.bomb().getViewport());
			break;
		case UNCOVERED_BOMB_TRIGGERED:
			this.setViewport(SpriteManager.triggeredBomb().getViewport());
			break;
		case UNCOVERED_FALSE_ALARM:
			this.setViewport(SpriteManager.notBomb().getViewport());
			break;
		case FLAGGED:
			this.setViewport(SpriteManager.flagged().getViewport());
			break;
		default:
			break;
		}
	}
	
	int getXPosition(){
		return x;
	}
	
	int getYPosition() {
		return y;
	}
}