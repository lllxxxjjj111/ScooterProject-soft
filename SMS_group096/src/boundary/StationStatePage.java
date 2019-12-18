package boundary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumn;

import control.StationControl;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class StationStatePage extends JFrame {

	private JPanel contentPane;
	private JButton btnFlash;
	private JButton btnBack;
	private final int stationAmount = 3;
	private final int slotAmount = 8;

	/**
	 * Create the frame.
	 * 
	 * @param managementPage
	 */
	public StationStatePage(ManagementSys managementPage) {
		setVisible(true);
		setTitle("Station State");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 500, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		String[] rowNames = new String[stationAmount+1];
		String[] columnNames = { "Total Scooters", "Available Scooters", "Currently in Use" };
		int[][] rawData = new int[stationAmount][3];
		
		rowNames[0] = "Station";
		for (int i = 1; i < stationAmount+1; i++) {
			rowNames[i] =Character.toString((char) (65 + i-1));			
		}
		
		for (int i = 0; i < stationAmount; i++) {
			rawData[i][0] = slotAmount;
			for (int j = 1; j < 3; j++) {
				rawData[i][j] = 0;
			}
		}

		JLabel raw[][] = new JLabel[stationAmount+1][1];
		for (int i = 0; i < stationAmount+1; i++) {
			raw[i][0] = new JLabel(rowNames[i]);
			raw[i][0].setBounds(10, 20 + i * 15, 60, 15);
			raw[i][0].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			contentPane.add(raw[i][0]);
		}
		JLabel column[] = new JLabel[3];
		for (int i = 0; i < 3; i++) {
			column[i] = new JLabel(columnNames[i]);
			column[i].setBounds(70 + i * 125, 20, 125, 15);
			column[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			contentPane.add(column[i]);
		}

		JLabel data[][] = new JLabel[stationAmount][3];
		for (int i = 0; i < stationAmount; i++) {
			for (int j = 0; j < 3; j++) {
				data[i][j] = new JLabel(String.valueOf(rawData[i][j]));
				data[i][j].setBounds(70 + j * 125, 35 + i * 15, 125, 15);
				data[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
				contentPane.add(data[i][j]);
			}
		}

		btnFlash = new JButton("Flash");
		btnFlash.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < stationAmount; i++) {
					rawData[i][1] = StationControl.scooterNumberInStation(i);
					rawData[i][2] = slotAmount - StationControl.scooterNumberInStation(i);
					data[i][1].setText(String.valueOf(rawData[i][1]));
					data[i][2].setText(String.valueOf(rawData[i][2]));
				}
			}
		});
		btnFlash.setBounds(147, 186, 120, 40);
		contentPane.add(btnFlash);

		btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				managementPage.setVisible(true);
			}
		});
		btnBack.setBounds(294, 186, 120, 40);
		contentPane.add(btnBack);

	}
}
