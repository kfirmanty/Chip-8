package pl.kfirmanty.chip8.cpu;

import pl.kfirmanty.chip8.controller.Controller;
import pl.kfirmanty.chip8.controller.KeyboardController;
import pl.kfirmanty.chip8.display.Display;
import pl.kfirmanty.chip8.exception.Chip8Exception;
import pl.kfirmanty.chip8.helpers.ByteHelper;
import syntesthesia.Synesthesia;

import com.sun.istack.internal.logging.Logger;

public class Cpu {
	private static Logger logger = Logger.getLogger(Cpu.class);
	private Registers registers;
	private Display display;
	private Synesthesia synesthesia;
	private Controller controller;
	private boolean idle;
	
	private short registerToWriteKey;
	
	public Cpu(){
		registers = new Registers();
		display = new Display(registers);
		synesthesia = new Synesthesia();
		controller = new KeyboardController();
		idle = false;
	}
	
	public void setIdle(boolean idle){
		this.idle = idle;
	}
	
	public void update() throws Chip8Exception{
		if(idle){
			if(controller.getLastPressedKey() != null){
				setIdle(false);
				registers.setV(controller.getLastPressedKey(), registerToWriteKey);
			}
			return;
		}
		short opcode = registers.fetchNextOpcode();
		decodeOpcode(opcode);
		registers.updateTimers();
		synesthesia.changeFrequency(ByteHelper.getNibbles(opcode));
		synesthesia.playSound();
	}
	
