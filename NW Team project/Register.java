import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;

import javax.swing.*;
import java.awt.*;
public class Register extends JFrame {//회원가입화면
	int flag = 0;
	public Register(MessengerClient myMessenger){
		JPanel p = new JPanel();
		Label l1= new Label("이름*");
		Label l2 = new Label("생년 월일*");	
		Label l3 = new Label("아이디*");
		Label l4= new Label("패스워드*");
		Label l5 = new Label("Email 주소*");
		Label l6 = new Label("상태 메시지");
		add(l1);
		add(l2);
		add(l3);
		add(l4);
		add(l5);
		add(l6);

		TextField t1 = new TextField(); // 이름
		TextField t2 = new TextField("EX: 2000년 10월 7일 --> 20001007"); // 생년월일
		t2.setEditable(false);
		t2.setBackground(Color.white);
		t2.setForeground(Color.GRAY);
		TextField t3 = new TextField(); // 아이디
		TextField t4 = new TextField(); // 패스워드
		TextField t5 = new TextField(); // 이메일
		TextField t6 = new TextField(); // 상태메시지 
		add(t1);
		add(t2);
		add(t3);
		add(t4);
		add(t5);
		add(t6);

		t4.setEchoChar('*');
		JButton j1 = new JButton("저장");
		JButton j2 = new JButton("취소");
		add(j1);
		add(j2);
		l1.setBounds(40, 5, 40, 40);
		l2.setBounds(40, 45, 60, 40);
		l3.setBounds(40, 85, 50, 40);
		l4.setBounds(40, 125, 60, 40);
		l5.setBounds(40, 165, 70, 40);	       
		l6.setBounds(40, 205, 80, 40);
		t1.setBounds(120, 5, 200, 30);
		t2.setBounds(120, 45, 200, 30);
		t3.setBounds(120, 85, 200, 30);
		t4.setBounds(120, 125, 200, 30);
		t5.setBounds(120, 165, 280, 30);
		t6.setBounds(120, 205, 320, 120);
		j1.setBounds(125, 360, 80, 30);
		j2.setBounds(240, 360, 80, 30); 
		add(p);
		setSize(500,500);
		setTitle("회원가입");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setVisible(true);
		t2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(flag == 0)
				{
					t2.setText("");
					t2.setEditable(true);
					t2.setBackground(Color.white);
					t2.setForeground(Color.black);
					flag = 1;
				}
			}
		});
		t2.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {
				if(flag == 0)
				{
					t2.setText("");
					t2.setEditable(true);
					t2.setBackground(Color.white);
					t2.setForeground(Color.black);
					flag = 1;
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {}
		});
		j1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent T) {//회원가입 데이터 저장
				try{
					// 빼먹은 정보가 있는지 확인(상태메시지는 생략가능)
					if(t1.getText().equals("") || t2.getText().equals("") || t3.getText().equals("")
							|| t4.getText().equals("") || t5.getText().equals("") || !t2.isEditable())
					{
						JOptionPane.showMessageDialog(null, "누락된 정보가 있습니다 !");
						return;
					}

					String r = "";
					String r2 = "";
					myMessenger.out.println("CHECKDUPLICATED"); // ID 중복여부 체크 
					myMessenger.out.println(t3.getText());

					r = myMessenger.in.nextLine();
					if(r.equals("IDDUPLICATED"))
					{
						JOptionPane.showMessageDialog(null, "ID가 중복됩니다");
						t3.setText("");
						return;
					}
					if(t6.getText().equals(""))
					{
						t6.setText("상태메시지를 입력해주세요");
					}
					myMessenger.out.println("UPDATEMEMBERLIST");
					myMessenger.out.println(t1.getText()+"/" + t2.getText()+"/" + t3.getText()+"/"
							+ t4.getText()+"/" + t5.getText()+"/" + t6.getText()+ "\n");

					r2 = myMessenger.in.nextLine();
					if(r2.equals("UPDATESUCCESS")) // 회원가입 성공 
					{
						JOptionPane.showMessageDialog(null, "회원가입을 축하합니다!!."); // 회원가입을 한 뒤 로그인을 하면 반드시 한번 에러가 생기는 현상 발생
						dispose();
					}
					else 
						JOptionPane.showMessageDialog(null, "오류발생");

				}catch (Exception ex){
					System.out.println(ex.getMessage());
				}
			}
		});

		j2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent T) {//취소 버튼
				try{
					dispose();
				}catch (Exception ex){
					System.out.println(ex.getMessage());
				}
			}
		});
	}    
}