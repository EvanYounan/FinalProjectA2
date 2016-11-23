import java.io.Serializable;

public class Tag implements Serializable {
	private String name;
	
	public Tag(String name) {
		this.name = name;
	}

	public boolean equals(Tag otherTag) {
		return this.getName().equals(otherTag.getName());
	}
	
	public String getName() {
		return this.name;
	}
	
	public String toString() {
		return "@" + this.name;
	}
}
