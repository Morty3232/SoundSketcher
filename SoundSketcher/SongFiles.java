package assign11;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Utility class for reading and writing song files. Provides functionality to save and load 
 * song data, including tracks, audio events, and tempo.
 */
public class SongFiles {

	/**
     * Writes the given song data, including tempo, tracks, and the song panel, to the specified file.
     * 
     * Parameters:
     * file - The file to write the song data to.
     * tempo - The tempo of the song.
     * trackPanels - A dynamic array containing all track panels of the song.
     * songPanel - The song panel containing additional song information.
     */
	public static void writeFile(File file, int tempo, BetterDynamicArray<TrackPanel> trackPanels, SongPanel songPanel) {
		//creates an instance of filewriter
		
		try (FileWriter writer  = new FileWriter(file)){
			//starts with printing tempo and number of track panels
			writer.write("" + tempo + "\n");
			writer.write("" + trackPanels.size() + "\n");
			//loops through and adds each track panel before the song panel
			for(int i = 0; i <trackPanels.size(); i++) {
				TrackPanel trackPanel = trackPanels.get(i);
				writer.write("track\n");
				writer.write(i + "\n");
				writer.write(trackPanel.getInstrument() + "\n");
				writer.write("" + trackPanel.getVolume() + "\n");
				writer.write("" + trackPanel.getLength() + "\n");
				writer.write("" + trackPanel.getSequencer().getEventCount() + "\n");
				//writes the audio event part of each track event
				audioEventTextBlock(trackPanel.getSequencer(), writer );
			}
			//writes the song panel part of the file
			writer.write("song\n");
			writer.write(songPanel.getLength() + "\n");
			writer.write(songPanel.getSequencer().getEventCount() + "\n");
			audioEventTextBlock(songPanel.getSequencer(), writer );
			writer.close();
			//catches the IOException
		} catch (IOException e) {
			System.out.println("An error has occurred");
			e.printStackTrace();
		}
	}
	
	/**
     * Converts all audio events from a given sequencer into a formatted string block for saving to a file.
     * 
     * Parameters:
     * sequencer - The sequencer containing audio events to be converted.
     * 
     * Returns:
     * A string representing the audio events in the sequencer.
     */
	private static void audioEventTextBlock(SimpleSequencer sequencer, FileWriter writer) {
		
		try {
			for (AudioEvent event : sequencer) {
	            // Event value if Change, pitch if Note, 0 if Track
	            if (event instanceof ChangeEvent) {
	            	writer.write("change" + "\n");
	                writer.write("" + ( (ChangeEvent) event).getValue() + "\n");
	                // Event name
		            writer.write("" + event.getName() + "\n");
		            // Event time
		            writer.write("" + event.getTime() + "\n");
		            // Event channel
		            writer.write("" + event.getChannel() + "\n");
	                writer.write("" + 0 + "\n");
	            } else if (event instanceof NoteEvent) {
	            	writer.write("note" + "\n");
	                // Event name
		            writer.write("" + event.getName() + "\n");
		            // Event time
		            writer.write("" + event.getTime() + "\n");
		            // Event channel
		            writer.write("" + event.getChannel() + "\n");
		            writer.write("" + ( (NoteEvent) event).getPitch() + "\n");
	                writer.write("" + ( (NoteEvent) event).getDuration() + "\n");
	            } else if (event instanceof TrackEvent) {
	            	writer.write("track" + "\n");
	            	 // Event name
		            writer.write("" + event.getName() + "\n");
		            // Event time
		            writer.write("" + event.getTime() + "\n");
		            // Event channel
		            writer.write("" + event.getChannel() + "\n");
	                writer.write("" + 0 + "\n");
	                writer.write("" + ( (TrackEvent) event).getDuration() + "\n");
	            }
			}
        } catch (IOException e) {
        	System.out.println(" there was an IOexception");
        	e.printStackTrace();
        }
	}
	
