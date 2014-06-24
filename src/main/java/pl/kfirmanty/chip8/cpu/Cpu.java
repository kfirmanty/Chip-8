package pl.kfirmanty.chip8.cpu;

import pl.kfirmanty.chip8.display.Display;

public class Cpu {
	Registers registers;
	Display display;
	public Cpu(){
		registers = new Registers();
		display = new Display();
	}
	
	public void update(){
		short opcode = registers.fetchNextOpcode();
		decodeOpcode(opcode);
		registers.updateTimers();
	}
	
	//FIXME:Increase readibility
	public void decodeOpcode(short opcode){
		short[] opcodeNibbles = new short[4];
		opcodeNibbles[0] = getOpcodePart(opcode, 3); 
		opcodeNibbles[1] = getOpcodePart(opcode, 2);
		opcodeNibbles[2] = getOpcodePart(opcode, 1);
		opcodeNibbles[3] = getOpcodePart(opcode, 0);
		//Formatting convetion for if/else if other than in rest of the code to increase visual separation
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
						registers.setVf(vF);
						registers.setV(value, opcodeNibbles[1]);
						break;
					case 5:
						vF = 0;
						value = (short) (vX - vY);
						if(vX > vY){
							vF = 1;
						}
						registers.setVf(vF);
						registers.setV(value, opcodeNibbles[1]);
						break;
					case 6:
						vF = 0;
						value = (short) (vX/vY);
						if((vX & 0x1) == 1){
							vF = 1;
						}
						registers.setVf(vF);
						registers.setV(value, opcodeNibbles[1]);
						break;
					case 7:
						vF = 0;
						value = (short) (vY - vX);
						if(vY > vX){
							vF = 1;
						}
						registers.setVf(vF);
						registers.setV(value, opcodeNibbles[1]);
						break;
					case 0xE:
						vF = 0;
						value = (short) (vX * 2);
						if((vX & 0b10000000) == 0b10000000){
							vF = 1;
						}
						registers.setVf(vF);
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
		}
	}
	
	public short getOpcodePart(short opcode, int index){ // Counted from right, starting from 0
		return (short)((opcode >> 4 * index) & 0xF);	
	}
	
	public Registers getRegisters(){
		return registers;
	}
	
	public Display getDisplay(){
		return display;
	}
}
