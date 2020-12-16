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
	static String[] memberList; // 회원 정보 
	HashSet<String> Online = new HashSet<String>(); // 온라인 친구
	HashSet<String> Offline = new HashSet<String>(); // 온라인 친구
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
			if(getResponse.startsWith("USERENTRANCE")) //새로운 유저가 들어왔을 때
			{
				myUI.MyFriendOn(getResponse.substring(13)); // 내 친구가 들어온 경우, 오프라인상태에서 온라인 상태로 바꾸기
			}
			else if(getResponse.startsWith("EXIT")) //유저가 나갈 때
			{
				myUI.MyFriendOff(getResponse.substring(5)); // 내 친구가 나간 경우, 온라인상태에서 오프라인 상태로 바꾸기
			}
			else if(getResponse.startsWith("FINDSUCCESS")) // 친구 찾기 성공
			{
				myUI.setFindList(getResponse.substring(12));
			}
			else if(getResponse.startsWith("ADDSUCCESS")) // 친구 추가 성공
			{
				String s = in.nextLine();
				String b[] = s.split(" ");
				myUI.setFriend(b[1], b[0], Integer.parseInt(b[2]));
				JOptionPane.showMessageDialog(null, "추가되었습니다");
			}
			else if(getResponse.startsWith("OTHERADDME")) // 다른 친구가 나를 추가 
			{
				String[] s = getResponse.split(" ");
				myUI.setFriend(s[2], s[1], 1);
			}
			else if(getResponse.startsWith("ADDOWN")) // 자기 자신한테 친추한 경우
				JOptionPane.showMessageDialog(null, "본인은 친구추가 할 수 없습니다");
			else if(getResponse.startsWith("ADDALREADY")) // 이미 친구인 유저에게 친추한 경우
				JOptionPane.showMessageDialog(null, "이미 친구입니다");
			else if(getResponse.startsWith("EDITSUCCESS")) // 내 정보 수정 성공
				JOptionPane.showMessageDialog(null, "정보가 수정되었습니다");
			else if(getResponse.startsWith("USERINFOCHANGE")) // 친구의 정보가 수정된 경우
			{
				String[] u = getResponse.split(" ");
				myUI.MyFriendChange(u[1], u[2]);
			}
			else if(getResponse.startsWith("GIVEUSERINFO")) // 요청한 유저 정보를 받은 경우
			{
				String s = getResponse.substring(13);
				myUI.setClickedUserInfo(s);
			}
			else if(getResponse.startsWith("CHATREQUEST")) // 채팅 요청을 받은 경우
			{
				String[] o = getResponse.split(" ");
				int d =  myUI.responseChatRequest(o[1], o[2]);
				if(d == 0)
				{
					myUI.cu = new ChattingUI(o[2] + " (" + o[1] + ")", this);
					myUI.cu.textField.setEditable(true);
					myUI.cu.setTitle(myUI.name + " (" + myUI.ID + ")'s Chatter");
				}

				out.println("CHATRESPONSE " + o[1] + " " + d); // 응답 회신
			}
			else if(getResponse.startsWith("CHATRESPONSE")) // 채팅 응답을 받은 경우 
			{
				String opponent = getResponse.substring(13);
				int d = Integer.parseInt(getResponse.substring(getResponse.length() - 1));
				if(d == 0)
					myUI.cu.textField.setEditable(true); // 승인
				else if(d == 1)
					myUI.cu.setVisible(false); // 거절 
			}
			else if(getResponse.startsWith("MESSAGE")) // 메시지 수신
			{
				myUI.cu.messageArea.append(getResponse.substring(8) + "\n");
			}
			else if(getResponse.equals("OUTOPP")) // 상대가 메시지창을 닫으면 나도 종료
			{
				myUI.cu.messageArea.append("상대방이 나갔습니다.\n");
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
		Login f = new Login(myMessenger, myUI); // 로그인 하기 
		f.setVisible(true);
		while(true)
		{
			Thread.sleep(2000);
			if(!f.isVisible()) // 로그인 성공 시 
			{
				Thread.sleep(1000); // 메신저 UI를 활성화시키기 위해 지연
				myMessenger.run(myUI); // 본격적인 통신 시작
				break;
			}
		}
		myMessenger.socket.close();
		myMessenger.in.close();
		myMessenger.out.close();

	}


}