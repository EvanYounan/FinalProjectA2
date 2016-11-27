import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Object to store the information of an image. Each ImageNode has a parent and
 * child. The parent of an ImageNode is an older version of an image. The
 * child of an ImageNode is a newer version of the image. The last child
 * of the straight-lined structure (defined as the ImageNode with no child) holds
 * the information of the current configuration of the image that the ImageNode 
 * represents. The root of the straight-lined structure holds the information of
 * the image when it was first loaded into the system.
 * @author Evan
 *
 */
public class ImageNode extends FileNode {
	
	/** ID for Serialization */
	private static final long serialVersionUID = 1L;
	/** The list of tags of this ImageNode */
	private ArrayList<Tag> tags;
	/** The ID of this ImageNode */
	private long ID;
	/** Used for generating a random ID */
	public Random x;
	/** The parent of this ImageNode */
	private ImageNode parent;
	/** The child of this ImageNode */
	private ImageNode child;
	/** The log of this ImageNode. This log is generated when the user adds/removes a tag 
	 or loads the image into the system.*/
	private Log log;
	
	/**
	 * Creates an ImageNode with the given path and name. Initializes an empty ArrayList
	 * for the tags to be saved for this ImageNode. Sets an ID for this ImageNode.
	 * 
	 * @param path - the path of the ImageNode
	 * @param name - the name of the ImageNode
	 */
	public ImageNode(String path, String name) {
		super(path, name);
		this.tags = new ArrayList<>();
		x = new Random();
		this.ID = x.nextLong(); 
	}
	
	/**
	 * Creates an ImageNode with empty ArrayList for the tags of this ImageNode. This
	 * constructor is used when adding a child to another ImageNode, therefore setting
	 * the path, name, and ID manually using the other ImageNode.
	 */
	public ImageNode() {
		this.tags = new ArrayList<>();
	}
	
	/**
	 * Create a new log with the given description and set it 
	 * as this ImageNode's log.
	 * 
	 * @param description - the description of the log
	 */
	public void setLog(String description) {
		this.log = new Log(description);
	}
	
	/**
	 * Returns the log of the current ImageNode.
	 * 
	 * @return 		the log of this ImageNode
	 */
	public Log getLog() {
		return this.log;
	}
	
	/**
	 * Add a tag to this ImageNode's tag list.
	 * 
	 * @param tag - the tag to be added
	 */
	public void addTag(Tag tag) {
		tags.add(tag);
	}
	
	/**
	 * Remove a tag from this ImageNode's list of tags.
	 * 
	 * @param tag - the tag to be removed
	 */
	public void removeTag(Tag tag) {
		for (Tag t : tags) {
			if (tag.equals(t)) {
				tags.remove(t);
				break;
			}
		}
	}
	
	/**
	 * Return the ID of this ImageNode.
	 * 
	 * @return		ID of this ImageNode
	 */
	public long getID() {
		return ID;
	}

	/**
	 * Set the ID of this ImageNode
	 * 
	 * @param ID - ID to be set 
	 */
	public void setID(long ID) {
		this.ID = ID;
	}
	
	/**
	 * Return the immediate parent of this ImageNode.
	 * 
	 * @return		the parent of this ImageNode
	 */
	public ImageNode getParent() {
		return this.parent;
	}
	
	/**
	 * Return the immediate child of this ImageNode.
	 * 
	 * @return		the child of this ImageNode
	 */
	public ImageNode getChild() {
		return this.child;
	}
	
	/**
	 * Recursively look for the absolute child of 
	 * this straight-lined structure and return it.
	 * The absolute child is the last ImageNode 
	 * with no child following it.
	 * 
	 * @param img - any ImageNode in the straight-lined structure of ImageNodes
	 * @return		the absolute child of this ImageNode
	 */
	public ImageNode findChild(ImageNode img) {
		if (img.hasChild()) {
			return findChild(img.getChild());
		}
		return img;
	}
	
	/**
	 * Recursively look for the root of
	 * this straight-lined structure of ImageNodes and return it.
	 * The root is the first ImageNode and is defined as the only ImageNode with no parent.
	 * 
	 * @param img - any ImageNode in the straight-lined structure of ImageNodes
	 * @return 		the root of this ImageNode
	 */
	public ImageNode findRoot(ImageNode img) {
		if (img.hasParent()) {
			return findRoot(img.getParent());
		}
		return img;
	}
	
	/**
	 * Return true if and only if this ImageNode has a parent.
	 * 
	 * @return		true iff this ImageNode has a parent.
	 */
	public boolean hasParent() {
		return this.parent != null;
	}
	
	/**
	 * Return true if and only if this ImageNode has a child.
	 * 
	 * @return		true iff this ImageNode has a child.
	 */
	public boolean hasChild() {
		return this.child != null;
	}
	
	/**
	 * Given a tag, if the ImageNode has this tag, return the ImageNode's tag.
	 * This function will return null if the ImageNode does not have the tag.
	 * 
	 * @param tag - tag to be looked for
	 * @return		a tag from this ImageNode's tag or null
	 */
	public Tag retrieveTag(Tag tag) {
		for (Tag existingTag : this.tags) {
			if (tag.equals(existingTag)) {
				return existingTag;
			}
		}
		return null;
	}
	
