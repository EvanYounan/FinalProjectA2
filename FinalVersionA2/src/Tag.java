import java.io.Serializable;

/**
 * Object to represent a tag.
 * @author Evan
 *
 */
public class Tag implements Serializable {
	
	/** ID for Serialization */
	private static final long serialVersionUID = 1L;
	/** Name of this tag */
	private String name;
	
	/**
	 * Create a tag with the given name.
	 * 
	 * @param name - name of this new tag
	 */
	public Tag(String name) {
		this.name = name;
	}
	
	/**
	 * Return true if and only if the name of this tag is the same as the name of
	 * the other tag.
	 *  
	 * @param otherTag - other tag being compared
	 * @return		true if both tags share the same name
	 */
	public boolean equals(Tag otherTag) {
		return this.getName().equals(otherTag.getName());
	}
	
	/**
	 * Return the name of this tag
	 * 
	 * @return		name of this tag
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Return a string representation of this tag.
	 * 
	 * @return 		string representation of this tag
	 */
	public String toString() {
		return "@" + this.name;
	}
}
