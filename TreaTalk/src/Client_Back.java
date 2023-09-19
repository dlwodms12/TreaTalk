import java.awt.Color;
import java.awt.Component;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class Client_Back extends Thread {
	private String NickName, IPAddress;
	private int Port;
	private Socket socket;
	private String Message;
	private DataInputStream in;
	private DataOutputStream out;
	private Client_ChatGUI chatgui;
	ArrayList<String> NickNameList = new ArrayList<String>(); // 유저목록을 저장합니다.
	Color chatColor = new Color(255,255,255); //흰색
	Color conceptColor = new Color(157,193,131); //퍼스널 컬러
	Color speech_bubble = new Color(255,255,0); //노란색 
	Color backColor = new Color(155,187,212); //카톡배경색
	Color informColor = new Color(177,195,213); //공지용 색상

	public void getUserInfo(String NickName, String IPAddress, int Port) {
		// Client_GUI로부터 닉네임, 아이피, 포트 값을 받아옵니다.
		this.NickName = NickName;
		this.IPAddress = IPAddress;
		this.Port = Port;
	}

	public void setGui(Client_ChatGUI chatgui) {
		// 실행했던 Client_GUI 그 자체의 정보를 들고옵니다.
		this.chatgui = chatgui;
	}

	public void run() {
		// 서버 접속을 실행합니다.
		try {
			socket = new Socket(IPAddress, Port);
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
			out.writeUTF(NickName);
			while (in != null) { 
				// 임의의 식별자를 받아 닉네임 혹은 일반 메세지인지 등을 구분시킵니다.
				Message = in.readUTF();
				if (Message.contains("+++닉네임의시작+++")) { 
					// +++닉네임의시작+++이라는 수식어가 붙어있을 경우엔 닉네임으로 간주합니다.
					chatgui.UserList.setText(null);
					NickNameList.add(Message.substring(12));
					chatgui.AppendUserList(NickNameList);
				} else if (Message.contains("님이 입장하셨습니다.")) {
					// ~~ 님이 입장하셨습니다. 라는 식별자를 받으면 기존의 닉네임 리스트 초기화 후 새로 입력시킵니다.
					NickNameList.clear();
					chatgui.UserList.setText(null);
					chatgui.AppendMessage(Message);
					
					//입장 알림 메시지를 말풍선으로 화면에 출력합니다.
					JLabel label = new JLabel(NickName + "님이 입장하셨습니다.\n");
					//말풍선 색 설정
					Color chatColor = new Color(255,255,255); //흰색
			        label.setOpaque(true); 
			        label.setBackground(informColor);
					label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
					label.setAlignmentX(Component.LEFT_ALIGNMENT);
			        //label.setAlignmentY(Component.TOP_ALIGNMENT);
			        chatgui.chatPanel.add(label);
			        chatgui.chatPanel.validate();
			        chatgui.scroll.revalidate();
			        chatgui.scrollToBottom();
					
				} else if (Message.contains("님이 퇴장하셨습니다.")) {
					// ~~ 님이 퇴장하셨습니다. 라는 식별자를 받으면 기존의 닉네임 리스트 초기화 후 새로 입력시킵니다.
					NickNameList.clear();
					chatgui.UserList.setText(null);
					chatgui.AppendMessage(Message);
					//입장 알림 메시지를 말풍선으로 화면에 출력합니다.
					JLabel label = new JLabel(NickName + "님이 퇴장하셨습니다.\n");
					//말풍선 색 설정
					Color chatColor = new Color(255,255,255); //흰색
			        label.setOpaque(true); 
			        label.setBackground(informColor);
					label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
					label.setAlignmentX(Component.LEFT_ALIGNMENT);
			        //label.setAlignmentY(Component.TOP_ALIGNMENT);
			        chatgui.chatPanel.add(label);
			        chatgui.chatPanel.validate();
			        chatgui.scroll.revalidate();
			        chatgui.scrollToBottom();
			        
				} else if (Message.contains(NickName)) {
					//서버로부터 수신한 메시지가 자신이 보낸 메시지라면, 무시합니다. 
					continue;
				} else {
					// 위 모든 값이 아닐 시엔 일반 메세지로 간주합니다.
					chatgui.AppendMessage(Message);
					
					//일반 메시지를 말풍선으로 화면에 출력합니다. 
					JLabel messageLabel = new JLabel(Message);
					messageLabel.setOpaque(true); 
					messageLabel.setBackground(chatColor);
					messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
					messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
					//messageLabel.setAlignmentY(Component.TOP_ALIGNMENT);
					chatgui.chatPanel.add(messageLabel);
					chatgui.chatPanel.validate();
					chatgui.scroll.revalidate();
					chatgui.scrollToBottom();
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void Transmit(String Message) {
		// 입력받은 값을 서버로 전송(out) 해줍니다.
		try {
			out.writeUTF(Message);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}