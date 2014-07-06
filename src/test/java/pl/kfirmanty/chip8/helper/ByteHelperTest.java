package pl.kfirmanty.chip8.helper;

import static org.junit.Assert.*;

import org.junit.Test;

import pl.kfirmanty.chip8.helpers.ByteHelper;

public class ByteHelperTest {

	@Test
	public void getBitsTest() {
		byte number = (byte)0b11001010;
		boolean[] bits = ByteHelper.getBits(number);
		assertTrue(bits[0] == true);
		assertTrue(bits[1] == true);
		assertTrue(bits[2] == false);
		assertTrue(bits[3] == false);
		
		assertTrue(bits[4] == true);
		assertTrue(bits[5] == false);
		assertTrue(bits[6] == true);
		assertTrue(bits[7] == false);
	}
	
	@Test
	public void getNibblesTest() {
		short number = (short)0xFA03;
		short[] nibbles = ByteHelper.getNibbles(number);
		assertTrue(nibbles[0] == 0xF);
		assertTrue(nibbles[1] == 0xA);
		assertTrue(nibbles[2] == 0x0);
		assertTrue(nibbles[3] == 0x3);
	}
	
	@Test
	public void byteCalcTest() {
		byte left = (byte) 0b11111111;
		byte right = (byte)0b11111111;

		assertTrue((((left << 8)& 0xFF00) | (right & 0xFF)) == 0xFFFF);
		
		short check = 0x2ABC;
		
		assertTrue(((check >> 12) & 0xF) == 2);
		assertTrue(((check >> 8) & 0xF) == 0xA);
		assertTrue(((check >> 4) & 0xF) == 0xB);
		assertTrue((check & 0xF) == 0xC);
	}
	
	@Test
	public void convertFromByteToShort(){
		byte val = (byte) 0b11111111;
		short shortVal = (short) (val & 0xFF);
		
		boolean[] valBits = ByteHelper.getBits(val);
		boolean[] shortValBits = ByteHelper.getBits(shortVal);
		
		for(int i = 0; i < valBits.length; i++){
			assertTrue(valBits[i] == shortValBits[i]);
		}
	}
}
