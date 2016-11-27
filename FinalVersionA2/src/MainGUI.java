import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JList;

public class MainGUI {

	private JFrame frame;
	private ImageNodeHandler inh;
	File f = new File("src/imageNodeHandlerInformation.ser");
	Serializing ser = new Serializing("src/imageNodeHandlerInformation.ser");
	private JTextField retrievedTextField;
	private JTextField textField;
	private JTextField textFieldfromAll;
	@SuppressWarnings("rawtypes")
	private JList listOfImages;
	@SuppressWarnings("rawtypes")
	private JList listOfActivities;
	@SuppressWarnings("rawtypes")
	private JList listOfExisting;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI window = new MainGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainGUI() {
		initialize();
	}
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Photo Renamer");
		frame.setBounds(100, 100, 968, 717);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout());
		panel.setBounds(0, 0, 952, 304);
		
		JLabel statusLabel = new JLabel("Status bar:");
		statusLabel.setBounds(10, 653, 808, 14);
		frame.getContentPane().add(statusLabel);
		
		listOfActivities = new JList();
		listOfActivities.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				statusLabel.setText("Status bar: The list of all operations performed on the current file "
						+ "being operated on.");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				statusLabel.setText("Status bar:");
			}
		});
		listOfImages = new JList();
		listOfImages.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (listOfImages.getModel().getSize() == 0) {
					statusLabel.setText("Status bar: Please click Load Files at the bottom of the screen"
							+ " to begin operating on them.");
				} else {
					statusLabel.setText("Status bar: All of the images loaded into the program. Click "
						+ "an image to view recent activity and to begin performing operations on the"
						+ " image.");
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				statusLabel.setText("Status bar:");
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
				ImageNode temp = (ImageNode) listOfImages.getSelectedValue();
				retrievedTextField.setText(temp.findChild(temp).getPathName());
				
				ArrayList<ImageNode> topToBottom = inh.toTopToBottomArray(temp);
				ArrayList<Log> logsTopToBottom = new ArrayList<Log>();
				for (ImageNode imgN : topToBottom) {
					logsTopToBottom.add(imgN.getLog());
				}
				
				listOfActivities.setListData(logsTopToBottom.toArray());
			}
		});
