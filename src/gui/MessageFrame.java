package gui;

import config.DbConnectionThreadLocal;
import member.MemberDto;
import member.MemberRepository;
import message.dbconnection.MessageRepository;
import message.dto.MessageInfo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static config.GUI.*;

public class MessageFrame extends JFrame {
    private JPanel contentPane;
    private JTextPane messageArea;
    private JTextField inputField;
    private JButton sendButton;
    private MessageRepository messageRepository = new MessageRepository();
    private long senderId;
    private long receiverId;
    private JPanel panel;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    DbConnectionThreadLocal.initialize();

                    MessageFrame frame = new MessageFrame(1, 2);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MessageFrame(long senderId, long receiverId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(DEFAULT_FRAME_WIDTH, 600);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(79, 168, 202));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("1:1 메시지");
        lblNewLabel.setForeground(new Color(255, 255, 255));
        lblNewLabel.setBounds(5, 55, 374, 19);
        lblNewLabel.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, 18));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(lblNewLabel);

        messageArea = new JTextPane();
        messageArea.setBackground(new Color(173, 216, 230));
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setBounds(5, 87, 374, 440);
        contentPane.add(scrollPane);

        panel = new JPanel();
        panel.setBounds(5, 527, 374, 29);
        contentPane.add(panel);
        panel.setLayout(new BorderLayout(0, 0));

        inputField = new JTextField();
        inputField.setBackground(new Color(255, 255, 255));
        panel.add(inputField, BorderLayout.CENTER);
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        sendButton = new JButton("전송");
        sendButton.setForeground(new Color(255, 255, 255));
        sendButton.setBorderPainted(false);
        sendButton.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_BIG_FONT_SIZE));
        sendButton.setBackground(new Color(79, 168, 202));
        panel.add(sendButton, BorderLayout.EAST);
        JPanel nav = new JPanel();
        nav.setLayout(null);
        nav.setBackground(new Color(84, 112, 182));
        nav.setBounds(0, 0, 384, 40);
        
        //학부모 이름 연결해야함.
        MemberDto memberDto = new MemberRepository().findByMemberNo(senderId);
        JLabel parentName = new JLabel(memberDto.getFullNname() + " 학부모님");
        parentName.setHorizontalAlignment(SwingConstants.CENTER);
        parentName.setForeground(Color.WHITE);
        parentName.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_MEDIUM_FONT_SIZE));
        parentName.setBorder(BorderFactory.createEmptyBorder(10 , 0, 10, 0));
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
        nav.add(myPageBt);
        contentPane.add(nav);

        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        loadMessages();
    }

    private void sendMessage() {
        String content = inputField.getText();
        if (!content.trim().isEmpty()) {
            boolean success = messageRepository.sendMessage(senderId, receiverId, content);
            if (success) {
                inputField.setText("");
                loadMessages();
            } else {
                JOptionPane.showMessageDialog(this, "메시지 전송에 실패했습니다.");
            }
        }
    }

    private void loadMessages() {
        List<MessageInfo> messages = messageRepository.getMessages(receiverId, senderId);
        messageArea.setText("");
        StyledDocument doc = messageArea.getStyledDocument();
        Style defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        Style myStyle = doc.addStyle("MyStyle", defaultStyle);
        StyleConstants.setForeground(myStyle, Color.BLUE);

        for (MessageInfo message : messages) {
            try {
                if (message.getSenderId() == senderId) {
                    doc.insertString(doc.getLength(), message.getTimestamp().toString().substring(0, 16) + " - " + message.getSenderName() + " : " + message.getContent() + "\n", myStyle);
                } else {
                    doc.insertString(doc.getLength(), message.getTimestamp().toString().substring(0, 16) + " - " + message.getSenderName() + " : " + message.getContent() + "\n", defaultStyle);
                }
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }
}