package gui;

import board.dbconnection.DislikeRepository;
import board.dbconnection.LikeRepository;
import image.ImageManager;
import image.dbconnection.ImageRepository;
import member.MemberDto;
import member.MemberRepository;
import member.TokenInfo;
import util.DialogChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static config.GUI.*;

public class MyPageFrame extends JFrame {
    private JTextField passwordField;
    private JTextField phoneField;
    private final MemberRepository memberRepository;
    private final TokenInfo tokenInfo;
    private boolean isImageExists;
    private final Long memberNo;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MyPageFrame window = new MyPageFrame(new MemberRepository(), new TokenInfo());
                    window.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MyPageFrame(MemberRepository memberRepository, TokenInfo tokenInfo) {
        getContentPane().setBackground(new Color(79, 168, 202));
        this.memberRepository = memberRepository;
        this.tokenInfo = tokenInfo;
        this.memberNo = tokenInfo.getMemberNo();
        initialize();
    }

    private void initialize() {
        MemberDto memberDto = memberRepository.findByMemberNo(memberNo);

        setTitle("마이 페이지");
        setBackground(new Color(255, 255, 255));
        setResizable(false);

        setSize(DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT);
        setLocation(DEFAULT_FRAME_POINT_X, DEFAULT_FRAME_POINT_Y);
        getContentPane().setLayout(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(79, 168, 202));
        mainPanel.setLayout(null);
        mainPanel.setBounds(0, 0, 384, 601);
        getContentPane().add(mainPanel);

        JPanel panel_4 = new JPanel();
        panel_4.setBackground(new Color(79, 168, 202));
        panel_4.setBounds(12, 32, 360, 35);
        mainPanel.add(panel_4);
        panel_4.setLayout(null);

        JLabel Title = new JLabel("학부모 회원정보");
        Title.setForeground(new Color(255, 255, 255));
        Title.setBackground(new Color(79, 168, 202));
        Title.setBounds(0, 0, 368, 35);
        Title.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, 19));
        Title.setHorizontalAlignment(SwingConstants.CENTER);
        panel_4.add(Title);

        JPanel panel_1 = new JPanel();
        panel_1.setLayout(null);
        panel_1.setBounds(12, 77, 360, 500);
        mainPanel.add(panel_1);

        JPanel panel_6 = new JPanel();
        panel_6.setLayout(null);
        panel_6.setBackground(new Color(79, 168, 202));
        panel_6.setBounds(0, 0, 368, 500);
        panel_1.add(panel_6);

        JLabel lblFullName = new JLabel("이름");
        lblFullName.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_MEDIUM_FONT_SIZE));
        lblFullName.setForeground(new Color(255, 255, 255));
        lblFullName.setBounds(12, 43, 57, 15);
        panel_6.add(lblFullName);

        JLabel lblNewLabel_1 = new JLabel(memberDto.getFullNname());
        lblNewLabel_1.setFont(new Font("Dialog", Font.PLAIN, 17));
        lblNewLabel_1.setForeground(new Color(255, 255, 255));
        lblNewLabel_1.setBounds(100, 43, 106, 15);
        panel_6.add(lblNewLabel_1);

        JLabel lblGender = new JLabel("성별");
        lblGender.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_MEDIUM_FONT_SIZE));
        lblGender.setForeground(new Color(255, 255, 255));
        lblGender.setBounds(12, 68, 57, 15);
        panel_6.add(lblGender);

        JLabel lblNewLabel_2 = new JLabel("남");
        lblNewLabel_2.setFont(new Font("Dialog", Font.PLAIN, 17));
        lblNewLabel_2.setForeground(new Color(255, 255, 255));
        lblNewLabel_2.setBounds(100, 68, 57, 15);
        panel_6.add(lblNewLabel_2);

        JLabel lblEmail = new JLabel("이메일");
        lblEmail.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_MEDIUM_FONT_SIZE));
        lblEmail.setForeground(new Color(255, 255, 255));
        lblEmail.setBounds(12, 93, 57, 15);
        panel_6.add(lblEmail);

        JLabel lblNewLabel_3 = new JLabel(memberDto.getEmail());
        lblNewLabel_3.setFont(new Font("Dialog", Font.PLAIN, 17));
        lblNewLabel_3.setForeground(new Color(255, 255, 255));
        lblNewLabel_3.setBounds(100, 93, 100, 15);
        panel_6.add(lblNewLabel_3);

        JLabel lblPassword = new JLabel("비밀번호");
        lblPassword.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_MEDIUM_FONT_SIZE));
        lblPassword.setForeground(new Color(255, 255, 255));
        lblPassword.setBounds(12, 122, 76, 15);
        panel_6.add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 120, 180, 21);
        panel_6.add(passwordField);
        passwordField.setColumns(10);

        JButton modificationButton_1 = new JButton("수정");
        modificationButton_1.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_BIG_FONT_SIZE));
        modificationButton_1.setForeground(new Color(255, 255, 255));
        modificationButton_1.setBounds(290, 118, 63, 23);
        modificationButton_1.setBackground(new Color(79, 168, 202));
        panel_6.add(modificationButton_1);

        modificationButton_1.addActionListener(actionEvent -> {
            memberRepository.updatePwd(memberNo, passwordField.getText());
            JOptionPane.showMessageDialog(this, "비밀번호가 수정되었습니다.", "수정 완료", JOptionPane.INFORMATION_MESSAGE);
        });

        JLabel lblPhoneNumber = new JLabel("전화번호");
        lblPhoneNumber.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_MEDIUM_FONT_SIZE));
        lblPhoneNumber.setForeground(new Color(255, 255, 255));
        lblPhoneNumber.setBounds(12, 154, 76, 15);
        panel_6.add(lblPhoneNumber);

        phoneField = new JTextField(memberDto.getPhoneNumber());
        phoneField.setColumns(10);
        phoneField.setBounds(100, 152, 180, 21);
        panel_6.add(phoneField);

        JButton modificationButton_2 = new JButton("수정");
        modificationButton_2.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_BIG_FONT_SIZE));
        modificationButton_2.setBackground(new Color(79, 168, 202));
        modificationButton_2.setForeground(new Color(255, 255, 255));
        modificationButton_2.setBounds(290, 150, 63, 23);
        panel_6.add(modificationButton_2);

        modificationButton_2.addActionListener(actionEvent -> {
            memberRepository.updatePhoneNumber(memberNo, phoneField.getText());
            JOptionPane.showMessageDialog(this, "전화번호가 수정되었습니다.", "수정 완료", JOptionPane.INFORMATION_MESSAGE);
        });

        JLabel lblBirthDate = new JLabel("생일");
        lblBirthDate.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_MEDIUM_FONT_SIZE));
        lblBirthDate.setForeground(new Color(255, 255, 255));
        lblBirthDate.setBounds(12, 185, 57, 15);
        panel_6.add(lblBirthDate);

        JLabel lblNewLabel_4 = new JLabel(memberDto.getBirthNate());
        lblNewLabel_4.setFont(new Font("Dialog", Font.PLAIN, 17));
        lblNewLabel_4.setForeground(new Color(255, 255, 255));
        lblNewLabel_4.setBounds(100, 185, 180, 15);
        panel_6.add(lblNewLabel_4);

        JLabel lblSignedUpdate = new JLabel("가입일");
        lblSignedUpdate.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_MEDIUM_FONT_SIZE));
        lblSignedUpdate.setForeground(new Color(255, 255, 255));
        lblSignedUpdate.setBounds(12, 210, 57, 15);
        panel_6.add(lblSignedUpdate);

        JLabel lblNewLabel_5 = new JLabel(memberDto.getRegDate());
        lblNewLabel_5.setFont(new Font("Dialog", Font.PLAIN, 17));
        lblNewLabel_5.setForeground(new Color(255, 255, 255));
        lblNewLabel_5.setBounds(100, 210, 180, 15);
        panel_6.add(lblNewLabel_5);

        JLabel lblProfileImageMenu = new JLabel("프로필 이미지");
        lblProfileImageMenu.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_MEDIUM_FONT_SIZE));
        lblProfileImageMenu.setForeground(new Color(255, 255, 255));
        lblProfileImageMenu.setBounds(12, 279, 93, 37);
        panel_6.add(lblProfileImageMenu);

        JLabel lblProfileImage = new JLabel("이미지 없음");
        lblProfileImage.setBackground(new Color(255, 255, 255));
        lblProfileImage.setForeground(new Color(255, 255, 255));
        lblProfileImage.setHorizontalAlignment(SwingConstants.CENTER);
        lblProfileImage.setBounds(100, 245, 106, 106);
        lblProfileImage.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel_6.add(lblProfileImage);

        ImageRepository imageRepository = new ImageRepository();
        ImageIcon profileImage = imageRepository.findImageFileByMemnberNo(memberNo);
        if (profileImage != null) {
            lblProfileImage.setIcon(profileImage);
            isImageExists = true;
        }

        lblProfileImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ImageManager imageManager = new ImageManager();
                byte[] uploadedImage = imageManager.uploadImage();
                ImageIcon profileImage = new ImageIcon(uploadedImage);
                lblProfileImage.setIcon(profileImage);
                lblProfileImage.setBounds(100, 245, 106, 106);

                if (isImageExists) {
                    imageRepository.update(memberNo, uploadedImage);
                    return;
                }

                imageRepository.create(memberNo, uploadedImage);
            }
        });

        JLabel lblLike = new JLabel("나의 게시글 추천 수");
        lblLike.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_MEDIUM_FONT_SIZE));
        lblLike.setForeground(new Color(255, 255, 255));
        lblLike.setBounds(12, 365, 200, 30);
        mainPanel.add(lblLike);
        panel_6.add(lblLike);

        JLabel lblNewLabel_6 = new JLabel(String.valueOf(new LikeRepository().findCountByMemberNo(memberNo)));
        lblNewLabel_6.setFont(new Font(DEFAULT_FONT_NAME, Font.PLAIN, DEFAULT_MEDIUM_FONT_SIZE));
        lblNewLabel_6.setForeground(new Color(255, 255, 255));
        lblNewLabel_6.setBounds(150, 365, 200, 30);
        mainPanel.add(lblNewLabel_6);
        panel_6.add(lblNewLabel_6);

        JLabel lblDislike = new JLabel("나의 게시글 비추천 수");
        lblDislike.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_MEDIUM_FONT_SIZE));
        lblDislike.setForeground(new Color(255, 255, 255));
        lblDislike.setBounds(12, 390, 200, 30);
        mainPanel.add(lblDislike);
        panel_6.add(lblDislike);

        JLabel lblNewLabel_7 = new JLabel(String.valueOf(new DislikeRepository().findCountByMemberNo(memberNo)));
        lblNewLabel_7.setFont(new Font(DEFAULT_FONT_NAME, Font.PLAIN, DEFAULT_MEDIUM_FONT_SIZE));
        lblNewLabel_7.setForeground(new Color(255, 255, 255));
        lblNewLabel_7.setBounds(150, 390, 200, 30);
        mainPanel.add(lblNewLabel_7);
        panel_6.add(lblNewLabel_7);

        if (memberDto.isRoleAdmin()) {
            JButton adminButton = new JButton("관리자 페이지");
            adminButton.setBounds(12, 470, 120, 23);
            panel_6.add(adminButton);

            adminButton.addActionListener(actionEvent -> {
                AdminPageFrame adminPageFrame = new AdminPageFrame();
                adminPageFrame.setVisible(true);
            });
        }

        JButton btnWithdrawal = new JButton("회원탈퇴");
        btnWithdrawal.setFont(new Font("굴림", Font.BOLD, 16));
        btnWithdrawal.setForeground(new Color(255, 255, 255));
        btnWithdrawal.setBackground(new Color(79, 168, 202));
        btnWithdrawal.setBounds(250, 470, 100, 23);
        panel_6.add(btnWithdrawal);
        btnWithdrawal.addActionListener(actionEvent -> {
            boolean isConfirmed = DialogChooser.isYes(
                    JOptionPane.showConfirmDialog(this, "정말 탈퇴하시겠습니까?", "탈퇴 확인", JOptionPane.YES_NO_OPTION)
            );
            if (isConfirmed) {
                memberRepository.deleteByMemberNo(memberNo);
            }
        });
    }
}
