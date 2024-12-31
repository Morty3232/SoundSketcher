package assign11;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.event.ChangeEvent;

import java.awt.*;
import java.awt.event.*;

/**
 * The TrackPanel class provides a graphical user interface for editing and managing 
 * individual tracks in a multi-track audio sequencer. It allows users to control track 
 * properties such as volume, instrument, and length, as well as mute or unmute the track.
 */
public class TrackPanel extends SketchingPanel implements ActionListener, ChangeListener {
	private TrackEditor trackEditor;
	private JToggleButton muteButton;
	private JSpinner lengthSpinner;
	private JSlider volumeSlider;
	private JComboBox<String> instrumentBox;
	private int trackNumber;
	private int instrumentNumber;
	private JLabel lengthLabel, volumeLabel, instrumentLabel;
	private SimpleSynthesizer simpleSynth;
	
	/**
     * Constructs a new TrackPanel with the specified dimensions and track number.
     * The panel includes controls for volume, instrument selection, length adjustment, 
     * and mute/unmute functionality. It also features a track editor for audio events.
     *
     * @param width       the width of the panel in pixels
     * @param height      the height of the panel in pixels
     * @param trackNumber the number of the track represented by this panel
     */
	public TrackPanel(int width, int height, int trackNumber, SimpleSynthesizer simpleSynth) {
		setLayout(new BorderLayout());
		
		this.trackNumber = trackNumber;
		this.trackEditor = new TrackEditor(width, height, trackNumber, simpleSynth);
		
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		
		muteButton = new JToggleButton("Mute/Unmute");
		muteButton.addActionListener(this);
		controlPanel.add(muteButton);
		
		lengthLabel = new JLabel("Track Length (beats)");
		controlPanel.add(lengthLabel);
		
		Integer[] lengthOptions = {4, 8, 16, 32, 64, 128, 256, 512};
		lengthSpinner = new JSpinner(new SpinnerListModel(lengthOptions));
		lengthSpinner.addChangeListener(this);
		controlPanel.add(lengthSpinner);
		
		volumeLabel = new JLabel("Volume: ");
		controlPanel.add(volumeLabel);
		
		volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 127, trackEditor.getVolume());
		volumeSlider.setMajorTickSpacing(32);
		volumeSlider.setMinorTickSpacing(8);
		volumeSlider.setPaintTicks(true);
		volumeSlider.setPaintLabels(true);
		volumeSlider.addChangeListener(this);
		controlPanel.add(volumeSlider);
		
		instrumentLabel = new JLabel("Instrument: ");
		controlPanel.add(instrumentLabel);
		
		instrumentBox = new JComboBox<>(trackEditor.getInstrumentNames());
		instrumentBox.addActionListener(this);
		controlPanel.add(instrumentBox);
		
		add(controlPanel, BorderLayout.EAST);
		add(trackEditor, BorderLayout.CENTER);
	}
	
	/**
     * Retrieves the current volume level for the track.
     *
     * @return the volume level, ranging from 0 (mute) to 127 (maximum volume)
     */
	public int getVolume() {
		return trackEditor.getVolume();
	}
	
	/**
     * Sets the volume level for the track. Updates both the internal editor 
     * and the volume slider to reflect the new volume level.
     *
     * @param volume the new volume level, ranging from 0 to 127
     */
	public void setVolume(int volume) {
		trackEditor.setVolume(volume);
		volumeSlider.setValue(volume);
	}
	
	 /**
     * Retrieves the currently selected instrument for the track.
     *
     * @return the index of the selected instrument
     */
	public int getInstrument() {
		return instrumentNumber;
	}
	
	/**
     * Sets the instrument for the track. Updates both the internal editor 
     * and the instrument dropdown box to reflect the selected instrument.
     *
     * @param instrument the index of the instrument to set
     */
	public void setInstrument(int instrument) {
		this.instrumentNumber = instrument;
		instrumentBox.setSelectedIndex(instrument);
		trackEditor.setInstrument(instrument);
	}

	/**
     * Handles changes to the spinner and slider components. When the track 
     * length spinner is adjusted, updates the track length accordingly.
     *
     * @param e the event generated when the value of a control changes
     */
	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == lengthSpinner) {
			int length = (Integer) lengthSpinner.getValue();
			setLength(length);
		}
	}

	/**
     * Responds to user actions such as muting/unmuting the track or selecting an instrument.
     * Updates the internal state of the track editor based on the user's interactions.
     *
     * @param e the event triggered by a button press or selection
     */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == muteButton) {
			if (muteButton.isSelected()) {
				trackEditor.setMute(true);
			} else { 
				trackEditor.setMute(false);
			}
		} else if (e.getSource() == instrumentBox) {
			int selectedInstrument = instrumentBox.getSelectedIndex();
			setInstrument(selectedInstrument);
			requestFocus();
		}
	}

	/**
     * Retrieves the sequencer used to manage audio events for this track.
     *
     * @return the sequencer associated with this track
     */
	@Override
	public SimpleSequencer getSequencer() {
		return trackEditor.getSequencer();
	}

	/**
     * Sets the length of the track in beats. Updates the internal track editor 
     * to accommodate the new length.
     *
     * @param length the new length of the track in beats
     */
	@Override
	public void setLength(int length) {
		trackEditor.setLength(length);
	}

	/**
     * Sets the audio events for this track. This allows the user to define 
     * or modify the sequence of events played by the track.
     *
     * @param events the collection of audio events to use in the track
     */
	@Override
	public void setEvents(BetterDynamicArray<AudioEvent> events) {
		trackEditor.setEvents(events);
	}

	 /**
     * Clears all audio events from the track. This resets the track editor 
     * to an empty state, removing any existing events or patterns.
     */
	@Override
	public void clear() {
		trackEditor.clear();
	}
}