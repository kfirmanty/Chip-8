package pl.kfirmanty.chip8;

import java.io.File;
import java.util.Map;

import pl.kfirmanty.chip8.configuration.Configuration;
import pl.kfirmanty.chip8.controller.Controller;
import pl.kfirmanty.chip8.cpu.Cpu;
import pl.kfirmanty.chip8.cpu.Registers;
import pl.kfirmanty.chip8.display.renderer.ProcessingRenderer;
import pl.kfirmanty.chip8.exception.Chip8Exception;
import pl.kfirmanty.chip8.helpers.FileHelper;
import processing.core.PApplet;

import com.sun.istack.internal.logging.Logger;



public class Main extends PApplet{

	private static final long serialVersionUID = -3651935202343786554L;
	private static Logger logger = Logger.getLogger(Main.class);
	private Cpu cpu;
	private Controller controller;
	
	@Override
	public void setup(){
		Configuration configuration;
		configuration = Configuration.getInstance();
		size(configuration.getWindowWidth(), configuration.getWindowHeight());
		selectInput("Select a file to process:", "initEmulator");
		frameRate(2f);
	}
	
	@Override
	public void draw(){
		try{
			if(cpu != null){
				cpu.update();
				cpu.getDisplay().update();
			}
		} catch(Chip8Exception e){
			logger.severe("", e);
		}
	}

	public static void main(String[] args){
		PApplet.main("pl.kfirmanty.chip8.Main", args);
	}
	
	private void initController(){
		Map<Character, Short> scheme = controller.getKeyScheme();
		scheme.put('1', (short) 1);
		scheme.put('2', (short) 2);
		scheme.put('3', (short) 3);
		scheme.put('4', (short) 0xC);
		
		scheme.put('q', (short) 4);
		scheme.put('w', (short) 5);
		scheme.put('e', (short) 6);
		scheme.put('r', (short) 0xD);
		
		scheme.put('a', (short) 7);
		scheme.put('s', (short) 8);
		scheme.put('d', (short) 9);
		scheme.put('f', (short) 0xE);
		
		scheme.put('z', (short) 0xA);
		scheme.put('x', (short) 0x0);
		scheme.put('c', (short) 0xB);
		scheme.put('v', (short) 0xF);
	}

	@Override
	public void keyPressed() {
		controller.pressKey(key);
	}

	@Override
	public void keyReleased() {
		controller.releaseKey(key);
	}
	
	public void initEmulator(File file){
		try {
			cpu = new Cpu();
			Registers registers = cpu.getRegisters();
			cpu.getDisplay().setRenderer(new ProcessingRenderer(this));
			registers.loadProgram(FileHelper.readProgramMemoryFromFile(file.getAbsolutePath()), (short) 0x200);
			registers.setProgramCounter((short) 0x200);
			controller = cpu.getController();
			initController();
		} catch (Chip8Exception e) {
			logger.severe("", e);
			System.exit(-1);
		}
	}
}
