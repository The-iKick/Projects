import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Represents our About Window. Implements EventHandler in order to be opened on click of another button
 * 
 * @author Shawn Holman
 * @version 0.1.0
 */
public class AboutWindow implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		Stage stage = new Stage(); // create a new window
		
		HBox root = new HBox();
		VBox descriptions = new VBox();
		Scene scene = new Scene(root);
        stage.setTitle("About Shawn Holman");
        
        String textStyle = "-fx-fill: rgb(100, 100, 100);";
        
        // create a circuluar image 
        Circle image = new Circle(150,150,70);
        image.setStroke(Color.GREY);
        Image im = new Image("http://shawnholman.com/images/me.jpg",false);
        image.setFill(new ImagePattern(im));
        
    		// set up the information
    		Text name = new Text("Shawn Holman");
    		
    		TextFlow emailBlock = new TextFlow();
    		Text emailLabel = new Text("Email   ");
    		Text email = new Text("smh27299@uga.edu");
    		emailLabel.setStyle("-fx-font-weight: bold;");
    		emailBlock.setStyle(textStyle);
    		emailBlock.getChildren().addAll(emailLabel, email);
    		
    		Text version = new Text("Version 0.1.0");
    		Text disclaimer = new Text("Images supplied by iTunes Search API");
    		
    		// style everything
    		name.setStyle("-fx-font: 35 \"Courier New\", Courier, monospacef");
    		email.setStyle(textStyle);
    		version.setStyle(textStyle);
    		image.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(77,77,77,0.8), 5, 0, 0, 0);");
    		root.setStyle("-fx-background-color: rgb(240,240,240)");
    		
    		// set all of our margins
    		HBox.setMargin(image,new Insets(30,30,30,30));
    		HBox.setMargin(descriptions,new Insets(10,10,10,10));
    		VBox.setMargin(version,new Insets(-5,0,0,0));
    		VBox.setMargin(emailBlock,new Insets(15,0,35,0));
    		
    		// put it all in
    		descriptions.setAlignment(Pos.CENTER_LEFT);
    		descriptions.getChildren().addAll(name, version, emailBlock, disclaimer);
    		root.getChildren().addAll(image, descriptions);
	    
    		// set up stage
    		stage.setWidth(490);
    		stage.setScene(scene);
	    stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
	}
}
