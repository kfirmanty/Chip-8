package pl.kfirmanty.chip8.display;

import pl.kfirmanty.chip8.configuration.Configuration;
import pl.kfirmanty.chip8.cpu.Registers;
import pl.kfirmanty.chip8.display.renderer.Renderer;
import pl.kfirmanty.chip8.helpers.ByteHelper;

public class Display {
	
	private boolean[][] display;
	private int width;
	private int height;
	private Renderer renderer;
	private Registers registers;
	
	public Display(){
		Configuration config = Configuration.getInstance();
		width = config.getDisplayWidth();
		height = config.getDisplayHeight();
		display = new boolean[width][height];
	}
	
	public Display(Registers registers){
		this();
		this.registers = registers;
	}
	
	public void setRenderer(Renderer renderer){
		this.renderer = renderer;
	}
	
	public Renderer getRenderer(){
		return renderer;
	}
	
	public void cls(){
		for(int i = 0; i < display.length; i++){
			for(int j = 0; j < display[i].length; j++){
				display[i][j] = false;
			}
		}
		renderer.clearDisplay();
	}
	
	public void update(){
		renderer.render(display);
	}
	
	public void loadSpriteFromMemory(int x, int y, int bytesCount){
		short addres = registers.getI();
		registers.setV((short)0, (short)0xF);
		for(int i = 0; i < bytesCount; i++){
			int yTemp = y + i;
			short sprite = registers.getMemoryContent(addres + i);
			boolean[] bits = ByteHelper.getBits(sprite);
			for(int j = 0; j < bits.length; j++){
				int xTemp = x + j;
				int xDisp = xTemp % width;
				int yDisp = yTemp % height;
				boolean oldPixelValue = display[xDisp][yDisp];
				boolean newPixelValue = oldPixelValue ^ bits[j];
				if(oldPixelValue && !newPixelValue){
					registers.setV((short)1, (short)0xF);
				}
				display[xDisp][yDisp] = newPixelValue;
			}
		}
	}
}
