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
		short firstPart = getOpcodePart(opcode, 3); 
		short secondPart = getOpcodePart(opcode, 2);
		short thirdPart = getOpcodePart(opcode, 1);
		short fourthPart = getOpcodePart(opcode, 0);
		//Formatting convetion for if/else if other than in rest of the code to increase visual separation
		if(firstPart == 0){ //0xxx
			if(opcode == 0x00E0){
				display.cls();
			}else if(opcode == 0x00EE){
				registers.setProgramCounter(registers.popFromStack());
			}
		} 
		else if(firstPart == 1){ //1xxx
			registers.setProgramCounter((short)(opcode & 0xFFF));
		}
		else if(firstPart == 2){ //2xxx
			registers.addToStack(registers.getProgramCounter());
			registers.setProgramCounter((short)(opcode & 0xFFF));
		}
		else if(firstPart == 3 || firstPart == 4){ //3xxx AND 4xxx
			short value = (short)(opcode & 0xFF);
			if(registers.getV(secondPart) == value && firstPart == 3){
				registers.skipNextOpcode();
			}else if(registers.getV(secondPart) != value && firstPart == 4){
				registers.skipNextOpcode();
			}
		}
		else if(firstPart == 5){ //5xxx
			if(registers.getV(secondPart) == registers.getV(thirdPart)){
				registers.skipNextOpcode();
			}
		}
		else if(firstPart == 6){ //6xxx
			short value = (short)(opcode & 0xFF);
			registers.setV(value, secondPart);
		}
		else if(firstPart == 7){ //7xxx
			short value = (short)((opcode & 0xFF) + registers.getV(secondPart));
			registers.setV(value, secondPart);
		}
		else if(firstPart == 8){//8xxx
			short vX = registers.getV(secondPart);
			short vY = registers.getV(thirdPart);
			short vF = 0;
			short value = 0;
			switch (fourthPart){
				case 0:
					registers.setV(vY, secondPart);
					break;
				case 1:
					registers.setV((short)(vX | vY), secondPart);
					break;
				case 2:
					registers.setV((short)(vX & vY), secondPart);
					break;
				case 3:
					registers.setV((short)(vX ^ vY), secondPart);
					break;
				case 4:
					value = (short) (vX + vY);
					vF = 0;
					if(value > 0xFF){
						vF = 1;
					}
					registers.setVf(vF);
					registers.setV(value, secondPart);
					break;
				case 5:
					vF = 0;
					value = (short) (vX - vY);
					if(vX > vY){
						vF = 1;
					}
					registers.setVf(vF);
					registers.setV(value, secondPart);
					break;
			}
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
