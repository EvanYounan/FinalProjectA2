import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ImageNodeHandler implements Serializable {
	private ArrayList<ImageNode> imgs = new ArrayList<ImageNode>();
	private ArrayList<Tag> existing = new ArrayList<Tag>();
	private ArrayList<Tag> removedTags = new ArrayList<Tag>();
	
	/**
	 * Create an ImageNodeHandler object which loads all of the
	 * ImageNodes into a map with no Logs.
	 * @param loadedImages
	 */
	public ImageNodeHandler(ArrayList<ImageNode> loadedImages) {
		for (ImageNode img : loadedImages) {
			img.setLog("Creating new image. Name: " + img.getName());
			imgs.add(img);
		}
	}
	
	/** Empty constructor for serialization */
	public ImageNodeHandler() {}
	
	public void addImageNodes(ArrayList<ImageNode> imagesToAdd) {
		for (ImageNode img : imagesToAdd) {
			if (!this.hasImage(img)) {
				img.setLog("Creating new image. Name: " + img.getName());
				imgs.add(img);
			}
		}
	}
	
	public boolean hasImage(ImageNode img) {
		for (ImageNode theseImgs : imgs) {
			if (img.getPathName().equals(theseImgs.findChild(theseImgs).getPathName())) {
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Tag> getExistingTags() {
		return this.existing;
	}
	
	public void addTag(ImageNode img, Tag tag) {
		ImageNode someChild = new ImageNode();
		
		someChild.setParent(img);
		img.setChild(someChild);
		someChild.tags = img.getTags();
		someChild.setID(img.getID());
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
	
	public void addTagToRemoved(Tag tag) {
		if (!removedContainsTag(tag)) {
			removedTags.add(tag);
		}
	}
	
	public boolean removedContainsTag(Tag tag) {
		for (Tag t : removedTags) {
			if (tag.getName().equals(t.getName())) {
				return true;
			}
		}
		return false;
	}
	
	public void addTagToExisting(Tag tag) {
		existing.add(tag);
	}
	
	public boolean tagInExisting(Tag tag) {
		for (Tag thisTag : existing) {
			if (thisTag.equals(tag)) {
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<ImageNode> getKeys() {
		ArrayList<ImageNode> temp = new ArrayList<ImageNode>();
		for (ImageNode i : imgs) {
			temp.add(i);
		}
		return temp;
	}
	
	public void removeTag(ImageNode img, Tag tag) {		
		ImageNode someChild = new ImageNode();
		
		someChild.setParent(img);
		img.setChild(someChild);
		someChild.tags = img.getTags();
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
	
//	public String toString() {
//		String retString = "";
//		for (ImageNode thisImg : imgs) {
//			retString += thisImg.getChild().toString() + "\n";
//			if (imgs.get(thisImg) != null) {
//				for (Log thisLog : imgs.get(thisImg)) {
//					retString += thisLog + "\n";
//				}
//			}
//		}
//		return retString;
//	}
	
}
