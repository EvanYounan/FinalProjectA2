import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;

public class Serializing {

	String serFilePath;
	
	public Serializing(String path) {
		this.serFilePath = path;
	}
	
	public void Serialize(ImageNodeHandler imgs) {
		try {
			FileOutputStream fos = new FileOutputStream(this.serFilePath);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(imgs);
			
			oos.close();
			fos.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ImageNodeHandler Deserialize() {
		ImageNodeHandler temp = null;
		try {
			FileInputStream fis = new FileInputStream(this.serFilePath);
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			temp = (ImageNodeHandler)ois.readObject();
			ois.close();
			fis.close();
		} catch (IOException | ClassNotFoundException e){
			e.printStackTrace();
		}
		return temp;
	}
	
	public static void main(String[] args) {
//		Serializing ser = new Serializing("info/this.ser");
//		Tag evan = new Tag("Evan");
//		ArrayList<Tag> x = new ArrayList<Tag>();
//		x.add(evan);
//		x.add(new Tag("Marvin"));
//		ArrayList<Tag> x = null;
//		x = ser.Deserialize();
//		System.out.println(x.toString());
//		
//		ArrayList<Tag> theseTags = new ArrayList<Tag>();
//		theseTags.add(new Tag("Plots"));
//		ser.Serialize(theseTags);
//		x = ser.Deserialize();
//		System.out.println(x.toString());
//		
//		ImageNode img = new ImageNode("C://SomePath/fileImage.jpg",
//				"fileImage.jpg");
//		ser.Serialize(img);
//		System.out.println(img.getID());
//		
//		ImageNode img = null;
//		img = ser.Deserialize();
//		System.out.println(img.getID());
//		Date d2 = new Date();
//		Date d1 = ser.Deserialize();
//		
//		System.out.println(d1);
//		System.out.println(d2);
//		System.out.println(d1.before(d2));
		
	}
}
