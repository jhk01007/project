import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

import javax.swing.*;
public class Login extends JFrame{//�α���ȭ��
	Image img = null;
	public Login(MessengerClient myMessenger, MessengerUI myUI)
	{
		JPanel p = new JPanel();
		p.setLayout(null);
		JLabel label = new JLabel(new ImageIcon("D:\\ä�û���\\123.jpg"));
		add(label);
		Label t1= new Label("�ȳ��ϼ���.");
		add(t1);
		Label t2= new Label("�� ���α׷��� ������ �Ͻð�");
		add(t2);
		Label t3= new Label("�α����� �ؾ� ����� �����մϴ�.");
		add(t3);
		Label t4= new Label("ID�Է� �� �α��� ��ư�� Ŭ���ϼ���.");
		add(t4);
		Label b2= new Label("���̵�:");
		add(b2);
		Label b3= new Label("��й�ȣ:");
		add(b3);
		TextField b4 = new TextField();
		add(b4);
		TextField b5 = new TextField();
		add(b5);
		b5.setEchoChar('*');//��ȣȭ
		JButton b6 = new JButton("�α���");
		add(b6);
		JButton b7 = new JButton("ȸ������");
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
		setTitle("�α��� ȭ�� ");
		b7.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {//ȸ������â���� �̵�
				// TODO Auto-generated method stub
				new Register(myMessenger);
			}
		});;
		b6.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e2) {//�α��� �Ҷ� 
				// TODO Auto-generated method stub
				myMessenger.out.println("LOGINREQUEST");
				myMessenger.out.println(b4.getText());
				myMessenger.out.println(b5.getText());

				String r = myMessenger.in.nextLine();

				if(r.equals("LOGINSUCCESS")) // �α��� ������ 
				{
					JOptionPane.showMessageDialog(null, "�α����� �Ǿ����ϴ�!!");
					dispose();
					try {
						String myInfo = myMessenger.in.nextLine();
						String array[] = myInfo.split("/");
						String getResponse = null;
						String myID = array[2];
						myUI.setMyInfo(array[2], array[0], array[1], array[4], array[5]);
						myMessenger.out.println("REQUESTFRIENDLIST " + array[2]); // ģ����� ��û�ϱ�
						while(true) // ģ������ �¶���, ������������ ������ UI�� ��ġ 
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
				else if(r.equals("LOGINFAIL")) //�α��� ���� �� 
				{
					JOptionPane.showMessageDialog(null, "�α����� �����Ͽ����ϴ�.");
					b4.setText("");
					b5.setText("");
				}

			}
		});
	}
}

