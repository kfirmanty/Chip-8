package pl.kfirmanty.synesthesia;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import pl.kfirmanty.chip8.configuration.Configuration;
import ddf.minim.AudioOutput;
import ddf.minim.Minim;
import ddf.minim.ugens.ADSR;
import ddf.minim.ugens.Delay;
import ddf.minim.ugens.Oscil;
import ddf.minim.ugens.Wavetable;

public class Synesthesia {
	private Minim minim;

	private List<Oscil> oscillators;
	private List<ADSR> adsrs;
	private List<Delay> delays;
	private float[] notesFreq = { 440.f // A
			, 587.33f // D
			, 659.26f // E
			, 783.99f // G
	};

	public Synesthesia() {
		minim = new Minim(new Object() {
			@SuppressWarnings("unused")
			String sketchPath(String fileName) {
				return "";
			}

			@SuppressWarnings("unused")
			InputStream createInput(String fileName) {
				try {
					return new FileInputStream(fileName);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return null;
				}
			}
		});
		oscillators = new ArrayList<>();
		adsrs = new ArrayList<>();
		delays = new ArrayList<>();
		createInstrument();
	}

	public void createInstrument() {		
		Wavetable[] oscillatorsShapes = Configuration.getInstance().getOscillatorsShapes();
		float[] adsrAmplitudes = Configuration.getInstance().getAmplitudesForADSR();
		
		AudioOutput out = minim.getLineOut();
		
		for(int i = 0; i < 4; i++){
			Oscil oscil = new Oscil(440, 0.5f, oscillatorsShapes[i]);
			ADSR adsr = new ADSR(adsrAmplitudes[i], 0.01f, 0.05f, 0.5f, 0.5f);
			oscil.patch(adsr);
			Delay delay = new Delay(0.4f, 0.5f, true, true);
			adsr.patch(delay).patch(out);
			oscillators.add(oscil);
			adsrs.add(adsr);
			delays.add(delay);
		}
	}

	public void changeFrequency(short[] opcodeNibbles) {
		for (int i = 0; i < oscillators.size(); i++) {
			Oscil oscillator = oscillators.get(i);
			int note = (int) (opcodeNibbles[i] % notesFreq.length);
			oscillator.reset();
			int octave = i + 1;
			oscillator.setFrequency(notesFreq[note] * octave);
		}
	}

	public void playSound() {
		for (ADSR adsr : adsrs) {
			adsr.noteOn();
		}
	}

	public void stopSound() {
		for (ADSR adsr : adsrs) {
			adsr.noteOff();
		}
	}

	public List<Oscil> getOscillators() {
		return oscillators;
	}

	public List<ADSR> getAdsrs() {
		return adsrs;
	}

	public List<Delay> getDelays() {
		return delays;
	}
}
