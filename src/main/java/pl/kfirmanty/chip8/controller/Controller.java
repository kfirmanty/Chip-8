package pl.kfirmanty.chip8.controller;

import java.util.Map;

public interface Controller {
	boolean isKeyPressed(short key);
	boolean isKeyReleased(short key);
	void pressKey(char key);
	void releaseKey(char key);
	Map<Character, Short> getKeyScheme();
	Short getLastPressedKey();
}
