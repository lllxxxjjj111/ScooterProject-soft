package boundary;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import control.StationControl;
import control.UsageControl;
import control.UserControl;

public class StationPage extends JFrame {

	Thread thrd;

	private final int stationAmount = 3;
	private final int slotAmount = 8;

	JPanel station[] = new JPanel[stationAmount];
	JTextField textField[] = new JTextField[stationAmount];
	JButton btnSubmit[] = new JButton[stationAmount];
	JButton btnBack[] = new JButton[stationAmount];
	JLabel message[] = new JLabel[stationAmount];
	JLabel lblQm[] = new JLabel[stationAmount];
	JLabel light[][] = new JLabel[stationAmount][slotAmount];
	JButton button[][] = new JButton[stationAmount][slotAmount];;
	JTabbedPane tabbed = new JTabbedPane();

	private boolean status = false;
	private boolean timeout = false;

	/**
	 * Create the frame.
	 */
	public StationPage() {
		setTitle("Station");
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 900, 300);
		boolean slotFlag[][] = new boolean[stationAmount][slotAmount];
		for (int i = 0; i < stationAmount; i++) {
			for (int j = 0; j < slotAmount; j++) {
				slotFlag[i][j] = false;
			}
		}

		for (int i = 0; i < stationAmount; i++) {

			station[i] = new JPanel();
			station[i].setLayout(null);
			lblQm[i] = new JLabel("QM ID");
			textField[i] = new JTextField();
			btnSubmit[i] = new JButton("Submit");
			btnBack[i] = new JButton("Back");
			String tabName = "Station " + (char) (65 + i);
			tabbed.add(tabName, station[i]);

			textField[i].setBounds(700, 46, 131, 21);
			//station[i].add(textField[i]);
			//textField[i].setColumns(10);

			message[i] = new JLabel();
			message[i].setBounds(10, 140, 820, 80);
			//station[i].add(message[i]);
			
			lblQm[i].setFont(new Font(null,0,15));
			lblQm[i].setBounds(700, 17, 99, 22);
			//station[i].add(lblQm[i]);
			
			btnSubmit[i].setBounds(719, 91, 93, 23);
			//station[i].add(btnSubmit[i]);
			
			btnBack[i].setBounds(719, 125, 93, 23);
			btnBack[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			station[i].add(btnBack[i]);

			for (int j = 0; j < slotAmount; j++) {
				light[i][j] = new JLabel("Slot" + j);
				button[i][j] = new JButton("take/put");
				int x = j * 80 + 10;
				light[i][j].setBounds(x, 20, 80, 60);
				button[i][j].setBounds(x, 80, 80, 50);
				light[i][j].setOpaque(true);
				if (StationControl.stationArrayList.get(i).getSlot(j).isHasScooter()) {
					light[i][j].setBackground(Color.GREEN);
				} else {
					light[i][j].setBackground(Color.RED);
				}
				light[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

				station[i].add(light[i][j]);
				station[i].add(button[i][j]);
			}

		}

		for (int i = 0; i < stationAmount; i++) {
			btnSubmit[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int stationIndex = -1;
					for (int k = 0; k < stationAmount; k++) {
						if (e.getSource() == btnSubmit[k])
							stationIndex = k;
					}

					String inputID = textField[stationIndex].getText();
					try {
						if (!UserControl.isIDLegal(inputID)) {
							message[stationIndex].setText(
									"<html>The ID input is illegal!<br>You're allowed to input 9 digits.</html>");
						} else if (!UserControl.isDuplication(inputID)) {
							message[stationIndex]
									.setText("<html>The ID isn't in the system.<br>Please input a new QM ID.</html>");
						} else {
							int result = StationControl.scanCard(inputID);
							if (result == 3) {
								message[stationIndex].setText(
										"<html>Please pay your fine in the office before using a scooter.</html>");
							} else if (result == 1) {
								int slotPos = StationControl.findFreeSlot(stationIndex);
								if (slotPos == -1) {
									message[stationIndex].setText(
											"<html>There is no spare slot to return a scooter.<br>Please choose another station.</html>");
								} else {
									StationControl.userReturnSccoter(stationIndex);
									message[stationIndex].setText("<html>The slot " + slotPos
											+ " is available to return a scooter.<br>Please return the scooter in 60 seconds.</html>");
									slotFlag[stationIndex][slotPos] = true;
									thrd = new Thread(new FlashTimeout(light[stationIndex][slotPos]));
									status = true;
									timeout = false;
									thrd.start();
								}
							} else if (result == 2) {
								int scooterPos = StationControl.findFreeScooter(stationIndex);
								if (scooterPos == -1) {
									message[stationIndex].setText(
											"<html>There is no scooter to take.<br>Please choose another station.</html>");
								} else {
									StationControl.userTakeScooter(stationIndex);
									message[stationIndex].setText("<html>The scooter in slot " + scooterPos
											+ " is available to take.<br>Please take the scooter in 60 seconds.</html>");
									slotFlag[stationIndex][scooterPos] = true;
									thrd = new Thread(new FlashTimeout(light[stationIndex][scooterPos]));
									status = true;
									timeout = false;
									thrd.start();
								}
							}
						}
					} catch (Exception ex) {
					}
				}
			});

		}

