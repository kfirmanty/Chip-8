package pl.kfirmanty.chip8.display;

import pl.kfirmanty.chip8.configuration.Configuration;
import pl.kfirmanty.chip8.display.renderer.Renderer;

public class Display {
	
	private boolean[][] display;
	private Renderer renderer;
	
	public Display(){
		Configuration config = Configuration.getInstance();
		display = new boolean[config.getDisplayWidth()][config.getDisplayHeight()];
	}
	
	public void setRenderer(Renderer renderer){
		this.renderer = renderer;
	}
	
	public void cls(){
		
	}
	
	public void update(){
		renderer.render(display);
	}
	
	public void loadSpriteFromMemory(int bytesCount){
		
	}
}
