package gui;
import board.dbconnection.BoardRepository;
import board.model.Board;
import member.MemberDto;
import member.MemberRepository;
import member.TokenInfo;
import message.dbconnection.MessageRepository;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import static config.GUI.*;
import static java.awt.Font.BOLD;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
public class MainFrameScroll extends JFrame {
    ImageIcon mainIcon;
    TitledBorder tBorder;
    AbstractBorder border = new LineBorder(Color.BLACK, 2); //2굵기로 검정
    TokenInfo tokenInfo;
    Long memberNo;
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TokenInfo tokenInfo = new TokenInfo();
                    tokenInfo.setMemberNo(4L);
                    tokenInfo.setToken("abc");
                    MainFrameScroll window = new MainFrameScroll(tokenInfo);
                    window.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public MainFrameScroll(TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
        this.memberNo = tokenInfo.getMemberNo();
        initialize();
    }
    ImageIcon imageSetSize(ImageIcon icon, int i, int j) { // image Size Setting
        Image ximg = icon.getImage();
        Image yimg = ximg.getScaledInstance(i, j, java.awt.Image.SCALE_SMOOTH);
        ImageIcon xyimg = new ImageIcon(yimg);
        return xyimg;
    }
    private void initialize() {
        List<Board> notiBoardsTitle = new ArrayList<>();
        List<Board> popuBoardsTitle = new ArrayList<>();
        List<Board> elementaryBoardsTitle = new ArrayList<>();
        List<Board> middleBoardsTitle = new ArrayList<>();
        List<Board> highBoardsTitle = new ArrayList<>();
        BoardRepository boardRepository = new BoardRepository();
        int popuCategoryNo = 0;
        int notiCategoryNo = 1;
        int elementaryCategoryNo = 2;
        int middleCategoryNo = 3;
        int highCategoryNo = 4;
        popuBoardsTitle = boardRepository.findFiveByCategoryNoDesc(popuCategoryNo);
        notiBoardsTitle = boardRepository.findFiveByCategoryNoDesc(notiCategoryNo);
        elementaryBoardsTitle = boardRepository.findFiveByCategoryNoDesc(elementaryCategoryNo);
        middleBoardsTitle = boardRepository.findFiveByCategoryNoDesc(middleCategoryNo); //게시판의 제목 최신순으로 4개
        highBoardsTitle = boardRepository.findFiveByCategoryNoDesc(highCategoryNo);
        //pln(""+boardRepository.findAllByCategoryNoDesc(categoryNo).get(3).getWriteDate());
        setBounds(100, 100, 286, 487);
        setResizable(false);
        
        setLocation(DEFAULT_FRAME_POINT_X, DEFAULT_FRAME_POINT_Y);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout(0, 0));
        setSize(new Dimension(DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT));
        JPanel board = new JPanel(new GridLayout(7, 1, 10, 10));
        ImageIcon mainIcon = new ImageIcon("resources/mainImage.png");
        ImageIcon updateIcon = imageSetSize(mainIcon, 300, 200);
        JPanel mainPanel = new JPanel();
        JPanel imageP = new JPanel();
        JLabel laImg = new JLabel(updateIcon);
        imageP.setOpaque(false);
        imageP.add(laImg);
        // JScrollPane 추가
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        JPanel boardNoti = new JPanel(new GridLayout(5, 1));
        JButton NotiPlus = new JButton("+");
        NotiPlus.setBorderPainted(false);
        NotiPlus.setFont(new Font(DEFAULT_FONT_NAME, Font.PLAIN, 20));
        NotiPlus.setOpaque(false);
        NotiPlus.setBackground(Color.white);
        NotiPlus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BoardListFrame boardListFrame = new BoardListFrame(notiCategoryNo, tokenInfo);
                boardListFrame.setVisible(true);
            }
        });
        JLabel notiText1 = new JLabel(notiBoardsTitle.get(0).getTitle());
        JLabel notiText2 = new JLabel(notiBoardsTitle.get(1).getTitle());
        JLabel notiText3 = new JLabel(notiBoardsTitle.get(2).getTitle());
        JLabel notiText4 = new JLabel(notiBoardsTitle.get(3).getTitle());
        // JLabel notiText5 = new JLabel(notiBoardsTitle.get(4).getTitle());
        
        notiText1.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_BIG_FONT_SIZE));
        notiText2.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_BIG_FONT_SIZE));
        notiText3.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_BIG_FONT_SIZE));
        notiText4.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_BIG_FONT_SIZE));
        
        boardNoti.add(notiText1);
        boardNoti.add(notiText2);
        boardNoti.add(notiText3);
        boardNoti.add(notiText4);
        boardNoti.add(NotiPlus);
        JPanel boardElementary = new JPanel(new GridLayout(5, 1));
        JButton elementaryPlus = new JButton("+");
        elementaryPlus.setBackground(Color.white);
        elementaryPlus.setBorderPainted(false);
        elementaryPlus.setFont(new Font(DEFAULT_FONT_NAME, Font.PLAIN, 20));
        elementaryPlus.setOpaque(false);
        elementaryPlus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BoardListFrame boardListFrame = new BoardListFrame(elementaryCategoryNo, tokenInfo);
                boardListFrame.setVisible(true);
            }
        });
        JLabel elementaryText1 = new JLabel(elementaryBoardsTitle.get(0).getTitle());
        JLabel elementaryText2 = new JLabel(elementaryBoardsTitle.get(1).getTitle());
        JLabel elementaryText3 = new JLabel(elementaryBoardsTitle.get(2).getTitle());
        JLabel elementaryText4 = new JLabel(elementaryBoardsTitle.get(3).getTitle());
        
        elementaryText1.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_BIG_FONT_SIZE));
        elementaryText2.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_BIG_FONT_SIZE));
        elementaryText3.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_BIG_FONT_SIZE));
        elementaryText4.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_BIG_FONT_SIZE));
        
        boardElementary.add(elementaryText1);
        boardElementary.add(elementaryText2);
        boardElementary.add(elementaryText3);
        boardElementary.add(elementaryText4);
        boardElementary.add(elementaryPlus);
        JPanel boardMiddle = new JPanel(new GridLayout(5, 1));
        JButton middlePlus = new JButton("+");
        middlePlus.setBackground(Color.white);
        middlePlus.setBorderPainted(false);
        middlePlus.setFont(new Font(DEFAULT_FONT_NAME, Font.PLAIN, 20));
        middlePlus.setOpaque(false);
        middlePlus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BoardListFrame boardListFrame = new BoardListFrame(middleCategoryNo, tokenInfo);
                boardListFrame.setVisible(true);
            }
        });
        JLabel middleText1 = new JLabel(middleBoardsTitle.get(0).getTitle());
        JLabel middleText2 = new JLabel(middleBoardsTitle.get(1).getTitle());
        JLabel middleText3 = new JLabel(middleBoardsTitle.get(2).getTitle());
        JLabel middleText4 = new JLabel(middleBoardsTitle.get(3).getTitle());
        
        middleText1.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_BIG_FONT_SIZE));
        middleText2.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_BIG_FONT_SIZE));
        middleText3.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_BIG_FONT_SIZE));
        middleText4.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_BIG_FONT_SIZE));
        
        boardMiddle.add(middleText1);
        boardMiddle.add(middleText2);
        boardMiddle.add(middleText3);
        boardMiddle.add(middleText4);
        boardMiddle.add(middlePlus);
        JPanel boardHigh = new JPanel(new GridLayout(5, 1));
        JButton highPlus = new JButton("+");
        highPlus.setBorderPainted(false);
        highPlus.setFont(new Font(DEFAULT_FONT_NAME, Font.PLAIN, 20));
        highPlus.setOpaque(false);
        highPlus.setBackground(Color.white);
        highPlus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BoardListFrame boardListFrame = new BoardListFrame(highCategoryNo, tokenInfo);
                boardListFrame.setVisible(true);
            }
        });
        JLabel highText1 = new JLabel(highBoardsTitle.get(0).getTitle());
        JLabel highText2 = new JLabel(highBoardsTitle.get(1).getTitle());
        JLabel highText3 = new JLabel(highBoardsTitle.get(2).getTitle());
        JLabel highText4 = new JLabel(highBoardsTitle.get(3).getTitle());
        
        highText1.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_BIG_FONT_SIZE));
        highText2.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_BIG_FONT_SIZE));
        highText3.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_BIG_FONT_SIZE));
        highText4.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_BIG_FONT_SIZE));
        boardHigh.add(highText1);
        boardHigh.add(highText2);
        boardHigh.add(highText3);
        boardHigh.add(highText4);
        boardHigh.add(highPlus);
        //boardFree.add(freeTest5);
        JPanel boardPopu = new JPanel(new GridLayout(5, 1));
        JButton popuPlus = new JButton("+");
        popuPlus.setBorderPainted(false);
        popuPlus.setFont(new Font(DEFAULT_FONT_NAME, Font.PLAIN, 20));
        popuPlus.setOpaque(false);
        popuPlus.setBackground(Color.white);
        popuPlus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BoardListFrame boardListFrame = new BoardListFrame(popuCategoryNo, tokenInfo);
                boardListFrame.setVisible(true);
            }
        });
        JLabel popuText1 = new JLabel(popuBoardsTitle.get(0).getTitle());
        JLabel popuText2 = new JLabel(popuBoardsTitle.get(1).getTitle());
        JLabel popuText3 = new JLabel(popuBoardsTitle.get(2).getTitle());
        JLabel popuText4 = new JLabel(popuBoardsTitle.get(3).getTitle());
        
        popuText1.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_BIG_FONT_SIZE));
        popuText2.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_BIG_FONT_SIZE));
        popuText3.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_BIG_FONT_SIZE));
        popuText4.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_BIG_FONT_SIZE));
        boardPopu.add(popuText1);
        boardPopu.add(popuText2);
        boardPopu.add(popuText3);
        boardPopu.add(popuText4);
        boardPopu.add(popuPlus);
        JPanel nav = new JPanel(new BorderLayout());
        JPanel nav1 = new JPanel(new BorderLayout());
        nav.setBackground(Color.WHITE);
        JButton myPageBt = new JButton();
        myPageBt.setIcon(new ImageIcon("resources/myPageImage.png"));
        myPageBt.setBackground(Color.white);
        myPageBt.setForeground(new Color(255, 255, 255));
        myPageBt.setBorderPainted(false);
        myPageBt.setFont(new Font(DEFAULT_FONT_NAME, Font.PLAIN, 30));
        myPageBt.setOpaque(false);
        MemberRepository memberRepository = new MemberRepository();
        MemberDto memberDto = memberRepository.findByMemberNo(memberNo);
        JLabel parentName = new JLabel(memberDto.getFullNname());
        parentName.setFont(new Font(DEFAULT_FONT_NAME, Font.PLAIN, 40));
        parentName.setForeground(new Color(255, 255, 255));
        myPageBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MyPageFrame myPageFrame = new MyPageFrame(memberRepository, tokenInfo);
                myPageFrame.setVisible(true);
            }
        });
        //JLabel parentText = new JLabel("학부모님, 안녕하세요.");
        //parentText.setFont(new Font(DEFAULT_FONT_NAME, Font.PLAIN, DEFAULT_SMALL_FONT_SIZE));
        //parentText.setForeground(new Color(255, 255, 255));
        tBorder = new TitledBorder("학부모님, 안녕하세요.");
        JLabel nullLabel = new JLabel("                 ");
        JLabel nullLabel1 = new JLabel("                              ");
        JLabel nullLabel2 = new JLabel("                 ");
        JLabel nullLabel3 = new JLabel("                 ");
        nullLabel1.setHorizontalAlignment(JLabel.CENTER);
        nav1.add(nullLabel, BorderLayout.NORTH);
        nav1.add(nullLabel1, BorderLayout.WEST);
        nav1.add(nullLabel3, BorderLayout.SOUTH);
        nav1.add(parentName, BorderLayout.CENTER);
        JLabel lblAlarm = new JLabel("알림: ");
        lblAlarm.setFont(new Font(DEFAULT_FONT_NAME, Font.PLAIN, DEFAULT_SMALL_FONT_SIZE));
        lblAlarm.setBounds(265, 30, 100, 100);
        nav.add(lblAlarm);
        MessageRepository messageRepository = new MessageRepository();
        int noticeCount = messageRepository.getCountNoticeMessage(memberNo);
        JLabel lblAlarmCount = new JLabel(String.valueOf(noticeCount));
        lblAlarmCount.setBounds(300, 30, 100, 100);
        lblAlarmCount.setForeground(new Color(255, 255, 255));
        nav.add(lblAlarmCount);
        lblAlarmCount.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                NoticeInfoFrame noticeInfoFrame = new NoticeInfoFrame(memberNo);
                noticeInfoFrame.setVisible(true);
                messageRepository.updateReceiveCheckToZero(memberNo);
            }
        });
        nav.add(nullLabel2, BorderLayout.NORTH);
        nav.add(myPageBt, BorderLayout.CENTER);
        nav.add(getLabel(nav1, tBorder), BorderLayout.SOUTH);
        //footer.add(lblNewLabel);
        nav.setBackground(new Color(79, 168, 202));
        nav1.setBackground(new Color(84, 112, 182));
        board.add(nav);
        board.add(imageP);
        board.add(getLabel(boardNoti, border, "공지"));
        board.add(getLabel(boardElementary, border, "초등"));
        board.add(getLabel(boardMiddle, border, "중등"));
        board.add(getLabel(boardHigh, border, "고등")); // db에서 얻어오는걸로 바꿔주기.
        board.add(getLabel(boardPopu, border, "인기"));
        boardNoti.setBackground(new Color(173, 216, 230));
        boardElementary.setBackground(new Color(173, 216, 230));
        boardMiddle.setBackground(new Color(173, 216, 230));
        boardHigh.setBackground(new Color(173, 216, 230));
        boardPopu.setBackground(new Color(173, 216, 230));
        board.setBackground(new Color(79, 168, 202));
        mainPanel.setBackground(new Color(79, 168, 202));
        board.setBounds(50, 5055, 50, 50);
        mainPanel.add(board);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        getContentPane().add(scrollPane);
    }
    private JPanel getLabel(JPanel lbl, AbstractBorder titleBorder, String title) {
        AbstractBorder lineBorder = new LineBorder(new Color(70 , 130, 180), 2, true); //2굵기로 검정
        titleBorder = new TitledBorder(title);
        CompoundBorder border = new CompoundBorder(lineBorder,titleBorder);
        lbl.setBorder(border);
        return lbl;
    }
    private JPanel getLabel(JPanel lbl, AbstractBorder border) {
        tBorder.setTitleJustification(TitledBorder.CENTER);
        tBorder.setTitlePosition(TitledBorder.BOTTOM);
        tBorder.setTitleColor(Color.white);
        lbl.setBorder(border);
        return lbl;
    }
    private void p(String str) {
        System.out.print(str);
    }
    private void pln(String str) {
        System.out.println(str);
    }
}
