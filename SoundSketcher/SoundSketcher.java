package assign11;

/**
 * The SoundSketcher  serves as the entry point for the SoundSketcher application.
 * This application provides a graphical interface for creating and manipulating sound sequences.
 */
public class SoundSketcher {

    /**
     * The main method initializes the application by creating and displaying the 
     * SoundSketcherFrame, which serves as the primary window for user interaction.
     *
     * @param args command-line arguments (not used in this application)
     */
    public static void main(String[] args) {
        SoundSketcherFrame frame = new SoundSketcherFrame(800, 800);
        frame.setVisible(true);
    }
}