import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChattingUI extends JFrame {
 /* ä��â UI */
	// ���� ��ó: https://www.flaticon.com/kr/
	JTextField textField = new JTextField(50);
	JTextArea messageArea = new JTextArea(16, 50);
	ImageIcon m = new ImageIcon("out.png"); 
	JButton out;
	JButton sending = new JButton("Send");
	JPanel panel1 = new JPanel();
	JPanel panel2 = new JPanel();
	JLabel label = new JLabel();
	JLabel space = new JLabel(" ");
	Color c1 = new Color(255, 241, 222);
	Color c2 = new Color(241, 228, 211);
	Color c3 = new Color(73, 73, 75);
	public ChattingUI(String opponent, MessengerClient myC)
	{
		super("Chatter");
		messageArea.setBackground(c1);
		textField.setEditable(false);
		messageArea.setEditable(false);
		
		label.setText(opponent);
		label.setFont(label.getFont().deriveFont(14.0f));
		label.setForeground(c3);
		
		panel1.setBackground(c2);
		panel1.add(label);
	
		out = new JButton(setImageSize(m, 20, 20));
		out.setPreferredSize(new Dimension(30, 30));
		out.setContentAreaFilled(false);
		out.setFocusPainted(false);
		out.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		sending.setBackground(Color.YELLOW);
		sending.setCursor(new Cursor(Cursor.HAND_CURSOR));
		sending.setFocusPainted(false);
		panel2.add(out, BorderLayout.EAST);
		panel2.add(textField,BorderLayout.WEST);
		panel2.add(sending, BorderLayout.EAST);
		
		getContentPane().add(panel1,BorderLayout.NORTH);
		getContentPane().add(new JScrollPane(messageArea), BorderLayout.CENTER);
		getContentPane().add(panel2, BorderLayout.SOUTH);
		pack();
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {// �޽��� ������
				myC.out.println("MESSAGE " + textField.getText());  
				textField.setText("");
			}
		});
		sending.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {// �޽��� ������ 
				myC.out.println("MESSAGE " + textField.getText()); 
				textField.setText("");
			}
		});
		out.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {// ���� 
				int d = JOptionPane.showConfirmDialog(null, "������ �����Ͻðڽ��ϱ�?", "�ݱ�"
						,JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if(d == 0)
				{
					myC.out.println("OUTCHAT");
					dispose();
				}
			}
		});
		addWindowListener(new WindowAdapter() {// ���� 
			  public void windowClosing(WindowEvent e) {
			    int confirmed = JOptionPane.showConfirmDialog(null, 
			        "������ �����Ͻðڽ��ϱ�?", "�ݱ�",
			        JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
			    if (confirmed == 0) 
			    {
			    	myC.out.println("OUTCHAT");
			    	dispose();
			    }
			  }
			});
		setVisible(true);
	}
	private static ImageIcon setImageSize(ImageIcon i, int width, int height)
	{
		Image im = i.getImage();
		ImageIcon newImage;
		im = im.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		newImage = new ImageIcon(im);
		return newImage;
	}
}
