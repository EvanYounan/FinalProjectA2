
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.swing.JFileChooser;

public class LoadingDirectory {
	
	JFileChooser jfc;
	
	public LoadingDirectory() {
		jfc = new JFileChooser();
	}
	
	/**
	 * Given a certain Path to a directory, find all the files in the
	 * directory that contain the .jpg, .png, and .tif extensions
	 * and return them as an ArrayList.
	 * 
	 * @param fileNames
	 * @param dir
	 * @return
	 */
	public ArrayList<File> getFileNames(ArrayList<File> fileNames, Path dir) {
		
		try {
			DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
			
			for (Path path : stream) {
				if (path.toFile().isDirectory()) {
	                getFileNames(fileNames, path);
	            } else {
	            	if (path.toAbsolutePath().toString().endsWith(".jpg") ||
	            			path.toAbsolutePath().toString().endsWith(".png") ||
	            			path.toAbsolutePath().toString().endsWith(".tif")) {
	            		System.out.println("HEHE PATHS: " + path.toFile().toString());
	            		fileNames.add(path.toFile());
	            	}
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return fileNames;
	} 
	
	public ArrayList<File> showFileChooserAndReturnFiles() {
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		ArrayList<File> fileNames = new ArrayList<File>();
		
		if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			
			String absolutePathOfFile = jfc.getSelectedFile().getAbsolutePath();
			Path path = Paths.get(absolutePathOfFile);
			
			
			LoadingDirectory x = new LoadingDirectory();
			fileNames = x.getFileNames(fileNames, path);
		}
		return fileNames;
	}
	
	public ArrayList<ImageNode> convertToImageNodes(ArrayList<File> files) {
		ArrayList<ImageNode> theseImgs = new ArrayList<ImageNode>();
		
		for (File x : files) {
			theseImgs.add(new ImageNode(x.getAbsolutePath(),
					x.getName()));
		}
		return theseImgs;
	}
	
	public static void main(String[] args) {
//		JFileChooser jfc = new JFileChooser();
//		File serFile = new File("src/imageNodeHandlerInformation.ser");
//		if (serFile.exists()) {
//			if (serFile.length() == 0) {
//				
//			}
//		}
//		System.out.println(serFile.exists());
//		System.out.println(serFile.length());
//		ImageNodeHandler inh;
//	
//		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//		ArrayList<File> fileNames = new ArrayList<File>();
//		
//		if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
//			
//			String absolutePathOfFile = jfc.getSelectedFile().getAbsolutePath();
//			Path path = Paths.get(absolutePathOfFile);
//			
//			
//			LoadingDirectory x = new LoadingDirectory();
//			fileNames = x.getFileNames(fileNames, path);
////			System.out.println(fileNames);
//			System.out.println(fileNames);
//			
//		}
//		ArrayList<ImageNode> theseImgs = new ArrayList<ImageNode>();
//		for (File x : fileNames) {
//			theseImgs.add(new ImageNode(x.getAbsolutePath(),
//					x.getName()));
//		}
//		inh = new ImageNodeHandler(theseImgs);
//		Tag y = new Tag("Marvin");
//		Tag a = new Tag("Evan");
//		Tag d = new Tag("Jacky");
//		inh.addTagToAll(y);
//		inh.addTagToAll(a);
//		inh.addTagToAll(d);
//		System.out.println(inh);
//
//
//		System.out.println();
//		System.out.println();
//		System.out.println();
//		System.out.println();
//		System.out.println("REMOVING ALL THE TAGS.");
//		
//		//This tag does not exist for these pictures, only "Marvin" does.
////		inh.removeTagFromAll(new Tag("Evan"));
////		System.out.println(inh);
//		
//		Serializing ser = new Serializing("src/imageNodeHandlerInformation.ser");
//		ser.Serialize(inh);
//
//		
////		Serializing ser = new Serializing("src/imageNodeHandlerInformation.ser");
////		ImageNodeHandler inh = ser.Deserialize();
////		if (inh == null) {
////			System.out.println("Hi mom");
////		}
//		/**
//		 * Deserialization process
//		 */
////		Serializing ser = new Serializing("src/imageNodeHandlerInformation.ser");
////		ImageNodeHandler inh = null;
////		inh = ser.Deserialize();
////		System.out.println(inh);
////		inh.removeTagFromAll(new Tag("Marvin"));
////		System.out.println(inh);
//
	}
	
}
