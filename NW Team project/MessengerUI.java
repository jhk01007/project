import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
/* �޽��� UI*/
public class MessengerUI extends JFrame{
	// ���� ��ó: https://www.flaticon.com/kr/
	String getResponse;
	MessengerUI myUI;
	MessengerClient myC;
	ArrayList<String> findList = new ArrayList<String>();
	String clickedUserInfo;
	ChattingUI cu;

	Color c1 = new Color(236, 236, 237);
	Color c2 = new Color(231, 231, 232);

	JPanel mainPanel = new JPanel();

	/* ���� �޴��� */
	JPanel midpanel1 = new JPanel();
	JButton find;
	//	JButton add;
	JButton edit;

	/* �̹����� */
	ImageIcon f_pre = new ImageIcon("find_pre.png");
	ImageIcon f_af = new ImageIcon("find_af.png");
	//	ImageIcon p_pre = new ImageIcon("plus_pre.png");
	//	ImageIcon p_af = new ImageIcon("plus_af.png");
	ImageIcon e_pre = new ImageIcon("edit_pre.png");
	ImageIcon e_af = new ImageIcon("edit_af.png");
	ImageIcon mi = new ImageIcon("messenger.png");

	/* ������, �¶��� �������� ģ�� �κ� */
	String ID;
	String name;
	String eMail;
	String birth;
	String condMessage;
	JPanel midpanel2 = new JPanel();
	JLabel name_ID = new JLabel();
	JLabel condMes = new JLabel();
	JPanel subpanel1 = new JPanel(); // ������
	JPanel subpanel1_1 = new JPanel();
	JLabel messenger = new JLabel(setImageSize(mi, 50, 50));
	JPanel subpanel2 = new JPanel(); // �¶��� �������� ģ�� ǥ��
	JButton[] OnlineF_But = new JButton[50]; // �¶��� ģ�� ��ư
	int onCount = 0;
	JButton[] OfflineF_But = new JButton[50]; // �������� ģ�� ��ư 
	int offCount = 0;
	JPanel subpanel2_1 = new JPanel(); // �¶���
	JPanel subpanel2_2 = new JPanel(); // �������� 



	/* ���� ������  �κ�*/
	JPanel bottompanel = new JPanel();
	JLabel title = new JLabel();
	JPanel publicData = new JPanel();
	JLabel decidecnt = new JLabel();
	JLabel examcnt = new JLabel();
	JLabel clearcnt = new JLabel();
	JLabel deathcnt = new JLabel();
	JLabel increase_decidecnt = new JLabel();
	JLabel increase_examcnt = new JLabel();
	JLabel increase_clearcnt = new JLabel();
	JLabel increase_deathcnt = new JLabel();
	JLabel decide = new JLabel("       Ȯ���� ��");
	JLabel exam = new JLabel("       �˻����� ��");
	JLabel clear = new JLabel("       �ݸ����� ��");
	JLabel death = new JLabel("       ����� ��");

