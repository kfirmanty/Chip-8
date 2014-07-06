package pl.kfirmanty.chip8.helpers;

public class ByteHelper {
	
	public static short[] getNibbles(short number){
		short[] numberNibbles = new short[4];
		for(int i = 0; i < numberNibbles.length; i++){
			numberNibbles[i] = getNumberPart(number, numberNibbles.length - 1 - i); 
		}
		return numberNibbles;
	}
	
	public static short getNumberPart(short number, int index){ // Counted from right, starting from 0
		return (short)((number >> 4 * index) & 0xF);	
	}
	
	public static boolean[] getBits(short number){
		boolean[] bits = new boolean[8];
		for(int i = 0; i < bits.length; i++){
			bits[bits.length - 1 - i] = (((number >> i ) & 0b1) == 0b1) ? true : false;
		}
		return bits;
	}
}
