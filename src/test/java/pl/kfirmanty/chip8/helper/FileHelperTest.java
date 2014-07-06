package pl.kfirmanty.chip8.helper;

import java.io.File;
import java.util.Arrays;

import pl.kfirmanty.chip8.exception.Chip8Exception;
import pl.kfirmanty.chip8.helpers.FileHelper;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class FileHelperTest extends TestCase{
	
	public FileHelperTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(FileHelperTest.class);
	}


	public void testApp() {
		try {
			short[] memory = FileHelper.readProgramMemoryFromFile("programs" + File.separator + "test.ch8");
			assertNotNull(memory);
			int sum = 0;
			for(int i = 0; i < memory.length; i++){
				sum += memory[i];
			}
			assertTrue(sum > 0);
		} catch (Chip8Exception e) {
			assertTrue(false);
		}
	}
}
