package assign11;

/**
 * This is the Parent class of all the other 3 Classes, NoteEvent, ChangeEvent, and TrackEvent.
 * It is an abstract class so you must make one of the other 3 when creating an object. 
 * @author Arthur Morton
 * @version 10-24-2020
 */
public abstract class AudioEvent implements Comparable<AudioEvent>{
	private int time;
	private String name;
	private int channel;
	
	/**
	 * This is the framework constructor for the AudioEvent Class. You cannot create an 
	 * AudioEvent object but it is referred to from the other 3 classes. 
	 * @param time is when in the track the event occurs.
	 * @param name is what kind of event occurs.
	 * @param channel is which channel or layer the event occurs.
	 */
	public AudioEvent(int time, String name, int channel) {
		this.time = time;
		this.name = name;
		this.channel = channel;
	}
	
	/**
	 * This is the getter of time.
	 * @return the time variable.
	 */
	public int getTime() {
		return this.time;
	}
	
	/**
	 * This is the getter of name.
	 * @return the name variable.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * This is the getter of channel.
	 * @return the channel variable.
	 */
	public int getChannel() {
		return this.channel;
	}
	
	/**
	 * This is a temporary method for future assignments.
	 */
	public abstract void execute();
	
	/**
	 * This is a temporary method for future assignments.
	 */
	public abstract void complete();
	
	
	/**
	 * This is a temporary method for future assignments.
	 */
	public abstract void cancel();
}
