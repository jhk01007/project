import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class UserInfoWindow extends JFrame{
	String id;
	String name;
	String birth;
	String email;
	String message;
	/* ��Ŭ���� ������ ����â */
	public UserInfoWindow(MessengerClient myMessenger, MessengerUI myUI, String ID) throws InterruptedException
	{
		myUI.clickedUserInfo = "";
		myMessenger.out.println("GETUSERINFO " + ID); // ���� ��û
		Thread.sleep(1000);
		String userInfo = myUI.getClickedUserInfo();
		String[] arr = userInfo.split("/");
		name = arr[0];
		birth = arr[1];
		id = arr[2];
		email = arr[3];
		message = arr[4];
		if(message.equals("���¸޽����� �Է����ּ���"))
			message = "";
		
		setPreferredSize(new Dimension(420, 300));
		setLayout(null);
		setResizable(false);
		pack();
		
		JPanel mainPanel = new JPanel();
		JLabel n = new JLabel("�̸�: " + name);
		JLabel b = new JLabel("�������: " + birth);
		JLabel i = new JLabel("���̵�: " + id);
		JLabel e = new JLabel("�̸���: " + email);
		JLabel m = new JLabel("���¸޽���: " + message);
		
		
		mainPanel.setLayout(null);
		mainPanel.setBorder(new TitledBorder(new LineBorder(Color.lightGray, 2),"������"));
		mainPanel.setBounds(5, 5, 410, 290);
		n.setBounds(60, 20, 200, 30);
		mainPanel.add(n);
		i.setBounds(60, 55, 200, 30);
		mainPanel.add(i);
		b.setBounds(60, 90, 200, 30);
		mainPanel.add(b);
		e.setBounds(60, 125, 220, 30);
		mainPanel.add(e);
		m.setBounds(60, 160, 235, 30);
		mainPanel.add(m);
		
		getContentPane().add(mainPanel);
		
		setVisible(true);
	}
}
