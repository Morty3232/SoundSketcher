/**
 * The SongEditor class is a graphical interface for editing a song's sequence of events.
 * It extends the GridCanvas to allow users to visually add, drag, and remove events
 * while interacting with a SimpleSequencer and a list of TrackPanel} objects.
 * This class supports operations such as updating the length of the sequencer, setting audio events,
 * and dynamically managing track panels.
 */
package assign11;

import java.awt.Color;
import java.awt.Graphics;

public class SongEditor extends GridCanvas {
    private SimpleSequencer sequencer;
    private BetterDynamicArray<TrackPanel> trackPanels;
    private int width, height, currentTrack;

    /**
     * Constructs a SongEditor} with the specified width and height.
     * Initializes the sequencer and track panels and sets default grid properties.
     *
     * @param width  the width of the song editor
     * @param height the height of the song editor
     */
    public SongEditor(int width, int height) {
        super(width, height, 1, 16, 12, 4);
        this.width = width;
        this.height = height;
        this.currentTrack = -1;

        this.sequencer = new SimpleSequencer(16);
        this.trackPanels = new BetterDynamicArray<>();

        addMouseListener(this);
        addMouseMotionListener(this);

        setRows(Math.max(1, trackPanels.size()));
        setColumns(sequencer.getLength());
        setRestrictions(1, -1);
    }

    /**
     * Sets the length of the sequencer and updates the grid's column count accordingly.
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
     * Returns the SimpleSequencer} used by this song editor.
     *
     * @return the sequencer instance
     */
    public SimpleSequencer getSequencer() {
        return sequencer;
    }

    /**
     * Clears all audio events from the sequencer and resets the grid.
     */
    @Override
    public void clear() {
        super.clear();
        sequencer.stop();
        sequencer.clear();
    }

    /**
     * Sets the sequence of audio events in the editor.
     * Updates the grid and sequencer to reflect the new events.
     *
     * @param newEvents the list of AudioEvent objects to set
     */
    public void setEvents(BetterDynamicArray<AudioEvent> newEvents) {
        clear();
        for (AudioEvent event : newEvents) {
            if (event instanceof TrackEvent trackEvent) {
                addCell(trackEvent.getChannel(), trackEvent.getTime(), 1, trackEvent.getDuration());
            }
        }
        sequencer.updateSequence(newEvents);
    }

    /**
     * Sets the list of track panels to be displayed and updates the grid's row count.
     *
     * @param trackList the new list of TrackPanel objects
     */
    public void setTrackList(BetterDynamicArray<TrackPanel> trackList) {
        this.trackPanels = trackList;
        setRows(Math.max(1, trackPanels.size()));
    }

    /**
     * Paints the song editor, including the grid and a time indicator.
     *
     * @param g the Graphics object used for painting
     */
    @Override
    public void paintComponent(Graphics g) {
        this.width = getWidth();
        this.height = getHeight();
        setRows(trackPanels.size());
        super.paintComponent(g);

        // Draw time indicator
        int timeX = (int) (sequencer.getElapsedTime() * width / sequencer.getLength());
        g.setColor(Color.RED);
        g.fillRect(timeX, 0, 2, height);

        repaint();
    }

    /**
     * Handles cell press events. Updates the current track and sets grid restrictions.
     *
     * @param row     the row index of the pressed cell
     * @param col     the column index of the pressed cell
     * @param rowSpan the row span of the pressed cell
     * @param colSpan the column span of the pressed cell
     */
    @Override
    public void onCellPressed(int row, int col, int rowSpan, int colSpan) {
        currentTrack = row;
        setRestrictions(1, trackPanels.get(currentTrack).getSequencer().getLength());
    }

    /**
     * Handles cell drag events. Updates the current track and grid restrictions if necessary.
     *
     * @param row     the row index of the dragged cell
     * @param col     the column index of the dragged cell
     * @param rowSpan the row span of the dragged cell
     * @param colSpan the column span of the dragged cell
     */
    @Override
    public void onCellDragged(int row, int col, int rowSpan, int colSpan) {
        if (row != currentTrack) {
            currentTrack = row;
            setRestrictions(1, trackPanels.get(currentTrack).getSequencer().getLength());
        }
    }

    /**
     * Handles cell release events. Creates and adds a new TrackEvent to the sequencer.
     *
     * @param row     the row index of the released cell
     * @param col     the column index of the released cell
     * @param rowSpan the row span of the released cell
     * @param colSpan the column span of the released cell
     */
    @Override
    public void onCellReleased(int row, int col, int rowSpan, int colSpan) {
        TrackEvent event = new TrackEvent(col, "TrackEvent", currentTrack, colSpan, trackPanels.get(currentTrack).getSequencer());
        sequencer.add(event);
    }

    /**
     * Handles cell removal events. Removes the corresponding TrackEvent from the sequencer.
     *
     * @param row the row index of the removed cell
     * @param col the column index of the removed cell
     */
    @Override
    public void onCellRemoved(int row, int col) {
        for (AudioEvent event : sequencer) {
            if (event instanceof TrackEvent trackEvent && trackEvent.getChannel() == row && trackEvent.getTime() == col) {
                sequencer.remove(event);
            }
        }
    }
}
