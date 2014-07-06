package pl.kfirmanty.chip8.exception;


public class Chip8Exception extends Exception{

	public Chip8Exception(Exception e) {
		super(e);
	}
	
	public Chip8Exception(){
		super();
	}

	public Chip8Exception(String message) {
		super(message);
	}

	private static final long serialVersionUID = 226440534006921377L;

}
