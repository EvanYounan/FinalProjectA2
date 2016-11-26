import java.io.Serializable;
import java.util.Date;

public class Log implements Serializable {
	String description;
	Date timeOfEvent;
	
	public Log(String desc) {
		this.timeOfEvent = new Date();
		this.description = desc;
	}
	
	public Log() {}
	
	public Date getCreationTime() {
		return this.timeOfEvent;
	}
	
	public boolean laterThan(Date date) {
		return this.timeOfEvent.after(date);
	}
	
	//Newly added 
	public boolean earlierThan(Date date) {
		return this.timeOfEvent.before(date);
	}
	
	public String getStatus() {
		return this.description;
	}
	
	public boolean equals(Log otherLog) {
		return (this.timeOfEvent.equals(otherLog.timeOfEvent)
				&& this.description.equals(otherLog.description));
	}
	
	public String toString() {
		return "Status: " + this.description + ", at: " + 
				this.timeOfEvent.toString(); 
	}
} 
