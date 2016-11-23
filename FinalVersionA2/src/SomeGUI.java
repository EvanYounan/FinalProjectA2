import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
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

public class SomeGUI {

	private JFrame frame;
	public ImageNodeHandler inh;
	File f = new File("src/imageNodeHandlerInformation.ser");
	Serializing ser = new Serializing("src/imageNodeHandlerInformation.ser");
	private JTextField retrievedTextField;
	private JTextField textField;
	private JTextField textFieldfromAll;
	String tempPathToParent = "";
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SomeGUI window = new SomeGUI();
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
	public SomeGUI() {
		initialize();
	}
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Photo Renamer");
		frame.setBounds(100, 100, 968, 699);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 772, 321);
		JTextArea display = new JTextArea(16,58);
		display.setEditable(false);
		JScrollPane scroller = new JScrollPane(display);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panel.add(scroller);
		frame.getContentPane().add(panel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(10, 347, 290, 166);
		JList list = new JList();
		list.setBounds(10, 347, 290, 166);
		JScrollPane scroll = new JScrollPane(list);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		JLabel lblNewLabel = new JLabel("Existing Tags");
		panel_1.add(scroll);
		frame.getContentPane().add(panel_1);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(599, 347, 290, 140);
		JList lst2 = new JList();
		lst2.setBounds(599, 347, 290, 149);
		JScrollPane scroll2 = new JScrollPane(lst2);
		scroll2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panel_2.add(scroll2);
		frame.getContentPane().add(panel_2);
		
		if (f.exists()) {
			if (f.length() > 0) {
				inh = ser.Deserialize();
				display.setText(getAllImages());
				ArrayList<Tag> someExistingTags = inh.getExistingTags();
				list.setListData(someExistingTags.toArray());
			}
		}
		
		//DO SOMETHING BEFORE CLOSING THE PROGRAM
		frame.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
		        ser.Serialize(inh);
		        System.out.println("System is closing now...");
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
					if (f.length() > 0 || display.getText().length() > 0) {
						inh.addImageNodes(ld.convertToImageNodes(tempFiles));
					} else {
						inh = new ImageNodeHandler(ld.convertToImageNodes(tempFiles));
					}
				}
				ArrayList<Tag> someExistingTags = inh.getExistingTags();
				list.setListData(someExistingTags.toArray());
				display.setText(getAllImages());
				
			}
		});
		btnNewButton.setBounds(10, 626, 932, 23);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Add Tag");
		btnNewButton_1.setBounds(10, 558, 89, 23);
		frame.getContentPane().add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Remove Tag");

		btnNewButton_2.setBounds(109, 558, 108, 23);
		frame.getContentPane().add(btnNewButton_2);
		
		retrievedTextField = new JTextField();
		retrievedTextField.setBounds(171, 527, 418, 20);
		frame.getContentPane().add(retrievedTextField);
		retrievedTextField.setColumns(10);
		
		
		JButton btnNewButton_3 = new JButton("Retrieve File");
		btnNewButton_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (display.getSelectedText() != null) {
					tempPathToParent = display.getSelectedText();
					String somePath = display.getSelectedText();
					retrievedTextField.setText(somePath);
					ImageNode temp = inh.findImageNode(somePath);
					
					ArrayList<ImageNode> topToBottom = inh.toTopToBottomArray(temp);
					ArrayList<Log> logsTopToBottom = new ArrayList<Log>();
					for (ImageNode imgN : topToBottom) {
						logsTopToBottom.add(imgN.getLog());
					}
					
					lst2.setListData(logsTopToBottom.toArray());
				}
			}
		});
		//addTag
		btnNewButton_1.addMouseListener(new MouseAdapter() {
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
				
				list.setListData(inh.getExistingTags().toArray());
				retrievedTextField.setText(tempNode.findChild(tempNode).getPathName());
				display.setText(getAllImages());
				
				ImageNode temp = inh.findImageNode(retrievedTextField.getText());
				
				ArrayList<ImageNode> topToBottom = inh.toTopToBottomArray(temp);
				ArrayList<Log> logsTopToBottom = new ArrayList<Log>();
				for (ImageNode imgN : topToBottom) {
					logsTopToBottom.add(imgN.getLog());
				}
				
				lst2.setListData(logsTopToBottom.toArray());
				
			}
		});
		
		btnNewButton_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				String newTagToRemove = textField.getText().trim();
				Tag tagToRemove = new Tag(newTagToRemove);
				ImageNode tempNode = inh.findImageNode(retrievedTextField.getText());
				if (!retrievedTextField.getText().isEmpty()) { 
					inh.removeTag(tempNode.findChild(tempNode), tagToRemove);
				}
				list.setListData(inh.getExistingTags().toArray());
				retrievedTextField.setText(tempNode.findChild(tempNode).getPathName());