		for (int i = 0; i < stationAmount; i++) {
			for (int j = 0; j < slotAmount; j++) {
				button[i][j].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int stationIndex = -1;
						int slotIndex = -1;
						for (int k = 0; k < stationAmount; k++) {
							for (int m = 0; m < slotAmount; m++) {
								if (e.getSource() == button[k][m]) {
									stationIndex = k;
									slotIndex = m;
								}
							}
						}
						String studentID = textField[stationIndex].getText();

						try {
							if (!UserControl.isIDLegal(studentID)) {
								message[stationIndex].setText(
										"<html>The ID input is illegal!<br>You're allowed to input 9 digits.</html>");
							} else if (!UserControl.isDuplication(studentID)) {
								message[stationIndex].setText(
										"<html>The ID isn't in the system.<br>Please input a new QM ID.</html>");
							} else {

								if (!slotFlag[stationIndex][slotIndex]) {
									message[stationIndex]
											.setText("<html>Please operate in the slot with flashing light.</html>");
								} else if (timeout) {
									StationControl.stationArrayList.get(stationIndex).getSlot(slotIndex)
											.setLocked(true);
									message[stationIndex].setText("<html>Timeout! Please scan your card again.</html>");
								} else {
									if (UserControl.isAbleToBorrow(studentID)) {
										if (slotFlag[stationIndex][slotIndex]) {
											StationControl.success = true;
											StationControl.reSetAfterBorrow(stationIndex, slotIndex);
											message[stationIndex].setText(
													"<html>You have borrowed the scooter successfully.</html>");
											UserControl.startUsing(studentID);
											status = false;
											light[stationIndex][slotIndex].setBackground(Color.RED);
											slotFlag[stationIndex][slotIndex] = false;
											textField[stationIndex].setText("");
											UserControl.write();
											StationControl.write();
											UsageControl.write();
										}
									} else if (UserControl.isCurrentUsing(studentID)) {
										if (slotFlag[stationIndex][slotIndex]) {
											StationControl.success = true;
											StationControl.reSetAfterReturn(stationIndex, slotIndex);
											message[stationIndex].setText(
													"<html>You have returned the scooter successfully.</html>");
											status = false;
											light[stationIndex][slotIndex].setBackground(Color.GREEN);
											UserControl.endUsing(studentID);
											slotFlag[stationIndex][slotIndex] = false;
											textField[stationIndex].setText("");
											UserControl.write();
											StationControl.write();
											UsageControl.write();
										}
									}
								}
							}

						} catch (Exception ex) {
						}
					}
				});
			}
		}

		getContentPane().add(BorderLayout.CENTER, tabbed);
	}

	class FlashTimeout implements Runnable {
		private JLabel light;

		public FlashTimeout(JLabel light) {
			this.light = light;
		}

		public void run() {
			int times = 0;
			while (status) {
				light.setVisible(true);
				try {
					Thread.sleep(500);
				} catch (Exception e) {
				}
				light.setVisible(false);
				try {
					Thread.sleep(500);
				} catch (Exception e) {
				}
				light.setVisible(true);

				times++;
				if (times == 60) {
					status = false;
					timeout = true;//60s timer
				}

			}
		}
	}

}
