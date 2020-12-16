import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JOptionPane;

import java.util.HashSet;


public class MessengerClient{

	String serverAddress;
	int serverPortNum;
	static String[] memberList; // ȸ�� ���� 
	HashSet<String> Online = new HashSet<String>(); // �¶��� ģ��
	HashSet<String> Offline = new HashSet<String>(); // �¶��� ģ��
	String[] array;
	public Scanner in;
	public PrintWriter out;
	Socket socket = null;

	String getResponse;
	String getInfo;

	public MessengerClient(String serverAddress, int serverPortNum) throws UnknownHostException, IOException {
		this.serverAddress = serverAddress;
		this.serverPortNum = serverPortNum;
		try {
			socket = new Socket(serverAddress, serverPortNum);
			in = new Scanner(socket.getInputStream());
			out = new PrintWriter(socket.getOutputStream(), true);
		} finally {

		}

	}

	public void run(MessengerUI myUI) throws IOException {

		while(in.hasNextLine())
		{
			getResponse = in.nextLine();	
			if(getResponse.startsWith("USERENTRANCE")) //���ο� ������ ������ ��
			{
				myUI.MyFriendOn(getResponse.substring(13)); // �� ģ���� ���� ���, �������λ��¿��� �¶��� ���·� �ٲٱ�
			}
			else if(getResponse.startsWith("EXIT")) //������ ���� ��
			{
				myUI.MyFriendOff(getResponse.substring(5)); // �� ģ���� ���� ���, �¶��λ��¿��� �������� ���·� �ٲٱ�
			}
			else if(getResponse.startsWith("FINDSUCCESS")) // ģ�� ã�� ����
			{
				myUI.setFindList(getResponse.substring(12));
			}
			else if(getResponse.startsWith("ADDSUCCESS")) // ģ�� �߰� ����
			{
				String s = in.nextLine();
				String b[] = s.split(" ");
				myUI.setFriend(b[1], b[0], Integer.parseInt(b[2]));
				JOptionPane.showMessageDialog(null, "�߰��Ǿ����ϴ�");
			}
			else if(getResponse.startsWith("OTHERADDME")) // �ٸ� ģ���� ���� �߰� 
			{
				String[] s = getResponse.split(" ");
				myUI.setFriend(s[2], s[1], 1);
			}
			else if(getResponse.startsWith("ADDOWN")) // �ڱ� �ڽ����� ģ���� ���
				JOptionPane.showMessageDialog(null, "������ ģ���߰� �� �� �����ϴ�");
			else if(getResponse.startsWith("ADDALREADY")) // �̹� ģ���� �������� ģ���� ���
				JOptionPane.showMessageDialog(null, "�̹� ģ���Դϴ�");
			else if(getResponse.startsWith("EDITSUCCESS")) // �� ���� ���� ����
				JOptionPane.showMessageDialog(null, "������ �����Ǿ����ϴ�");
			else if(getResponse.startsWith("USERINFOCHANGE")) // ģ���� ������ ������ ���
			{
				String[] u = getResponse.split(" ");
				myUI.MyFriendChange(u[1], u[2]);
			}
			else if(getResponse.startsWith("GIVEUSERINFO")) // ��û�� ���� ������ ���� ���
			{
				String s = getResponse.substring(13);
				myUI.setClickedUserInfo(s);
			}
			else if(getResponse.startsWith("CHATREQUEST")) // ä�� ��û�� ���� ���
			{
				String[] o = getResponse.split(" ");
				int d =  myUI.responseChatRequest(o[1], o[2]);
				if(d == 0)
				{
					myUI.cu = new ChattingUI(o[2] + " (" + o[1] + ")", this);
					myUI.cu.textField.setEditable(true);
					myUI.cu.setTitle(myUI.name + " (" + myUI.ID + ")'s Chatter");
				}

				out.println("CHATRESPONSE " + o[1] + " " + d); // ���� ȸ��
			}
			else if(getResponse.startsWith("CHATRESPONSE")) // ä�� ������ ���� ��� 
			{
				String opponent = getResponse.substring(13);
				int d = Integer.parseInt(getResponse.substring(getResponse.length() - 1));
				if(d == 0)
					myUI.cu.textField.setEditable(true); // ����
				else if(d == 1)
					myUI.cu.setVisible(false); // ���� 
			}
			else if(getResponse.startsWith("MESSAGE")) // �޽��� ����
			{
				myUI.cu.messageArea.append(getResponse.substring(8) + "\n");
			}
			else if(getResponse.equals("OUTOPP")) // ��밡 �޽���â�� ������ ���� ����
			{
				myUI.cu.messageArea.append("������ �������ϴ�.\n");
				myUI.cu.textField.setEditable(false);
			}
		}
	}


	public static void main(String[] args) throws Exception {
		File serverInfo = new File("serverinfo.dat");
		String ip;
		int portNum;
		if(serverInfo.exists()) // if file exists
		{
			DataInputStream readServerInfo = new DataInputStream(new FileInputStream(serverInfo));

			ip = readServerInfo.readUTF();
			portNum = readServerInfo.readInt();
			readServerInfo.close();
		}
		else // default
		{ 
			ip = "localhost";
			portNum = 59001;
		}
		MessengerClient myMessenger = new MessengerClient(ip, portNum);
		MessengerUI myUI = new MessengerUI(myMessenger);
		Login f = new Login(myMessenger, myUI); // �α��� �ϱ� 
		f.setVisible(true);
		while(true)
		{
			Thread.sleep(2000);
			if(!f.isVisible()) // �α��� ���� �� 
			{
				Thread.sleep(1000); // �޽��� UI�� Ȱ��ȭ��Ű�� ���� ����
				myMessenger.run(myUI); // �������� ��� ����
				break;
			}
		}
		myMessenger.socket.close();
		myMessenger.in.close();
		myMessenger.out.close();

	}


}