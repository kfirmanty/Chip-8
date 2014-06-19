package pl.kfirmanty.chip8;

import pl.kfirmanty.chip8.configuration.Configuration;
import processing.core.PApplet;



public class Main extends PApplet{

	private static final long serialVersionUID = -3651935202343786554L;
	
	@Override
	public void setup(){
		Configuration configuration;
		configuration = Configuration.getInstance();
		size(configuration.getWindowWidth(), configuration.getWindowHeight());
	}
	
	@Override
	public void draw(){
		
	}

	public static void main(String[] args){
		PApplet.main("pl.kfirmanty.chip8.Main", args);
	}
}
