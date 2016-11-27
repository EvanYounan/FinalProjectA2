package photo_renamer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RevertPopup extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField dayTxt;
	private JTextField hourTxt;
	private JTextField minTxt;
	private JTextField secTxt;
	private Date dateInputted;


	/**
	 * Create the Revert frame and allow it to change state of ImageNodes.
	 */
	public RevertPopup(ImageNodeHandler inh) {
		setTitle("Revert");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 397, 261);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		JButton revertButton = new JButton("Revert");
		revertButton.setBounds(98, 164, 89, 23);
		contentPane.add(revertButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				dispose();
			}
		});
		cancelButton.setBounds(197, 164, 89, 23);
		contentPane.add(cancelButton);
		
		String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
		JComboBox dayCombo = new JComboBox(days);
		dayCombo.setBounds(10, 88, 73, 20);
		contentPane.add(dayCombo);
		
		String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", 
				"Nov", "Dec"};
		JComboBox monthCombo = new JComboBox(months);
		monthCombo.setBounds(93, 88, 68, 20);
		contentPane.add(monthCombo);
		
		Integer[] years = new Integer[85];
		for (int i = 0; i < years.length; i++) {
			years[i] = i + 2016;
		}
		
		JComboBox yearCombo = new JComboBox(years);
		yearCombo.setBounds(311, 88, 59, 20);
		contentPane.add(yearCombo);
		
		dayTxt = new JTextField();
		dayTxt.setBounds(171, 88, 25, 20);
		contentPane.add(dayTxt);
		dayTxt.setColumns(10);
		
		hourTxt = new JTextField();
		hourTxt.setColumns(10);
		hourTxt.setBounds(206, 88, 25, 20);
		contentPane.add(hourTxt);
		
		minTxt = new JTextField();
		minTxt.setColumns(10);
		minTxt.setBounds(241, 88, 25, 20);
		contentPane.add(minTxt);
		
		secTxt = new JTextField();
		secTxt.setColumns(10);
		secTxt.setBounds(276, 88, 25, 20);
		contentPane.add(secTxt);
		
		JLabel lblNewLabel = new JLabel(" :");
		lblNewLabel.setBounds(230, 91, 12, 17);
		contentPane.add(lblNewLabel);
		
		JLabel label = new JLabel(" :");
		label.setBounds(264, 91, 12, 17);
		contentPane.add(label);
		
		JLabel numDayLabel = new JLabel("Day");
		numDayLabel.setBounds(171, 63, 25, 14);
		contentPane.add(numDayLabel);
		
		JLabel lblHourminutesecond = new JLabel("Hr     /  Min     /Sec");
		lblHourminutesecond.setBounds(206, 63, 98, 14);
		contentPane.add(lblHourminutesecond);
		
		JLabel lblNewLabel_3 = new JLabel("Year");
		lblNewLabel_3.setBounds(311, 63, 46, 14);
		contentPane.add(lblNewLabel_3);
		
		JLabel monthLabel = new JLabel("Month");
		monthLabel.setBounds(93, 63, 46, 14);
		contentPane.add(monthLabel);
		
		JLabel dayLabel = new JLabel("Day");
		dayLabel.setBounds(10, 63, 46, 14);
		contentPane.add(dayLabel);
		
		JLabel statusLabel = new JLabel("");
		statusLabel.setBounds(10, 197, 362, 14);
		contentPane.add(statusLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Please enter the date you would like to revert to and click Revert.");
		lblNewLabel_1.setBounds(10, 11, 362, 14);
		contentPane.add(lblNewLabel_1);
		
		revertButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				String day = (String) dayCombo.getSelectedItem();
				String month = (String) monthCombo.getSelectedItem();
				String dayInt = dayTxt.getText();
				String hour = hourTxt.getText();
				String minute = minTxt.getText();
				String second = secTxt.getText();
				Integer year = (Integer) yearCombo.getSelectedItem();
				
				if (dayInt.equals("") || hour.equals("") || minute.equals("") ||
						second.equals("")) {
					statusLabel.setText("Complete all fields in order to revert!");
				} else {
					statusLabel.setText("");
					String dateStr = day + " " + month + " " + dayInt + " " + hour + ":" +
							minute + ":" + second + " EST " + year.toString();
					SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",
							Locale.ENGLISH);
					Date convertedDate = null;
					
					try {
						convertedDate = format.parse(dateStr);
						dateInputted = convertedDate;
						inh.revertAllImages(dateInputted);
						inh.updateExisting();
						dispose();
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
}