//		display.setEditable(false);
		JScrollPane scroller = new JScrollPane(listOfImages);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panel.add(scroller);
		frame.getContentPane().add(panel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(new GridLayout());
		panel_1.setBounds(10, 347, 290, 166);
		listOfExisting = new JList();
		listOfExisting.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				statusLabel.setText("Status bar: The current tags being used in the program.");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				statusLabel.setText("Status bar:");
			}
		});
		listOfExisting.setBounds(10, 347, 290, 166);
		JScrollPane scroll = new JScrollPane(listOfExisting);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		JLabel lblNewLabel = new JLabel("Existing Tags");
		panel_1.add(scroll);
		frame.getContentPane().add(panel_1);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(599, 347, 290, 140);
		
		listOfActivities.setBounds(599, 347, 290, 149);
		JScrollPane scroll2 = new JScrollPane(listOfActivities);
		scroll2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panel_2.add(scroll2);
		panel_2.setLayout(new GridLayout());
		frame.getContentPane().add(panel_2);
		
		if (f.exists()) {
			if (f.length() > 0) {
				inh = ser.Deserialize();
				listOfImages.setListData(inh.getImages().toArray());
				ArrayList<Tag> someExistingTags = inh.getExistingTags();
				listOfExisting.setListData(someExistingTags.toArray());
			} else {
				inh = new ImageNodeHandler();
			}
		}
		
		
		//DO SOMETHING BEFORE CLOSING THE PROGRAM
		frame.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
		        if (inh.getImages() != null) {
		        	ser.Serialize(inh);
		        	System.out.println("System has saved all images and is closing...");
		        } else {
		        	System.out.println("There was nothing to save since no images were loaded in."
		        			+ " Closing now...");
		        }
		    }
		});
		
		JButton btnNewButton = new JButton("Load Files");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				LoadingDirectory ld = new LoadingDirectory();
				ArrayList<File> tempFiles = ld.showFileChooserAndReturnFiles();
				File f = new File("src/imageNodeHandlerInformation.ser");
				if (f.exists()) {
					if (f.length() > 0 || listOfImages.getModel().getSize() > 0) {
						inh.addImageNodes(ld.convertToImageNodes(tempFiles));
					} else {
						inh = new ImageNodeHandler(ld.convertToImageNodes(tempFiles));
					}
				}
				ArrayList<Tag> someExistingTags = inh.getExistingTags();
				listOfExisting.setListData(someExistingTags.toArray());
				listOfImages.setListData(inh.getImages().toArray());
				
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				statusLabel.setText("Status bar: Choose a directory to load all"
						+ " of the images in that directory into the program.");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				statusLabel.setText("Status bar:");
			}
		});
		btnNewButton.setBounds(10, 626, 932, 23);
		frame.getContentPane().add(btnNewButton);
		
		JButton addTagButton = new JButton("Add Tag");
		addTagButton.setBounds(10, 558, 89, 23);
		frame.getContentPane().add(addTagButton);
		
		JButton removeTagButton = new JButton("Remove Tag");

		removeTagButton.setBounds(109, 558, 108, 23);
		frame.getContentPane().add(removeTagButton);
		
		retrievedTextField = new JTextField();
		retrievedTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				statusLabel.setText("Status bar: The path of the current file being operated on.");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				statusLabel.setText("Status bar:");
			}
		});
		retrievedTextField.setBounds(171, 527, 418, 20);
		frame.getContentPane().add(retrievedTextField);
		retrievedTextField.setColumns(10);
		//addTag
		addTagButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				String newTagName = textField.getText().trim();
				Tag tempTag = new Tag(newTagName);
				
				ImageNode tempNode = inh.findImageNode(retrievedTextField.getText());
				
				if (!retrievedTextField.getText().isEmpty()) { 
					if (tempNode.findChild(tempNode) != null) {
						inh.addTag(tempNode.findChild(tempNode), tempTag);
					}
				}
				
				listOfExisting.setListData(inh.getExistingTags().toArray());
				retrievedTextField.setText(tempNode.findChild(tempNode).getPathName());
				listOfImages.setListData(inh.getImages().toArray());
				
				ImageNode temp = inh.findImageNode(retrievedTextField.getText());
				
				ArrayList<ImageNode> topToBottom = inh.toTopToBottomArray(temp);
				ArrayList<Log> logsTopToBottom = new ArrayList<Log>();
				for (ImageNode imgN : topToBottom) {
					logsTopToBottom.add(imgN.getLog());
				}
				
				listOfActivities.setListData(logsTopToBottom.toArray());
				
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				statusLabel.setText("Status bar: This button allows you to add a tag with"
						+ " the name inside the box to the retrieved file above.");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				statusLabel.setText("Status bar:");
			}
		});
		
		removeTagButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				String newTagToRemove = textField.getText().trim();
				Tag tagToRemove = new Tag(newTagToRemove);
				ImageNode tempNode = inh.findImageNode(retrievedTextField.getText());
				if (!retrievedTextField.getText().isEmpty()) { 
					inh.removeTag(tempNode.findChild(tempNode), tagToRemove);
				}
				listOfExisting.setListData(inh.getExistingTags().toArray());
				retrievedTextField.setText(tempNode.findChild(tempNode).getPathName());
