package boundary;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import control.StationControl;
import control.UsageControl;
import control.UserControl;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ManagementSys extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public ManagementSys() {
		setTitle("Management System");
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnRegister = new JButton("Register");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				new RegisterPage(getone());
				UserControl.write();
			}
		});
		btnRegister.setBounds(59, 36, 128, 38);
		contentPane.add(btnRegister);
		
		JButton btnFine = new JButton("Fine");
		btnFine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				new FinePage(getone());
				UserControl.write();
			}
		});
		btnFine.setBounds(246, 36, 128, 38);
		contentPane.add(btnFine);
		
		JButton btnStationState = new JButton("Station State");
		btnStationState.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				new StationStatePage(getone());
			}
		});
		btnStationState.setBounds(246, 110, 128, 38);
		contentPane.add(btnStationState);
		
		JButton btnUserUsage = new JButton("User Usage");
		btnUserUsage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				new UsagePage(getone());
			}
		});
		btnUserUsage.setBounds(59, 110, 128, 38);
		contentPane.add(btnUserUsage);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				UserControl.write();
			}
		});
		btnBack.setBounds(153, 184, 128, 38);
		contentPane.add(btnBack);
	}
	public ManagementSys getone() {
		return this;
	}

}