//				ArrayList<Tag> someExistingTags = inh.getExistingTags();
//				list.setListData(someExistingTags.toArray());
				display.setText(getAllImages());
				
				ImageNode temp = inh.findImageNode(retrievedTextField.getText());
				
				ArrayList<ImageNode> topToBottom = inh.toTopToBottomArray(temp);
				ArrayList<Log> logsTopToBottom = new ArrayList<Log>();
				for (ImageNode imgN : topToBottom) {
					logsTopToBottom.add(imgN.getLog());
				}
				
				lst2.setListData(logsTopToBottom.toArray());
				
			}
		});
		
		btnNewButton_3.setBounds(10, 524, 151, 23);
		frame.getContentPane().add(btnNewButton_3);
		
		textField = new JTextField();
		textField.setBounds(227, 559, 207, 20);
		textField.setEditable(true);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		textFieldfromAll = new JTextField();
		textFieldfromAll.setBounds(310, 593, 207, 20);
		frame.getContentPane().add(textFieldfromAll);
		textFieldfromAll.setColumns(10);
		
		JButton btnAddToAll = new JButton("Add Tag to All");
		btnAddToAll.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				String newTagName = textFieldfromAll.getText().trim();
				Tag tempTag = new Tag(newTagName);
				inh.addTagToAll(tempTag);
				list.setListData(inh.getExistingTags().toArray());
				display.setText(getAllImages());
				
			}
		});

		btnAddToAll.setBounds(10, 592, 122, 23);
		frame.getContentPane().add(btnAddToAll);
		
		JButton btnRemoveFromAll = new JButton("Remove Tag from All");
		btnRemoveFromAll.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				String newTagName = textFieldfromAll.getText().trim();
				Tag tempTag = new Tag(newTagName);
				System.out.println("Before removing all: " + inh.getExistingTags());
				inh.removeTagFromAll(tempTag);
				list.setListData(inh.getExistingTags().toArray());
				System.out.println("After removing all of the files: " + inh.getExistingTags());
				display.setText(getAllImages());
			}
		});

		btnRemoveFromAll.setBounds(142, 592, 158, 23);
		frame.getContentPane().add(btnRemoveFromAll);
		
		JLabel lblNewLabel_1 = new JLabel("Existing Tags");
		lblNewLabel_1.setBounds(53, 322, 164, 14);
		frame.getContentPane().add(lblNewLabel_1);
		
		JButton btnNewButton_6 = new JButton("Add Chosen Tag to retrieved file");
		
		btnNewButton_6.setBounds(310, 347, 279, 23);
		frame.getContentPane().add(btnNewButton_6);
		
		JButton btnNewButton_7 = new JButton("Remove chosen Tag from retrieved file");

		btnNewButton_7.setBounds(310, 381, 279, 23);
		frame.getContentPane().add(btnNewButton_7);
		
		JButton btnAddTagTo = new JButton("Add Tag to all files");
		btnAddTagTo.setBounds(310, 418, 279, 23);
		frame.getContentPane().add(btnAddTagTo);
		
		JButton btnNewButton_8 = new JButton("Remove Tag from all files");
		btnNewButton_8.setBounds(310, 452, 279, 23);
		frame.getContentPane().add(btnNewButton_8);
		

		
		JLabel lblNewLabel_2 = new JLabel("Older names");
		lblNewLabel_2.setBounds(599, 322, 89, 14);
		frame.getContentPane().add(lblNewLabel_2);
		
		JButton revertButton = new JButton("Revert");

		revertButton.setBounds(790, 498, 87, 23);
		frame.getContentPane().add(revertButton);
		
		
//		JPanel panel = new JPanel();
//		panel.setBounds(0, 0, 772, 336);
//		
//		JTextArea display = new JTextArea(16,58);
//		display.setEditable(false);
//		
//		
//		JScrollPane scroller = new JScrollPane(display);
//		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
//		
//		panel.add(scroller);
//		
//		frame.getContentPane().add(panel);
		

	}
	
	public String getAllImages() {
		String textAreaStr = "";
		for (ImageNode imgz : inh.getKeys()) {
			textAreaStr += imgz.findChild(imgz).getPathName() + "\n";
		}
		return textAreaStr;
	}
}
