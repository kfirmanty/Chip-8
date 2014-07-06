package pl.kfirmanty.chip8.controller;

import java.util.HashMap;
import java.util.Map;

public class KeyboardController implements Controller{
	Map<Character, Short> keyScheme;
	Map<Short, Boolean> keys;
	Short lastPressedKey;
	
	public KeyboardController(){
		keyScheme = new HashMap<>();
		keys = new HashMap<>();
		initKeys();
	}
	
	private void initKeys(){
		for(short i = 0; i < 0x10; i++){
			keys.put(i, false);
		}
	}
	
	public boolean isKeyPressed(short key){
		return keys.get(key);
	}
	
	public boolean isKeyReleased(short key){
		return !isKeyPressed(key);
	}
	
	public void pressKey(char key){
		Short pressedKey = keyScheme.get(key);
		if(pressedKey == null) return;
		keys.put(pressedKey, true);
		lastPressedKey = pressedKey;
	}
	
	public void releaseKey(char key){
		keys.put(keyScheme.get(key), false);
		lastPressedKey = null;
	}

	public Map<Character, Short> getKeyScheme() {
		return keyScheme;
	}
	
	@Override
	public Short getLastPressedKey(){
		return lastPressedKey;
	}
	
}
