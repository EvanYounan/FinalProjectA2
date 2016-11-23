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
	
	public String getStatus() {
		return this.description;
	}
	
	public String toString() {
		return "Status: " + this.description + ", at: " + 
				this.timeOfEvent.toString(); 
	}
} 
