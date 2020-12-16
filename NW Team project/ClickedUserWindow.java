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
	/* 클릭한 유저의 상세정보 보기 및 친구추가 창 */
	public ClickedUserWindow(MessengerClient myMessenger, MessengerUI myUI,  String[] clickedUserInfo)
	{
		name = clickedUserInfo[0];
		birth = clickedUserInfo[1];
		id = clickedUserInfo[2];
		email = clickedUserInfo[3];
		message = clickedUserInfo[4];
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
		JButton addFriend = new JButton("친구추가");
		
		
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
		addFriend.setBounds(165, 200, 90, 30);
		mainPanel.add(addFriend);
		
		getContentPane().add(mainPanel);
		
		addFriend.addActionListener(new ActionListener() { // 친구 추가
			public void actionPerformed(ActionEvent e) {
				myMessenger.out.println("ADDFRIEND " + id);
			}	
			
		});
		setVisible(true);
	}
}
