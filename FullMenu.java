import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import java.io.File;
import javafx.application.Platform;
import javafx.scene.control.*;

/**
 * Represents our menu bar and sets it up for us
 * 
 * @author Shawn Holman
 * @version 0.1.0
 */
public class FullMenu extends MenuBar {
	FullMenu () {
		super();
		
		File main = new File("src/main.css"); // stop the main css for use in the switcher
		
		this.getMenus().addAll(
			menuItem("File", 
				menuSubItem("Exit", (e) -> {
					Platform.exit();
				})
			),
			menuItem("Themes",
				radioSubItem("Light", (e) -> {
					File f = new File("src/light.css");
					this.getScene().getStylesheets().clear();
					this.getScene().getStylesheets().addAll("file:///" + main.getAbsolutePath().replace("\\", "/"), "file:///" + f.getAbsolutePath().replace("\\", "/"));
				}),
				
				radioSubItem("Dark", (e) -> {
					File f = new File("src/dark.css");
					this.getScene().getStylesheets().clear();
					this.getScene().getStylesheets().addAll("file:///" + main.getAbsolutePath().replace("\\", "/"), "file:///" + f.getAbsolutePath().replace("\\", "/"));
				}, true),
				
				radioSubItem("Cherry Bloosom", (e) -> {
					File f = new File("src/cherryblossom.css");
					this.getScene().getStylesheets().clear();
					this.getScene().getStylesheets().addAll("file:///" + main.getAbsolutePath().replace("\\", "/"), "file:///" + f.getAbsolutePath().replace("\\", "/"));
				}),
				
				radioSubItem("Waves", (e) -> {
					File f = new File("src/Waves.css");
					this.getScene().getStylesheets().clear();
					this.getScene().getStylesheets().addAll("file:///" + main.getAbsolutePath().replace("\\", "/"), "file:///" + f.getAbsolutePath().replace("\\", "/"));
				}),
				
				radioSubItem("Grass", (e) -> {
					File f = new File("src/grass.css");
					this.getScene().getStylesheets().clear();
					this.getScene().getStylesheets().addAll("file:///" + main.getAbsolutePath().replace("\\", "/"), "file:///" + f.getAbsolutePath().replace("\\", "/"));
				})
			),
			menuItem("Help",
				menuSubItem("About", new AboutWindow())
			)
		);
	}
	
	/**
	 * Creates a Menu item with regular sub items
	 * @param title of the menu
	 * @param buttons a variadic list of menu subitems
	 * @return the menu item
	 */
	protected Menu menuItem (String title, MenuItem ... buttons) {
		Menu fileMenu = new Menu(title);
		
		for (MenuItem s : buttons)
			fileMenu.getItems().add(s);
		
		return fileMenu;
	}
	
	/**
	 * Creates a Menu item with radio sub items
	 * @param title of the menu
	 * @param buttons a variadic list of menu radio buttons
	 * @return the menu item
	 */
	protected Menu menuItem (String title, RadioMenuItem ... radioGroup) {
		Menu menu = new Menu(title);
		ToggleGroup toggleGroup = new ToggleGroup();

		for (RadioMenuItem item: radioGroup) {
			item.setToggleGroup(toggleGroup);
			menu.getItems().add(item);
		}
        
        return menu;
	}
	
	/**
	 * default radioSubItem
	 * 
	 * @see radioSubItem (String text, EventHandler<ActionEvent> action, boolean selected)
	 */
	protected RadioMenuItem radioSubItem (String text, EventHandler<ActionEvent> action) {
		return radioSubItem(text, action, false);
	}
	
	/**
	 * Creates a radio sub menu item
	 * 
	 * @param text of the sub menu item
	 * @param action the action that should happen on click
	 * @param selected Should we select this radio button?
	 * @return sub menu item
	 */
	protected RadioMenuItem radioSubItem (String text, EventHandler<ActionEvent> action, boolean selected) {
		RadioMenuItem radioItem = new RadioMenuItem(text);
		radioItem.setOnAction(action);
		radioItem.setSelected(selected);
		
		return radioItem;
	}
	
	/**
	 * Creates a regular sub menu item
	 * 
	 * @param text of the sub menu item
	 * @param action the action that should happen on click
	 * @return sub menu item
	 */
	protected MenuItem menuSubItem (String text, EventHandler<ActionEvent> action) {
		MenuItem subitem = new MenuItem(text);
		
		subitem.setOnAction(action);
		
		return subitem;
	}
}
