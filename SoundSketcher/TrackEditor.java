/**
 * The TrackEditor class provides a graphical interface for editing a specific audio track.
 * It extends GridCanvas to allow users to visually create, modify, and manage note events
 * on a track, integrating with a SimpleSynthesizer and SimpleSequencer for playback.
 * This editor allows for pitch manipulation, instrument selection, volume control, and more,
 * providing an intuitive interface for music composition.
 * @version 11/22/2024
 * @author Arthur Morton
 */
package assign11;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Vector;

public class TrackEditor extends GridCanvas {
    private SimpleSynthesizer synthesizer;
    private SimpleSequencer sequencer;
    private int trackNumber;
    private int width, height, currentPitch;

    /**
     * Constructs a TrackEditor with the specified dimensions, track number, 
     * and synthesizer for audio playback.
     *
     * @param width       the width of the editor in pixels
     * @param height      the height of the editor in pixels
     * @param trackNumber the track number this editor is associated with
     * @param synthesizer the synthesizer used for audio playback
     */
    public TrackEditor(int width, int height, int trackNumber, SimpleSynthesizer synthesizer) {
        super(width, height, 120, 16, 12, 4);
        this.width = width;
        this.height = height;
        this.trackNumber = trackNumber;
        this.synthesizer = synthesizer;
        this.currentPitch = -1;

        setMaximumSize(new Dimension(width, height));

        setRestrictions(1, -1); // Restrict height to 1, width unrestricted
        this.sequencer = new SimpleSequencer(16);

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /**
     * Sets the length of the sequencer and updates the grid's column count.
     *
     * @param length the new length of the sequencer
     */
    public void setLength(int length) {
        sequencer.setLength(length);
        setColumns(length);
    }

    /**
     * Returns the current length of the sequencer.
     *
     * @return the length of the sequencer
     */
    public int getLength() {
        return sequencer.getLength();
    }

    /**
     * Sets the volume for this track in the synthesizer.
     *
     * @param volume the new volume (0 to 100)
     */
    public void setVolume(int volume) {
        synthesizer.setVolume(trackNumber, volume);
    }

    /**
     * Gets the current volume for this track from the synthesizer.
     *
     * @return the current volume
     */
    public int getVolume() {
        return synthesizer.getVolume(trackNumber);
    }

    /**
     * Mutes or unmutes this track in the synthesizer.
     *
     * @param mute true} to mute the track, false} to unmute
     */
    public void setMute(boolean mute) {
        synthesizer.setMute(trackNumber, mute);
    }

    /**
     * Sets the instrument for this track in the synthesizer.
     *
     * @param instrument the instrument index
     */
    public void setInstrument(int instrument) {
        synthesizer.setInstrument(trackNumber, instrument);
    }

    /**
     * Returns a list of instrument names supported by the synthesizer.
     *
     * @return a Vector of instrument names
     */
    public Vector<String> getInstrumentNames() {
        return new Vector<>(synthesizer.getInstrumentNames());
    }

    /**
     * Returns the sequencer associated with this track editor.
     *
     * @return the sequencer instance
     */
    public SimpleSequencer getSequencer() {
        return sequencer;
    }

    /**
     * Clears all note events from the sequencer and resets the grid.
     */
    @Override
    public void clear() {
        super.clear();
        sequencer.stop();
        sequencer.clear();
    }

    /**
     * Sets a sequence of audio events in the editor.
     * Updates the grid and sequencer with the new events.
     *
     * @param newEvents the list of AudioEvent objects to set
     */
    public void setEvents(BetterDynamicArray<AudioEvent> newEvents) {
        clear();
        for (AudioEvent event : newEvents) {
            if (event instanceof NoteEvent noteEvent) {
                addCell(noteEvent.getPitch(), noteEvent.getTime(), 1, noteEvent.getDuration());
            }
        }
        sequencer.updateSequence(newEvents);
    }

    /**
     * Paints the track editor, including the grid and a time indicator.
     *
     * @param g the Graphics object used for painting
     */
    @Override
    public void paintComponent(Graphics g) {
        this.width = getWidth();
        this.height = getHeight();
        super.paintComponent(g);

        // Draw time indicator
        int timeX = (int) (sequencer.getElapsedTime() * width / sequencer.getLength());
        g.setColor(Color.RED);
        g.fillRect(timeX, 0, 2, height);

        repaint(); // Continuously repaint to update the time indicator
    }

    /**
     * Handles cell press events. Activates the corresponding pitch on the synthesizer.
     *
     * @param row     the row index of the pressed cell
     * @param col     the column index of the pressed cell
     * @param rowSpan the row span of the pressed cell
     * @param colSpan the column span of the pressed cell
     */
    @Override
    public void onCellPressed(int row, int col, int rowSpan, int colSpan) {
        currentPitch = row;
        synthesizer.noteOn(trackNumber, currentPitch);
    }

    /**
     * Handles cell drag events. Switches the active pitch on the synthesizer if necessary.
     *
     * @param row     the row index of the dragged cell
     * @param col     the column index of the dragged cell
     * @param rowSpan the row span of the dragged cell
     * @param colSpan the column span of the dragged cell
     */
    @Override
    public void onCellDragged(int row, int col, int rowSpan, int colSpan) {
        if (row != currentPitch) {
            synthesizer.noteOff(trackNumber, currentPitch);
            currentPitch = row;
            synthesizer.noteOn(trackNumber, currentPitch);
        }
    }

    /**
     * Handles cell release events. Creates and adds a NoteEvent to the sequencer.
     *
     * @param row     the row index of the released cell
     * @param col     the column index of the released cell
     * @param rowSpan the row span of the released cell
     * @param colSpan the column span of the released cell
     */
    @Override
    public void onCellReleased(int row, int col, int rowSpan, int colSpan) {
        if (colSpan > 0) {
            NoteEvent note = new NoteEvent(col, "Note", trackNumber, colSpan, row, synthesizer);
            sequencer.add(note);
        }
        synthesizer.noteOff(trackNumber, currentPitch);
    }

    /**
     * Handles cell removal events. Removes the corresponding NoteEvent from the sequencer.
     *
     * @param row the row index of the removed cell
     * @param col the column index of the removed cell
     */
    @Override
    public void onCellRemoved(int row, int col) {
        for (AudioEvent event : sequencer) {
            if (event instanceof NoteEvent noteEvent && noteEvent.getPitch() == row && noteEvent.getTime() == col) {
                sequencer.remove(noteEvent);
            }
        }
    }
}
