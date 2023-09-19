import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//

public class Server_GUI {

	public static void main(String[] args) {
		new ManagerLogin();
	}
}

class ManagerLogin extends JFrame implements ActionListener, KeyListener { 
	// 로그인 창
	Server_ChatGUI Server_chat = null;
	JPanel Port_Log = new JPanel();
	JLabel Port_Label = new JLabel("입력을 허용할 포트 번호를 입력하세요.");
	JLabel Port_Warning = new JLabel("(단, 포트 번호는 0 ~ 65535까지)");
	JTextField Port_Text = new JTextField(20);
	JButton Port_Enter = new JButton("접속!");

	//2022년 12월 4주차 추가 코드 
	//#9dc183 -> RGB(157,193,131)
	Color conceptColor = new Color(157,193,131);

	public ManagerLogin() {
		setTitle("서버 매니저 창"); //"매니저"로 수정 
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 닫았을 때 메모리에서 제거되도록 설정합니다.
		setSize(300, 200);
		setVisible(true);
		setResizable(false);
		Port_Enter.addActionListener(this);
		Port_Text.addKeyListener(this);
		Port_Log.add(Port_Label);
		Port_Log.add(Port_Warning);
		Port_Log.add(Port_Text);
		Port_Log.add(Port_Enter);
		add(Port_Log);

		//2022년 12월 4주차 추가 코드 
		Port_Log.setBackground(conceptColor);
	}

	public void actionPerformed(ActionEvent e) { 
		// "접속!" 버튼을 누르면 작동이 됩니다.
		try {
			int Port = Integer.parseInt(Port_Text.getText().trim());
			if (e.getSource() == Port_Enter) {
				Server_chat = new Server_ChatGUI(Port);
				setVisible(false);
			}
		} catch (Exception a) {
			JOptionPane.showMessageDialog(null, "올바르지 않은 입력입니다!");
		}
	}

	public void keyPressed(KeyEvent e) { 
		// 텍스트필드에 값을 입력한 후 엔터키를 누르면 작동이 됩니다.
		try {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				int Port = Integer.parseInt(Port_Text.getText().trim());
				Server_chat = new Server_ChatGUI(Port);
				setVisible(false);
			}
		} catch (Exception a) {
			JOptionPane.showMessageDialog(null, "올바르지 않은 입력입니다!");
		}

	}

	public void keyTyped(KeyEvent e) { // 불필요
	}

	public void keyReleased(KeyEvent e) { // 불필요
	}

}

class Server_ChatGUI extends JFrame implements ActionListener, KeyListener {
	// 서버용 채팅창
	JPanel ServerGUI_Panel = new JPanel();
	JLabel ServerLabel = new JLabel("Main Server");
	//유저라벨 붙이는 코드 주석처리됨->삭제할꺼면 같이 삭제
	//JLabel UserLabel = new JLabel("유저 목록");

	//1월 코드 수정 - 버튼에 이미지 넣기
	//이미지 크기 조절을 위해, image로 변환했다가 다시 
	//imageIcon으로 변환
	ImageIcon icon = new ImageIcon("./Button_Image/Send_Button_Image.png");
	Image img = icon.getImage();
	Image changeimg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
	ImageIcon changeicon = new ImageIcon(changeimg);
	JTextField Chat = new JTextField(45);
	//JButton Enter = new JButton("전송");
	JButton Enter;

	//고정할 사이즈 
	Dimension menuPanel_size = new Dimension(700,10);
	Dimension userlist_size = new Dimension(700,100); //유저리스트
	Dimension chat_size = new Dimension(500,100); //Chat 크기
	Dimension chatPanel_size = new Dimension(700,400); //chatPanel 크기

	// 화면의 가로 길이 취득
	TextArea ServerChatList = new TextArea(10, 100);
	TextArea UserList = new TextArea("", 1, 70, TextArea.SCROLLBARS_HORIZONTAL_ONLY);

	Server_Back SB = new Server_Back();
	//2022년 12월 4주차 추가 코드 
	//4번째 매개변수는 투명도임 
	Color conceptColor = new Color(157,193,131); //퍼스널 컬러
	Color chatColor = new Color(255,255,255); //흰색
	Color speech_bubble = new Color(255,255,0); //노란색
	Color backColor = new Color(155,187,212); //카톡배경색
	Color informColor = new Color(177,195,213); //공지용색상

