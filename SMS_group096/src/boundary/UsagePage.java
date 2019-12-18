package boundary;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import control.StationControl;
import control.UsageControl;
import control.UserControl;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.awt.event.ActionEvent;

public class UsagePage extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JLabel message;
	private JButton btnSubmit;
	private JButton btnSendMail;
	private JButton btnBack;
	private JScrollPane scrollPane;

	/**
	 * Create the frame.
	 * 
	 * @param managementPage
	 */
	public UsagePage(ManagementSys managementPage) {
		setTitle("User Usage");
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		message = new JLabel("");
		message.setBounds(43, 107, 300, 67);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(43, 107, 300, 67);
		scrollPane.setViewportView(message);
		contentPane.add(scrollPane);

		JLabel lblQmId = new JLabel("QM ID");
		lblQmId.setFont(new Font(null, 0, 15));
		lblQmId.setBounds(37, 29, 63, 21);
		contentPane.add(lblQmId);

		textField = new JTextField();
		textField.setBounds(37, 60, 130, 21);
		contentPane.add(textField);
		textField.setColumns(10);

		btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputID = textField.getText();
				try {
					if (!UserControl.isIDLegal(inputID)) {
						message.setText("<html>The ID input is illegal!<br>You're allowed to input 9 digits.</html>");
					} else if (!UserControl.isDuplication(inputID)) {
						message.setText("<html>The ID isn't in the system.<br>Please input a new QM ID.</html>");
					} else if (UsageControl.searchID(inputID) == -1) {
						message.setText("<html>The user hasn't use the scooter in last 7 days.</html>");
					} else {
						message.setText("<html>The user's usage in last 7 days are as follows.");
						for (int i = 0; i < UsageControl.usageArrayList.size(); i++) {
							if (UsageControl.usageArrayList.get(i).getStudentID().equals(inputID)) {
								String preMessage = message.getText();
								message.setText(
										preMessage + "<br>" + getDate(UsageControl.usageArrayList.get(i).getDate())
												+ " " + UsageControl.usageArrayList.get(i).getDayUsage() + "minutes");
							}
						}
						String preMessage = message.getText();
						message.setText(preMessage + "</html>");
					}
				} catch (Exception ex) {
				}
			}
		});
		btnSubmit.setBounds(253, 59, 93, 23);
		contentPane.add(btnSubmit);

		btnSendMail = new JButton("Send Mail");
		btnSendMail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String content = message.getText();
				new EmailPage(content);
			}
		});
		btnSendMail.setBounds(112, 187, 106, 34);
		contentPane.add(btnSendMail);

		btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				managementPage.setVisible(true);
			}
		});
		btnBack.setBounds(253, 187, 93, 34);
		contentPane.add(btnBack);

	}

	public static String getDate(int date) {
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int months[] = new int[] { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		int month = 1;
		int day = 1;

		if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
			months[1] = 29;
		}

		while (date >= 0) {
			if (date > months[month - 1]) {
				date -= months[month - 1];
				month += 1;
			} else {
				day = date;
				break;
			}
		}
		return String.format("%04d", year) + "." + String.format("%02d", month) + "." + String.format("%02d", day);

	}

}
