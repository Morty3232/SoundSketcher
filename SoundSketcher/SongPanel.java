package assign11;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;


/**
 * The SongPanel class represents a graphical interface for editing a song 
 * within the Sound Sketcher application. It allows users to control playback, 
 * enable or disable looping, adjust the song's length, and manage audio events.
 * @version 11/22/2024
 * @author Arthur Morton
 */
public class SongPanel extends SketchingPanel implements ActionListener, ChangeListener{

	private SongEditor songEditor;
	private JPanel controlPanel;
	private JToggleButton playStopButton, loopToggleButton;
	private JSpinner lengthSpinner;
	private JLabel lengthLabel;
	private BetterDynamicArray<TrackPanel> trackList;
	
	/**
     * Constructs a new SongPanel with the specified width and height.
     *
     * @param width  the width of the panel in pixels
     * @param height the height of the panel in pixels
     */
	public SongPanel(int width, int height) {
		trackList = new BetterDynamicArray<>();
		
		setPreferredSize(new Dimension(width, height));
		setLayout(new BorderLayout());
		
		songEditor = new SongEditor(width , height);
		add(songEditor, BorderLayout.CENTER);
		
		controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());
		add(controlPanel, BorderLayout.SOUTH);
		
		playStopButton = new JToggleButton("Play");
		playStopButton.addActionListener(this);
		controlPanel.add(playStopButton);
		
		loopToggleButton = new JToggleButton("Loop: Off");
		loopToggleButton.addActionListener(this);
		controlPanel.add(loopToggleButton);
		
		Integer[] lengthOptions = {4, 8, 16, 32, 64, 128, 256, 512};
		lengthSpinner = new JSpinner(new SpinnerListModel(lengthOptions));
		lengthSpinner.addChangeListener(this);
		
		lengthLabel = new JLabel("Song length: ");
		controlPanel.add(lengthLabel);
		controlPanel.add(lengthSpinner);
		
	}

	/**
     * Sets the list of track panels for this SongPanel.
     * This overrides any previous list and updates the SongPanel state.
     *
     * @param trackList the dynamic array of TrackPanel objects.
     */
    public void setTrackList(BetterDynamicArray<TrackPanel> trackList) {
       songEditor.setTrackList(trackList);
        repaint();
    }
	
	/**
     * Handles changes in the length spinner control. When the user changes the song length, 
     * this method updates the song's length accordingly.
     *
     * @param e the event triggered by a change in the spinner's value
     */
	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == lengthSpinner) {
			int length = (Integer) lengthSpinner.getValue();
			setLength(length);
		}
	}

	/**
     * Processes user actions such as toggling playback or looping. This method updates 
     * the play/stop and loop controls based on the user's input.
     *
     * @param e the event triggered by user interaction with a control element
     */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source == playStopButton) {
			if (playStopButton.isSelected()) {
				play();
				playStopButton.setText("Stop");
			}
			else {
				stop();
				playStopButton.setText("Play");
			}
		}
		
		if (source == loopToggleButton) {
			boolean isLooping = loopToggleButton.isSelected();
			setLoop(isLooping);
			loopToggleButton.setText("Loop: " + (isLooping ? "On" : "Off"));
		}
	}

	/**
     * Returns the sequencer that is used to manage audio events in the song.
     *
     * @return the sequencer for the song
     */
	@Override
	public SimpleSequencer getSequencer() {
		return songEditor.getSequencer();
	}

	/**
     * Updates the length of the song. This method modifies the internal song editor to 
     * reflect the new length.
     *
     * @param length the new length of the song
     */
	@Override
	public void setLength(int length) {
		songEditor.setLength(length);
	}

	/**
     * Sets the audio events for the song. This method passes the provided events 
     * to the song editor for playback and editing.
     *
     * @param events the collection of audio events to use in the song
     */
	@Override
	public void setEvents(BetterDynamicArray<AudioEvent> events) {
		songEditor.setEvents(events);
	}

	/**
     * Clears all audio events from the song editor. This resets the song to an empty state.
     */
	@Override
	public void clear() {
			songEditor.clear();
		}
}
