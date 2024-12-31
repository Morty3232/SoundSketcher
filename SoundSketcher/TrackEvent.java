package assign11;

/**
 * TrackEvent is one of three child classes of AudioEvent. It contains many new custom methods 
 * such as a custom toString and compareTo. It also has its own new getters for its new variables. 
 * @author Arthur Morton
 * @version 10-24-2024
 */
public class TrackEvent extends AudioEvent{
	private int duration;
	private SimpleSequencer sequence;
	
	/**
	 * This is the constructor for the TrackEvent class. It takes the 3 parameters from the parent 
	 * class and 2 of its own, duration and the sequence array of AudioEvent objects. 
	 * @param time is when in the track the event occurs.
	 * @param trackName is the title of the track being played.
	 * @param channel is which layer the event occurs. 
	 * @param duration is how long the track plays for
	 * @param sequence is all of the events that play in a track. 
	 */
	public TrackEvent(int time, String trackName, int channel, int duration, SimpleSequencer sequence) {
		super(time, trackName, channel);
		this.duration = duration;
		this.sequence = sequence;
	}
	
	/**
	 * This is the getter for duration.
	 * @return the duration variable.
	 */
	public int getDuration() {
		return this.duration;
	}
	
	/**
	 * This is the getter for the sequence array. 
	 * @return the sequence array.
	 */
	public SimpleSequencer getSequence() {
		return this.sequence;
	}
	
	/**
	 * This is the custom toString method that neatly outputs the information from the class into 
	 * readable and understandable writing. It even has all of the individual events added to it.
	 */
	public String toString() {
		String result = getName() + "[" + getChannel() + ", " + getTime() + ", " + duration + ", " + sequence.getLength() + "]";
		
		for(AudioEvent event : sequence) {
			result += "\n- " + event;
		}
		return result ;
	}
	
	/**
	 * This is the custom compareTo method that can compare different objects from different 
	 * classes based on time. In the rare chance that events occur at the same time it prioritizes 
	 * them from ChangeEvent, NoteEvent then TrackEvent in that order. 
	 */
	public int compareTo(AudioEvent other) {
		if (this.getTime() != other.getTime())
			return this.getTime() - other.getTime();
		
		if (other instanceof TrackEvent) 
			return 0;
		return 1;
	}

	public void execute() {
		sequence.start();
	}

	public void complete() {
		
	}

	public void cancel() {
		sequence.stop();
	}
}
