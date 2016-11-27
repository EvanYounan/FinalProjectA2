import java.io.Serializable;
import java.util.Date;

/**
 * Object to hold activities performed on ImageNodes and the date they were performed, otherwise
 * known as a log.
 * @author Evannnn
 *
 */
public class Log implements Serializable {
	
	/** ID for Serialization */
	private static final long serialVersionUID = 1L;
	/** Description of log */
	private String description;
	/** The date the operation was performed */
	private Date timeOfEvent;
	
	/**
	 * Create a Log object with a given description and set its date to the current time.
	 * 
	 * @param desc - description of the operation/log
	 */
	public Log(String desc) {
		this.timeOfEvent = new Date();
		this.description = desc;
	}
	
	/**
	 * Empty constructor for serialization
	 */
	public Log() {}
	
	/**
	 * Return the creation time of this log
	 * 
	 * @return 		date of creation
	 */
	public Date getCreationTime() {
		return this.timeOfEvent;
	}
	
	/**
	 * Return true if and only if the given date is later than this log's date.
	 * 
	 * @param date - date to be compared
	 * @return		true if given date is later than log's date
	 */
	public boolean laterThan(Date date) {
		return this.timeOfEvent.after(date);
	}
	
	/**
	 * Return true if and only if the given date is earlier than this log's date.
	 * 
	 * @param date - date to be compared
	 * @return		true if given date is earlier than log's date.
	 */
	public boolean earlierThan(Date date) {
		return this.timeOfEvent.before(date);
	}
	
	/**
	 * Return the description of this log.
	 * 
	 * @return		description of this log
	 */
	public String getStatus() {
		return this.description;
	}
	
	/**
	 * Return true if and only if this log has the same date and description as the other log.
	 * 
	 * @param otherLog - other log to be compared to this log
	 * @return		true if and only if they have same date and description
	 */
	public boolean equals(Log otherLog) {
		return (this.timeOfEvent.equals(otherLog.timeOfEvent)
				&& this.description.equals(otherLog.description));
	}
	
	/**
	 * Return a string representation of this log object.
	 * 
	 * @return 		string representation of this log
	 */
	public String toString() {
		return "Status: " + this.description + ", at: " + 
				this.timeOfEvent.toString(); 
	}
} 
