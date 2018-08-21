package cs1302.tetris;

import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;

/**
 * KeyListener registers, and holds onto key values for as long as i
 * @author Shawn
 *
 */
public class KeyListener {
	interface Callee {
		void call ();
	}
	
	HashSet<KeyCode> keyPressed = new HashSet<KeyCode>();
	Map<KeyCode, Callee> keyMap = new HashMap<KeyCode, Callee>();
	
	int throttle = 0;
	
	/**
	 * Register a key
	 * @param keyCode
	 */
	public void register (KeyCode keyCode) {
		keyPressed.add(keyCode);
	}
	
	/**
	 * Clear a key from the registery
	 * @param keyCode
	 */
	public void clear(KeyCode keyCode) {
		keyPressed.remove(keyCode);
	}
	
	/**
	 * Call defined method if the key is being pressed
	 * @param keyName
	 */
	public void pressing(KeyCode keyName) {
		if (isPressing(keyName) && keyMap.containsKey(keyName)) {
			keyMap.get(keyName).call();
		}
	}
	
	/**
	 * Executes the defined callee if the key is pressed
	 */
	public void checkDefined () {
		for (KeyCode key : keyMap.keySet()) {
			// throttle the call in order to reduce responsiveness
			// in the case of this game, we do not want left and right to be as smooth as down
			// hence why we have two different throttle point
			if (throttle % 6 == 0 || (throttle % 3 == 0 && key == KeyCode.SPACE)) {
			    pressing(key);
			} 
		}
		throttle++;
	}
	
	/**
	 * Checks to see if a key is being pressed
	 * @param keyName 
	 * @return
	 */
	public boolean isPressing (KeyCode keyName) {
		return keyPressed.contains(keyName);
	}
	
	/**
	 * Defines a key and callee relationship
	 * @param keyName Defines the name of the key
	 * @param onKey defines the keys action
	 */
	public void define (KeyCode keyName, Callee onKey) {
		keyMap.put(keyName, onKey);
	}
}
