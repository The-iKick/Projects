package cs1302.minesweeper;

import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class HelpWindow extends Stage{
	static final int maxWidth = 300;
	Font headerFont = Font.font("Verdana",FontWeight.BOLD,15);
	Font plainFont = Font.font("Verdana",10);
	
	/**
	 * Creates a hidden stage that tells the user how to play Minesweeper
	 */
	HelpWindow(){
		Text helpTitle = new Text("HOW TO PLAY MINESWEEPER");
		helpTitle.setFont(Font.font("Helvetica",FontWeight.BOLD,20));
		
		//Separator
		Separator seperator1 = new Separator();
		seperator1.setOrientation(Orientation.HORIZONTAL);
		
		//Explains the objective
		FlowPane objective = makeSection("Objective",
				"The goal of the game is to locate and mark all of the mines and reveal all squares"
				+ "that don't contain mines. When all the mines have been marked the face at the center of the screen will "
				+ "put on a pair of sunglasses - indicating a completed game."
		);
		
		//Separator
		Separator seperator2 = new Separator();
		seperator2.setOrientation(Orientation.HORIZONTAL);
		
		//Explains how the User Interface and Controls
		FlowPane userInterfaceAndControls = makeSection("User Interface and Controls",
				"The game is played by left clicking tiles that you want to reveal and right"
				+ "clicking tiles that you want to mark as possible mines. The counter on the top left keeps track of how many"
				+ "mines are remaining on the field. The counter on the top right is a timer that tracks how long you've been "
				+ "playing and functions as your score"
		);

		//Creates the OK button
		Button OK = new Button("OK");
		OK.setOnAction(e -> {
			this.close();
		});
		
		//Add all to the root
		VBox root = new VBox();
		root.getChildren().addAll(
				helpTitle, 
				seperator1, 
				objective, 
				seperator2, 
				userInterfaceAndControls
		);

		//Create a scene and set the scene of this to that scene
		Scene scene = new Scene(root);
		this.setScene(scene);
		this.sizeToScene();
		this.setResizable(false);
	}
	
	//Creates a FlowPane with format of HEADER \n ...text...
	private FlowPane makeSection(String inHeader, String inText) {
		Text header = new Text(inHeader);
		header.setFont(headerFont);
		
		Text text = new Text(inText);
		text.setFont(plainFont);
		
		FlowPane out = new FlowPane();
		out.getChildren().addAll(header,text);
		
		out.setMaxWidth(maxWidth);
		
		return out;
		
	}
}
