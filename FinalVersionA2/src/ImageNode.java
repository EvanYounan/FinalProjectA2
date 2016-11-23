
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class ImageNode extends FileNode {
	
	public ArrayList<Tag> tags;
	private long ID;
	Random x;
	private ImageNode parent;
	private ImageNode child;
	private Log log;
	
	public ImageNode(String path, String name) {
		super(path, name);
		this.tags = new ArrayList<>();
		x = new Random();
		this.ID = x.nextLong(); 
	}
	
	public ImageNode() {
		this.tags = new ArrayList<>();
	}
	
	public void setLog(String description) {
		this.log = new Log(description);
	}
	
	public Log getLog() {
		return this.log;
	}
	
	public void addTag(Tag tag) {
		tags.add(tag);
	}
	
	public void removeTag(Tag tag) {
		for (Tag t : tags) {
			if (tag.equals(t)) {
				tags.remove(t);
				break;
			}
		}
	}
	
	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}
	
	public ImageNode getParent() {
		return this.parent;
	}
	
	public ImageNode getChild() {
		return this.child;
	}
	
	public ImageNode findChild(ImageNode img) {
		if (img.hasChild()) {
			return findChild(img.getChild());
		}
		return img;
	}
	
	public ImageNode findRoot(ImageNode img) {
		if (img.hasParent()) {
			return findRoot(img.getParent());
		}
		return img;
	}
	
	public boolean hasParent() {
		return this.parent != null;
	}
	
	public boolean hasChild() {
		return this.child != null;
	}
	
	public Tag retrieveTag(Tag tag) {
		for (Tag existingTag : this.tags) {
			if (tag.equals(existingTag)) {
				return existingTag;
			}
		}
		return null;
	}
	
	public void clearTags(ImageNode img) {
		this.tags.clear();
	}
	
	public ArrayList<Tag> getTags() {
		return this.tags;
	}
	
	public boolean hasTag(Tag tag) {
		for (Tag thisTag : this.tags) {
			if (thisTag.equals(tag)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean tagInList(ArrayList<Tag> temp, Tag t) {
		for (Tag tg : temp) {
			if (t.equals(tg)) {
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

	@Override
	public String toString() {
		return "ImageNode [path=" + this.getName() + ", name= " + this.getPathName() + "]";
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
