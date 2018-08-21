package cs1302.minesweeper;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CustomDimensions extends Stage{
	Minesweeper minesweeper;
	TextField heightField, widthField, mineField;
	
	CustomDimensions(Minesweeper minesweeper){
		this.minesweeper = minesweeper;
		
		VBox root = new VBox();
		
		Text title = new Text("Custom Size");
		
		BorderPane heightPanel = new BorderPane();
		Label heightLabel = new Label("Height:");
		heightField = new TextField();
		heightPanel.setLeft(heightLabel);
		heightPanel.setRight(heightField);
		heightPanel.setPadding(new Insets(10, 10, 10, 10));
		heightField.setPrefWidth(70);
		heightPanel.setPrefWidth(300);
		
		BorderPane widthPanel = new BorderPane();
		Label widthLabel = new Label("Width:");
		widthField = new TextField();
		widthPanel.setLeft(widthLabel);
		widthPanel.setRight(widthField);
		widthPanel.setPadding(new Insets(10, 10, 10, 10));
		widthField.setPrefWidth(70);
		widthPanel.setPrefWidth(300);

		BorderPane minePanel = new BorderPane();
		Label mineLabel = new Label("Mines:");
		mineField = new TextField();
		minePanel.setLeft(mineLabel);
		minePanel.setRight(mineField);
		minePanel.setPadding(new Insets(10, 10, 10, 10));
		mineField.setPrefWidth(70);
		minePanel.setPrefWidth(300);

		BorderPane buttonBox = new BorderPane();
		
		Button OK = new Button("OK");
		OK.setOnAction(e -> {
			int width = Integer.parseInt(widthField.getText());
			int height = Integer.parseInt(heightField.getText());
			int mines = Integer.parseInt(mineField.getText());
			if(width > 100 || width < 10 || height > 100 || height < 10) {
				Alert invalidSize = new Alert(AlertType.ERROR);
				invalidSize.setContentText("The width and height must be between 10 and 100");
				invalidSize.show();
			} else if(mines > (width*height*0.5)){
				Alert invalidMineCount = new Alert(AlertType.ERROR);
				invalidMineCount.setContentText("You can only have up to 50% of your field be mines");
				invalidMineCount.show();
			} else {
				try {
					minesweeper.startNewGame(width,height,mines);
				} catch (Exception exc) {
					//Do nothing	
				}
				this.close();
			}
		});
		
		Button cancel = new Button("Cancel");
		cancel.setOnAction(e -> {
			this.close();
		});
		
		buttonBox.setLeft(OK);
		buttonBox.setRight(cancel);
		buttonBox.setPadding(new Insets(10, 10, 10, 10));
		
		root.getChildren().addAll(title,widthPanel, heightPanel, minePanel, buttonBox);
		root.setPadding(new Insets(10, 0, 10, 0));
		
		Scene scene = new Scene(root);
		
		this.setScene(scene);
		this.initModality(Modality.WINDOW_MODAL);
	}
}
