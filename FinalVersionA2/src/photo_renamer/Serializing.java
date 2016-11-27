package photo_renamer;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serializing {

	/** Path of Serialized file */
	private String serFilePath;
	
	/**
	 * Create a Serializing object with the given path to the serialization file.
	 * 
	 * @param path - path to serialization file
	 */
	public Serializing(String path) {
		this.serFilePath = path;
	}
	
	/**
	 * Serialize the top-level ImageNodeHandler which manages all the ImageNodes in the
	 * systen.
	 * 
	 * @param inh - object which contains information on all ImageNodes, tags, etc., in the system
	 */
	public void Serialize(ImageNodeHandler inh) {
		try {
			FileOutputStream fos = new FileOutputStream(this.serFilePath);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(inh);
			oos.close();
			fos.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Deserialize the file from the given path and return the top-level ImageNodeHandler
	 * which holds all the information on all ImageNodes in the system.
	 * 
	 * @return - object which contains all information on the ImageNodes, tags, etc., in the system
	 */
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
}
