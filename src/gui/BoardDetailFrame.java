package gui;

import board.dbconnection.BoardRepository;
import board.dbconnection.CategoryRepository;
import board.dbconnection.DislikeRepository;
import board.dbconnection.LikeRepository;
import board.model.Board;
import board.model.Category;
import config.AuthenticationFilter;
import exception.AuthenticationFailedException;
import exception.AuthorizationFailedException;
import exception.DuplicateTrialException;
import exception.GlobalExceptionHandler;
import member.MemberDto;
import member.MemberRepository;
import member.TokenInfo;
import util.DialogChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.AccessDeniedException;

import static config.GUI.*;
import static exception.ExceptionMessage.*;

public class BoardDetailFrame extends JFrame {
    private JPanel contentPane;
    private JTextArea textArea;
    private MemberRepository memberRepository;
    private BoardListFrame boardListFrame;
    private BoardRepository boardRepository;
    private CategoryRepository categoryRepository;
    private LikeRepository likeRepository;
    private DislikeRepository dislikeRepository;
    private Long boardNo;
    private JLabel lblLikes;
    private JLabel lblDislikes;
    private TokenInfo tokenInfo;
    private final int isAuthenticated;
    private final int isAuthorized;

    public BoardDetailFrame(
            Long boardNo, Long memberNo, Category category, String title, String authorName, String writeDate,
            String content, BoardListFrame boardListFrame, BoardRepository boardRepository,
            CategoryRepository categoryRepository, LikeRepository likeRepository, DislikeRepository dislikeRepository,
            TokenInfo tokenInfo, int isAuthenticated
    ) {
        this.boardListFrame = boardListFrame;
        this.boardRepository = boardRepository;
        this.categoryRepository = categoryRepository;
        this.likeRepository = likeRepository;
        this.dislikeRepository = dislikeRepository;
        this.boardNo = boardNo;
        this.tokenInfo = tokenInfo;

        this.isAuthenticated = isAuthenticated;
        isAuthorized = AuthenticationFilter.authorizationFilter(memberNo, tokenInfo);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT);
        setLocation(DEFAULT_FRAME_POINT_X, DEFAULT_FRAME_POINT_Y);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(79, 168, 202));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        JLabel lblNewLabel = new JLabel(title);
        lblNewLabel.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, 18));
        lblNewLabel.setForeground(new Color(255, 255, 255));
        lblNewLabel.setBounds(29, 83, 240, 23);
        contentPane.add(lblNewLabel);
        textArea = new JTextArea(content);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setBorder(new LineBorder(Color.BLACK));
        textArea.setBackground(new Color(173, 216, 230));
        textArea.setSize(200, 200);
        JScrollPane contentScrollPane = new JScrollPane(textArea);
        contentScrollPane.setBounds(29, 116, 328, 250);
        contentScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentPane.add(contentScrollPane);
        JButton btnComments = new JButton("댓글 목록");
        btnComments.setBounds(29, 376, 328, 23);
        btnComments.setForeground(new Color(255, 255, 255));
        btnComments.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_BIG_FONT_SIZE));
        btnComments.setBackground(new Color(79, 168, 202));
        btnComments.addActionListener(actionEvent -> {
            CommentListFrame commentFrame
                    = new CommentListFrame(boardNo, category, tokenInfo, isAuthenticated, memberRepository);
            commentFrame.setVisible(true);
        });
        contentPane.add(btnComments);
        JLabel lblAuthor = new JLabel("작성자 : " + authorName);
        lblAuthor.setBounds(29, 440, 158, 15);
        lblAuthor.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_BIG_FONT_SIZE));
        lblAuthor.setForeground(new Color(255, 255, 255));
        lblAuthor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int choice = JOptionPane.showConfirmDialog(
                        BoardDetailFrame.this,
                        authorName + "님의 프로필을 보시겠습니까?",
                        "프로필 확인",
                        JOptionPane.YES_NO_OPTION
                );
                if (choice == JOptionPane.YES_OPTION) {
                    if (isAuthenticated == 0) {
                        GlobalExceptionHandler.throwCheckedException(
                                new AccessDeniedException(AUTHENTICATION_FAILED_EXCEPTION_MESSAGE)
                        );
                        return;
                    }
                    MemberProfileFrame profileFrame
                            = new MemberProfileFrame(tokenInfo.getMemberNo(), memberNo, memberRepository);
                    profileFrame.setVisible(true);
                }
            }
        });
        contentPane.add(lblAuthor);
        JLabel lblDate = new JLabel("작성일: " + writeDate);
        lblDate.setFont(new Font(DEFAULT_FONT_NAME, Font.PLAIN, DEFAULT_SMALL_FONT_SIZE));
        lblDate.setForeground(new Color(255, 255, 255));
        lblDate.setBounds(225, 441, 132, 15);
        contentPane.add(lblDate);
        lblLikes = new JLabel("추천 : 0");
        lblLikes.setBounds(29, 409, 100, 15);
        lblLikes.setForeground(new Color(255, 255, 255));
        lblLikes.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_SMALL_FONT_SIZE));
        contentPane.add(lblLikes);
        lblDislikes = new JLabel("비추천 : 0");
        lblDislikes.setBounds(225, 409, 112, 15);
        lblDislikes.setForeground(new Color(255, 255, 255));
        lblDislikes.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_SMALL_FONT_SIZE));
        contentPane.add(lblDislikes);
        JButton btnEdit = new JButton("수정");
        btnEdit.setForeground(new Color(255, 255, 255));
        btnEdit.setFont(new Font(DEFAULT_FONT_NAME, Font.PLAIN, DEFAULT_BIG_FONT_SIZE));
        btnEdit.setBackground(new Color(79, 168, 202));
        btnEdit.setBounds(97, 538, 87, 23);
        btnEdit.addActionListener(actionEvent -> {
            if (isAuthenticated == 0) {
                GlobalExceptionHandler.throwCheckedException(
                        new AuthenticationFailedException(AUTHENTICATION_FAILED_EXCEPTION_MESSAGE)
                );
                return;
            }

            if (isAuthorized == 0) {
                GlobalExceptionHandler.throwCheckedException(
                        new AuthorizationFailedException(AUTHORIZATION_FAILED_EXCEPTION_MESSAGE)
                );
                return;
            }

            BoardEditFrame editFrame = new BoardEditFrame(
                    boardNo, category, title, content, boardListFrame, boardRepository, categoryRepository, tokenInfo
            );
            editFrame.setVisible(true);
            dispose();
        });
        contentPane.add(btnEdit);
        JButton btnDelete = new JButton("삭제");
        btnDelete.setForeground(new Color(255, 255, 255));
        btnDelete.setFont(new Font(DEFAULT_FONT_NAME, Font.PLAIN, DEFAULT_BIG_FONT_SIZE));
        btnDelete.setBackground(new Color(79, 168, 202));
        btnDelete.setBounds(215, 538, 87, 23);
        btnDelete.addActionListener(actionEvent -> {
            if (isAuthenticated == 0) {
                GlobalExceptionHandler.throwCheckedException(
                        new AuthenticationFailedException(AUTHENTICATION_FAILED_EXCEPTION_MESSAGE)
                );
                return;
            }

            if (isAuthorized == 0) {
                GlobalExceptionHandler.throwCheckedException(
                        new AuthorizationFailedException(AUTHORIZATION_FAILED_EXCEPTION_MESSAGE)
                );
                return;
            }

            boolean isConfirmed = DialogChooser.isYes(
                    JOptionPane.showConfirmDialog(this, "게시글을 삭제하시겠습니까?",
                            "삭제 확인", JOptionPane.YES_NO_OPTION
                    )
            );
            if (isConfirmed) {
                boardRepository.deleteByBoardNo(boardNo);
                boardListFrame.refreshBoardList();
                dispose();
            }
        });
        contentPane.add(btnDelete);
        JButton btnLike = new JButton("추천");
        btnLike.setForeground(new Color(255, 255, 255));
        btnLike.setFont(new Font(DEFAULT_FONT_NAME, Font.PLAIN, DEFAULT_BIG_FONT_SIZE));
        btnLike.setBackground(new Color(79, 168, 202));
        btnLike.setBounds(29, 484, 100, 23);
        btnLike.addActionListener(actionEvent -> {
            if (isAuthenticated == 0) {
                GlobalExceptionHandler.throwCheckedException(
                        new AuthenticationFailedException(AUTHENTICATION_FAILED_EXCEPTION_MESSAGE)
                );
                return;
            }

            if (isDuplicateRequest(memberNo)) {
                GlobalExceptionHandler.throwRuntimeException(
                        new DuplicateTrialException(DUPLICATE_LIKE_TRIAL_EXCEPTION_MESSAGE)
                );
                return;
            }
            boardRepository.incrementLikeByBoardNo(boardNo);
            likeRepository.create(boardNo, memberNo);
            refreshBoardDetails();
        });
        contentPane.add(btnLike);
        JButton btnDislike = new JButton("비추천");
        btnDislike.setForeground(new Color(255, 255, 255));
        btnDislike.setFont(new Font(DEFAULT_FONT_NAME, Font.PLAIN, DEFAULT_BIG_FONT_SIZE));
        btnDislike.setBackground(new Color(79, 168, 202));
        btnDislike.setBounds(257, 484, 100, 23);
        btnDislike.addActionListener(actionEvent -> {
            if (isAuthenticated == 0) {
                GlobalExceptionHandler.throwCheckedException(
                        new AuthenticationFailedException(AUTHENTICATION_FAILED_EXCEPTION_MESSAGE)
                );
                return;
            }

            if (isDuplicateRequest(memberNo)) {
                GlobalExceptionHandler.throwRuntimeException(
                        new DuplicateTrialException(DUPLICATE_LIKE_TRIAL_EXCEPTION_MESSAGE)
                );
                return;
            }

            boardRepository.incrementDislikeByBoardNo(boardNo);
            dislikeRepository.create(boardNo, memberNo);
            refreshBoardDetails();
        });
        JPanel nav = new JPanel();
        nav.setBackground(new Color(84, 112, 182));
        nav.setBounds(0, 0, 384, 32);
        nav.setLayout(null);
        memberRepository = new MemberRepository();
        MemberDto memberDto = memberRepository.findByMemberNo(tokenInfo.getMemberNo());
        JLabel parentName = new JLabel(memberDto.getFullNname() + " 학부모님");
        parentName.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_MEDIUM_FONT_SIZE));
        parentName.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        parentName.setForeground(new Color(255, 255, 255));
        parentName.setHorizontalAlignment(SwingConstants.CENTER);
        parentName.setBounds(87, 0, 215, 32);
        nav.add(parentName);
        JButton backButton = new JButton();
        backButton.setIcon(new ImageIcon("resources/backImage.png"));
        backButton.setBorderPainted(false);
        backButton.setBackground(new Color(84, 112, 182));
        backButton.addActionListener(actionEvent -> dispose());
        backButton.setBounds(0, 0, 54, 32);
        nav.add(backButton);
        JButton myPageBt = new JButton("");
        myPageBt.setIcon(new ImageIcon("resources/myPageBt.png"));
        myPageBt.setBorderPainted(false);
        myPageBt.setBackground(new Color(84, 112, 182));
        myPageBt.setBounds(331, 0, 54, 32);
        myPageBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MyPageFrame myPageFrame = new MyPageFrame(memberRepository, tokenInfo);
                myPageFrame.setVisible(true);
            }
        });
        nav.add(myPageBt);
        contentPane.add(nav);
        contentPane.add(btnDislike);
        refreshBoardDetails();
    }

    private void refreshBoardDetails() {
        Board board = boardRepository.findByBoardNo(boardNo);
        if (board != null) {
            textArea.setText(board.getContent());
            lblLikes.setText("추천: " + board.getLikeCount());
            lblDislikes.setText("비추천: " + board.getDislikeCount());
        }
    }

    private boolean isDuplicateRequest(Long memberNo) {
        return likeRepository.findCountByMemberNoAndBoardNo(memberNo, boardNo) > 0
                || dislikeRepository.findCountByMemberNoAndBoardNo(memberNo, boardNo) > 0;
    }
}