	//메인 화면 - 라벨이 올라갈 chatPanel을 따로 만듦
	//chatPanel에 적용할 스크롤바를 선언
	JPanel chatPanel = new JPanel();
	JScrollPane scroll;

	public Server_ChatGUI(int Port) {
		setTitle("TreaTalk Server");
		setVisible(true);
		setLocationRelativeTo(null);
		setSize(750, 700);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 닫았을 때 메모리에서 제거되도록 설정합니다.

		//말풍선 패널 수정 파트
		ServerGUI_Panel.setLayout(new BoxLayout(ServerGUI_Panel, BoxLayout.Y_AXIS));
		chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));

		//TextArea에 있는 텍스트는 편집불가하도록 설정 
		ServerChatList.setEditable(false); 
		UserList.setEditable(false); 

		//1월 코드 수정 버튼 작업 
		Enter = new JButton(changeicon);
		Enter.addActionListener(this);
		Chat.addKeyListener(this);
		Enter.setPreferredSize(new Dimension(100,100)); //버튼크기

		//menuPanel은 최상단패널(공간확보)
		Panel menuPanel = new Panel();
		menuPanel.setLayout(new BorderLayout());
		menuPanel.add(ServerLabel);

		//Users는 상단유저목록패널
		Panel Users=new Panel();
		Users.setLayout(new BorderLayout());
		Users.add(UserList, "Center");

		//Input은 하단 Chat과 Enter가 들어갈 패널
		Panel Input = new Panel();
		Input.setLayout(new BorderLayout());
		Input.add(Chat, "Center");
		Input.add(Enter, "East");

		//스크롤바 추가, chatPanel을 JScrollPane인 scroll로 감싸고,
		//scroll을 ServerGUIPanel에 추가합니다. 
		//그 밑에 코드는 항상 수직인 스크롤바를 추가한다는 의미
		scroll = new JScrollPane(chatPanel);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//사이즈 조정
		menuPanel.setMaximumSize(menuPanel_size);
		scroll.setMaximumSize(chatPanel_size);
		Users.setMaximumSize(userlist_size);
		Chat.setMaximumSize(chat_size);
		Input.setMaximumSize(userlist_size);

		//패널 순서 조정
		ServerGUI_Panel.add(menuPanel);
		ServerGUI_Panel.add(Users,"Center"); //유저 패널 부착(유저리스트)
		ServerGUI_Panel.add(scroll);
		ServerGUI_Panel.add(Input,"South");
		//ServerGUI_Panel.add(Server); //서버 패널 부착(서버라벨, 챗리스트)
		//ServerGUI_Panel.add(Chat); //챗을 입력하는 공간 부착
		//ServerGUI_Panel.add(Enter); //버튼을 부착
		add(ServerGUI_Panel); //프레임에 패널 부착 

		//2022년 12월 4주차 추가 코드 
		ServerGUI_Panel.setBackground(conceptColor);
		//Server.setBackground(conceptColor);
		UserList.setBackground(chatColor);
		ServerChatList.setBackground(chatColor);
		//2023년 3월 추가
		//chatPanel.setBackground(chatColor);
		chatPanel.setBackground(backColor);
		menuPanel.setBackground(conceptColor);

		UserList.append("Admin\n"); // 실행과 동시에 서버주인(Admin)을 유저목록에 추가하도록 합니다.
		SB.setGUI(this);
		SB.Start_Server(Port);
		SB.start(); // 서버 채팅창이 켜짐과 동시에 서버소켓도 함께 켜집니다.
	}

	public void actionPerformed(ActionEvent e) { // 전송 버튼을 누르고, 입력값이 1이상일때만 전송되도록 합니다.
		String Message = "서버 : " + Chat.getText().trim();
		if (e.getSource() == Enter && Message.length() > 0) {
			AppendMessage("서버 : " + Message + "\n");
			SB.Transmitall("서버 : " + Message + "\n");
			Chat.setText(null); // 채팅창 입력값을 초기화 시켜줍니다.
			Chat.setText("");
			//줄바꿈
			String message = Message;
			if (message.length() > 30) {
			    StringBuilder sb = new StringBuilder("<html>");
			    while (message.length() > 30) {
			        sb.append(message.substring(0, 30)).append("<br>");
			        message = message.substring(30);
			    }
			    sb.append(message).append("</html>");
			    message = sb.toString();
			}

			JLabel label = new JLabel(message);

			//말풍선 색 설정
			label.setOpaque(true); 
			label.setBackground(speech_bubble);
			label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

			//TOP_ALIGNMENT는 상단에서 아래로
			//label.setAlignmentY(Component.TOP_ALIGNMENT);
			//label.setAlignmentX(Component.RIGHT_ALIGNMENT);
			//label.setHorizontalAlignment(JLabel.CENTER);
			chatPanel.add(label);
			scrollToBottom();
			reconstruction();
		}
	}

	public void keyPressed(KeyEvent e) { // 키보드 엔터키를 누르고, 입력값이 1이상일때만 전송되도록 합니다.
		String Message = "서버 : " + Chat.getText().trim();
		String GAP = "      ";
		if (e.getKeyCode() == KeyEvent.VK_ENTER && Message.length() > 0) {
			AppendMessage("서버 : " + Message + "\n");
			SB.Transmitall("서버 : " + Message + "\n");
			Chat.setText(null); // 채팅창 입력값을 초기화 시켜줍니다.

			Chat.setText("");
			//줄바꿈
			String message = Message;
			if (message.length() > 30) {
			    StringBuilder sb = new StringBuilder("<html>");
			    while (message.length() > 30) {
			        sb.append(message.substring(0, 30)).append("<br>");
			        message = message.substring(30);
			    }
			    sb.append(message).append("</html>");
			    message = sb.toString();
			}
			else {
				StringBuilder sb = new StringBuilder("<html>");
				sb.append(message).append("</html>");
			    message = sb.toString();
			}
			
			JLabel label = new JLabel(message);
			//마지막 업데이트 
			JLabel gap_Label = new JLabel(GAP);
			
			label.setVerticalAlignment(JLabel.TOP);
			label.setMaximumSize(new Dimension(30*label.getFontMetrics(label.getFont()).charWidth('W'), label.getPreferredSize().height + 10));
			//label.setPreferredSize(new Dimension(30*label.getFontMetrics(label.getFont()).charWidth('W'), label.getPreferredSize().height + 10));

			gap_Label.setVerticalAlignment(JLabel.TOP);
			gap_Label.setMaximumSize(new Dimension(label.getPreferredSize().width, 10));
			//gap_Label.getPreferredSize().height
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.setPreferredSize(new Dimension(label.getPreferredSize().width, label.getPreferredSize().height));
			panel.add(label);
			//
			//panel.add(gap_Label);

			//말풍선 색 설정
			label.setOpaque(true); 
			label.setBackground(speech_bubble);
			label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

			//TOP_ALIGNMENT는 상단에서 아래로
			//label.setAlignmentY(Component.TOP_ALIGNMENT);
			//label.setAlignmentX(Component.RIGHT_ALIGNMENT);
			//label.setHorizontalAlignment(JLabel.CENTER);
			chatPanel.add(panel);
			chatPanel.add(gap_Label);
			scrollToBottom();
			reconstruction();
		}
	}
	//화면재구성
	public void reconstruction() {
		chatPanel.validate();
		chatPanel.revalidate();
		chatPanel.repaint();
	}

	// 스크롤바를 맨 아래쪽으로 이동시키는 메서드
	public void scrollToBottom() {
		JScrollBar verticalScrollBar = scroll.getVerticalScrollBar();
		verticalScrollBar.setValue(verticalScrollBar.getMaximum() - verticalScrollBar.getVisibleAmount());
	}

	public void AppendMessage(String Message) {
		ServerChatList.append(Message);
	}

	public void AppendUserList(ArrayList NickName) {
		String name;
		for (int i = 0; i < NickName.size(); i++) {
			name = (String) NickName.get(i);
			//UserList.append(name + "\n");
			//추가한 코드
			if (i == NickName.size()-1) {
				UserList.append(name);
			} else {
				UserList.append(name + ", ");
			}
		}
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}
}