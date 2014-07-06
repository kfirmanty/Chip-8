package syntesthesia;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ddf.minim.AudioOutput;
import ddf.minim.Minim;
import ddf.minim.ugens.ADSR;
import ddf.minim.ugens.Delay;
import ddf.minim.ugens.Oscil;
import ddf.minim.ugens.Waves;

public class Synesthesia {
	private Minim minim;
	private ADSR adsr;
	private ADSR adsr2;
	private ADSR adsr3;
	private ADSR adsr4;
	private Oscil sineOsc;
	private Oscil sawOsc;
	private List<Oscil> oscillators;
	private float[] notesFreq = { 440.f			//A
									,587.33f	//D
	                           		,659.26f	//E
	                           		,783.99f	//G
								};
			
	
	public Synesthesia(){
		minim = new Minim(new Object(){
			@SuppressWarnings("unused")
			String sketchPath( String fileName ){
				return "";
			}
			@SuppressWarnings("unused")
			InputStream createInput( String fileName ){
				try {
					return new FileInputStream(fileName);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return null;
				}
			}
		});
		oscillators = new ArrayList<>();
		createInstrument();
	}
	
	public void createInstrument(){
		AudioOutput out = minim.getLineOut();
	    sineOsc = new Oscil( 440, 0.5f, Waves.SINE );
	    adsr = new ADSR( 0.5f, 0.01f, 0.05f, 0.5f, 0.5f );
	    sineOsc.patch( adsr );
		Delay delay = new Delay( 0.4f, 0.5f, true, true );
		adsr.patch(delay).patch(out);
		
	    sawOsc = new Oscil( 440, 0.5f, Waves.SAW );
	    adsr2 = new ADSR( 0.4f, 0.01f, 0.05f, 0.5f, 0.5f );
	    sawOsc.patch( adsr2 );
	    Delay delay2 = new Delay( 0.4f, 0.5f, true, true );
		adsr2.patch(delay2).patch(out);
		
	    Oscil sineOsc2 = new Oscil( 440, 0.5f, Waves.SINE );
	    adsr3 = new ADSR( 0.5f, 0.01f, 0.05f, 0.5f, 0.5f );
	    sineOsc2.patch( adsr3 );
	    Delay delay3 = new Delay( 0.4f, 0.5f, true, true );
		adsr3.patch(delay3).patch(out);
		
	    Oscil sineOsc3 = new Oscil( 440, 0.5f, Waves.SINE );
	    adsr4 = new ADSR( 0.5f, 0.01f, 0.05f, 0.5f, 0.5f );
	    sineOsc3.patch( adsr4 );
	    Delay delay4 = new Delay( 0.4f, 0.5f, true, true );
		adsr4.patch(delay4).patch(out);
		
		oscillators.add(sawOsc);
		oscillators.add(sineOsc);
		oscillators.add(sineOsc2);
		oscillators.add(sineOsc3);
	}
	
	public void changeFrequency(){
		int note = (int) (Math.random() * notesFreq.length);
		sineOsc.reset();
		int octave = (int) (Math.random() * 3 + 1);
		sineOsc.setFrequency(notesFreq[note] * octave);
	}
	
	public void changeFrequency(short[] opcodeNibbles){
		for(int i = 0; i < oscillators.size(); i++){
			Oscil oscillator = oscillators.get(i);
			int note = (int) (opcodeNibbles[i] % notesFreq.length);
			oscillator.reset();
			int octave = i + 1;
			oscillator.setFrequency(notesFreq[note] * octave);
		}
	}
	
	public void playSound(){
		//changeFrequency();
		adsr.noteOn();
		adsr2.noteOn();
		adsr3.noteOn();
		adsr4.noteOn();
	}
	
	public void stopSound(){
		adsr.noteOff();
	}
}