	//FIXME:Increase readibility
	public void decodeOpcode(short opcode) throws Chip8Exception{
		short[] opcodeNibbles = ByteHelper.getNibbles(opcode);
		short value = 0;
		
		switch(opcodeNibbles[0]){
			case 0:{ //0xxx
				if(opcode == 0x00E0){
					display.cls();
				}else if(opcode == 0x00EE){
					registers.setProgramCounter(registers.popFromStack());
				}
			}
			break;
			case 1: //1xxx
				registers.setProgramCounter((short)(opcode & 0xFFF));
				break;
			case 2: //2xxx
				registers.addToStack(registers.getProgramCounter());
				registers.setProgramCounter((short)(opcode & 0xFFF));
				break;
			case 3:
			case 4: //3xxx AND 4xxx
				value = (short)(opcode & 0xFF);
				if(registers.getV(opcodeNibbles[1]) == value && opcodeNibbles[0] == 3){
					registers.skipNextOpcode();
				}else if(registers.getV(opcodeNibbles[1]) != value && opcodeNibbles[0] == 4){
					registers.skipNextOpcode();
				}
				break;
			case 5: //5xxx
				if(registers.getV(opcodeNibbles[1]) == registers.getV(opcodeNibbles[2])){
					registers.skipNextOpcode();
				}
				break;
			case 6: //6xxx
				value = (short)(opcode & 0xFF);
				registers.setV(value, opcodeNibbles[1]);
				break;
			case 7: //7xxx
				value = (short)((opcode & 0xFF) + registers.getV(opcodeNibbles[1]));
				registers.setV(value, opcodeNibbles[1]);
				break;
			case 8://8xxx
				short vX = registers.getV(opcodeNibbles[1]);
				short vY = registers.getV(opcodeNibbles[2]);
				short vF = 0;
				value = 0;
				switch (opcodeNibbles[3]){
					case 0:
						registers.setV(vY, opcodeNibbles[1]);
						break;
					case 1:
						registers.setV((short)(vX | vY), opcodeNibbles[1]);
						break;
					case 2:
						registers.setV((short)(vX & vY), opcodeNibbles[1]);
						break;
					case 3:
						registers.setV((short)(vX ^ vY), opcodeNibbles[1]);
						break;
					case 4:
						value = (short) (vX + vY);
						vF = 0;
						if(value > 0xFF){
							vF = 1;
						}
						registers.setV(vF, (short)0xF);
						registers.setV(value, opcodeNibbles[1]);
						break;
					case 5:
						vF = 0;
						value = (short) (vX - vY);
						if(vX > vY){
							vF = 1;
						}
						registers.setV(vF, (short)0xF);
						registers.setV(value, opcodeNibbles[1]);
						break;
					case 6:
						vF = 0;
						value = (short) (vX/vY);
						if((vX & 0x1) == 1){
							vF = 1;
						}
						registers.setV(vF, (short)0xF);
						registers.setV(value, opcodeNibbles[1]);
						break;
					case 7:
						vF = 0;
						value = (short) (vY - vX);
						if(vY > vX){
							vF = 1;
						}
						registers.setV(vF, (short)0xF);
						registers.setV(value, opcodeNibbles[1]);
						break;
					case 0xE:
						vF = 0;
						value = (short) (vX * 2);
						if((vX & 0b10000000) == 0b10000000){
							vF = 1;
						}
						registers.setV(vF, (short)0xF);
						registers.setV(value, opcodeNibbles[1]);
						break;
				}
				break;
			case 9://9xxx
				if(registers.getV(opcodeNibbles[1]) != registers.getV(opcodeNibbles[2])){
					registers.skipNextOpcode();
				}
			break;
			case 0xA:
				registers.setI((short)(opcode & 0xFFF));
				break;
			case 0xB:
				value = (short) (registers.getV(0) + (short)(opcode & 0xFFF));
				registers.setProgramCounter(value);
				break;
			case 0xC:
				short random = (short)(Math.random()*256);
				value = (short)(random & (opcode & 0xFF));
				registers.setV(value, opcodeNibbles[1]);
				break;
			case 0xD:
				display.loadSpriteFromMemory(registers.getV(opcodeNibbles[1]), registers.getV(opcodeNibbles[2]), opcodeNibbles[3]);
				break;
			case 0xE:
				if(opcodeNibbles[2] == 0x9 && opcodeNibbles[3] == 0xE){
					if(controller.isKeyPressed(opcodeNibbles[1])){
						registers.skipNextOpcode();
					}
				} else if(opcodeNibbles[2] == 0xA && opcodeNibbles[3] == 0x1){
					if(controller.isKeyReleased(opcodeNibbles[1])){
						registers.skipNextOpcode();
					}
				}
				break;
			case 0xF:
				if(opcodeNibbles[2] == 0x0 && opcodeNibbles[3] == 0x7){
					registers.setV(registers.getDelayTimer(), opcodeNibbles[1]);
				}else if(opcodeNibbles[2] == 0x0 && opcodeNibbles[3] == 0xA){//WAIT FOR KEY PRESS
					setIdle(true);
					registerToWriteKey = opcodeNibbles[1];
				}else if(opcodeNibbles[2] == 0x1 && opcodeNibbles[3] == 0x5){
					registers.setDelayTimer(registers.getV(opcodeNibbles[1]));
				}else if(opcodeNibbles[2] == 0x1 && opcodeNibbles[3] == 0x8){
					registers.setSoundTimer(registers.getV(opcodeNibbles[1]));
				}else if(opcodeNibbles[2] == 0x1 && opcodeNibbles[3] == 0xE){
					registers.setI((short)(registers.getI() + registers.getV(opcodeNibbles[1])));
				}else if(opcodeNibbles[2] == 0x2 && opcodeNibbles[3] == 0x9){
					registers.setIToAddresOfDigitSprite(opcodeNibbles[1]);
				}else if(opcodeNibbles[2] == 0x3 && opcodeNibbles[3] == 0x3){
					short val = opcodeNibbles[1];
					short decim = (short) (val % 10);
					val = (short) (val/10);
					short tenths = (short) (val % 10);
					val = (short) (opcodeNibbles[0]/10);
					short hundreths = (short) (val % 10);
					registers.setMemoryContent(hundreths, registers.getI());
					registers.setMemoryContent(tenths, (short) (registers.getI() + 1));
					registers.setMemoryContent(decim, (short) (registers.getI() + 2));
				}else if(opcodeNibbles[2] == 0x5 && opcodeNibbles[3] == 0x5){
					for(int i = 0; i < 0x10; i++){
						short registerVal = registers.getV(i);
						registers.setMemoryContent(registerVal, (short) (registers.getI() + i));
					}
				}else if(opcodeNibbles[2] == 0x6 && opcodeNibbles[3] == 0x5){
					for(int i = 0; i < 0x10; i++){
						short registerVal = registers.getMemoryContent(registers.getI() + i);
						registers.setV(registerVal, (short) i);
					}
				}
				break;
			default:
				logger.warning("Unknown opcode: " + opcode);
				break;
				
		}
	}
	
	public Registers getRegisters(){
		return registers;
	}
	
	public Display getDisplay(){
		return display;
	}

	public Controller getController() {
		return controller;
	}
}
