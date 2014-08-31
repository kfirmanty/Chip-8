package pl.kfirmanty.chip8.helpers;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import pl.kfirmanty.chip8.exception.Chip8Exception;


public class FileHelper {
	private static Logger logger = Logger.getLogger(FileHelper.class.getSimpleName());
	
	public static short[] readProgramMemoryFromFile(String fileName) throws Chip8Exception{
		short[] memory = null;
		Path path = FileSystems.getDefault().getPath("", fileName);
		logger.info("Trying to load file: " + path.toAbsolutePath());
		try {
			byte[] fileData = Files.readAllBytes(path);
			memory = new short[fileData.length];
			for(int i = 0; i < fileData.length; i++){
				memory[i] = (short) (fileData[i] & 0xFF);
			}
			logger.info("Loaded file " + fileName);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "", e);
			throw new Chip8Exception(e);
		}
		return memory;
	}
}
