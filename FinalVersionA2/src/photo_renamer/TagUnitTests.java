package photo_renamer;
import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Test;

public class TagUnitTests {

	@Test
	public void testOneTagAddedToOneImageNode() {
		ArrayList<ImageNode> imgs = new ArrayList<ImageNode>();
		ImageNode img = new ImageNode("C:/somePath/photo.jpg", "photo.jpg");
		imgs.add(img);
		ImageNodeHandler inh = new ImageNodeHandler(imgs);
		
		Tag newTag = new Tag("Evan");
		
		inh.addTag(img, newTag);
		
		ArrayList<Tag> existingTags = new ArrayList<Tag>();
		existingTags.add(newTag);
		
		assertEquals(existingTags, inh.getExistingTags());
	}
	
	@Test
	public void testOneTagAddedToTwoImageNodes() {
		ArrayList<ImageNode> imgs = new ArrayList<ImageNode>();
		ImageNode imgOne = new ImageNode("C:/somePath/photo.jpg", "photo.jpg");
		ImageNode imgTwo = new ImageNode("C:/somePath/photo2.jpg", "photo2.jpg");
		imgs.add(imgOne);
		imgs.add(imgTwo);
		ImageNodeHandler inh = new ImageNodeHandler(imgs);
		
		Tag newTag = new Tag("Evan");
		
		inh.addTagToAll(newTag);
		
		ArrayList<Tag> existingTags = new ArrayList<Tag>();
		existingTags.add(newTag);
		
		assertEquals(existingTags, inh.getExistingTags());
	}
	
	@Test
	public void testTwoTagsAddedToOneImage() {
		ArrayList<ImageNode> imgs = new ArrayList<ImageNode>();
		ImageNode imgOne = new ImageNode("C:/somePath/photo.jpg", "photo.jpg");
		imgs.add(imgOne);		
		ImageNodeHandler inh = new ImageNodeHandler(imgs);
		
		Tag firstTag = new Tag("Evan");
		Tag secondTag = new Tag("Marvin");
		
		inh.addTag(imgOne, firstTag);
		inh.addTag(imgOne, secondTag);
		
		ArrayList<Tag> existingTags = new ArrayList<Tag>();
		existingTags.add(firstTag);
		existingTags.add(secondTag);
		
		assertEquals(existingTags, inh.getExistingTags());
	}
	
	@Test
	public void testTwoTagsAddedToOneImageAndOneRemoved() {
		ArrayList<ImageNode> imgs = new ArrayList<ImageNode>();
		ImageNode imgOne = new ImageNode("C:/somePath/photo.jpg", "photo.jpg");
		imgs.add(imgOne);		
		ImageNodeHandler inh = new ImageNodeHandler(imgs);
		
		Tag firstTag = new Tag("Evan");
		Tag secondTag = new Tag("Marvin");
		
		inh.addTag(imgOne, firstTag);
		inh.addTag(imgOne, secondTag);
		
		inh.removeTag(imgOne, firstTag);
		
		ArrayList<Tag> removedTags = new ArrayList<Tag>();
		ArrayList<Tag> existingTags = new ArrayList<Tag>();
		existingTags.add(secondTag);
		removedTags.add(firstTag);
		
		assertEquals(existingTags, inh.getExistingTags());
		assertEquals(removedTags, inh.getRemovedTags());
	}
	
	@Test
	public void testTwoOfSameTagsAddedToOneImageNode() {
		ArrayList<ImageNode> imgs = new ArrayList<ImageNode>();
		ImageNode imgOne = new ImageNode("C:/somePath/photo.jpg", "photo.jpg");
		imgs.add(imgOne);		
		ImageNodeHandler inh = new ImageNodeHandler(imgs);
		
		Tag firstTag = new Tag("Evan");
		
		inh.addTag(imgOne, firstTag);
		inh.addTag(imgOne, firstTag);
		
		ArrayList<Tag> existingTags = new ArrayList<Tag>();
		existingTags.add(firstTag);
		
		assertEquals(existingTags, inh.getExistingTags());
	}
	
	@Test
	public void testAddEmptyStringTagToImageNode() {
		ArrayList<ImageNode> imgs = new ArrayList<ImageNode>();
		ImageNode imgOne = new ImageNode("C:/somePath/photo.jpg", "photo.jpg");
		imgs.add(imgOne);		
		ImageNodeHandler inh = new ImageNodeHandler(imgs);
		
		Tag firstTag = new Tag("");
		
		inh.addTag(imgOne, firstTag);
		
		ArrayList<Tag> existingTags = new ArrayList<Tag>();
		
		assertEquals(existingTags, inh.getExistingTags());
	}
	
	@Test
	public void testTwoTagsAddedToAllImageNodesAndRemoveOneFromOneImageNode() {
		ArrayList<ImageNode> imgs = new ArrayList<ImageNode>();
		ImageNode imgOne = new ImageNode("C:/somePath/photo.jpg", "photo.jpg");
		ImageNode imgTwo = new ImageNode("C:/somePath/photo2.jpg", "photo2.jpg");
		imgs.add(imgOne);		
		imgs.add(imgTwo);
		ImageNodeHandler inh = new ImageNodeHandler(imgs);
		
		Tag firstTag = new Tag("Evan");
		Tag secondTag = new Tag("Marvin");
		
		inh.addTagToAll(firstTag);
		inh.addTagToAll(secondTag);
		
		inh.removeTag(imgOne, firstTag);
		
		ArrayList<Tag> removedTags = new ArrayList<Tag>();
		ArrayList<Tag> existingTags = new ArrayList<Tag>();
		existingTags.add(firstTag);
		existingTags.add(secondTag);
		
		assertEquals(true, existingTags.containsAll(inh.getExistingTags()));
		assertEquals(true, removedTags.containsAll(inh.getRemovedTags()));
	}
	
	@Test
	public void testThreeTagsAddedToAllImageNodesAndRemoveOneFromAllImageNodes() {
		ArrayList<ImageNode> imgs = new ArrayList<ImageNode>();
		ImageNode imgOne = new ImageNode("C:/somePath/photo.jpg", "photo.jpg");
		ImageNode imgTwo = new ImageNode("C:/somePath/photo2.jpg", "photo2.jpg");
		ImageNode imgThree = new ImageNode("C:/somePath/photo3.jpg", "photo3.jpg");
		
		imgs.add(imgOne);		
		imgs.add(imgTwo);
		imgs.add(imgThree);
		
		ImageNodeHandler inh = new ImageNodeHandler(imgs);
		
		Tag firstTag = new Tag("Evan");
		Tag secondTag = new Tag("Marvin");
		Tag thirdTag = new Tag("Tony");
		
		inh.addTagToAll(firstTag);
		inh.addTagToAll(secondTag);
		inh.addTagToAll(thirdTag);
		
		inh.removeTagFromAll(firstTag);
		
		ArrayList<Tag> removedTags = new ArrayList<Tag>();
		removedTags.add(firstTag);
		ArrayList<Tag> existingTags = new ArrayList<Tag>();
		existingTags.add(secondTag);
		existingTags.add(thirdTag);
		
		assertEquals(true, existingTags.containsAll(inh.getExistingTags()));
		assertEquals(true, removedTags.containsAll(inh.getRemovedTags()));
	}
}
