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
	/* 우클릭한 유저의 정보창 */
	public UserInfoWindow(MessengerClient myMessenger, MessengerUI myUI, String ID) throws InterruptedException
	{
		myUI.clickedUserInfo = "";
		myMessenger.out.println("GETUSERINFO " + ID); // 정보 요청
		Thread.sleep(1000);
		String userInfo = myUI.getClickedUserInfo();
		String[] arr = userInfo.split("/");
		name = arr[0];
		birth = arr[1];
		id = arr[2];
		email = arr[3];
		message = arr[4];
		if(message.equals("상태메시지를 입력해주세요"))
			message = "";
		
		setPreferredSize(new Dimension(420, 300));
		setLayout(null);
		setResizable(false);
		pack();
		
		JPanel mainPanel = new JPanel();
		JLabel n = new JLabel("이름: " + name);
		JLabel b = new JLabel("생년월일: " + birth);
		JLabel i = new JLabel("아이디: " + id);
		JLabel e = new JLabel("이메일: " + email);
		JLabel m = new JLabel("상태메시지: " + message);
		
		
		mainPanel.setLayout(null);
		mainPanel.setBorder(new TitledBorder(new LineBorder(Color.lightGray, 2),"상세정보"));
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
