import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Top-level Object to handle ImageNode activities and tags in the system.
 * @author Evannnn
 */
public class ImageNodeHandler implements Serializable {
	
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
	 * Add a tag to the ImageNode given. This function sets up the new ImageNode's attributes
	 * and appends it as the absolute child of the straight-lined structure of ImageNodes.
	 * 
	 * @param img - any ImageNode in the straight-lined structure
	 * @param tag - tag to be added to the ImageNode's absolute child
	 */
	public void addTag(ImageNode img, Tag tag) {
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
	}
	
	/**
	 * Add a tag to the removed list of tags.
	 * 
	 * @param tag - tag to be added
	 */
	public void addTagToRemoved(Tag tag) {
		if (!removedContainsTag(tag) && !tagInExisting(tag)) {
			removedTags.add(tag);
		}
	}
	
	/**
	 * Add a tag to the existing
	 * @param tag
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
	
	public boolean tagInExisting(Tag tag) {
		for (Tag thisTag : existing) {
			if (thisTag.equals(tag)) {
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<ImageNode> getImages() {
		return this.imgs;
	}
	
	public void removeTag(ImageNode img, Tag tag) {		
		ImageNode someChild = new ImageNode();
		
		someChild.setParent(img);
		img.setChild(someChild);
		someChild.setTags(img.getTags());
		someChild.setID(img.getID());
		someChild.removeTag(tag);
		addTagToRemoved(tag);
		updateExisting();
		someChild.setFileNameAndFilePath();
		someChild.setLog("Removing tag @" + tag.getName() + ". New Name: " + someChild.getName());
		
		File oldFile = new File(img.getPathName());
		File newFile = new File(someChild.getPathName());
		oldFile.renameTo(newFile);
//		System.out.println(removedTags);
	}
	
	public void renameFile(String oldPath, String newPath) {
		File oldFile = new File(oldPath);
		File newFile = new File(newPath);
		oldFile.renameTo(newFile);
	}
	
	public ArrayList<ImageNode> toTopToBottomArray(ImageNode imgN) {
		ArrayList<ImageNode> temp = new ArrayList<ImageNode>();
		imgN = imgN.findRoot(imgN);
		while (imgN != null) {
			temp.add(imgN);
			imgN = imgN.getChild();
		}
		return temp;
	}
	
	public ArrayList<Log> logsFromTopToBottom(ImageNode imgN) {
		ArrayList<ImageNode> allImgNodes = toTopToBottomArray(imgN);
		ArrayList<Log> logsToReturn = new ArrayList<Log>();
		for (ImageNode img : allImgNodes) {
			logsToReturn.add(img.getLog());
		}
		return logsToReturn;
	}
	
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
	
	public boolean canRemoveTag(Tag tag, ImageNode thisImg) {
		if (thisImg.getPathName().contains("@" + tag.getName())) {
			System.out.println("Apparently this file reports a truth!");
			return true;
		}
		System.out.println("Could not remove Tags that do not exist for the picture!");
		return false;
	}
	
	public void removeTagFromAll(Tag tag) {
		for (ImageNode thisImg : imgs) {
			if (canRemoveTag(tag, thisImg.findChild(thisImg))) {
				removeTag(thisImg.findChild(thisImg), tag);
			}
		}
	}
	
	public void addTagToAll(Tag tag) {
		for (ImageNode thisImg : imgs) {
			addTag(thisImg.findChild(thisImg), tag);
		}
	}
	
	public ImageNode findImageNode(String path) {
		for (ImageNode tempImage : imgs) {
			if (path.equals(tempImage.findChild(tempImage).getPathName())) {
				return tempImage;
			}
		}
		System.out.println("Got a null return from findImageNode");
		return null;
	}
	
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
