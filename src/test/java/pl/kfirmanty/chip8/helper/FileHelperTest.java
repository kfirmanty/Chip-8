package pl.kfirmanty.chip8.helper;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import pl.kfirmanty.chip8.cpu.Registers;
import pl.kfirmanty.chip8.exception.Chip8Exception;
import pl.kfirmanty.chip8.helpers.FileHelper;

public class FileHelperTest extends TestCase{
	private static final String TEST_FILE = "memory.bin";
	
	public FileHelperTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(FileHelperTest.class);
	}


	public void testLoadingProgramToArray() {
		try {
			short[] memory = FileHelper.readProgramMemoryFromFile("programs" + File.separator + "test.ch8");
			assertNotNull(memory);
			int sum = 0;
			for(int i = 0; i < memory.length; i++){
				sum += memory[i];
			}
			assertTrue(sum > 0);
		} catch (Chip8Exception e) {
			fail();
		}
	}
	
	public void testLoadingProgramToRegisters(){
		try {
			Registers registers = new Registers();
			registers.loadProgram(FileHelper.readProgramMemoryFromFile(TEST_FILE), (short)0);
			byte[] bytes = Files.readAllBytes(FileSystems.getDefault().getPath(TEST_FILE));
			for(int i = 0; i < bytes.length; i++){
				assertTrue(bytes[i] == (byte)registers.getMemoryContent(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
