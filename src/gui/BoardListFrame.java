package gui;

import board.dbconnection.BoardRepository;
import board.dbconnection.CategoryRepository;
import board.dbconnection.DislikeRepository;
import board.dbconnection.LikeRepository;
import board.model.Board;
import board.model.BoardData;
import board.model.Category;
import config.AuthenticationFilter;
import exception.AuthenticationFailedException;
import exception.AuthorizationFailedException;
import exception.GlobalExceptionHandler;
import member.MemberDto;
import member.MemberRepository;
import member.TokenInfo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static config.GUI.*;
import static exception.ExceptionMessage.AUTHENTICATION_FAILED_EXCEPTION_MESSAGE;
import static exception.ExceptionMessage.NOT_ADMIN_EXCEPTION_MESSAGE;
import static java.awt.Font.BOLD;
import static java.awt.Font.PLAIN;

public class BoardListFrame extends JFrame {
    private static final String PROFILE_CONFIRM_MESSAGE = "님의 프로필을 보시겠습니까?";
    private static final String PROFILE_CONFIRM_TITLE = "프로필 확인";

    private JPanel contentPane;
    private JPanel boardPanel;
    private BoardRepository boardRepository;
    private MemberRepository memberRepository;
    private CategoryRepository categoryRepository;
    private LikeRepository likeRepository;
    private DislikeRepository dislikeRepository;
    private Map<Long, BoardData> boardDataMap;
    private Category category;
    private TokenInfo tokenInfo;
    private final int isAuthenticated;

