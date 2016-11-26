import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

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
		ImageNode tempChildOfWhole = img.findChild(img);
		
		someChild.setParent(tempChildOfWhole);
		tempChildOfWhole.setChild(someChild);
		someChild.setID(img.getID());
		
		for (Tag t : tempChildOfWhole.getTags()) {
			someChild.addTag(t);
		}
		someChild.addTag(tag);
		
//		for (ImageNode tempImg : toTopToBottomArray(img)) {
//			System.out.println(tempImg.getTags());
//		}
		
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
	
	public ArrayList<ImageNode> getImages() {
		return this.imgs;
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

	
	public static void main(String[] args) {
		ArrayList<ImageNode> allImgs = new ArrayList<ImageNode>();
		Scanner scan = new Scanner(System.in);
		ImageNode i1 = new ImageNode("C:/somepath/photo.jpg", "photo.jpg");
		allImgs.add(i1);
		
		
		
		ImageNodeHandler inh = new ImageNodeHandler(allImgs);
		
		System.out.println(inh.logsFromTopToBottom(i1));
		
		
		String date = "Sun Nov 27 05:00:00 EST 2016";
		SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",
				Locale.ENGLISH);
		Date convertedDate = null;
		
		try {
			convertedDate = format.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		System.out.println("add first tag?");
		scan.nextLine();
		inh.addTag(i1, new Tag("Evan"));
		System.out.println(i1.findChild(i1).getPathName());
		

		

		Date currDate = new Date();
		System.out.println(convertedDate.after(currDate));
		System.out.println("The date to revert to is: " + convertedDate);
		scan.nextLine();
		
		
		
		System.out.println("Add second tag?");
		scan.nextLine();
		inh.addTag(i1, new Tag("Marvin"));
		System.out.println(i1.findChild(i1).getPathName());
		
		System.out.println(inh.logsFromTopToBottom(i1));
		
		System.out.println("Before reverting: " + i1.findChild(i1).getPathName());
		inh.revertAllImages(convertedDate);
		
		System.out.println();
		System.out.println("After reverting: ");
		System.out.println(i1.findChild(i1).getPathName());
		System.out.println("Existing tags: " + inh.getExistingTags());
		
		System.out.println(inh.logsFromTopToBottom(i1));
		
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
