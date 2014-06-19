package pl.kfirmanty;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ByteCalcTest extends TestCase {

	public ByteCalcTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(ByteCalcTest.class);
	}


	public void testApp() {
		byte left = (byte) 0b11111111;
		byte right = (byte)0b11111111;

		assertTrue((((left << 8)& 0xFF00) | (right & 0xFF)) == 65535);
		
		short check = 0x2ABC;
		
		assertTrue(((check >> 12) & 0xF) == 2);
		assertTrue(((check >> 8) & 0xF) == 0xA);
		assertTrue(((check >> 4) & 0xF) == 0xB);
		assertTrue((check & 0xF) == 0xC);
	}
}