    public BoardListFrame(int categoryNo, TokenInfo tokenInfo) {
        boardRepository = new BoardRepository();
        categoryRepository = new CategoryRepository();
        likeRepository = new LikeRepository();
        dislikeRepository = new DislikeRepository();
        category = Category.get(categoryNo);
        this.tokenInfo = tokenInfo;
        isAuthenticated = AuthenticationFilter.authenticationFilter(tokenInfo.getToken());

        setSize(DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT);
        setLocation(DEFAULT_FRAME_POINT_X, DEFAULT_FRAME_POINT_Y);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setBackground(new Color(79, 168, 202));
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel(category.getDescription() + " 게시판");
        lblNewLabel.setBounds(5, 45, 374, 52);
        lblNewLabel.setFont(new Font(DEFAULT_FONT_NAME, BOLD, 24));
        lblNewLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        lblNewLabel.setForeground(new Color(255, 255, 255));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(lblNewLabel);

        if (!category.isPopularity()) {
            JButton btnNewPost = new JButton("새 글 쓰기");
            btnNewPost.setBounds(5, 559, 374, 47);
            btnNewPost.setFont(new Font(DEFAULT_FONT_NAME, Font.PLAIN, 20));
            btnNewPost.setForeground(new Color(255, 255, 255));
            btnNewPost.setBorderPainted(false);
            btnNewPost.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            btnNewPost.setBackground(new Color(79, 168, 202));
            btnNewPost.addActionListener(actionEvent -> {
                if (isAuthenticated == 0) {
                    GlobalExceptionHandler.throwCheckedException(
                            new AuthenticationFailedException(AUTHENTICATION_FAILED_EXCEPTION_MESSAGE)
                    );
                    return;
                }

                MemberDto memberDto = new MemberRepository().findByMemberNo(tokenInfo.getMemberNo());

                if (category.isNotification() && !memberDto.isRoleAdmin()) {
                    GlobalExceptionHandler.throwCheckedException(
                            new AuthorizationFailedException(NOT_ADMIN_EXCEPTION_MESSAGE)
                    );
                    return;
                }
                openBoardRegisterFrame();
            });
            contentPane.add(btnNewPost);
        }

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(5, 97, 374, 469);
        contentPane.add(scrollPane);

        boardPanel = new JPanel();
        boardPanel.setLayout(new BoxLayout(boardPanel, BoxLayout.Y_AXIS));
        scrollPane.setViewportView(boardPanel);

        JPanel nav = new JPanel();
        nav.setLayout(null);
        nav.setBackground(new Color(84, 112, 182));
        nav.setBounds(0, 0, 384, 40);
        contentPane.add(nav);

        memberRepository = new MemberRepository();
        MemberDto memberDto = memberRepository.findByMemberNo(tokenInfo.getMemberNo());
        JLabel parentName = new JLabel(memberDto.getFullNname() + " 학부모님");
        parentName.setHorizontalAlignment(SwingConstants.CENTER);
        parentName.setForeground(Color.WHITE);
        parentName.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_MEDIUM_FONT_SIZE));
        parentName.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        parentName.setBounds(86, 2, 207, 40);
        nav.add(parentName);

        JButton backButton = new JButton();
        backButton.setIcon(new ImageIcon("resources/backImage.png"));
        backButton.setBorderPainted(false);
        backButton.setBackground(new Color(84, 112, 182));
        backButton.addActionListener(actionEvent -> dispose());
        backButton.setBounds(16, 5, 54, 32);
        nav.add(backButton);

        JButton myPageBt = new JButton("");
        myPageBt.setIcon(new ImageIcon("resources/myPageBt.png"));
        myPageBt.setBorderPainted(false);
        myPageBt.setBackground(new Color(84, 112, 182));
        myPageBt.setBounds(320, 5, 54, 32);
        myPageBt.addActionListener(actionEvent -> {
            MyPageFrame myPageFrame = new MyPageFrame(memberRepository, tokenInfo);
            myPageFrame.setVisible(true);
        });
        nav.add(myPageBt);

        boardDataMap = new HashMap<>();
        refreshBoardList();
    }

    private void openBoardRegisterFrame() {
        gui.BoardRegisterFrame registerFrame
                = new BoardRegisterFrame(this, boardRepository, categoryRepository, tokenInfo, category);
        registerFrame.setVisible(true);
    }

    private void addBoard(BoardData boardData) {
        Long boardNo = boardData.getBoardNo();
        Long memberNo = boardData.getAuthorNo();
        String title = boardData.getTitle();
        String content = boardData.getContent();
        String authorName = boardData.getAuthorName();
        String writeDate = boardData.getWriteDate();
        int viewCount = boardData.getViewCount();

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.setLayout(new GridLayout(0, 1));
        panel.setBackground(new Color(173, 216, 230));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font(DEFAULT_FONT_NAME, BOLD, 16));
        lblTitle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                boardRepository.incrementViewByBoardNo(boardNo);
                BoardDetailFrame detailFrame = new BoardDetailFrame(
                        boardNo, memberNo, category, title, authorName, writeDate,
                        content, BoardListFrame.this, boardRepository, categoryRepository,
                        likeRepository, dislikeRepository, tokenInfo, isAuthenticated
                );
                detailFrame.setVisible(true);
                refreshBoardList();
            }
        });
        panel.add(lblTitle);

        JLabel lblAuthor = new JLabel("작성자: " + authorName);
        lblAuthor.setFont(new Font(DEFAULT_FONT_NAME, PLAIN, DEFAULT_SMALL_FONT_SIZE));
        lblAuthor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int choice = JOptionPane.showConfirmDialog(
                        BoardListFrame.this, authorName + PROFILE_CONFIRM_MESSAGE,
                        PROFILE_CONFIRM_TITLE, JOptionPane.YES_NO_OPTION
                );
                if (choice == JOptionPane.YES_OPTION) {
                    MemberProfileFrame profileFrame
                            = new MemberProfileFrame(tokenInfo.getMemberNo(), memberNo, memberRepository);
                    profileFrame.setVisible(true);
                }
            }
        });
        panel.add(lblAuthor);

        JLabel lblViewCount = new JLabel("조회수: " + viewCount);
        lblViewCount.setFont(new Font(DEFAULT_FONT_NAME, PLAIN, DEFAULT_SMALL_FONT_SIZE));
        panel.add(lblViewCount);

        JLabel lblDate = new JLabel("작성일: " + writeDate);
        lblDate.setFont(new Font(DEFAULT_FONT_NAME, PLAIN, DEFAULT_SMALL_FONT_SIZE));
        panel.add(lblDate);

        boardPanel.add(panel);
        boardDataMap.put(boardNo, boardData);
        boardPanel.setBackground(new Color(79, 168, 202));
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    public void refreshBoardList() {
        boardPanel.removeAll();
        boardDataMap.clear();
        List<Board> boards = boardRepository.findAllByCategoryNoDesc(category.getCategoryNo());
        for (Board board : boards) {
            addBoard(BoardData.from(board, category));
        }
    }
}