	/**
     * Reads song data from the specified file and populates the given tracks and song panel. 
     * Returns the tempo of the song.
     * 
     * Parameters:
     * file - The file to read the song data from.
     * synthesizer - The synthesizer used to create audio events.
     * tracks - A dynamic array to store the loaded track panels.
     * song - The song panel to store additional song information.
     * width - The width of the track panels.
     * height - The height of the track panels.
     * 
     * Returns:
     * The tempo of the song as an integer.
     */
	public static int readFile(File file, SimpleSynthesizer synthesizer, BetterDynamicArray<TrackPanel> tracks, SongPanel song, int width, int height) {
		int tempo = -1;
		//create the scanner for the file.
		try {
			Scanner fileScanner = new Scanner(file);
			//clear the previous file that the application could have been reading.
			tracks.clear();
			song.clear();
			
			//initialize and set tempo and trackSize to their value in the file read.
			tempo = fileScanner.nextInt();
			
			int trackSize = fileScanner.nextInt();
			
			//loop through and read all of the file
			for (int i = 0; i < trackSize; i++) { 
				String trackName = fileScanner.next();
				int trackNum = fileScanner.nextInt();
				int instrument = fileScanner.nextInt();
				int volume = fileScanner.nextInt();
				int trackBeatLength = fileScanner.nextInt();
				int audioEventNum = fileScanner.nextInt();
				
				//create trackPanel
				TrackPanel trackPanel = new TrackPanel (width, height, trackNum, synthesizer);
				trackPanel.setVolume(volume);
				trackPanel.setLength(trackBeatLength);
				
				//nested loop through the AudioEvents
				for (int j = 0; j < audioEventNum; j++) {
		
					String eventType = fileScanner.next();
					String name = fileScanner.next();
					int time = fileScanner.nextInt();
					int channel = fileScanner.nextInt();
					int valueOrPitch = fileScanner.nextInt();
					int duration = fileScanner.nextInt();
					
					// value for ChangeEvents, pitch for NoteEvents
					if (eventType == "change") 
						trackPanel.getSequencer().add(new ChangeEvent(time, name, channel, valueOrPitch, synthesizer));
					 else if (eventType == "note") 		
						trackPanel.getSequencer().add(new NoteEvent(time, name, channel, duration, valueOrPitch, synthesizer));
				}
				
				tracks.add(trackPanel);
			}
			
			String song1 = fileScanner.next();
			int length = fileScanner.nextInt();
			song.setLength(length);
			int audioEventNum = fileScanner.nextInt();
			
			//nested loop through the AudioEvents
			for (int i = 0; i < audioEventNum; i++) {
	
				String eventType = fileScanner.next();
				String name = fileScanner.next();
				int time = fileScanner.nextInt();
				int channel = fileScanner.nextInt();
				int valueOrPitch = fileScanner.nextInt();
				int duration = fileScanner.nextInt();
				
				// value for ChangeEvents, pitch for NoteEvents
				if (eventType.equals("change"))
					song.getSequencer().add(new ChangeEvent(time, name, channel, valueOrPitch, new SimpleSynthesizer()));
				if (eventType.equals("note"))	
					song.getSequencer().add(new NoteEvent(time, name, channel, duration, valueOrPitch, new SimpleSynthesizer()));
				 else
					 song.getSequencer().add(new TrackEvent(time, name, channel, duration, tracks.get(channel).getSequencer()));
			}
			
			//returns the tempo
			fileScanner.close();
			
			
		} catch(IllegalStateException e) {
			System.out.println("Illegal state");
			e.printStackTrace();
		} catch(InputMismatchException e) {
			System.out.println("Input Mismatch");
			e.printStackTrace();
		} catch(FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
		} catch (@SuppressWarnings("hiding") IOException e) {
			System.out.println("IOException");
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			System.out.println("No Such Elelment");
			e.printStackTrace();
		}
		return tempo;
	}		
	
	
}