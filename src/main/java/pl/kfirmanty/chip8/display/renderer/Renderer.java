package pl.kfirmanty.chip8.display.renderer;

public interface Renderer {
	void clearDisplay();
	
	void render(boolean[][] display);
	
	void setFrameRate(float frameRate);
}
