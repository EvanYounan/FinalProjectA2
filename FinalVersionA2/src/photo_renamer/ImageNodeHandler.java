package photo_renamer;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Top-level Object to handle ImageNode activities and tags in the system.
 * @author Evannnn
 */
public class ImageNodeHandler implements Serializable {
	 //This class uses proxy design pattern to control the creation of and
	 //access to ImageNode class.
	 
	 //This class uses facade design pattern to perform a process calling Log,
	 //Tag, and ImageNode class.
	
	/** ID for Serialization */
	private static final long serialVersionUID = 1L;
	/** All ImageNodes in the system */
	private ArrayList<ImageNode> imgs = new ArrayList<ImageNode>();
	/** All tags being used currently by ImageNodes */
	private ArrayList<Tag> existing = new ArrayList<Tag>();
	/** All removed tags of the system */
	private ArrayList<Tag> removedTags = new ArrayList<Tag>();
	
	/**
	 * Create an ImageNodeHandler object which loads all of the
	 * ImageNodes into an ArrayList. Create a log for all these ImageNodes.
	 * 
	 * @param loadedImages - all ImageNodes to be loaded
	 */
	public ImageNodeHandler(ArrayList<ImageNode> loadedImages) {
		for (ImageNode img : loadedImages) {
			img.setLog("Creating new image. Name: " + img.getName());
			imgs.add(img);
		}
	}
	
	/** Empty constructor for serialization */
	public ImageNodeHandler() {}
	
	/**
	 * If there are ImageNodes already loaded into the system, append these new ImageNodes
	 * to the list of ImageNodes if they are not in the system.
	 * 
	 * @param imagesToAdd - all ImageNodes to be added
	 */
	public void addImageNodes(ArrayList<ImageNode> imagesToAdd) {
		for (ImageNode img : imagesToAdd) {
			if (!this.hasImage(img)) {
				img.setLog("Creating new image. Name: " + img.getName());
				imgs.add(img);
			}
		}
	}
	