//				ArrayList<Tag> someExistingTags = inh.getExistingTags();
//				list.setListData(someExistingTags.toArray());
				listOfImages.setListData(inh.getImages().toArray());
				
				ImageNode temp = inh.findImageNode(retrievedTextField.getText());
				
				ArrayList<ImageNode> topToBottom = inh.toTopToBottomArray(temp);
				ArrayList<Log> logsTopToBottom = new ArrayList<Log>();
				for (ImageNode imgN : topToBottom) {
					logsTopToBottom.add(imgN.getLog());
				}
				
				listOfActivities.setListData(logsTopToBottom.toArray());
				
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				statusLabel.setText("Status bar: This button allows you to remove a tag from"
						+ " the retrieved file above.");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				statusLabel.setText("Status bar:");
			}
		});
		
		textField = new JTextField();
		textField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				statusLabel.setText("Status bar: Type the name of the tag you would like to remove "
						+ "or add to the retrieved file above.");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				statusLabel.setText("Status bar:");
			}
		});
		textField.setBounds(227, 559, 207, 20);
		textField.setEditable(true);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		textFieldfromAll = new JTextField();
		textFieldfromAll.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				statusLabel.setText("Status bar: Type the name of the tag you would like to remove "
						+ "or add to ALL files in the program.");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				statusLabel.setText("Status bar:");
			}
		});
		textFieldfromAll.setBounds(310, 593, 207, 20);
		frame.getContentPane().add(textFieldfromAll);
		textFieldfromAll.setColumns(10);
		
		JButton btnAddToAll = new JButton("Add Tag to All");
		btnAddToAll.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				String newTagName = textFieldfromAll.getText().trim();
				Tag newTag = new Tag(newTagName);
					
				ImageNode tempNode = null;
					
				if (!retrievedTextField.getText().isEmpty()) { 
					tempNode = inh.findImageNode(retrievedTextField.getText());
					if (tempNode.findChild(tempNode) != null) {
						inh.addTagToAll(newTag);
					}
						
					ArrayList<ImageNode> topToBottom = inh.toTopToBottomArray(tempNode);
					ArrayList<Log> logsTopToBottom = new ArrayList<Log>();
						
					for (ImageNode imgN : topToBottom) {
						logsTopToBottom.add(imgN.getLog());
					}
						
					listOfActivities.setListData(logsTopToBottom.toArray());
					retrievedTextField.setText(tempNode.findChild(tempNode).getPathName());
					
				} else {
					inh.addTagToAll(newTag);
				}
					
				inh.updateExisting();
				listOfExisting.setListData(inh.getExistingTags().toArray());
				listOfImages.setListData(inh.getImages().toArray());
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				statusLabel.setText("Status bar: This button allows you to add a tag with the"
						+ " name inside the box to all files loaded into the program.");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				statusLabel.setText("Status bar:");
			}
		});

		btnAddToAll.setBounds(10, 592, 122, 23);
		frame.getContentPane().add(btnAddToAll);
		
		JButton btnRemoveFromAll = new JButton("Remove Tag from All");
		btnRemoveFromAll.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				String newTagName = textFieldfromAll.getText().trim();
				Tag newTag = new Tag(newTagName);
					
				ImageNode tempNode = null;
					
				if (!retrievedTextField.getText().isEmpty()) { 
					tempNode = inh.findImageNode(retrievedTextField.getText());
					if (tempNode.findChild(tempNode) != null) {
						inh.removeTagFromAll(newTag);
					}
						
					ArrayList<ImageNode> topToBottom = inh.toTopToBottomArray(tempNode);
					ArrayList<Log> logsTopToBottom = new ArrayList<Log>();
						
					for (ImageNode imgN : topToBottom) {
						logsTopToBottom.add(imgN.getLog());
					}
						
					listOfActivities.setListData(logsTopToBottom.toArray());
					retrievedTextField.setText(tempNode.findChild(tempNode).getPathName());
					
				} else {
					inh.removeTagFromAll(newTag);
				}
					
				inh.updateExisting();
				listOfExisting.setListData(inh.getExistingTags().toArray());
				listOfImages.setListData(inh.getImages().toArray());
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				statusLabel.setText("Status bar: This button allows you to remove a tag with the"
						+ " name inside the box from all images loaded into the program.");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				statusLabel.setText("Status bar:");
			}
		});

		btnRemoveFromAll.setBounds(142, 592, 158, 23);
		frame.getContentPane().add(btnRemoveFromAll);
		
		JLabel lblNewLabel_1 = new JLabel("Existing Tags");
		lblNewLabel_1.setBounds(53, 322, 164, 14);
		frame.getContentPane().add(lblNewLabel_1);
		
		JButton btnNewButton_6 = new JButton("Add Chosen Tag to retrieved file");
		btnNewButton_6.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (!listOfExisting.isSelectionEmpty() && retrievedTextField.getText().length() > 0) {
					Tag newTag = (Tag) listOfExisting.getSelectedValue();
//					Tag tempTag = new Tag(newTagName);
					
					ImageNode tempNode = inh.findImageNode(retrievedTextField.getText());
					
					if (!retrievedTextField.getText().isEmpty()) { 
						if (tempNode.findChild(tempNode) != null) {
							inh.addTag(tempNode.findChild(tempNode), newTag);
						}
					}
					
					listOfExisting.setListData(inh.getExistingTags().toArray());
					retrievedTextField.setText(tempNode.findChild(tempNode).getPathName());
					listOfImages.setListData(inh.getImages().toArray());
					
					ImageNode temp = inh.findImageNode(retrievedTextField.getText());
					
					ArrayList<ImageNode> topToBottom = inh.toTopToBottomArray(temp);
					ArrayList<Log> logsTopToBottom = new ArrayList<Log>();
					for (ImageNode imgN : topToBottom) {
						logsTopToBottom.add(imgN.getLog());
					}
					
					listOfActivities.setListData(logsTopToBottom.toArray());
				}
				
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				statusLabel.setText("Status bar: Select a tag from the Existing Tags and click"
						+ " this button to add the tag to the current retrieved file.");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				statusLabel.setText("Status bar:");
			}
		});
		
		btnNewButton_6.setBounds(310, 347, 279, 23);
		frame.getContentPane().add(btnNewButton_6);
		
		JButton btnNewButton_7 = new JButton("Remove chosen Tag from retrieved file");
		btnNewButton_7.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (!listOfExisting.isSelectionEmpty() && retrievedTextField.getText().length() > 0) {
					Tag tagToRemove = (Tag)listOfExisting.getSelectedValue();
					
					ImageNode tempNode = inh.findImageNode(retrievedTextField.getText());
					if (!retrievedTextField.getText().isEmpty()) { 
						inh.removeTag(tempNode.findChild(tempNode), tagToRemove);
					}
					listOfExisting.setListData(inh.getExistingTags().toArray());
					retrievedTextField.setText(tempNode.findChild(tempNode).getPathName());
//					ArrayList<Tag> someExistingTags = inh.getExistingTags();
//					list.setListData(someExistingTags.toArray());
					listOfImages.setListData(inh.getImages().toArray());
					
					ImageNode temp = inh.findImageNode(retrievedTextField.getText());
					
					ArrayList<ImageNode> topToBottom = inh.toTopToBottomArray(temp);
					ArrayList<Log> logsTopToBottom = new ArrayList<Log>();
					for (ImageNode imgN : topToBottom) {
						logsTopToBottom.add(imgN.getLog());
					}
					
					listOfActivities.setListData(logsTopToBottom.toArray());
				}
				
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				statusLabel.setText("Status bar: Select a tag from the Existing Tags and click "
						+ "this button to remove this tag from the retrieved file.");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				statusLabel.setText("Status bar:");
			}
		});

		btnNewButton_7.setBounds(310, 381, 279, 23);
		frame.getContentPane().add(btnNewButton_7);
		
		JButton btnAddTagTo = new JButton("Add Tag to all files");
		btnAddTagTo.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (!listOfExisting.isSelectionEmpty()) {
					Tag newTag = (Tag) listOfExisting.getSelectedValue();
//					Tag tempTag = new Tag(newTagName);
					
					
					ImageNode tempNode = null;
					if (!retrievedTextField.getText().isEmpty()) { 
						tempNode = inh.findImageNode(retrievedTextField.getText());
						if (tempNode.findChild(tempNode) != null) {
							inh.addTagToAll(newTag);
							retrievedTextField.setText(tempNode.findChild(tempNode).getPathName());
							
							ArrayList<ImageNode> topToBottom = inh.toTopToBottomArray(tempNode);
							ArrayList<Log> logsTopToBottom = new ArrayList<Log>();
							for (ImageNode imgN : topToBottom) {
								logsTopToBottom.add(imgN.getLog());
							}
							
							listOfActivities.setListData(logsTopToBottom.toArray());
						}
					} else {
						inh.addTagToAll(newTag);
					}
					
					listOfExisting.setListData(inh.getExistingTags().toArray());
					listOfImages.setListData(inh.getImages().toArray());
				}
				
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				statusLabel.setText("Status bar: Select a tag from the Existing Tags and click"
						+ " this button to add the tag to ALL files loaded into the program.");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				statusLabel.setText("Status bar:");
			}
		});
		btnAddTagTo.setBounds(310, 418, 279, 23);
		frame.getContentPane().add(btnAddTagTo);
		
		JButton btnNewButton_8 = new JButton("Remove Tag from all files");
		btnNewButton_8.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (!listOfExisting.isSelectionEmpty()) {
					Tag newTag = (Tag) listOfExisting.getSelectedValue();
//					Tag tempTag = new Tag(newTagName);
					
					ImageNode tempNode = null;
					
					if (!retrievedTextField.getText().isEmpty()) { 
						tempNode = inh.findImageNode(retrievedTextField.getText());
						if (tempNode != null) {
							inh.removeTagFromAll(newTag);
							
							ArrayList<ImageNode> topToBottom = inh.toTopToBottomArray(tempNode);
							ArrayList<Log> logsTopToBottom = new ArrayList<Log>();
							
							for (ImageNode imgN : topToBottom) {
								logsTopToBottom.add(imgN.getLog());
							}
							
							listOfActivities.setListData(logsTopToBottom.toArray());
							retrievedTextField.setText(tempNode.findChild(tempNode).getPathName());
						}
						
					} else {
						inh.removeTagFromAll(newTag);
					}
					
					inh.updateExisting();
					listOfExisting.setListData(inh.getExistingTags().toArray());
					listOfImages.setListData(inh.getImages().toArray());
				}
				
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				statusLabel.setText("Status bar: Select a tag from the Existing Tags and click"
						+ " this button to remove this tag from ALL files loaded into the system.");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				statusLabel.setText("Status bar:");
			}
		});
		btnNewButton_8.setBounds(310, 452, 279, 23);
		frame.getContentPane().add(btnNewButton_8);
		

		
		JLabel lblNewLabel_2 = new JLabel("Recent Activities");
		lblNewLabel_2.setBounds(599, 322, 108, 14);
		frame.getContentPane().add(lblNewLabel_2);
		
		JButton revertButton = new JButton("Revert");
		revertButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Log chosenLog = (Log) listOfActivities.getSelectedValue();
				ImageNode temp = inh.findImageNode(retrievedTextField.getText().toString());
				String oldPath = retrievedTextField.getText().toString();
				temp = temp.findRoot(temp);
				
				boolean condition = false;
				while (condition == false) {
					if (temp.getLog().equals(chosenLog)) {
						if (!(temp.getChild() == null)) {
							temp.getChild().setParent(null);
						}
						temp.setChild(null);
						condition = true;
					} else {
						temp = temp.getChild();
					}
				}
				retrievedTextField.setText(temp.findChild(temp).getPathName());
				inh.renameFile(oldPath, temp.getPathName());
				listOfImages.setListData(inh.getImages().toArray());
				
				temp = temp.findRoot(temp);
				
				ArrayList<ImageNode> topToBottom = inh.toTopToBottomArray(temp);
				ArrayList<Log> logsTopToBottom = new ArrayList<Log>();
				for (ImageNode imgN : topToBottom) {
					logsTopToBottom.add(imgN.getLog());
				}
				inh.updateExisting();
				listOfExisting.setListData(inh.getExistingTags().toArray());
				listOfActivities.setListData(logsTopToBottom.toArray());
				
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				statusLabel.setText("Status bar: Select a recent activity and click \'Revert\' to "
						+ "revert the file to the state it was in after that activity was performed.");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				statusLabel.setText("Status bar:");
			}
		});

		revertButton.setBounds(790, 498, 87, 23);
		frame.getContentPane().add(revertButton);
		
		JButton btnNewButton_4 = new JButton("Revert all images to date");
		btnNewButton_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				RevertPopup rp = new RevertPopup(inh);
				rp.setVisible(true);
				rp.toFront();
				rp.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent e) {
						
						if (inh.findImageNode(retrievedTextField.getText()) != null) {
							ImageNode tempNode = inh.findImageNode(retrievedTextField.getText());
							retrievedTextField.setText(tempNode.findChild(tempNode).getPathName());
						}
						listOfExisting.setListData(inh.getExistingTags().toArray());
						listOfImages.setListData(inh.getImages().toArray());
						listOfImages.setSelectedIndex(0);
						ImageNode temp = (ImageNode) listOfImages.getSelectedValue();
						retrievedTextField.setText(temp.findChild(temp).getPathName());
						
						ArrayList<ImageNode> topToBottom = inh.toTopToBottomArray(temp);
						ArrayList<Log> logsTopToBottom = new ArrayList<Log>();
						for (ImageNode imgN : topToBottom) {
							logsTopToBottom.add(imgN.getLog());
						}
						
						listOfActivities.setListData(logsTopToBottom.toArray());
					}
				});
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				statusLabel.setText("Status bar: Selecting this button opens another window which allows "
						+ "you to revert all images in the program to a certain date.");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				statusLabel.setText("Status bar:");
			}
		});
		btnNewButton_4.setBounds(599, 498, 181, 23);
		frame.getContentPane().add(btnNewButton_4);
		
		JLabel lblNewLabel_3 = new JLabel("Path of file being edited:");
		lblNewLabel_3.setBounds(10, 524, 158, 14);
		frame.getContentPane().add(lblNewLabel_3);
		
	}
	
	public String getAllImages() {
		String textAreaStr = "";
		for (ImageNode imgz : inh.getImages()) {
			textAreaStr += imgz.findChild(imgz).getPathName() + "\n";
		}
		return textAreaStr;
	}
}
