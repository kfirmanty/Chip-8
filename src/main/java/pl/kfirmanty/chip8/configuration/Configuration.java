package pl.kfirmanty.chip8.configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.logging.Level;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sun.istack.internal.logging.Logger;

public class Configuration {
	private static Logger logger = Logger.getLogger(Configuration.class);
	
	private static Configuration configuration;
	
	private static String BASE_DIR = System.getProperty("user.dir")+File.separator;
	private static String CONFIGURATION_FILE_NAME = "chip8.cfg";
	
	private static final int DISPLAY_WIDTH = 64;
	private static final int DISPLAY_HEIGHT = 32;
	
	private static final int DISPLAY_CHIP_48 = 2;
	
	private static final String SCALE = "scale";
	private static final int DEFAULT_SCALE = 10;
	private static final String IS_CHIP_48 = "isChip48";
	
	
	
	private JsonObject configurationJson;
	private int scale;
	private boolean isChip48;
	
	public static Configuration getInstance(){
		if(configuration == null){
			configuration = new Configuration();
		}
		return configuration;
	}
	
	private Configuration(){
		init();
	}
	
	private void init(){
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(BASE_DIR + CONFIGURATION_FILE_NAME));
			
			configurationJson = gson.fromJson(reader, JsonObject.class);
			reader.close();
			scale = configurationJson.get(SCALE).getAsInt();
			isChip48 = configurationJson.get(IS_CHIP_48).getAsBoolean();
		} catch(Exception e){
			logger.log(Level.WARNING, "Config file not found, initializing with default values", e);
			initDefault();
		}
		
	}
	
	private void initDefault(){
		scale = DEFAULT_SCALE;
		isChip48 = false;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}
	
	public int getWindowWidth(){
		return getDisplayWidth() * scale;
	}
	
	public int getWindowHeight(){
		return getDisplayHeight() * scale;
	}
	
	public int getDisplayWidth(){
		int width;
		if(isChip48){
			width = DISPLAY_WIDTH * DISPLAY_CHIP_48;
		}else{
			width = DISPLAY_WIDTH;
		}
		return width;
	}
	
	public int getDisplayHeight(){
		int width;
		if(isChip48){
			width = DISPLAY_HEIGHT * DISPLAY_CHIP_48;
		}else{
			width = DISPLAY_HEIGHT;
		}
		return width;
	}
}
