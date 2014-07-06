package pl.kfirmanty;

import java.nio.file.FileSystems;
import java.nio.file.Files;

import org.junit.Assert;
import org.junit.Test;

import pl.kfirmanty.chip8.cpu.Registers;
import pl.kfirmanty.chip8.helpers.FileHelper;

public class LoadProgramTest {
	private static final String TEST_FILE = "memory.bin";
	@Test
	public void loadProgramTest(){
		try {
			Registers registers = new Registers();
			registers.loadProgram(FileHelper.readProgramMemoryFromFile(TEST_FILE), (short)0);
			byte[] bytes = Files.readAllBytes(FileSystems.getDefault().getPath(TEST_FILE));
			for(int i = 0; i < bytes.length; i++){
				Assert.assertTrue(bytes[i] == (byte)registers.getMemoryContent(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	
}