	/**
	 * Return true if and only if the path of the given ImageNode matches
	 * the path of any ImageNode straight-line structure's absolute child (absolute
	 * child holds current configuration of every file).
	 * 
	 * @param img - ImageNode to be looked for
	 * @return		true if and only if this ImageNode is not in the system
	 */
	public boolean hasImage(ImageNode img) {
		for (ImageNode theseImgs : imgs) {
			if (img.getPathName().equals(theseImgs.findChild(theseImgs).getPathName())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Return all tags that are currently being used.
	 * 
	 * @return	all tags being used
	 */
	public ArrayList<Tag> getExistingTags() {
		return this.existing;
	}
	
	/**
	 * Return all tags that were used before but are not being currently used.
	 * 
	 * @return	all tags used before that are not being used currently
	 */
	public ArrayList<Tag> getRemovedTags() {
		return this.removedTags;
	}
	
	/**
	 * Add a tag to the ImageNode given. This function sets up the new ImageNode's attributes
	 * and appends it as the absolute child of the straight-lined structure of ImageNodes.
	 * 
	 * @param img - any ImageNode in the straight-lined structure
	 * @param tag - tag to be added to the ImageNode's absolute child
	 */
	public void addTag(ImageNode img, Tag tag) {
		if (!tag.getName().equals("") && !img.findChild(img).hasTag(tag)) {
			ImageNode someChild = new ImageNode();
			ImageNode tempChildOfWhole = img.findChild(img);
			
			someChild.setParent(tempChildOfWhole);
			tempChildOfWhole.setChild(someChild);
			someChild.setID(img.getID());
			
			for (Tag t : tempChildOfWhole.getTags()) {
				someChild.addTag(t);
			}
			someChild.addTag(tag);
			
			if (!tagInExisting(tag)) {
				addTagToExisting(tag);
			}
			
			someChild.setFileNameAndFilePath();
			someChild.setLog("Adding tag @" + tag.getName() + ". New Name: " + someChild.getName());
		
			File oldFile = new File(img.getPathName());
			File newFile = new File(someChild.getPathName());
			oldFile.renameTo(newFile);
		} else {
			System.out.println("Doesn't have the tag");
		}
	}
	
	/**
	 * Remove a tag from the ImageNode given. This function sets up the new ImageNode's attributes
	 * and appends it as the absolute child of the straight-lined structure of ImageNodes.
	 * 
	 * @param img - any ImageNode in the straight-lined structure
	 * @param tag - tag to be added to the ImageNode's absolute child
	 */
	public void removeTag(ImageNode img, Tag tag) {		
		if (!tag.getName().equals("")) {
			ImageNode someChild = new ImageNode();
			ImageNode tempChildOfWhole = img.findChild(img);
			
			someChild.setParent(tempChildOfWhole);
			tempChildOfWhole.setChild(someChild);
			someChild.setID(img.getID());
			
			for (Tag t : tempChildOfWhole.getTags()) {
				someChild.addTag(t);
			}
			
			someChild.setID(img.getID());
			someChild.removeTag(tag);
			updateExisting();
			addTagToRemoved(tag);
			someChild.setFileNameAndFilePath();
			someChild.setLog("Removing tag @" + tag.getName() + ". New Name: " + someChild.getName());
			
			File oldFile = new File(img.getPathName());
			File newFile = new File(someChild.getPathName());
			oldFile.renameTo(newFile);
		}
	}
	
	/**
	 * Add a tag to the removed list of tags.
	 * 
	 * @param tag - tag to be added to removed list of tags.
	 */
	public void addTagToRemoved(Tag tag) {
		if (!removedContainsTag(tag) && !tagInExisting(tag)) {
			removedTags.add(tag);
		}
	}
	
	/**
	 * Add a tag to the existing list of tags.
	 * 
	 * @param tag - tag to be added to existing list of tags.
	 */
	public void addTagToExisting(Tag tag) {
		existing.add(tag);
	}
	
	/**
	 * Return true if and only if the removed list of tags contains this tag.
	 * 
	 * @param tag - to be looked for in removed list of tags
	 * @return		true if tag is in the removed list of tags
	 */
	public boolean removedContainsTag(Tag tag) {
		for (Tag t : removedTags) {
			if (tag.getName().equals(t.getName())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Return true if and only if the tag is in the existing list of tags.
	 * 
	 * @param tag - tag to be looked for
	 * @return		true if and only this tag is in the existing list of tags.
	 */
	public boolean tagInExisting(Tag tag) {
		for (Tag thisTag : existing) {
			if (thisTag.equals(tag)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Return all of the ImageNodes loaded into the system.
	 * 
	 * @return		all ImageNodes loaded into system
	 */
	public ArrayList<ImageNode> getImages() {
		return this.imgs;
	}
	
	/**
	 * Rename a file given the old path and new path of the file.
	 * 
	 * @param oldPath - old path of the file
	 * @param newPath - new path of the file
	 */
	public void renameFile(String oldPath, String newPath) {
		File oldFile = new File(oldPath);
		File newFile = new File(newPath);
		oldFile.renameTo(newFile);
	}
	
	/**
	 * Given any ImageNode, return the ImageNodes in its straight-line structure as an array
	 * beginning from the root and ending at the absolute child.
	 * 
	 * @param imgN - any ImageNode in the straight-line of ImageNodes
	 * @return		a list of all ImageNodes beginning from the root to the absolute child
	 */
	public ArrayList<ImageNode> toTopToBottomArray(ImageNode imgN) {
		ArrayList<ImageNode> temp = new ArrayList<ImageNode>();
		imgN = imgN.findRoot(imgN);
		while (imgN != null) {
			temp.add(imgN);
			imgN = imgN.getChild();
		}
		return temp;
	}
	
	/**
	 * Given any ImageNode, return all of the logs in the straight-line structure as an array
	 * beginning from the root's logs and ending at the absolute child's log.
	 * 
	 * @param imgN - any ImageNode in the straight-line of ImageNodes
	 * @return		a list of all Logs beginning from the root ImageNode to the absolute child ImageNode
	 */
	public ArrayList<Log> logsFromTopToBottom(ImageNode imgN) {
		ArrayList<ImageNode> allImgNodes = toTopToBottomArray(imgN);
		ArrayList<Log> logsToReturn = new ArrayList<Log>();
		for (ImageNode img : allImgNodes) {
			logsToReturn.add(img.getLog());
		}
		return logsToReturn;
	}
	
	/**
	 * Based on all of the ImageNodes loaded into the system, update the existing
	 * list of tags by looking at the absolute child of all ImageNodes (which contain
	 * current configuration of ImageNodes).
	 */
	public void updateExisting() {
		existing.clear();
		for (ImageNode imgN : imgs) {
			for (Tag t : imgN.findChild(imgN).getTags()) {
				if (!tagInExisting(t)) {
					addTagToExisting(t);
				}
			}
		}
	}
	
	/**
	 * Return true if and only if the ImageNode's absolute child contains the tag given.
	 *  
	 * @param tag - tag to be looked for
	 * @param thisImg - ImageNode to check for tag
	 * @return		true if ImageNode contains the tag in current configuration
	 */
	public boolean canRemoveTag(Tag tag, ImageNode thisImg) {
		if (thisImg.findChild(thisImg).getPathName().contains("@" + tag.getName())) {
			return true;
		}
		return false;
	}
	
	/**
	 * Remove the given tag from all ImageNodes loaded into the system.
	 * 
	 * @param tag - tag to be removed
	 */
	public void removeTagFromAll(Tag tag) {
		for (ImageNode thisImg : imgs) {
			if (canRemoveTag(tag, thisImg.findChild(thisImg))) {
				removeTag(thisImg.findChild(thisImg), tag);
			}
		}
	}
	
	/**
	 * Add the given tag to all ImageNodes loaded into the system.
	 * 
	 * @param tag - tag to be added
	 */
	public void addTagToAll(Tag tag) {
		for (ImageNode thisImg : imgs) {
			addTag(thisImg.findChild(thisImg), tag);
		}
	}
	
	/**
	 * Return the ImageNode in the system that contains the path given.
	 * 
	 * @param path - path of the ImageNode
	 * @return		ImageNode with the path given
	 */
	public ImageNode findImageNode(String path) {
		for (ImageNode tempImage : imgs) {
			if (path.equals(tempImage.findChild(tempImage).getPathName())) {
				return tempImage;
			}
		}
		return null;
	}
	
	/**
	 * Revert all ImageNodes in the system to the given date.
	 * 
	 * @param date - date to revert all ImageNodes to
	 */
	public void revertAllImages(Date date) {
		for (ImageNode img : this.imgs) {
			String oldPath = img.findChild(img).getPathName();
			img.revertToDate(img, date);
			String newPath = img.findChild(img).getPathName();
			renameFile(oldPath, newPath);
		}
		updateExisting();
	}
}
