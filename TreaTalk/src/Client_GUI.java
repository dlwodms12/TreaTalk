
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client_GUI {
   public static void main(String[] args) {
      LoginGUI LG = new LoginGUI();
   }
}

class LoginGUI extends JFrame implements ActionListener {
   // 유저의 로그인 창
   private JPanel Login_GUIPanel = new JPanel();
   private JTextField NickName_Text = new JTextField(20);
   private JTextField Port_Text = new JTextField("####", 20);
   private JTextField IPAddress_Text = new JTextField("###.###.###.###", 20);
   private JLabel NickName_Label = new JLabel("유저 입력");
   private JLabel Port_Label = new JLabel("포트 입력");
   private JLabel IPAddress_Label = new JLabel("주소 입력");
   private JButton Login_GUI_Button = new JButton("접속!");
   
    //2022년 12월 4주차 추가 코드 
  	//#9dc183 -> RGB(157,193,131)
  	Color conceptColor = new Color(157,193,131);

   public LoginGUI() {
      setTitle("로그인 화면");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setLocationRelativeTo(null);
      setSize(300, 250);
      setResizable(false);
      setVisible(true);
      Login_GUI_Button.setPreferredSize(new Dimension(260, 40));
      Login_GUI_Button.addActionListener(this);
      Login_GUIPanel.add(NickName_Label);
      Login_GUIPanel.add(NickName_Text);
      Login_GUIPanel.add(Port_Label);
      Login_GUIPanel.add(Port_Text);
      Login_GUIPanel.add(IPAddress_Label);
      Login_GUIPanel.add(IPAddress_Text);
      Login_GUIPanel.add(Login_GUI_Button);
      add(Login_GUIPanel);
      
      //2022년 12월 4주차 추가 코드 
      Login_GUIPanel.setBackground(conceptColor);
   }

   public void actionPerformed(ActionEvent e) {
      // 닉네임, 주소, 포트값을 버튼을 통해 입력받습니다.
      try {
         if (e.getSource() == Login_GUI_Button) {
            String NickName = NickName_Text.getText().trim();
            String IPAddress = IPAddress_Text.getText().trim();
            int Port = Integer.parseInt(Port_Text.getText().trim());
            new Client_ChatGUI(NickName, IPAddress, Port);
            setVisible(false);
         }
      } catch (Exception a) {
         // 만약 올바르지 않는 값이 입력되면 팝업창을 띄워줍니다.
         JOptionPane.showMessageDialog(null, "올바르지 않은 입력입니다!");
      }
   }
}

class Client_ChatGUI extends JFrame implements ActionListener, KeyListener {
   //클라이언트용 채팅창
   String NickName;
   Client_Back CB = new Client_Back();
   JPanel ClientGUIPanel = new JPanel();
   JLabel UserLabel = new JLabel("유저 목록");
   JLabel User = new JLabel(NickName);
   JTextField Chat = new JTextField(45);
   //JButton Enter = new JButton("전송");
   JButton Enter;
   
    //메인 화면 - 라벨이 올라갈 chatPanel을 따로 만듦
  	//chatPanel에 적용할 스크롤바를 선언
  	JPanel chatPanel = new JPanel();
    JScrollPane scroll;
   
   // 화면의 가로 길이 취득
   TextArea ChatList = new TextArea(30, 70); //30,70으로 변경
   TextArea UserList = new TextArea("", 1, 70, TextArea.SCROLLBARS_HORIZONTAL_ONLY);
   
   //2022년 12월 4주차 추가 코드 
   //4번째 매개변수는 투명도임 
	Color conceptColor = new Color(157,193,131); //퍼스널 컬러
	Color chatColor = new Color(255,255,255); //흰색
	Color speech_bubble = new Color(255,255,0); //노란색 
	Color backColor = new Color(155,187,212); //카톡배경색
   
   //1월 코드 수정 - 버튼에 이미지 넣기
   //이미지 크기 조절을 위해, image로 변환했다가 다시 
   //imageIcon으로 변환
   ImageIcon icon = new ImageIcon("./Button_Image/Send_Button_Image.png");
   Image img = icon.getImage();
   Image changeimg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
   ImageIcon changeicon = new ImageIcon(changeimg);
   
   //고정할 사이즈 
   Dimension menuPanel_size = new Dimension(700,10);
   Dimension userlist_size = new Dimension(700,100); //유저리스트
   Dimension chat_size = new Dimension(500,100); //Chat 크기
   Dimension chatPanel_size = new Dimension(700,400); //chatPanel 크기

