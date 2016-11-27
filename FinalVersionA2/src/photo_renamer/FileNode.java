package photo_renamer;
import java.io.Serializable;

/**
 * Object that stores the pathname and name of a file.
 * @author Evan
 */
public abstract class FileNode implements Serializable {

	/** ID for Serialization */
	private static final long serialVersionUID = 1L;
	/** Path of file */
	private String pathName;
	/** Name of file */
	private String name;
	
	/**
	 * Create a FileNode object with a path and name.
	 * @param path
	 * 		path to file
	 * @param name
	 * 		name of file
	 */
	public FileNode(String path, String name) {
		this.pathName = path;
		this.name = name;
	}
	
	/**
	 * This constructor is used for serialization process.
	 */
	public FileNode() {}
	
	/**
	 * Return the path of this FileNode
	 * @return
	 * 		path of this FileNode
	 */
	public String getPathName() {
		return pathName;
	}

	/**
	 * Set this FileNode's path to pathName.
	 * @param pathName - the path to set this FileNode's path to
	 */
	public void setPathName(String pathName) {
		this.pathName = pathName;
	}

	/**
	 * Return this FileNode's name
	 * @return	the name of this FileNode
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of this FileNode
	 * @param name	the new name of this FileNode
	 */
	public void setName(String name) {
		this.name = name;
	}
}

