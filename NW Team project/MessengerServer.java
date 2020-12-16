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



	// ȸ������ ����� �����ϴ� �ؽ�Ʈ ����
	private static File memberList = new File("ȸ�����.txt");


	// Online ģ���� ID set
	private static HashSet<String> onlineClientID = new HashSet();

	// Online ģ���� PrintWriter set
	private static HashSet<PrintWriter> onlineWriters = new HashSet();

	// Online ģ���� ID, PrintWriter �ؽ���(ä�ÿ�û�� ����)
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
				in = new Scanner(socket.getInputStream()); // Ŭ���̾�Ʈ�� ����ϴ� ��ĳ��
				out = new PrintWriter(socket.getOutputStream(), true); // Ŭ���̾�Ʈ�� ����ϴ� ����Ʈ������
				readMemberList = new Scanner(new FileReader(memberList)); // ȸ����� �о���� ��ĳ��
				printMemberList = new PrintWriter(new FileWriter(memberList, true)); // ȸ����ܿ� ���� ����Ʈ������
				String getRequest = ""; // Ŭ���̾�Ʈ�� �������� �޴� ���ڿ�
				ID = "";
				String name = "";
				String password = "";
				String member = "";
				String array[];
				int flag = 0;
				boolean loginSuccess = false;
				String clientInfo = "";
				PrintWriter oppWriter = null; // ä���� ��� 
				while(true)
				{
					flag = 0;
					getRequest = in.nextLine();
					if(getRequest.equals("LOGINREQUEST")) // �α��� ��û
					{
						readMemberList = new Scanner(new FileReader(memberList));
						ID = in.nextLine(); // ��� 
						password = in.nextLine(); // ��� 

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
					else if(getRequest.equals("CHECKDUPLICATED")) // ȸ�����Խ� ���̵� �ߺ�����üũ
					{	
						ID = in.nextLine(); // ���  
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
					else if(getRequest.equals("UPDATEMEMBERLIST")) // ȸ������
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
						p.println("USERENTRANCE " + ID); // ���ο� ������ ���Դٰ� broadcast
					}
				}
				
				onlineWriters.add(out); // ���� ������ printWriter �߰�
				I_W.put(ID, out); // ���� ������ ID, printWriter map�� �߰� 
				out.println(clientInfo);// �α����� Ŭ���̾�Ʈ���� ���� ���� �Ѱ��ֱ�
				/* Ŭ���̾�Ʈ�� ģ������  �¶���/�������ο� �°� �Ѱ��ֱ�*/
				File f = null;
				getRequest = in.nextLine();
				f = new File(getRequest.substring(18) + "'s_friend.txt");
				if(!f.exists())// �ش������� ���� ��� ������ֱ�
					clientFriend_W = new PrintWriter(new FileWriter(f,true));
				else // �ִ� ��� 
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
					getRequest = in.nextLine(); // ���
					if(getRequest.startsWith("ADDFRIEND")) // ģ�� �߰� 
					{
						String f_id = null;
						String f_name = null;
						array = clientInfo.split("/");
						File myF = new File(array[2] + "'s_friend.txt"); // �� ģ����� ����
						File frF = null; // ģ�� �߰��� ģ���� ģ����� ����
						int ff = 0;

						if(getRequest.substring(10).equals(array[2])) // ���ο��� ģ���߰��� �� ���
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
						if(ff == 1) // �̹� �ִ� ģ���̸�
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
							out.println(f_id + " " + f_name + " 1");// ��ģ���� �¶����̸� 
						else
							out.println(f_id + " " + f_name + " 0"); // �� ģ���� ���������̸�

					}else if(getRequest.startsWith("FINDFRIEND")) // ģ�� ã�� 
					{
						if(getRequest.startsWith("ID", 11)) // ID�� ã��
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
									i = array[0] + "/" + array[1] + "/" + array[2] + "/" + array[4] + "/" + array[5]; //��й�ȣ ����
									out.println("FINDSUCCESS " + i);
								}
							}
							out.println("END");
						}
						else if(getRequest.startsWith("�̸�", 11)) // �̸����� ã�� 
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
									i = array[0] + "/" + array[1] + "/" + array[2] + "/" + array[4] + "/" + array[5]; //��й�ȣ ����
									out.println("FINDSUCCESS " + i);
								}
							}
							out.println("END");
						}

					}else if(getRequest.startsWith("EDITINFO")) //�� ��������
					{
						array = clientInfo.split("/");	//���� �α�������	
						String array2[];
						String editInfo;
						String newInfo;
						editInfo = in.nextLine();// �̸�/���¸޽����� �ٲ� ����
						array2 = editInfo.split("/");
						array[0] = array2[0]; // �̸�
						name = array[0];
						array[5] = array2[1]; // ���¸޽���
						newInfo = String.join("/", array); // ������ ����
						readMemberList = new Scanner(new FileReader(memberList));
						ArrayList<String> imBuffer = new ArrayList(); // �ӽ������� ���� ������ ��Ƶ� ��
						while(readMemberList.hasNextLine())
						{
							String l = readMemberList.nextLine();
							if(l.equals(clientInfo))
								imBuffer.add(newInfo);
							else
								imBuffer.add(l);
						}
						printMemberList = new PrintWriter(new FileWriter(memberList)); // �����ϱ� ���� �ٽþ��� 
						for(String l: imBuffer)
						{
							printMemberList.println(l);
							printMemberList.flush();
						}
						printMemberList.close();
						printMemberList = new PrintWriter(new FileWriter(memberList, true)); // �ٽ� �������� �̾�⸦ ���� �缱�� 
						out.println("EDITSUCCESS");
						for(PrintWriter p: onlineWriters) // �� ���� ���� ����� broadcast
						{
							if(p == out)
								continue;
							p.println("USERINFOCHANGE " + ID + " " + array[0]);
						}

					}else if(getRequest.startsWith("GETUSERINFO")) // ���� ������ ��û
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
								i = array[0] + "/" + array[1] + "/" + array[2] + "/" + array[4] + "/" + array[5]; //��й�ȣ ����
								out.println("GIVEUSERINFO " + i);
								break;
							}
						}
					}else if(getRequest.startsWith("CHATREQUEST")) // ä�� ��û
					{
						String opponent = getRequest.substring(12);
						oppWriter = I_W.get(opponent);
						oppWriter.println("CHATREQUEST " + ID + " " + name);
					}else if(getRequest.startsWith("CHATRESPONSE")) // ä�� ��û ����
					{
						String opponent = getRequest.substring(13);
						String[] o = opponent.split(" ");
						oppWriter = I_W.get(o[0]);
						oppWriter.println("CHATRESPONSE " + ID + " " + o[1]);
					}else if(getRequest.startsWith("MESSAGE")) // �޽��� ���� �� ����
					{
						String line = name + ": " + getRequest.substring(8);
						oppWriter.println("MESSAGE " + line);
						out.println("MESSAGE " + line);
					}else if(getRequest.startsWith("OUTCHAT")) // ä�� �����, ���濡�Ե� �����ٴ°� �˸�
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
						writer.println("EXIT " + ID); // ������ ������ �������� ��� �������� broadcast
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