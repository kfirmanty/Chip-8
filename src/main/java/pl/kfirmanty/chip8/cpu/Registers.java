package pl.kfirmanty.chip8.cpu;

import java.util.Arrays;
import java.util.Stack;

public class Registers {
	private static final int PROGRAM_COUNTER_STEP = 2;
	
	private short[] memory = new short[0x1000];
	private short[] v = new short[0x10];
	private short i = 0;
	private short vf = 0;
	
	private short delayTimer = 0;
	private short soundTimer = 0;
	
	private short programCounter = 0;
	
	public Registers(){
		reset();
	}
	
	public void loadProgram(short[] program, short offset){
		for(short i = offset; i < program.length; i++){
			memory[offset + i] = program[i];
		}
	}
	
	public void reset(){
		Arrays.fill(memory, (short)0);
		Arrays.fill(v, (short)0);
		i = 0;
		vf = (short)0;
		delayTimer = 0;
		soundTimer = 0;
		programCounter = 0;
		initMemory();
	}
	
	public void initMemory(){
		
	}
	
	private Stack<Short> stack;
	
	public void addToStack(short addr){
		if(stack.size() <= 0x10){
			stack.add(addr);
		}
	}
	
	public short popFromStack(){
		if(stack.size() <= 0){
			
		}
		return stack.pop();
	}
	
	public void updateTimers(){
		if(delayTimer > 0) delayTimer--;
		if(soundTimer > 0) soundTimer--;
	}
	
	public boolean isDelay(){
		return delayTimer > 0;
	}
	
	public boolean makeSound(){
		return soundTimer > 0;
	}
	
	public short fetchNextOpcode(){
		short opcodeLeft = memory[programCounter++];
		short opcodeRight = memory[programCounter++];
		
		short opcode = (short) (((opcodeLeft << 8) & 0xFF00) | (opcodeRight & 0xFF));
		return opcode;
	}
	
	public void setProgramCounter(short programCounter){
		this.programCounter = programCounter;
	}
	
	public short getProgramCounter(){
		return programCounter;
	}
	
	public void setV(short value, short index){
		v[index] = (short)(value & 0xFF); //TODO:Technicaly the register Vx value should be 8 bit so value overflow mechanism should be considered
	}
	public short getV(int index){
		return v[index];
	}

	public short getI() {
		return i;
	}

	public void setI(short i) {
		this.i = i;
	}

	public short getVf() {
		return vf;
	}

	public void setVf(short vf) {
		this.vf = vf;
	}

	public void setDelayTimer(short delayTimer) {
		this.delayTimer = delayTimer;
	}

	public void setSoundTimer(short soundTimer) {
		this.soundTimer = soundTimer;
	}
	
	public void skipNextOpcode(){
		programCounter += PROGRAM_COUNTER_STEP;
	}
	
}
