import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

import javax.swing.*;
public class Login extends JFrame{//로그인화면
	Image img = null;
	public Login(MessengerClient myMessenger, MessengerUI myUI)
	{
		JPanel p = new JPanel();
		p.setLayout(null);
		JLabel label = new JLabel(new ImageIcon("D:\\채팅사진\\123.jpg"));
		add(label);
		Label t1= new Label("안녕하세요.");
		add(t1);
		Label t2= new Label("본 프로그램은 가입을 하시고");
		add(t2);
		Label t3= new Label("로그인을 해야 사용이 가능합니다.");
		add(t3);
		Label t4= new Label("ID입력 후 로그인 버튼을 클릭하세요.");
		add(t4);
		Label b2= new Label("아이디:");
		add(b2);
		Label b3= new Label("비밀번호:");
		add(b3);
		TextField b4 = new TextField();
		add(b4);
		TextField b5 = new TextField();
		add(b5);
		b5.setEchoChar('*');//암호화
		JButton b6 = new JButton("로그인");
		add(b6);
		JButton b7 = new JButton("회원가입");
		add(b7);

		label.setBounds(0, 5, 350, 255);
		t1.setBounds(350, 5, 70, 40);
		t2.setBounds(350,35, 280, 40);
		t3.setBounds(350, 65,280, 40);
		t4.setBounds(350,95,250, 40);
		b2.setBounds(40, 265, 40, 40); 
		b3.setBounds(40, 305, 60, 40);
		b4.setBounds(150, 265, 200, 30);
		b5.setBounds(150, 305, 200, 30);
		b6.setBounds(380, 265, 80, 30);
		b7.setBounds(380, 305, 90, 30);
		add(p);
		setSize(700, 400);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("로그인 화면 ");
		b7.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {//회원가입창으로 이동
				// TODO Auto-generated method stub
				new Register(myMessenger);
			}
		});;
		b6.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e2) {//로그인 할때 
				// TODO Auto-generated method stub
				myMessenger.out.println("LOGINREQUEST");
				myMessenger.out.println(b4.getText());
				myMessenger.out.println(b5.getText());

				String r = myMessenger.in.nextLine();

				if(r.equals("LOGINSUCCESS")) // 로그인 성공시 
				{
					JOptionPane.showMessageDialog(null, "로그인이 되었습니다!!");
					dispose();
					try {
						String myInfo = myMessenger.in.nextLine();
						String array[] = myInfo.split("/");
						String getResponse = null;
						String myID = array[2];
						myUI.setMyInfo(array[2], array[0], array[1], array[4], array[5]);
						myMessenger.out.println("REQUESTFRIENDLIST " + array[2]); // 친구목록 요청하기
						while(true) // 친구들을 온라인, 오프라인으로 나눠서 UI에 배치 
						{
							getResponse = myMessenger.in.nextLine();
							if(getResponse.startsWith("FRIENDISONLINE"))
							{
								array = getResponse.substring(15).split("/");
								myUI.setFriend(array[0], array[2], 1);
								myUI.pack();
							}
							else if(getResponse.startsWith("FRIENDISOFFLINE"))
							{
								array = getResponse.substring(16).split("/");
								myUI.setFriend(array[0], array[2], 0);
								myUI.pack();  
							} 
							else if(getResponse.equals("FIN")) {
								break;
							}
						}
						myUI.setUI(myUI);
						myUI.setVisible(true);
						

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(r.equals("LOGINFAIL")) //로그인 실패 시 
				{
					JOptionPane.showMessageDialog(null, "로그인이 실패하였습니다.");
					b4.setText("");
					b5.setText("");
				}

			}
		});
	}
}

