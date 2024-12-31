package assign11;

/**
 * NoteEvent is one of three child classes of AudioEvent. It contains many new custom methods 
 * such as a custom toString and compareTo. It also has its own new getters for its new variables. 
 * @author Arthur Morton
 * @version 10-24-2024
 */
public class NoteEvent extends AudioEvent{
	private int duration;
	private int pitch;
	private SimpleSynthesizer simpleSynth;
	
	/**
	 * The constructor for the NoteEvent class. It takes 3 parameters for the AudioEvent constructor and
	 *  2 new ones custom for the class.
	 * @param time measures where in the track the note starts.
	 * @param instrument is the name of the instrument played. 
	 * @param channel is which layer the note is played on. 
	 * @param duration is how long the note plays for.
	 * @param pitch is the frequency of the sound. 
	 */
	public NoteEvent(int time, String instrument, int channel, int duration, int pitch, SimpleSynthesizer simpleSynth) {
		super(time, instrument, channel);
		this.duration = duration;
		this.pitch = pitch;
		this.simpleSynth = simpleSynth;
	}
	
	/**
	 * This is the getter for duration.
	 * @return the duration variable.
	 */
	public int getDuration() {
		return this.duration;
	}
	
	/**
	 * This is the getter for pitch.
	 * @return the pitch variable.
	 */
	public int getPitch() {
		return this.pitch;
	}
	
	/**
	 * This is the custom toString method that neatly outputs the information from the class into 
	 * readable and understandable writing.
	 */
	public String toString() {
		return getName() + "[" + getChannel() + ", " + getTime() + ", "+ duration + ", " + pitch + "]";
	}
	
	/**
	 * This is the custom compareTo method that can compare different objects from different 
	 * classes based on time. In the rare chance that events occur at the same time it prioritizes 
	 * them from ChangeEvent, NoteEvent then TrackEvent in that order. 
	 */
	public int compareTo(AudioEvent other) {
		if (this.getTime() != other.getTime())
			return this.getTime() - other.getTime();
		
		if (other instanceof NoteEvent) 
			return 0;
		else if (other instanceof ChangeEvent)
			return 1;
		return -1;
	}

	
	public void execute() {
		simpleSynth.noteOn(this.getChannel(), this.pitch);
	}

	
	public void complete() {
		simpleSynth.noteOff(this.getChannel(), this.pitch);
	}

	
	public void cancel() {
		simpleSynth.noteOff(this.getChannel(), this.pitch);
	}
}
