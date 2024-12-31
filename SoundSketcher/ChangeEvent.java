package assign11;

import javax.swing.JSpinner;

/**
 * ChangeEvent is one of three child classes of AudioEvent. It contains many new custom methods 
 * such as a custom toString and compareTo. It also has its own new getters for its new variables. 
 * @author Arthur Morton
 * @version 10-24-2024
 */
public class ChangeEvent extends AudioEvent{
	private int value;
	private SimpleSynthesizer simpleSynth;
	
	/**
	 * The constructor for the ChangeEvent class. It takes the 3 parameters for the parents class and 
	 * has one new parameter for value. 
	 * @param time measures when the change occurs.
	 * @param type is what kind of change occurs.
	 * @param channel is which layer the change occurs on.
	 * @param value is how much it will change by.
	 */
	public ChangeEvent(int time, String type, int channel, int value, SimpleSynthesizer simpleSynth) {
		super(time, type, channel);
		this.value = value;
		this.simpleSynth = simpleSynth;
	}
	
	/**
	 * This is the getter for value.
	 * @return the value variable.
	 */
	public int getValue() {
		return this.value;
	}
	
	/**
	 * This is the custom toString method that neatly outputs the information from the class into 
	 * readable and understandable writing.
	 */
	public String toString() {
		return getName() + "[" + getChannel() + ", " + getTime() + ", " + value + "]";
	}
	
	/**
	 * This is the custom compareTo method that can compare different objects from different 
	 * classes based on time. In the rare chance that events occur at the same time it prioritizes 
	 * them from ChangeEvent, NoteEvent then TrackEvent in that order. 
	 */
	public int compareTo(AudioEvent other) {
		if (this.getTime() != other.getTime())
			return this.getTime() - other.getTime();
		
		if (other instanceof ChangeEvent) 
			return 0;
		return -1;
	}

	
	public void execute() {
		
	}

	
	public void complete() {
		
	}

	
	public void cancel() {
		
	}
	
	

}
