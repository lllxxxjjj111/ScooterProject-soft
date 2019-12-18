package boundary;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JLabel;

public class EmailPage extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 * @param content 
	 */
	public EmailPage(String content) {
		setTitle("Email");
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel emailContent = new JLabel("");
		emailContent.setBounds(0, 0, 434, 250);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 434, 250);
		scrollPane.setViewportView(emailContent);
		
		emailContent.setText(content);
		
		contentPane.add(scrollPane);

	}

}
