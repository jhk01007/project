import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessengerServer {



	// 회원들의 명단을 관리하는 텍스트 파일
	private static File memberList = new File("회원명단.txt");


	// Online 친구의 ID set
	private static HashSet<String> onlineClientID = new HashSet();

	// Online 친구의 PrintWriter set
	private static HashSet<PrintWriter> onlineWriters = new HashSet();

	// Online 친구의 ID, PrintWriter 해쉬맵(채팅요청을 위함)
	private static HashMap<String,PrintWriter> I_W = new HashMap<String,PrintWriter>();
	
	public static void main(String[] args) throws Exception {
		int portNum = 59001;
		DataOutputStream writeServerInfo = new DataOutputStream(new FileOutputStream("serverinfo.dat"));
		writeServerInfo.writeUTF("localhost");
		writeServerInfo.writeInt(portNum);
		writeServerInfo.close();
		System.out.println("The chat server is running...");
		ExecutorService pool = Executors.newFixedThreadPool(500);
		try (ServerSocket listener = new ServerSocket(portNum)) {
			while (true) {
				pool.execute(new Handler(listener.accept()));
			}
		}
	}

	/**
	 * The client handler task.
	 */
	private static class Handler implements Runnable {
		private String ID = null;
		private Socket socket;
		private Scanner in; 
		private PrintWriter out;
		private PrintWriter printMemberList;
		private Scanner readMemberList;
		private PrintWriter clientFriend_W;
		private Scanner clientFriend_R;
		/**
		 * Constructs a handler thread, squirreling away the socket. All the interesting
		 * work is done in the run method. Remember the constructor is called from the
		 * server's main method, so this has to be as short as possible.
		 */
		public Handler(Socket socket) {
			this.socket = socket;
		}

		/**
		 * Services this thread's client by repeatedly requesting a screen name until a
		 * unique one has been submitted, then acknowledges the name and registers the
		 * output stream for the client in a global set, then repeatedly gets inputs and
		 * broadcasts them.
		 */
		public void run() {
			try {
				in = new Scanner(socket.getInputStream()); // 클라이언트와 통신하는 스캐너
				out = new PrintWriter(socket.getOutputStream(), true); // 클라이언트와 통신하는 프린트라이터
				readMemberList = new Scanner(new FileReader(memberList)); // 회원명단 읽어오는 스캐너
				printMemberList = new PrintWriter(new FileWriter(memberList, true)); // 회원명단에 적는 프린트라이터
				String getRequest = ""; // 클라이언트의 프로토콜 받는 문자열
				ID = "";
				String name = "";
				String password = "";
				String member = "";
				String array[];
				int flag = 0;
				boolean loginSuccess = false;
				String clientInfo = "";
				PrintWriter oppWriter = null; // 채팅할 상대 
				while(true)
				{
					flag = 0;
					getRequest = in.nextLine();
					if(getRequest.equals("LOGINREQUEST")) // 로그인 요청
					{
						readMemberList = new Scanner(new FileReader(memberList));
						ID = in.nextLine(); // 대기 
						password = in.nextLine(); // 대기 

						while(readMemberList.hasNextLine())
						{
							clientInfo = readMemberList.nextLine();
							array = clientInfo.split("/");
							name = array[0];
							if(ID.equals(array[2]) && password.equals(array[3]))
							{
								out.println("LOGINSUCCESS");
								flag = 1;
								synchronized (onlineClientID) {
									onlineClientID.add(array[2]); 
								}
								loginSuccess = true;
								break;
							}
						}
						if(flag == 0)
							out.println("LOGINFAIL");
						if(loginSuccess)
							break;
					}
					else if(getRequest.equals("CHECKDUPLICATED")) // 회원가입시 아이디 중복여부체크
					{	
						ID = in.nextLine(); // 대기  
						while(readMemberList.hasNextLine())
						{
							array = readMemberList.nextLine().split("/");
							if(ID.equals(array[2]))
							{
								out.println("IDDUPLICATED");
								flag = 1;
								break;
							}
						}	
						if(flag == 0)
							out.println("IDNOTDUPLICATED");
					}
					else if(getRequest.equals("UPDATEMEMBERLIST")) // 회원가입
					{
						member = in.nextLine();
						printMemberList.println(member);
						printMemberList.flush();
						out.println("UPDATESUCCESS");
					}
				}

				if(!onlineWriters.isEmpty())
				{
					for(PrintWriter p: onlineWriters)
					{
						p.println("USERENTRANCE " + ID); // 새로운 유저가 들어왔다고 broadcast
					}
				}
				
				onlineWriters.add(out); // 현재 유저의 printWriter 추가
				I_W.put(ID, out); // 현재 유저의 ID, printWriter map에 추가 
				out.println(clientInfo);// 로그인한 클라이언트에게 본인 정보 넘겨주기
				/* 클라이언트의 친구정보  온라인/오프라인에 맞게 넘겨주기*/
				File f = null;
				getRequest = in.nextLine();
				f = new File(getRequest.substring(18) + "'s_friend.txt");
				if(!f.exists())// 해당파일이 없는 경우 만들어주기
					clientFriend_W = new PrintWriter(new FileWriter(f,true));
				else // 있는 경우 
				{
					clientFriend_R = new Scanner(new FileReader(f));
					while(clientFriend_R.hasNextLine())
					{
						String l = clientFriend_R.nextLine(); 
						array = l.split("/");
						if(onlineClientID.contains(array[2]))
							out.println("FRIENDISONLINE " + l);
						else
							out.println("FRIENDISOFFLINE " + l);
					}	
					out.println("FIN");
				}


				while(true)
				{
					getRequest = in.nextLine(); // 대기
					if(getRequest.startsWith("ADDFRIEND")) // 친구 추가 
					{
						String f_id = null;
						String f_name = null;
						array = clientInfo.split("/");
						File myF = new File(array[2] + "'s_friend.txt"); // 내 친구목록 파일
						File frF = null; // 친구 추가할 친구의 친구목록 파일
						int ff = 0;

						if(getRequest.substring(10).equals(array[2])) // 본인에게 친구추가를 한 경우
						{
							out.println("ADDOWN");
							continue;
						}
						clientFriend_R = new Scanner(new FileReader(myF));
						while(clientFriend_R.hasNextLine())
						{
							String l = clientFriend_R.nextLine(); 
							String[] aaa = l.split("/");
							if(getRequest.substring(10).equals(aaa[2]))
							{
								ff = 1;
								out.println("ADDALREADY");
								break;
							}

						}
						if(ff == 1) // 이미 있는 친구이면
							continue;
						readMemberList = new Scanner(new FileReader(memberList));
						while(readMemberList.hasNextLine())
						{
							String i = readMemberList.nextLine();
							array = i.split("/");
							if(getRequest.substring(10).equals(array[2]))
							{	
								f_id = array[2];
								f_name = array[0];
								frF = new File(array[2] + "'s_friend.txt");
								clientFriend_W = new PrintWriter(new FileWriter(myF,true));
								clientFriend_W.println(i);
								clientFriend_W.flush();

								clientFriend_W = new PrintWriter(new FileWriter(frF, true));
								clientFriend_W.println(clientInfo);
								clientFriend_W.flush();

								clientFriend_W.close();
								break;
							}
						}
						out.println("ADDSUCCESS");
						if(onlineClientID.contains(f_id))
						{
							PrintWriter pw = I_W.get(f_id);
							pw.println("OTHERADDME " + ID + " " + name);
						}
						if(onlineClientID.contains(f_id))
							out.println(f_id + " " + f_name + " 1");// 그친구가 온라인이면 
						else
							out.println(f_id + " " + f_name + " 0"); // 그 친구가 오프라인이면

					}else if(getRequest.startsWith("FINDFRIEND")) // 친구 찾기 
					{
						if(getRequest.startsWith("ID", 11)) // ID로 찾기
						{
							String findID = getRequest.substring(14);
							readMemberList = new Scanner(new FileReader(memberList));
							while(readMemberList.hasNextLine())
							{
								String i = readMemberList.nextLine();
								array = i.split("/");
								String cmpID = array[2];
								if(cmpID.contains(findID))
								{
									i = array[0] + "/" + array[1] + "/" + array[2] + "/" + array[4] + "/" + array[5]; //비밀번호 제외
									out.println("FINDSUCCESS " + i);
								}
							}
							out.println("END");
						}
						else if(getRequest.startsWith("이름", 11)) // 이름으로 찾기 
						{
							String findName = getRequest.substring(14);
							readMemberList = new Scanner(new FileReader(memberList));
							while(readMemberList.hasNextLine())
							{
								String i = readMemberList.nextLine();
								array = i.split("/");
								String cmpID = array[0];
								if(cmpID.contains(findName))
								{
									i = array[0] + "/" + array[1] + "/" + array[2] + "/" + array[4] + "/" + array[5]; //비밀번호 제외
									out.println("FINDSUCCESS " + i);
								}
							}
							out.println("END");
						}

					}else if(getRequest.startsWith("EDITINFO")) //내 정보수정
					{
						array = clientInfo.split("/");	//현재 로그인정보	
						String array2[];
						String editInfo;
						String newInfo;
						editInfo = in.nextLine();// 이름/상태메시지가 바뀐 정보
						array2 = editInfo.split("/");
						array[0] = array2[0]; // 이름
						name = array[0];
						array[5] = array2[1]; // 상태메시지
						newInfo = String.join("/", array); // 수정된 정보
						readMemberList = new Scanner(new FileReader(memberList));
						ArrayList<String> imBuffer = new ArrayList(); // 임시적으로 파일 정보를 담아둘 곳
						while(readMemberList.hasNextLine())
						{
							String l = readMemberList.nextLine();
							if(l.equals(clientInfo))
								imBuffer.add(newInfo);
							else
								imBuffer.add(l);
						}
						printMemberList = new PrintWriter(new FileWriter(memberList)); // 수정하기 위해 다시쓰기 
						for(String l: imBuffer)
						{
							printMemberList.println(l);
							printMemberList.flush();
						}
						printMemberList.close();
						printMemberList = new PrintWriter(new FileWriter(memberList, true)); // 다시 정상적인 이어쓰기를 위해 재선언 
						out.println("EDITSUCCESS");
						for(PrintWriter p: onlineWriters) // 내 정보 변경 사실을 broadcast
						{
							if(p == out)
								continue;
							p.println("USERINFOCHANGE " + ID + " " + array[0]);
						}

					}else if(getRequest.startsWith("GETUSERINFO")) // 유저 상세정보 요청
					{
						String findID = getRequest.substring(12);
						readMemberList = new Scanner(new FileReader(memberList));
						while(readMemberList.hasNextLine())
						{
							String i = readMemberList.nextLine();
							array = i.split("/");
							String cmpID = array[2];
							if(cmpID.contains(findID))
							{
								i = array[0] + "/" + array[1] + "/" + array[2] + "/" + array[4] + "/" + array[5]; //비밀번호 제외
								out.println("GIVEUSERINFO " + i);
								break;
							}
						}
					}else if(getRequest.startsWith("CHATREQUEST")) // 채팅 요청
					{
						String opponent = getRequest.substring(12);
						oppWriter = I_W.get(opponent);
						oppWriter.println("CHATREQUEST " + ID + " " + name);
					}else if(getRequest.startsWith("CHATRESPONSE")) // 채팅 요청 응답
					{
						String opponent = getRequest.substring(13);
						String[] o = opponent.split(" ");
						oppWriter = I_W.get(o[0]);
						oppWriter.println("CHATRESPONSE " + ID + " " + o[1]);
					}else if(getRequest.startsWith("MESSAGE")) // 메시지 수신 및 전달
					{
						String line = name + ": " + getRequest.substring(8);
						oppWriter.println("MESSAGE " + line);
						out.println("MESSAGE " + line);
					}else if(getRequest.startsWith("OUTCHAT")) // 채팅 종료시, 상대방에게도 끝났다는걸 알림
					{
						oppWriter.println("OUTOPP");
					}

				} 

			} catch (Exception e) {
				System.out.println(e);
			}  finally {
				// System.out.println(ID + " is leaving");'
				if(out != null)
					onlineWriters.remove(out);

				if(ID != null)
				{
					onlineClientID.remove(ID);
					for(PrintWriter writer: onlineWriters)
					{
						writer.println("EXIT " + ID); // 유저가 나가면 접속중인 모든 유저에게 broadcast
					}
				}



				try { 
					socket.close(); 
					in.close();
					out.close();
					} catch (IOException e) {}
			}
		}
	}
}