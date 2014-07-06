package pl.kfirmanty.chip8.display.renderer;

import pl.kfirmanty.chip8.configuration.Configuration;
import processing.core.PApplet;

public class ProcessingRenderer implements Renderer{
	private PApplet p;
	private int blockSize;
	
	public ProcessingRenderer(PApplet p){
		this.p = p;
		blockSize = Configuration.getInstance().getScale();
	}

	@Override
	public void clearDisplay() {
		p.background(0);
		
	}

	@Override
	public void render(boolean[][] display) {
		p.background(0);
		p.fill(255);
		p.noStroke();
		for(int x = 0; x < display.length; x++){
			for(int y = 0; y < display[x].length; y++){
				if(display[x][y]){
					p.rect(x * blockSize, y * blockSize, blockSize, blockSize);
				}
			}
		}
	}
	
	@Override
	public void setFrameRate(float frameRate){
		p.frameRate(frameRate);
	}
}
