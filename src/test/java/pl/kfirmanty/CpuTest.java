package pl.kfirmanty;

import pl.kfirmanty.chip8.cpu.Cpu;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class CpuTest extends TestCase {
	Cpu cpu;
	public CpuTest(String testName) {
		super(testName);
		//cpu = new Cpu();
	}

	public static Test suite() {
		return new TestSuite(CpuTest.class);
	}
	
	public void testRET(){
		
	}
}
