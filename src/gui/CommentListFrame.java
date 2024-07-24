package gui;

import board.dbconnection.CommentRepository;
import board.model.Category;
import board.model.Comment;
import board.model.CommentData;
import config.AuthenticationFilter;
import exception.AuthenticationFailedException;
import exception.AuthorizationFailedException;
import exception.GlobalExceptionHandler;
import member.MemberDto;
import member.MemberRepository;
import member.TokenInfo;
import util.DialogChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static config.GUI.*;
import static exception.ExceptionMessage.*;
import static java.awt.Font.BOLD;
import static java.awt.Font.PLAIN;

public class CommentListFrame extends JFrame {
    private JPanel contentPane;
    private JPanel commentPanel;
    private JTextField commentField;
    private CommentRepository commentRepository;
    private MemberRepository memberRepository;
    private List<CommentData> commentsData;
    private Long boardNo;
    private final TokenInfo tokenInfo;
    private final int isAuthenticated;
    private int isAuthorized;

    public CommentListFrame(
            Long boardNo, Category category, TokenInfo tokenInfo, int isAuthenticated, MemberRepository memberRepository
    ) {
        this.boardNo = boardNo;
        this.tokenInfo = tokenInfo;
        this.isAuthenticated = isAuthenticated;
        commentsData = new ArrayList<>();
        this.memberRepository = memberRepository;
        commentRepository = new CommentRepository();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 400);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(79, 168, 202));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());

        JLabel lblNewLabel = new JLabel("댓글 목록");
        lblNewLabel.setFont(new Font(DEFAULT_FONT_NAME, BOLD, 24));
        lblNewLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        lblNewLabel.setForeground(new Color(255, 255, 255));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(lblNewLabel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);

        commentPanel = new JPanel();
        commentPanel.setBackground(new Color(173, 216, 230));
        commentPanel.setLayout(new BoxLayout(commentPanel, BoxLayout.Y_AXIS));
        scrollPane.setViewportView(commentPanel);

        refreshCommentList();

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.SOUTH);
        panel.setLayout(new BorderLayout(0, 0));

        commentField = new JTextField();
        panel.add(commentField, BorderLayout.CENTER);
        commentField.setColumns(10);

        JButton btnAddComment = new JButton("댓글 추가");
        btnAddComment.setForeground(new Color(255, 255, 255));
        btnAddComment.setBorderPainted(false);
        btnAddComment.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_BIG_FONT_SIZE));
        btnAddComment.setBackground(new Color(79, 168, 202));

        btnAddComment.addActionListener(actionEvent -> {
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

            String newContent = commentField.getText();
            if (!newContent.isEmpty()) {
                Comment comment = new Comment(
                        boardNo, tokenInfo.getMemberNo(), newContent, LocalDateTime.now(), LocalDateTime.now()
                );
                commentRepository.create(comment);
                refreshCommentList();
                commentField.setText("");
            }
        });
        panel.add(btnAddComment, BorderLayout.EAST);

        JPanel nav = new JPanel();
        nav.setLayout(null);
        nav.setBackground(new Color(84, 112, 182));
        nav.setBounds(0, 0, 284, 32);
        contentPane.add(nav, BorderLayout.NORTH);

        MemberDto memberDto = memberRepository.findByMemberNo(tokenInfo.getMemberNo());
        JLabel parentName = new JLabel(memberDto.getFullNname() + " 학부모님");
        parentName.setHorizontalAlignment(SwingConstants.CENTER);
        parentName.setForeground(Color.WHITE);
        parentName.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_MEDIUM_FONT_SIZE));
        parentName.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        parentName.setBounds(87, 0, 111, 32);
        nav.add(parentName);

        JButton backButton = new JButton();
        backButton.setBorderPainted(false);
        backButton.setBackground(new Color(84, 112, 182));
        backButton.setBounds(0, 0, 54, 32);
        nav.add(backButton);

        JButton myPageBt = new JButton("");
        myPageBt.setBorderPainted(false);
        myPageBt.setBackground(new Color(84, 112, 182));
        myPageBt.setBounds(230, 0, 54, 32);
        myPageBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MyPageFrame myPageFrame = new MyPageFrame(memberRepository, tokenInfo);
                myPageFrame.setVisible(true);
            }
        });
        nav.add(myPageBt);
    }

    private void refreshCommentList() {
        commentsData.clear();
        List<Comment> comments = commentRepository.findAllByBoardNoAsc(boardNo);
        for (Comment comment : comments) {
            commentsData.add(CommentData.from(comment, memberRepository.fin));
        }
        updateCommentPanel();
    }

    private void updateCommentPanel() {
        commentPanel.removeAll();
        for (CommentData commentData : commentsData) {
            JPanel panel = new JPanel();
            panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panel.setLayout(new GridLayout(0, 1));
            panel.setBackground(new Color(173, 216, 230));

            JLabel lblContent = new JLabel(commentData.getContent());
            lblContent.setFont(new Font(DEFAULT_FONT_NAME, BOLD, DEFAULT_BIG_FONT_SIZE));
            lblContent.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    String[] options = {"수정", "삭제", "취소"};
                    int choice = JOptionPane.showOptionDialog(null, "수정 또는 삭제를 선택하세요.", "댓글 관리",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

                    if (isEditButton(choice)) {
                        String newContent = JOptionPane.showInputDialog("댓글 수정", commentData.getContent());
                        if (newContent != null && !newContent.isEmpty()) {
                            if (isAuthenticated == 0) {
                                GlobalExceptionHandler.throwCheckedException(
                                        new AuthenticationFailedException(AUTHENTICATION_FAILED_EXCEPTION_MESSAGE)
                                );
                                return;
                            }

                            Long commentNo = commentData.getCommentNo();
                            isAuthorized
                                    = AuthenticationFilter.authorizationFilter(commentData.getAuthorNo(), tokenInfo);

                            if (isAuthorized == 0) {
                                GlobalExceptionHandler.throwCheckedException(
                                        new AuthorizationFailedException(AUTHORIZATION_FAILED_EXCEPTION_MESSAGE)
                                );
                                return;
                            }

                            updateComment(Comment.of(commentNo, newContent));
                        }

                        return;
                    }

                    if (isDeleteButton(choice)) {
                        int answer = JOptionPane.showConfirmDialog(
                                null, "댓글을 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION
                        );
                        if (DialogChooser.isYes(answer)) {
                            if (isAuthorized == 0) {
                                GlobalExceptionHandler.throwCheckedException(
                                        new AuthorizationFailedException(AUTHORIZATION_FAILED_EXCEPTION_MESSAGE)
                                );
                                return;
                            }

                            deleteComment(commentData.getCommentNo());
                        }
                    }
                }
            });
            panel.add(lblContent);

            JLabel lblAuthor = new JLabel("작성자: " + commentData.getAuthorName());
            lblAuthor.setFont(new Font(DEFAULT_FONT_NAME, PLAIN, DEFAULT_SMALL_FONT_SIZE));
            panel.add(lblAuthor);

            JLabel lblDate = new JLabel("작성일: " + commentData.getWriteDate());
            lblDate.setFont(new Font(DEFAULT_FONT_NAME, PLAIN, DEFAULT_SMALL_FONT_SIZE));
            panel.add(lblDate);

            commentPanel.add(panel);
        }
        commentPanel.revalidate();
        commentPanel.repaint();
    }

    private boolean isEditButton(int choice) {
        return choice == 0;
    }

    private boolean isDeleteButton(int choice) {
        return choice == 1;
    }

    private void updateComment(Comment comment) {
        commentRepository.updateContent(comment);
        refreshCommentList();
    }

    private void deleteComment(Long commentNo) {
        commentRepository.deleteByCommentNo(commentNo);
        refreshCommentList();
    }
}
