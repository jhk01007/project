import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class ClickedUserWindow extends JFrame{
	
	String id;
	String name;
	String birth;
	String email;
	String message;
	/* Ŭ���� ������ ������ ���� �� ģ���߰� â */
	public ClickedUserWindow(MessengerClient myMessenger, MessengerUI myUI,  String[] clickedUserInfo)
	{
		name = clickedUserInfo[0];
		birth = clickedUserInfo[1];
		id = clickedUserInfo[2];
		email = clickedUserInfo[3];
		message = clickedUserInfo[4];
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
		JButton addFriend = new JButton("ģ���߰�");
		
		
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
		addFriend.setBounds(165, 200, 90, 30);
		mainPanel.add(addFriend);
		
		getContentPane().add(mainPanel);
		
		addFriend.addActionListener(new ActionListener() { // ģ�� �߰�
			public void actionPerformed(ActionEvent e) {
				myMessenger.out.println("ADDFRIEND " + id);
			}	
			
		});
		setVisible(true);
	}
}
