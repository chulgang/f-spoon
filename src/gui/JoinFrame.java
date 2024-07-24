package gui;
import config.DbConnectionThreadLocal;
import member.MemberRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

import static config.GUI.*;
import static member.Role.ROLE_USER;

public class JoinFrame extends JFrame{
    private JTextField textField;
    private JPasswordField textField_1;
    //private JTextField textField_1;
    private JTextField textField_2;
    private JTextField textField_3;
    private JTextField textField_4;
    private final String roleName = ROLE_USER.toString();
    private JPasswordField passwordField;
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    JoinFrame window = new JoinFrame();
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
    public JoinFrame() {
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

        JPanel panel_1 = new JPanel();
        panel_1.setBackground(new Color(79, 168, 202));
        panel_1.setBounds(12, 79, 360, 61);
        getContentPane().add(panel_1);
        panel_1.setLayout(null);
        JLabel lblNewLabel_1 = new JLabel("이메일");
        lblNewLabel_1.setForeground(Color.WHITE);
        lblNewLabel_1.setFont(new Font(DEFAULT_FONT_NAME, Font.PLAIN, DEFAULT_SMALL_FONT_SIZE));
        lblNewLabel_1.setBounds(12, 10, 72, 25);
        panel_1.add(lblNewLabel_1);
        textField = new JTextField();
        textField.setBounds(12, 33, 336, 21);
        panel_1.add(textField);
        textField.setColumns(10);
        JPanel panel_2 = new JPanel();
        panel_2.setBackground(new Color(79, 168, 202));
        panel_2.setBounds(12, 150, 360, 61);
        getContentPane().add(panel_2);
        panel_2.setLayout(null);
        JLabel lblNewLabel_2 = new JLabel("비밀번호");
        lblNewLabel_2.setForeground(Color.WHITE);
        lblNewLabel_2.setFont(new Font(DEFAULT_FONT_NAME, Font.PLAIN, DEFAULT_SMALL_FONT_SIZE));
        lblNewLabel_2.setBounds(12, 10, 69, 15);
        panel_2.add(lblNewLabel_2);
        textField_1 = new JPasswordField();
        textField_1.setBounds(12, 30, 336, 21);
        panel_2.add(textField_1);
        textField_1.setColumns(10);



        JPanel panel_3 = new JPanel();
        panel_3.setBackground(new Color(79, 168, 202));
        panel_3.setBounds(12, 281, 360, 61);
        getContentPane().add(panel_3);
        panel_3.setLayout(null);
        JLabel lblNewLabel_3 = new JLabel("성명");
        lblNewLabel_3.setForeground(Color.WHITE);
        lblNewLabel_3.setFont(new Font(DEFAULT_FONT_NAME, Font.PLAIN, DEFAULT_SMALL_FONT_SIZE));
        lblNewLabel_3.setBounds(12, 10, 93, 15);
        panel_3.add(lblNewLabel_3);
        textField_2 = new JTextField();
        textField_2.setBounds(12, 30, 336, 21);
        // panel_2.add(textField_2);
        textField_1.setColumns(10);
        panel_3.add(textField_2);
        //textField_2 = new JPasswordField();
        //textField_2.setBounds(12, 30, 336, 21);
        //textField_2.setColumns(10);
        JPanel panel_4 = new JPanel();
        panel_4.setBackground(new Color(79, 168, 202));
        panel_4.setBounds(12, 352, 360, 67);
        getContentPane().add(panel_4);
        panel_4.setLayout(null);
        JLabel lblNewLabel_4 = new JLabel("연락처");
        lblNewLabel_4.setForeground(Color.WHITE);
        lblNewLabel_4.setFont(new Font(DEFAULT_FONT_NAME, Font.PLAIN, DEFAULT_SMALL_FONT_SIZE));
        lblNewLabel_4.setBounds(12, 10, 106, 15);
        panel_4.add(lblNewLabel_4);
        textField_3 = new JTextField();
        textField_3.setBounds(12, 36, 336, 21);
        panel_4.add(textField_3);
        textField_3.setColumns(10);
        JPanel panel_5 = new JPanel();
        panel_5.setBackground(new Color(79, 168, 202));
        panel_5.setBounds(12, 500, 360, 55);
        getContentPane().add(panel_5);
        panel_5.setLayout(null);
        JLabel lblNewLabel_5 = new JLabel("성별");
        lblNewLabel_5.setForeground(Color.WHITE);
        lblNewLabel_5.setFont(new Font(DEFAULT_FONT_NAME, Font.PLAIN, DEFAULT_SMALL_FONT_SIZE));
        lblNewLabel_5.setBounds(12, 5, 57, 15);
        panel_5.add(lblNewLabel_5);
        ButtonGroup group = new ButtonGroup();
        JRadioButton jradiobutton = new JRadioButton("남");
        jradiobutton.setForeground(Color.WHITE);
        jradiobutton.setFont(new Font(DEFAULT_FONT_NAME, Font.PLAIN, DEFAULT_SMALL_FONT_SIZE));
        jradiobutton.setBackground(new Color(79, 168, 202));
        jradiobutton.setBounds(12, 26, 70, 23);
        panel_5.add(jradiobutton);
        JRadioButton jradiobutton_1 = new JRadioButton("여");
        jradiobutton_1.setForeground(Color.WHITE);
        jradiobutton_1.setFont(new Font(DEFAULT_FONT_NAME, Font.PLAIN, DEFAULT_SMALL_FONT_SIZE));
        jradiobutton_1.setBackground(new Color(79, 168, 202));
        jradiobutton_1.setBounds(306, 26, 46, 23);
        panel_5.add(jradiobutton_1);
        group.add(jradiobutton); //하나만 선택 가능하게 그룹으로 묵어준다
        group.add(jradiobutton_1);//하나만 선택 가능하게 그룹으로 묵어준다
        JPanel panel_6 = new JPanel();
        panel_6.setBackground(new Color(79, 168, 202));
        panel_6.setBounds(12, 554, 360, 47);
        getContentPane().add(panel_6);
        panel_6.setLayout(null);
        JButton btnNewButton = new JButton("회원가입");
        btnNewButton.setForeground(Color.WHITE);
        btnNewButton.setFont(new Font(DEFAULT_FONT_NAME, Font.PLAIN, DEFAULT_SMALL_FONT_SIZE));
        btnNewButton.setBackground(new Color(79, 168, 202));
        btnNewButton.setBounds(12, 10, 336, 37);
        panel_6.add(btnNewButton);
        JPanel panel_7 = new JPanel();
        panel_7.setBackground(new Color(79, 168, 202));
        panel_7.setBounds(12, 429, 360, 61);
        getContentPane().add(panel_7);
        panel_7.setLayout(null);
        JLabel lblNewLabel_6 = new JLabel("생년월일");
        lblNewLabel_6.setForeground(Color.WHITE);
        lblNewLabel_6.setFont(new Font(DEFAULT_FONT_NAME, Font.PLAIN, DEFAULT_SMALL_FONT_SIZE));
        lblNewLabel_6.setBounds(12, 10, 57, 15);
        panel_7.add(lblNewLabel_6);
        textField_4 = new JTextField();
        textField_4.setBounds(12, 30, 336, 21);
        panel_7.add(textField_4);
        textField_4.setColumns(10);

        JLabel joinIcon = new JLabel("\r\n");
        joinIcon.setIcon(new ImageIcon("resources/joinLogo.png"));
        joinIcon.setHorizontalAlignment(SwingConstants.CENTER);
        joinIcon.setForeground(Color.WHITE);
        joinIcon.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, 25));
        joinIcon.setBounds(61, 20, 248, 67);
        getContentPane().add(joinIcon);

        passwordField = new JPasswordField();
        passwordField.setBounds(23, 250, 336, 21);
        getContentPane().add(passwordField);

        JLabel lblNewLabel = new JLabel("비밀번호 확인");

        lblNewLabel.setBounds(22, 214, 248, 36);
        getContentPane().add(lblNewLabel);
        lblNewLabel.setForeground(Color.WHITE);
        lblNewLabel.setFont(new Font(DEFAULT_FONT_NAME,Font.PLAIN, DEFAULT_SMALL_FONT_SIZE));

        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                DbConnectionThreadLocal.initialize();
                MemberRepository memberRepository = new MemberRepository();
                if (source == btnNewButton) {
                    String searchText = textField.getText().trim();
                    String pwdConfirm = passwordField.getText().trim();
                    String searchText_1 = textField_1.getText().trim();
                    String searchText_2 = textField_2.getText().trim();
                    String searchText_3 = textField_3.getText().trim();
                    String searchText_4 = textField_4.getText().trim();
                    boolean check = jradiobutton.isSelected();
                    boolean check_1 = jradiobutton_1.isSelected();
                    LocalDateTime regDate = LocalDateTime.now();
                    String strRegDate = String.valueOf(regDate);
                    int gen = 0;
                    if (check) {
                        System.out.println("남자");
                        gen = 0;
                    } else if (check_1) {
                        System.out.println("여자");
                        gen = 1;
                    }
                    boolean returnVal = memberRepository.join(searchText, searchText_1, pwdConfirm, searchText_2, searchText_3, searchText_4, roleName, gen);
                    if(returnVal) {
                        LoginFrame loginFrame = new LoginFrame();
                        loginFrame.setVisible(true);
                        dispose();
                    }



                    System.out.println("returnVal: " +returnVal);
                }
            }
        });
    }
}