	public MessengerUI(MessengerClient myMessenger) throws Exception
	{
		super("Messenger");
		this.setClient(myMessenger);; 
		setPreferredSize(new Dimension(420, 700));
		setResizable(false);
		mainPanel.setLayout(new  BoxLayout(mainPanel, BoxLayout.X_AXIS));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		bottompanel.setPreferredSize(new Dimension(420, 100));
		pack();

		/* ���� �޴��� */
		/* ģ�� ã�� �� �߰� ��� */
		find = new JButton(setImageSize(f_pre, 40, 40));
		find.setPreferredSize(new Dimension(65,65));
		find.setBorderPainted(false);
		find.setContentAreaFilled(false);
		find.setFocusPainted(false);
		FindButtonMouseListener fl = new FindButtonMouseListener();
		find.addMouseListener(fl);
		find.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new FindWindow(myMessenger, myUI);
			}

		});
		
		/* ���� ���� ��� */
		edit = new JButton(setImageSize(e_pre, 40, 40));
		edit.setPreferredSize(new Dimension(65,65));
		edit.setBorderPainted(false);
		edit.setContentAreaFilled(false);
		edit.setFocusPainted(false);
		EditButtonMouseListener el = new EditButtonMouseListener();
		edit.addMouseListener(el);
		edit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e99) {
				// TODO Auto-generated method stub
				new EditWindow(myMessenger, name, condMessage, myUI);
			}	
		});

		midpanel1.setLayout(new BoxLayout(midpanel1, BoxLayout.Y_AXIS));
		midpanel1.setBackground(c1);
		midpanel1.add(find);
		midpanel1.add(new JLabel(" "));
		midpanel1.add(edit);
		for(int i = 0; i < 18; i++)
		{
			midpanel1.add(new JLabel(" "));
		}


		/*������, �¶���,�������� ģ��*/
		midpanel2.setLayout(new BoxLayout(midpanel2, BoxLayout.Y_AXIS));
		midpanel2.setBorder(new LineBorder(c2, 3));
		subpanel1.add(messenger);
		subpanel1.add(new JLabel("    "));
		subpanel1.setBackground(c1);
		subpanel1_1.setLayout(new GridLayout(2,1));
		name_ID.setFont(name_ID.getFont().deriveFont(14.0f));
		subpanel1_1.add(name_ID);
		condMes.setForeground(Color.GRAY);
		subpanel1_1.add(condMes);
		subpanel1.add(subpanel1_1);
		subpanel2.setLayout(new GridLayout(2,1));
		subpanel2.setPreferredSize(new Dimension(355, 635));
		TitledBorder online = new TitledBorder(new LineBorder(new Color(242, 232, 229), 2),"Online");
		online.setTitleColor(new Color(255, 140, 140));
		TitledBorder offline = new TitledBorder(new LineBorder(new Color(232, 232, 239), 2), "Offline");
		offline.setTitleColor(new Color(140, 140, 255));
		subpanel2_1.setBorder(online);
		subpanel2_1.setBackground(new Color(242, 232, 229));
		subpanel2_1.setLayout(new BoxLayout(subpanel2_1, BoxLayout.Y_AXIS));

		subpanel2_2.setBorder(offline);
		subpanel2_2.setBackground(new Color(232, 232, 239));
		subpanel2_2.setLayout(new BoxLayout(subpanel2_2, BoxLayout.Y_AXIS));

		subpanel2.add(new JScrollPane(subpanel2_1));
		subpanel2.add(new JScrollPane(subpanel2_2));

		midpanel2.add(subpanel1);
		midpanel2.add(subpanel2);


		/* ���� ������ */ 
		SimpleDateFormat ymd = new SimpleDateFormat ( "yyyyMMdd", Locale.KOREA );
		SimpleDateFormat hm = new SimpleDateFormat ( "HHmm", Locale.KOREA );
		Date today = new Date();
		Date yesterday = new Date(today.getTime()+(1000*60*60*24*-1));
		Covid19_Data covid = new Covid19_Data(yesterday, today);
		String currentTime = hm.format(today);
		if(Integer.parseInt(currentTime) < 1000)
			today = new Date(today.getTime() + (1000*60*60*24*-1));

		String td = ymd.format(today);
		title.setText("���� �ڷγ� ������Ȳ - " + td  + " ����");

		publicData.setLayout(new GridLayout(3, 4));
		decide.setForeground(Color.red);
		exam.setForeground(new Color(237, 134, 15));
		clear.setForeground(Color.blue);

		decidecnt.setForeground(Color.red);
		decidecnt.setFont(decidecnt.getFont().deriveFont(16.0f));
		decidecnt.setText("     " + covid.today_decidecnt);
		examcnt.setForeground(new Color(237, 134, 15));
		examcnt.setFont(examcnt.getFont().deriveFont(16.0f));
		examcnt.setText("     " + covid.today_examcnt);
		clearcnt.setForeground(Color.blue);
		clearcnt.setFont(clearcnt.getFont().deriveFont(16.0f));
		clearcnt.setText("     " + covid.today_clearcnt);
		deathcnt.setFont(deathcnt.getFont().deriveFont(16.0f));
		deathcnt.setText("     " + covid.today_deathcnt);

		increase_decidecnt.setText("        " + covid.increase_decidecnt + "��");
		increase_decidecnt.setForeground(Color.red);
		increase_decidecnt.setFont(increase_decidecnt.getFont().deriveFont(10.0f));
		increase_examcnt.setText("        " + covid.increase_examcnt + "��");
		increase_examcnt.setForeground(new Color(237, 134, 15));
		increase_examcnt.setFont(increase_examcnt.getFont().deriveFont(10.0f));
		increase_clearcnt.setText("        " + covid.increase_clearcnt + "��");
		increase_clearcnt.setForeground(Color.blue);
		increase_clearcnt.setFont(increase_clearcnt.getFont().deriveFont(10.0f));
		increase_deathcnt.setText("        " + covid.increase_deathcnt + "��");
		increase_deathcnt.setFont(increase_deathcnt.getFont().deriveFont(10.0f));

		publicData.add(decide);
		publicData.add(exam);
		publicData.add(clear);
		publicData.add(death);

		publicData.add(decidecnt);
		publicData.add(examcnt);
		publicData.add(clearcnt);
		publicData.add(deathcnt);

		publicData.add(increase_decidecnt);
		publicData.add(increase_examcnt);
		publicData.add(increase_clearcnt);
		publicData.add(increase_deathcnt);
		publicData.setBackground(new Color(255, 255, 227));

		bottompanel.add(title, BorderLayout.CENTER);
		bottompanel.add(publicData, BorderLayout.SOUTH);
		bottompanel.setBackground(new Color(255, 255, 227));

		/* ������ ����*/
		mainPanel.add(midpanel1);
		mainPanel.add(midpanel2);
		getContentPane().add(bottompanel,BorderLayout.SOUTH);
		getContentPane().add(mainPanel, BorderLayout.CENTER);	
	}

	public void setUI(MessengerUI mu)
	{
		myUI = mu;
	}
	public void setClient(MessengerClient mc)
	{
		myC = mc;
	}
	public void changeInfo(String n, String m)// �������� 
	{
		name = n;
		condMessage = m;
		name_ID.setText(name + " (" + ID + ")");
		condMes.setText(condMessage);
	}
	public void setFindList(String s) // �˻��� ���� ���� ����Ʈ ���� 
	{
		findList.add(s);
	}
	public ArrayList<String> getFindList() // �˻��� ���� ���� ����Ʈ �������� 
	{
		return findList;
	}
	public void setClickedUserInfo(String s) // ��Ŭ���� ������ ���� ����
	{
		clickedUserInfo = s;
	}
	public String getClickedUserInfo() // ��Ŭ���� ������ ���� �������� 
	{
		return clickedUserInfo;
	}
	public int responseChatRequest(String opponentID, String opponentName) // ä�� ��û ����
	{
		int d = JOptionPane.showConfirmDialog(this, "\'" + opponentName + " (" + opponentID + ")\'" + " �κ��� ä�ÿ�û�� �Խ��ϴ� \n �����Ͻðڽ��ϱ�?", "ä�ÿ�û"
				,JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
		return d;
	}
	public void openChatRoom(String opponent, MessengerClient mc) // ä�ù� ����
	{
		cu = new ChattingUI(opponent, mc);
		cu.textField.setEditable(true);
		cu.setTitle(name + " (" + ID + ")'s Chatter");
	}
	public static ImageIcon setImageSize(ImageIcon i, int width, int height) // �̹��� ũ�� ����
	{
		Image im = i.getImage();
		ImageIcon newImage;
		im = im.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		newImage = new ImageIcon(im);
		return newImage;
	}
	public void setMyInfo(String myID, String myName, String myBirth, String myEmail, String myCondMessage)
	{//�ʱ� �� ���� ���� 
		ID = myID;
		name = myName;
		birth = myBirth;
		eMail = myEmail;
		condMessage = myCondMessage;
		name_ID.setText(name + " (" + ID + ")");
		condMes.setText(condMessage);
	}
	public void MyFriendChange(String changingID, String changedName) // �� ģ����, ��������
	{
		String arr[] = null;
		String f_ID = null;
		int flag = 0;
		for(int i = 0; i < onCount; i++)
		{
			arr = OnlineF_But[i].getText().split(" ");
			f_ID = arr[1].substring(1, arr[1].length() - 1);
			if(f_ID.equals(changingID))
			{
				OnlineF_But[i].setText(changedName + " (" + changingID + ")");
				flag = 1;
				break;
			}
		}
	}
	public void MyFriendOn(String newUserID) // �� ģ����, �������ο��� �¶������� 
	{
		String arr[] = null;
		String f_ID = null;
		int flag = 0;
		for(int i = 0; i < offCount; i++)
		{
			arr = OfflineF_But[i].getText().split(" ");
			f_ID = arr[1].substring(1, arr[1].length() - 1);
			if(f_ID.equals(newUserID))
			{
				OfflineF_But[i].setVisible(false);
				flag = 1;
				break;
			}
		}
		if(flag == 1)
			this.setFriend(arr[0], f_ID, 1);
	}
	public void MyFriendOff(String exitID)// �� ģ����, �¶��ο��� ������������ 
	{
		String arr[] = null;
		String f_ID = null;
		int flag = 0;
		for(int i = 0; i < onCount; i++)
		{
			arr = OnlineF_But[i].getText().split(" ");
			f_ID = arr[1].substring(1, arr[1].length() - 1);
			if(f_ID.equals(exitID))
			{
				OnlineF_But[i].setVisible(false);
				flag = 1;
				break;
			}
		}
		if(flag == 1)
			this.setFriend(arr[0], f_ID, 0);
	}
	public void setFriend(String f_name, String f_ID, int On_Off) // On: 1 Off:0
	{
		// �¶��� ģ���߰�
		if(On_Off == 1)
		{
			OnlineF_But[onCount] = new JButton(f_name + " (" + f_ID + ")");
			OnlineF_But[onCount].setContentAreaFilled(false);
			OnlineF_But[onCount].setFocusPainted(false);
			OnlineF_But[onCount].setBorderPainted(false);
			subpanel2_1.add(OnlineF_But[onCount]);
			OnlineF_But[onCount].setCursor(new Cursor(Cursor.HAND_CURSOR));
			OnlineF_But[onCount].setForeground(Color.red);
			OnlineF_But[onCount].setFont(name_ID.getFont().deriveFont(14.0f));
			Font f = OnlineF_But[onCount].getFont();
			OnlineF_But[onCount].addMouseListener(new MouseListener() {
				public void mouseClicked(MouseEvent e) {
					JButton j = (JButton) e.getSource();

					if(e.getButton() == MouseEvent.BUTTON3)
					{
						PopMenu onMenu = new PopMenu();
						onMenu.pm.show(e.getComponent(), e.getX(), e.getY());
						onMenu.pm_item1.addActionListener(new ActionListener() { // ����������
							public void actionPerformed(ActionEvent e2) {
								String arr[] = null;
								String f_ID = null;
								arr = j.getText().split(" ");
								f_ID = arr[1].substring(1, arr[1].length() - 1);
								try {
									new UserInfoWindow(myC, myUI, f_ID);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
						onMenu.pm_item2.addActionListener(new ActionListener() { // 1��1 ä���ϱ�
							public void actionPerformed(ActionEvent e3) {
								String arr[] = null;
								String f_ID = null;
								arr = j.getText().split(" ");
								f_ID = arr[1].substring(1, arr[1].length() - 1);
								myC.out.println("CHATREQUEST " + f_ID);
								cu = new ChattingUI(j.getText(), myC);
								cu.setTitle(name + " (" + ID + ")'s Chatter");
							}
						});
					}
				}
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
			onCount++;

		}//�������� ģ�� �߰�
		else if(On_Off == 0)
		{
			OfflineF_But[offCount] = new JButton(f_name + " (" + f_ID + ")");
			OfflineF_But[offCount].setContentAreaFilled(false);
			OfflineF_But[offCount].setFocusPainted(false);
			OfflineF_But[offCount].setBorderPainted(false);
			subpanel2_2.add(OfflineF_But[offCount]);
			OfflineF_But[offCount].setCursor(new Cursor(Cursor.HAND_CURSOR));
			OfflineF_But[offCount].setForeground(Color.blue);
			OfflineF_But[offCount].setFont(name_ID.getFont().deriveFont(14.0f));
			Font f = OfflineF_But[offCount].getFont();
			OfflineF_But[offCount].addMouseListener(new MouseListener() {
				public void mouseClicked(MouseEvent e) {
					JButton j = (JButton) e.getSource();
					if(e.getButton() == MouseEvent.BUTTON3)
					{
						PopMenu offMenu = new PopMenu();
						offMenu.pm_item2.setEnabled(false);
						offMenu.pm.show(e.getComponent(), e.getX(), e.getY());
						offMenu.pm_item1.addActionListener(new ActionListener() { // ����������
							public void actionPerformed(ActionEvent e) {
								String arr[] = null;
								String f_ID = null;
								arr = j.getText().split(" ");
								f_ID = arr[1].substring(1, arr[1].length() - 1);
								try {
									new UserInfoWindow(myC, myUI, f_ID);
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						});
					}
				}
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
			offCount++;

		}

	}

}