	/**
	 * Clear all tags that this ImageNode possesses.
	 * 
	 * @param img - the ImageNode being cleared of its tags
	 */
	public void clearTags(ImageNode img) {
		this.tags.clear();
	}
	
	/**
	 * Return the tags of this ImageNode.
	 * 
	 * @return	tags of this ImageNode
	 */
	public ArrayList<Tag> getTags() {
		return this.tags;
	}
	
	/**
	 * Set the tags of this ImageNode to the given ArrayList of tags.
	 * 
	 * @param newTags - the new tags of this ImageNode
	 */
	public void setTags(ArrayList<Tag> newTags) {
		this.tags = newTags;
	}
	
	/**
	 * Return true if and only if this ImageNode has the given tag.
	 * 
	 * @param tag - tag to be looked for
	 * @return		true if this ImageNode has given tag
	 */
	public boolean hasTag(Tag tag) {
		for (Tag thisTag : this.tags) {
			if (thisTag.equals(tag)) {
				return true;
			}
		}
		return false;
	}
	
	
	
//	public ArrayList<Tag> getTagsUpTo(ImageNode img, ArrayList<Tag> temp) {
//		if (img != null) {
//			if (!img.getTags().isEmpty()) {
//				for (Tag t : img.getTags()) {
//					if (!tagInList(temp, t)) {
//						temp.add(t);
//					}
//				}
//			}
//			if (img.getChild() != null) {
//				return getTagsUpTo(img.getChild(), temp);
//			} else {
//				return temp;
//			}
//		} else {
//		return temp;
//		}
//	}
	
	public void setFileNameAndFilePath() {
		String originalPathName = this.findRoot(this).getPathName();
		String originalName = this.findRoot(this).getName();
		originalName = originalName.substring(0, originalName.indexOf("."));
		String retString = originalName;
		
		for (Tag x : this.tags) {
			retString += "@" + x.getName();
		}
		
		int endIndex = originalPathName.lastIndexOf(".");
		String extension = "";
		if (endIndex > 0) {
			extension = originalPathName.substring(endIndex);
		}
		
		retString += extension;
		this.setName(retString);
		this.setPathName(originalPathName.substring(0, originalPathName.lastIndexOf("\\")+1) +
				retString);
	}

	public boolean equals(ImageNode otherImgNode) {
		return this.ID == otherImgNode.ID;
	}
	
	public void setParent(ImageNode temp) {
		this.parent = temp;
	}
	
	public void setChild(ImageNode temp) {
		this.child = temp;
	}
	
	public void revertToDate(ImageNode imgNodeToRevert, Date date) {
		if (imgNodeToRevert.findRoot(imgNodeToRevert).getLog().laterThan(date)) {
			if (imgNodeToRevert.findRoot(imgNodeToRevert).hasChild()) {
				imgNodeToRevert.getChild().setParent(null);
			}
			imgNodeToRevert.findRoot(imgNodeToRevert).setChild(null);
		} else if (!imgNodeToRevert.getLog().laterThan(date)) {
			if (imgNodeToRevert.hasChild()) {
				if (!imgNodeToRevert.getChild().getLog().laterThan(date)) {
					revertToDate(imgNodeToRevert.getChild(), date);
				} else {
					imgNodeToRevert.getChild().setParent(null);
					imgNodeToRevert.setChild(null);
				}
			}
		}
	}

	@Override
	public String toString() {
		return "ImageNode [name= " + this.findChild(this).getName() + "]";
	}
	
}
	
//	public static void main(String[] args) {
//		ArrayList<ImageNode> imgNodes = new ArrayList<ImageNode>();
//		ImageNode x = new ImageNode("C:/somePath/evan.jpg", "evan.jpg");
//		
//		ImageNode y = new ImageNode();
//		y.setParent(x);
//		x.setChild(y);
//		y.addTag(new Tag("Evan"));
//		y.setFileNameAndFilePath();
//		System.out.println(y);
////		imgNodes.add(x);
////		
////		ImageNodeHandler inh = new ImageNodeHandler(imgNodes);
//		
////		ImageNode n = new ImageNode("C:/somePath/photo.jpg" ,"photo.jpg");
////		
////		
////		ImageNode someChild = new ImageNode();
////		someChild.parent = n;
////		n.child = someChild;
////		someChild.tags = n.getTags();
////		someChild.addTag(new Tag("Evan"));
////		someChild.setFileNameAndFilePath();
////		System.out.println(someChild);
////		System.out.println(someChild.getName());
//		
////		System.out.println(n.getFileName());
//
////		n.setChild(someChild);
////		someChild.setParent(n);
////		System.out.println(someChild.getParent());
////		
////		String originalName = someChild.findRoot(someChild).getName();
////		originalName = originalName.substring(0, originalName.indexOf("."));
////		System.out.println(originalName);
//
//	}
//}
