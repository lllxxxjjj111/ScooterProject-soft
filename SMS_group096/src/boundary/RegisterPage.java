package boundary;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import control.StationControl;
import control.UsageControl;
import control.UserControl;
import entity.User;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RegisterPage extends JFrame {

	private JPanel contentPane;
	private JTextField qTextField;
	private JTextField nTextField;
	private JTextField eTextField;

	/**
	 * Create the frame.
	 */
	public RegisterPage(ManagementSys managementPage) {
		setTitle("Register");
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblQmId = new JLabel("QM ID");
		lblQmId.setFont(new Font(null,0,15));
		lblQmId.setBounds(35, 21, 99, 22);
		contentPane.add(lblQmId);
		
		qTextField = new JTextField();
		qTextField.setBounds(35, 57, 145, 21);
		contentPane.add(qTextField);
		qTextField.setColumns(10);
		
		JLabel lblName = new JLabel("Name");
		lblName.setFont(new Font(null,0,15));
		lblName.setBounds(35, 99, 99, 22);
		contentPane.add(lblName);
		
		nTextField = new JTextField();
		nTextField.setBounds(35, 135, 145, 21);
		contentPane.add(nTextField);
		nTextField.setColumns(10);
		
		JLabel lblEmailAddress = new JLabel("E-mail address");
		lblEmailAddress.setFont(new Font(null,0,15));
		lblEmailAddress.setBounds(35, 177, 99, 22);
		contentPane.add(lblEmailAddress);
		
		eTextField = new JTextField();
		eTextField.setBounds(35, 213, 145, 21);
		contentPane.add(eTextField);
		eTextField.setColumns(10);
		
		JLabel message = new JLabel("");
		message.setBounds(214, 31, 192, 149);
		contentPane.add(message);
		
		JButton btnRegister = new JButton("Register");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputID = qTextField.getText();
				String inputName = nTextField.getText().trim();
				String inputEmail = eTextField.getText();
				try {
					if (!UserControl.isIDLegal(inputID)) {
						message.setText("<html>The ID input is illegal!<br>You're allowed to input 9 digits.</html>");
					}
					else if (inputName.equals("")) {
						message.setText("Please input your name.");
					}
					else if (!UserControl.isEmailLegal(inputEmail)){
						message.setText("The Email input is illegal!");
					}
					else if (UserControl.isDuplication(inputID)) {
						message.setText("<html>The ID has be registered.<br>Please input a new QM ID.</html>");
					}
					else {
						User user = new User(inputID, inputEmail, inputName);
						UserControl.register(user);
						message.setText("<html>The user is registered successfully.</html>");
					}
						
				}catch (Exception ex) {
				}
			}
		});
		btnRegister.setBounds(214, 200, 93, 23);
		contentPane.add(btnRegister);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				UserControl.write();
				managementPage.setVisible(true);
			}
		});
		btnBack.setBounds(318, 200, 93, 23);
		contentPane.add(btnBack);
	}
}
