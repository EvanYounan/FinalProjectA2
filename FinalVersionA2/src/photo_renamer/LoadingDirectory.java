package photo_renamer;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.swing.JFileChooser;

public class LoadingDirectory {
	
	/** JFileChooser to be used */
	private JFileChooser jfc;
	
	/**
	 * Initialize a new JFileChooser object.
	 */
	public LoadingDirectory() {
		jfc = new JFileChooser();
	}
	
	/**
	 * Given a certain Path to a directory, recursively find all the files in the
	 * directory that contain the .jpg, .png, and .tif extensions
	 * and return them as an ArrayList.
	 * 
	 * @param fileNames - list of all files to be returned 
	 * @param dir - path to directory
	 * @return		list of all file in the directory that are images
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
	            			path.toAbsolutePath().toString().endsWith(".tif") ||
	            			path.toAbsolutePath().toString().endsWith(".JPG") ||
	            			path.toAbsolutePath().toString().endsWith(".jpeg") ||
	            			path.toAbsolutePath().toString().endsWith(".bmp")) {
	            		fileNames.add(path.toFile());
	            	}
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return fileNames;
	} 
	
	/**
	 * Show dialog for File Chooser and allow user to select a directory to return
	 * all of the files in that directory.
	 * 
	 * @return		all of the files in the user's given directory
	 */
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
	
	/**
	 * Convert all files given to ImageNodes
	 * 
	 * @param files - files to be converted to ImageNodes
	 * @return		list of all ImageNodes
	 */
	public ArrayList<ImageNode> convertToImageNodes(ArrayList<File> files) {
		ArrayList<ImageNode> theseImgs = new ArrayList<ImageNode>();
		
		for (File x : files) {
			theseImgs.add(new ImageNode(x.getAbsolutePath(),
					x.getName()));
		}
		return theseImgs;
	}
}
