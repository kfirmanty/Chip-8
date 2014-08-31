package pl.kfirmanty.chip8;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;

import pl.kfirmanty.chip8.configuration.Configuration;
import ddf.minim.ugens.Waves;
import ddf.minim.ugens.Wavetable;

public class SwingSettings extends JFrame {
	private static final long serialVersionUID = 126534889411860304L;
	private Configuration configuration;
	private JComboBox<String>[] wavesComboBoxes;
	private Wavetable[] possibleWaves = { Waves.SINE, Waves.SAW, Waves.SQUARE, Waves.TRIANGLE, Waves.QUARTERPULSE };
	private String[] wavesNames = { "SINE", "SAW", "SQUARE", "TRIANGLE", "QUARTERPULSE" };
	private JComboBox<Integer>[] volumeFields;
	private Integer[] volumeValues = { 0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100 };
	private static final int OSCILLATORS_NUMBER = 4;

	public SwingSettings() {
		this.setLayout(new GridLayout());
		configuration = Configuration.getInstance();
		addComboBoxesForWaves();
		addVolumeFields();
		setVisible(true);
		pack();
	}

	@SuppressWarnings("unchecked")
	private void addComboBoxesForWaves() {
		wavesComboBoxes = (JComboBox<String>[]) new JComboBox<?>[OSCILLATORS_NUMBER];
		for (int i = 0; i < wavesComboBoxes.length; i++) {
			wavesComboBoxes[i] = new JComboBox<String>(wavesNames);
			wavesComboBoxes[i].setSelectedIndex(0);
			final int comboIndex = i;
			wavesComboBoxes[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JComboBox<Wavetable> comboBox = (JComboBox<Wavetable>) e.getSource();
					configuration.getOscillatorsShapes()[comboIndex] = possibleWaves[comboBox.getSelectedIndex()];
				}

			});
			wavesComboBoxes[i].setLocation(100, i * 100);
			wavesComboBoxes[i].setSize(100, 80);
			add(wavesComboBoxes[i]);
		}
	}

	private void addVolumeFields() {
		volumeFields = (JComboBox<Integer>[]) new JComboBox<?>[OSCILLATORS_NUMBER];
		for (int i = 0; i < OSCILLATORS_NUMBER; i++) {
			volumeFields[i] = new JComboBox<Integer>(volumeValues);
			volumeFields[i].setLocation(300, i * 100);
			volumeFields[i].setSize(40, 80);
			volumeFields[i].setSelectedIndex(2);
			final int textFieldIndex = i;
			volumeFields[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int volume = (Integer) ((JComboBox<Integer>) e.getSource()).getSelectedItem();
					configuration.getAmplitudes()[textFieldIndex] = volume;
				}
			});
			add(volumeFields[i]);
		}
	}
}
