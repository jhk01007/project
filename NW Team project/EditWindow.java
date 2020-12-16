import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class EditWindow extends JFrame{
 /* ���� �����ϴ� â */
	public EditWindow(MessengerClient myMessenger, String n, String m, MessengerUI myUI)
	{
		if(m.equals("���¸޽����� �Է����ּ���"))
			m = "";
		
		JPanel p = new JPanel();
		Label name = new Label("�̸�*");
		Label message = new Label("���¸޽���*");
		JButton save = new JButton("����");
		JButton cancel = new JButton("���");
		JButton edit = new JButton("����");
		TextField nameField = new TextField();
		TextField messageField = new TextField();
		
		p.setLayout(null);
		name.setBounds(40, 20, 60, 30);
		message.setBounds(40, 55, 60, 30);
		nameField.setBounds(115, 20, 200, 30);
		messageField.setBounds(115, 55, 200, 30);
		edit.setBounds(320, 40, 60, 30);
		save.setBounds(150, 150, 60, 30);
		cancel.setBounds(215, 150, 60, 30);
		
		edit.setCursor(new Cursor(Cursor.HAND_CURSOR));
		save.setCursor(new Cursor(Cursor.HAND_CURSOR));
		cancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		nameField.setText(n);
		messageField.setText(m);
		nameField.setEditable(false);
		messageField.setEditable(false);
		nameField.setForeground(Color.gray);
		messageField.setForeground(Color.gray);
		
		p.add(name);
		p.add(message);
		
		p.add(nameField);
		p.add(messageField);
		
		p.add(save);
		p.add(cancel);
		p.add(edit);
		
		getContentPane().add(p);
		setPreferredSize(new Dimension(420, 240));
		setResizable(false);
		pack();
		setTitle("��������");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setVisible(true);
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent T) { // ���� ��ư 
				try{
					myMessenger.out.println("EDITINFO");
					String changeN = nameField.getText();
					String changeM = messageField.getText();
					if(changeN.equals(""))
					{
						JOptionPane.showMessageDialog(null, "�̸��� �Է����ּ���");
						return;
					}
					if(changeM.equals(""))
						changeM = "���¸޽����� �Է����ּ���";
					myMessenger.out.println(changeN+ "/" + changeM);
					myUI.changeInfo(changeN, changeM); // �� ���� UI�󿡼� ����
					dispose();
				}catch (Exception ex){
					System.out.println(ex.getMessage());
				}
			}
		});
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent T) {//��� ��ư
				try{
					dispose();

				}catch (Exception ex){
					System.out.println(ex.getMessage());
				}
			}
		});		
		edit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent T) {//���� ��ư
				nameField.setEditable(true);
				messageField.setEditable(true);
				nameField.setForeground(Color.black);
				messageField.setForeground(Color.black);
			}
		});
		
	}
}



