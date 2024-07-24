package gui;
import javax.swing.*;

import config.DbConnectionThreadLocal;
import member.MemberRepository;
import member.TokenInfo;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

import static config.GUI.*;

public class LoginFrame extends JFrame{
	JoinFrame joinFrame = new JoinFrame();
    //private JFrame frame;
    private JTextField textField_2;
    private JTextField textField_3;
    private JTextField textField_1;
    private JTextField textField;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LoginFrame window = new LoginFrame();
                    window.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public LoginFrame() {
    	joinFrame.setVisible(false);
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
      
        getContentPane().setBackground(new Color(79, 168, 202));
        setResizable(false);
        //frame.setLocationRelativeTo(null);
        setSize(DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT);
        setLocation(DEFAULT_FRAME_POINT_X, DEFAULT_FRAME_POINT_Y);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
        
        JPanel panel_4 = new JPanel();
        panel_4.setBackground(new Color(79, 168, 202));
        panel_4.setBounds(12, 35, 410, 516);
        getContentPane().add(panel_4);
        panel_4.setLayout(null);
        
        JLabel loginIcon = new JLabel("\r\n");
        loginIcon.setIcon(new ImageIcon("resources/loginLogo.png"));
        loginIcon.setForeground(new Color(255, 255, 255));
        loginIcon.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, 25));
        loginIcon.setHorizontalAlignment(SwingConstants.CENTER);
        loginIcon.setBounds(50, 114, 267, 96);
        panel_4.add(loginIcon);
        
        textField_1 = new JTextField();
        textField_1.setBounds(48, 220, 277, 29);
        panel_4.add(textField_1);
        textField_1.setColumns(10);
        
        JButton btnNewButton = new JButton("회원가입");
        
        
        btnNewButton.setFont(new Font(DEFAULT_FONT_NAME, Font.PLAIN, DEFAULT_MEDIUM_FONT_SIZE));
        btnNewButton.setForeground(new Color(255, 255, 255));
        btnNewButton.setBorderPainted(false);
        btnNewButton.setBounds(213, 301, 97, 23);
        btnNewButton.setOpaque(false);
        btnNewButton.setBackground(Color.white);
        panel_4.add(btnNewButton);
        
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JoinFrame joinFrame = new JoinFrame();
                joinFrame.setVisible(true);
                setVisible(false);
            }
        });
        
        JButton btnNewButton_2 = new JButton("로그인");
        btnNewButton_2.setFont(new Font(DEFAULT_FONT_NAME, Font.PLAIN, DEFAULT_MEDIUM_FONT_SIZE));
        btnNewButton_2.setForeground(new Color(255, 255, 255));
        btnNewButton_2.setBorderPainted(false);
        btnNewButton_2.setBounds(48, 301, 97, 23);
        btnNewButton_2.setOpaque(false);
        btnNewButton_2.setBackground(Color.white);
        panel_4.add(btnNewButton_2);
        
        btnNewButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                DbConnectionThreadLocal.initialize();
                MemberRepository memberRepository = new MemberRepository();
                String searchText = textField.getText().trim();
                String searchText_1 = textField_1.getText().trim();
                TokenInfo tokenInfo = null;
                if(source == btnNewButton_2) {
                    try {
                        tokenInfo = memberRepository.login(searchText_1, searchText);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                        DbConnectionThreadLocal.setSqlError(true);
                    } finally {
                        DbConnectionThreadLocal.reset();
                    }

                    MainFrameScroll mainFrameScroll = new MainFrameScroll(tokenInfo);
                    mainFrameScroll.setVisible(true);
                    dispose();
                }
            }
        });
        
        textField = new JPasswordField();
        textField.setColumns(10);
        textField.setBounds(48, 262, 277, 29);
        panel_4.add(textField);
        
        JLabel user = new JLabel("");
        user.setIcon(new ImageIcon("resources/login.png"));
        user.setBounds(20, 228, 16, 15);
        panel_4.add(user);
        
        JLabel pass = new JLabel("");
        pass.setIcon(new ImageIcon("resources/password.png"));
        pass.setBounds(20, 270, 16, 15);
        panel_4.add(pass);


    }
}