   public Client_ChatGUI(String NickName, String IPAddress, int Port) {
      this.NickName = NickName;

      setTitle("TreaTalk"); //고객창에서 TreaTalk으로 변경
      setVisible(true);
      setLocationRelativeTo(null);
      setSize(750, 700);
      setResizable(false);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      //말풍선 패널 수정 파트
      ClientGUIPanel.setLayout(new BoxLayout(ClientGUIPanel, BoxLayout.Y_AXIS));
      chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
      //TextArea에 있는 텍스트는 편집불가하도록 설정 
      ChatList.setEditable(false);
      UserList.setEditable(false);
      
      //1월 코드 수정 버튼 작업 
      Enter = new JButton(changeicon);
      Enter.addActionListener(this);
      Chat.addKeyListener(this);
      Enter.setPreferredSize(new Dimension(100,100)); //버튼크기
      
      //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      
      //menuPanel은 최상단패널(나중에 이미지 전송기능대비공간확보)
      Panel menuPanel = new Panel();
      menuPanel.setLayout(new BorderLayout());
      menuPanel.add(User);
      
      //Users는 상단유저목록패널
      Panel Users=new Panel();
      Users.setLayout(new BorderLayout());
      //Users.add(UserLabel, "Center");
      Users.add(UserList, "Center");
      
      //Input은 하단 Chat과 Enter가 들어갈 패널
      Panel Input = new Panel();
      Input.setLayout(new BorderLayout());
      Input.add(Chat, "Center");
      Input.add(Enter, "East");
      
      //Panel Server=new Panel();
      //Server.setLayout(new BorderLayout());
      //Server.add(ChatList, "North");
      // Server.add(new Label("메인 서버", Label.CENTER), "North");
      
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
      ClientGUIPanel.add(menuPanel);
      ClientGUIPanel.add(Users,"Center");
      ClientGUIPanel.add(scroll);
      ClientGUIPanel.add(Input,"South");
      add(ClientGUIPanel);
      
      CB.setGui(this);
      CB.getUserInfo(NickName, IPAddress, Port);
      CB.start(); // 채팅창이 켜짐과 동시에 접속을 실행해줍니다.
      
      //2022년 12월 4주차 추가 코드 
      ClientGUIPanel.setBackground(conceptColor);
      UserList.setBackground(chatColor);
      ChatList.setBackground(chatColor);
      //2023년 3월 추가
      chatPanel.setBackground(backColor);
      menuPanel.setBackground(conceptColor);
   }

   public void actionPerformed(ActionEvent e) { 
      // 전송 버튼을 누르고, 입력값이 1이상일때만 전송되도록 합니다.
      String Message = Chat.getText().trim();
      if (e.getSource() == Enter && Message.length() > 0) {
         CB.Transmit(NickName + " : " + Message + "\n");
         Chat.setText(null);
         
         Chat.setText("");
         JLabel label = new JLabel(NickName + " : " + Message);
         
         //말풍선 색 설정
         label.setOpaque(true); 
         label.setBackground(speech_bubble);
         label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
         //label.setAlignmentY(Component.TOP_ALIGNMENT);
         label.setAlignmentX(Component.RIGHT_ALIGNMENT);
         chatPanel.add(label);
         chatPanel.validate();
         scroll.revalidate();
         //chatPanel.repaint();
         scrollToBottom();
         
      }
   }

   public void keyPressed(KeyEvent e) { 
      // 키보드 엔터키를 누르고, 입력값이 1이상일때만 전송되도록 합니다.
      String Message = Chat.getText().trim();
      if (e.getKeyCode() == KeyEvent.VK_ENTER && Message.length() > 0) {
         CB.Transmit(NickName + " : " + Message + "\n");
         Chat.setText(null);
         
         Chat.setText("");
         JLabel label = new JLabel(NickName + " : " + Message);
         
         //말풍선 색 설정
         label.setOpaque(true); 
         label.setBackground(speech_bubble);
         label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
         //label.setAlignmentY(Component.TOP_ALIGNMENT);
         label.setAlignmentX(Component.RIGHT_ALIGNMENT);
         
         chatPanel.add(label);
         chatPanel.validate();
         scroll.revalidate();
         //chatPanel.repaint();
         scrollToBottom();
         
      }
   }
   //화면 재구성
   public void retry() {
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
      ChatList.append(Message);
   }

   public void AppendUserList(ArrayList NickName) {
      // 유저목록을 유저리스트에 띄워줍니다.
      String name;
      UserList.setText(null);
      for (int i = 0; i < NickName.size(); i++) {
         name = (String) NickName.get(i);
         //UserList.append(name + ", ");
         //수정한 코드
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