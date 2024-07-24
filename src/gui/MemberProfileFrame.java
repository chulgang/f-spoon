package gui;

import image.dbconnection.ImageRepository;
import member.MemberDto;
import member.MemberRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static config.GUI.*;
import static java.awt.Font.PLAIN;

public class MemberProfileFrame extends JFrame {
    private JPanel contentPane;

    public MemberProfileFrame(Long myNo, Long memberNo, MemberRepository memberRepository) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT);
        setLocation(DEFAULT_FRAME_POINT_X, DEFAULT_FRAME_POINT_Y);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setBackground(new Color(79, 168, 202));
        contentPane.setLayout(new GridBagLayout());

        MemberDto memberDto = memberRepository.findByMemberNo(memberNo);
        memberDto.setFullNname(memberDto.getFullNname());
        memberDto.setEmail(memberDto.getEmail());
        memberDto.setRegDate(memberDto.getRegDate());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblProfileImage = new JLabel();
        ImageIcon profileImage = new ImageRepository().findImageFileByMemnberNo(memberNo);
        lblProfileImage.setIcon(profileImage);
        lblProfileImage.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        contentPane.add(lblProfileImage, gbc);

        JLabel lblName = new JLabel(memberDto.getFullNname() + "님의 프로필");
        lblName.setFont(new Font(DEFAULT_FONT_NAME, PLAIN, DEFAULT_BIG_FONT_SIZE));
        lblName.setForeground(new Color(255, 255, 255));
        lblName.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        contentPane.add(lblName, gbc);

        JLabel lblEmail = new JLabel("이메일: " + memberDto.getEmail());
        lblEmail.setFont(new Font(DEFAULT_FONT_NAME, PLAIN, DEFAULT_BIG_FONT_SIZE));
        lblEmail.setForeground(new Color(255, 255, 255));
        lblEmail.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 2;
        contentPane.add(lblEmail, gbc);

        JLabel lblJoinDate = new JLabel("가입일: " + memberDto.getRegDate());
        lblJoinDate.setFont(new Font(DEFAULT_FONT_NAME, PLAIN, DEFAULT_BIG_FONT_SIZE));
        lblJoinDate.setForeground(new Color(255, 255, 255));
        lblJoinDate.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 3;
        contentPane.add(lblJoinDate, gbc);

        JButton btnMessage = new JButton("메시지 보내기");
        btnMessage.setFont(new Font(DEFAULT_FONT_NAME, PLAIN, DEFAULT_BIG_FONT_SIZE));
        btnMessage.setBorderPainted(false);
        btnMessage.setBackground(new Color(79, 168, 202));
        btnMessage.setForeground(new Color(255, 255, 255));
        btnMessage.addActionListener(actionEvent -> {
            MessageFrame messageFrame = new MessageFrame(myNo, memberNo);
            messageFrame.setVisible(true);
        });
        gbc.gridy = 4;
        contentPane.add(btnMessage, gbc);
    }
}