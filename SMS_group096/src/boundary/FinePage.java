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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FinePage extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	boolean fineFlag = false;

	/**
	 * Create the frame.
	 */
	public FinePage(ManagementSys managementPage) {
		setTitle("Fine");
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblQmId = new JLabel("QM ID");
		lblQmId.setBounds(29, 25, 54, 15);
		lblQmId.setFont(new Font(null, 0, 15));
		contentPane.add(lblQmId);

		textField = new JTextField();
		textField.setBounds(29, 50, 189, 21);
		contentPane.add(textField);
		textField.setColumns(10);

		JLabel message = new JLabel("");
		message.setBounds(29, 91, 355, 65);
		contentPane.add(message);

		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputID = textField.getText();
				try {
					if (!UserControl.isIDLegal(inputID)) {
						message.setText("<html>The ID input is illegal!<br>You're allowed to input 9 digits.</html>");
					} else if (!UserControl.isDuplication(inputID)) {
						message.setText("<html>The ID isn't in the system.<br>Please input a new QM ID.</html>");
					} else if (UserControl.userArrayList.get(UserControl.searchID(inputID)).isFine() == false) {
						message.setText("The user doesn't have a fine.");
					} else if (UserControl.userArrayList.get(UserControl.searchID(inputID)).isFine() == true) {
						message.setText("The user has a fine of 100 pounds.");
						fineFlag = true;
					}
				} catch (Exception ex) {
				}
			}
		});
		btnSubmit.setBounds(291, 49, 93, 23);
		contentPane.add(btnSubmit);

		JButton btnPayIt = new JButton("Pay it");
		btnPayIt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputID = textField.getText();
				try {
					if (!fineFlag) {
						message.setText("<html>Please verify the status firstly.</html>");
					}else {
						message.setText("<html>You have payed your fine successfully.</html>");
						UserControl.unBan(inputID);
					}
				} catch (Exception ex) {
				}

			}
		});
		btnPayIt.setBounds(29, 191, 130, 39);
		contentPane.add(btnPayIt);

		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				UserControl.write();
				managementPage.setVisible(true);
			}
		});
		btnBack.setBounds(254, 191, 130, 39);
		contentPane.add(btnBack);
	}
}
