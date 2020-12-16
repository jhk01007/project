import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class FindWindow extends JFrame{
	int foundCount = 0;
	HashSet<String> foundID = new HashSet();

/* 친구 찾기 창 */
	public FindWindow(MessengerClient myMessenger, MessengerUI myUI)
	{
		super("Find User");
		JPanel findPanel = new JPanel();
		JPanel foundPanel = new JPanel();
		JComboBox findCombo = new JComboBox(new String[] {"선택", "ID", "이름"});
		JTextField findText = new JTextField();
		JButton findButton = new JButton("검색");
		JScrollPane jsp = new JScrollPane();

		findPanel.setBounds(0, 0, 420, 50);
		findPanel.setLayout(null);
		findCombo.setBounds(5, 10, 70, 30);
		findPanel.add(findCombo);
		findText.setBounds(80, 10, 250, 30);
		findText.setEditable(false);
		findPanel.add(findText);
		findButton.setBounds(335, 10, 60, 30);
		findPanel.add(findButton);

		JButton[] foundUser = new JButton[50];
		foundPanel.setLayout(new BoxLayout(foundPanel, BoxLayout.Y_AXIS));
		foundPanel.setBorder(new TitledBorder("List"));
		jsp.setViewportView(foundPanel);
		jsp.setBounds(0, 55, 405, 220);
		setLayout(null);
		setPreferredSize(new Dimension(420, 300));
		setResizable(false);
		pack();
		getContentPane().add(findPanel);
		getContentPane().add(jsp);
		findCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox j = (JComboBox)e.getSource();
				if(j.getSelectedItem().equals("선택"))
					findText.setEditable(false);
				else
					findText.setEditable(true);
			}
		});
		findButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 초기화
				foundID.clear();
				for(int i = 0; i < foundCount; i++)
				{
					foundUser[i].setVisible(false);
				}
				foundCount = 0;
				myUI.findList.clear();
				//ID로 검색
				if(findCombo.getSelectedItem().equals("ID"))
				{
					myMessenger.out.println("FINDFRIEND " + "ID " + findText.getText()); // 친구 찾기 요청
					findText.setText("");
					try { Thread.sleep(1000); } catch (Exception e3) {}; // 서버에서 처리하는 동안 대기
					ArrayList<String> findList = myUI.getFindList();
					for(String s: findList)
					{	
						if(s.equals("END"))
							continue;
						foundID.add(s);
						String[] array = s.split("/");
						foundUser[foundCount] = new JButton(array[0] + " (" + array[2] + ")");
						foundUser[foundCount].setContentAreaFilled(false);
						foundUser[foundCount].setFocusPainted(false);
						foundUser[foundCount].setBorderPainted(false);
						foundPanel.add(foundUser[foundCount]);
						foundUser[foundCount].setCursor(new Cursor(Cursor.HAND_CURSOR));
						foundUser[foundCount].setForeground(new Color(38, 38, 38));
						foundUser[foundCount].setFont(foundUser[foundCount].getFont().deriveFont(13.0f));
						Font f = foundUser[foundCount].getFont();
						foundUser[foundCount].addMouseListener(new MouseListener() {
							public void mouseClicked(MouseEvent e) {}
							@Override
							public void mousePressed(MouseEvent e) {}
							@Override
							public void mouseReleased(MouseEvent e) {}

							@Override
							public void mouseEntered(MouseEvent e) {
								Map a = e.getComponent().getFont().getAttributes();
								a.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
								e.getComponent().setFont(e.getComponent().getFont().deriveFont(a));
							}
							@Override
							public void mouseExited(MouseEvent e) {
								e.getComponent().setFont(f);
							}
						});
						foundUser[foundCount].addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								JButton j = (JButton) e.getSource();
								String arr[] = j.getText().split(" ");
								String clickedID = arr[1].substring(1, arr[1].length() - 1);
								for(String s: foundID)
								{
									arr = s.split("/");
									if(arr[2].equals(clickedID))
									{
										new ClickedUserWindow(myMessenger, myUI, arr); // 클릭된 사람의 정보관련창 띄우기
										break;
									}
								}
							}
						});
						foundCount++;
					}
				}
				// 이름으로 검색
				else if(findCombo.getSelectedItem().equals("이름"))
				{
					myMessenger.out.println("FINDFRIEND " + "이름 " + findText.getText());// 친구 찾기 요청
					findText.setText("");
					try { Thread.sleep(1000); } catch (Exception e3) {};// 서버에서 처리하는 동안 대기
					ArrayList<String> findList = myUI.getFindList();
					for(String s: findList)
					{
						foundID.add(s);
						if(s.equals("END"))
							break;
						String[] array = s.split("/");
						foundUser[foundCount] = new JButton(array[0] + " (" + array[2] + ")");
						foundUser[foundCount].setContentAreaFilled(false);
						foundUser[foundCount].setFocusPainted(false);
						foundUser[foundCount].setBorderPainted(false);
						foundPanel.add(foundUser[foundCount]);
						foundUser[foundCount].setCursor(new Cursor(Cursor.HAND_CURSOR));
						foundUser[foundCount].setForeground(new Color(38, 38, 38));
						foundUser[foundCount].setFont(foundUser[foundCount].getFont().deriveFont(13.0f));
						Font f = foundUser[foundCount].getFont();
						foundUser[foundCount].addMouseListener(new MouseListener() {
							public void mouseClicked(MouseEvent e) {}
							@Override
							public void mousePressed(MouseEvent e) {}
							@Override
							public void mouseReleased(MouseEvent e) {}

							@Override
							public void mouseEntered(MouseEvent e) {
								Map a = e.getComponent().getFont().getAttributes();
								a.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
								e.getComponent().setFont(e.getComponent().getFont().deriveFont(a));
							}
							@Override
							public void mouseExited(MouseEvent e) {
								e.getComponent().setFont(f);
							}
						});
						foundUser[foundCount].addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								
								JButton j = (JButton) e.getSource();
								String arr[] = j.getText().split(" ");
								String clickedID = arr[1].substring(1, arr[1].length() - 1);
								for(String s: foundID)
								{
									arr = s.split("/");
									if(arr[2].equals(clickedID))
									{
										new ClickedUserWindow(myMessenger, myUI, arr);  // 클릭된 사람의 정보관련창 띄우기
										break;
									}
								}
							}
						});
						foundCount++;
					}
				}


			}
		});
		setVisible(true);
	}
}
