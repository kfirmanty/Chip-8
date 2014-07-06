package pl.kfirmanty.chip8.cpu;

import java.util.Arrays;
import java.util.Stack;

import pl.kfirmanty.chip8.exception.Chip8Exception;
import pl.kfirmanty.chip8.helpers.FileHelper;

public class Registers {
	private static final int PROGRAM_COUNTER_STEP = 2;
	
	private Stack<Short> stack;
	private short[] memory = new short[0x1000];
	private short[] v = new short[0x10];
	private short i = 0;
	
	private short delayTimer = 0;
	private short soundTimer = 0;
	
	private short programCounter = 0;
	
	public Registers(){
		stack = new Stack<>();
	}
	
	public void loadProgram(short[] program, short offset){
		for(short i = 0; i < program.length; i++){
			memory[offset + i] = program[i];
		}
	}
	
	public void reset() throws Chip8Exception{
		Arrays.fill(memory, (short)0);
		Arrays.fill(v, (short)0);
		i = 0;
		delayTimer = 0;
		soundTimer = 0;
		programCounter = 0;
		initMemory();
	}
	
	public void initMemory() throws Chip8Exception{
		loadProgram(FileHelper.readProgramMemoryFromFile("memory.bin"), (short) 0);
	}
	
	public void addToStack(short addr) throws Chip8Exception{
		if(stack.size() <= 0x10){
			stack.add(addr);
		}else{
			throw new Chip8Exception("Trying to push element to full stack");
		}
	}
	
	public short popFromStack() throws Chip8Exception{
		if(stack.size() > 0){
			return stack.pop();
		}else{
			throw new Chip8Exception("Trying to pop from empty stack");
		}
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
		this.i = (short)(i & 0xFFFF);
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
	
	public short getMemoryContent(int addres){
		return memory[addres];
	}

	public short getDelayTimer() {
		return delayTimer;
	}

	public short getSoundTimer() {
		return soundTimer;
	}
	
	public void setIToAddresOfDigitSprite(short digit){
		setI((short) (digit * 5));
	}
	
	public void setMemoryContent(short value, short index){
		memory[index] = (short)(value & 0xFF);
	}
	
}
