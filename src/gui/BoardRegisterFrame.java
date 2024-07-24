package gui;

import board.dbconnection.BoardRepository;
import board.dbconnection.CategoryRepository;
import board.model.Board;
import board.model.Category;
import member.TokenInfo;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

import static config.GUI.*;
import static java.awt.Font.BOLD;

public class BoardRegisterFrame extends JFrame {
    private JPanel contentPane;
    private JTextField titleField;
    private JTextArea contentArea;
    private JComboBox<String> comboBox;
    private BoardListFrame boardListFrame;
    private BoardRepository boardRepository;
    private CategoryRepository categoryRepository;
    private final TokenInfo tokenInfo;

    public BoardRegisterFrame(
            BoardListFrame boardListFrame, BoardRepository boardRepository,
            CategoryRepository categoryRepository, TokenInfo tokenInfo, Category category
    ) {
        this.boardListFrame = boardListFrame;
        this.boardRepository = boardRepository;
        this.categoryRepository = categoryRepository;
        this.tokenInfo = tokenInfo;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT);
        setLocation(DEFAULT_FRAME_POINT_X, DEFAULT_FRAME_POINT_Y);


        contentPane = new JPanel();
        contentPane.setBackground(new Color(79, 168, 202));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel titleLabel = new JLabel("게시판 생성");
        titleLabel.setBounds(91, 46, 200, 23);
        titleLabel.setFont(new Font(DEFAULT_FONT_NAME, BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(255, 255, 255));
        contentPane.add(titleLabel);

        List<String> categoryNames = categoryRepository.findAllNames();
        comboBox = new JComboBox<>(categoryNames.toArray(new String[0]));
        comboBox.setBounds(31, 89, 317, 23);
        contentPane.add(comboBox);
        comboBox.setSelectedIndex(category.getCategoryNo() - 1);

        JLabel lblTitle = new JLabel("제목:");
        lblTitle.setBounds(31, 119, 240, 23);
        lblTitle.setFont(new Font(DEFAULT_FONT_NAME, Font.PLAIN, DEFAULT_BIG_FONT_SIZE));
        lblTitle.setForeground(new Color(255, 255, 255));
        contentPane.add(lblTitle);

        titleField = new JTextField();
        titleField.setBounds(31, 149, 317, 23);
        contentPane.add(titleField);
        titleField.setColumns(10);

        JLabel lblContent = new JLabel("내용:");
        lblContent.setBounds(31, 179, 240, 23);
        lblContent.setFont(new Font(DEFAULT_FONT_NAME, Font.PLAIN, DEFAULT_BIG_FONT_SIZE));
        lblContent.setForeground(new Color(255, 255, 255));
        contentPane.add(lblContent);

        contentArea = new JTextArea();
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setBorder(new LineBorder(Color.BLACK));

        JScrollPane contentScrollPane = new JScrollPane(contentArea);
        contentScrollPane.setBounds(31, 209, 317, 297);
        contentScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentPane.add(contentScrollPane);

        JButton btnSave = new JButton("저장");
        btnSave.setBounds(122, 531, 65, 23);
        btnSave.setForeground(new Color(255, 255, 255));
        btnSave.setBorderPainted(false);
        btnSave.setFont(new Font(DEFAULT_FONT_NAME, BOLD, DEFAULT_BIG_FONT_SIZE));
        btnSave.setBackground(new Color(79, 168, 202));
        btnSave.addActionListener(actionEvent -> saveBoard());
        contentPane.add(btnSave);

        JButton btnCancel = new JButton("취소");
        btnCancel.setBounds(197, 531, 65, 23);
        btnCancel.setForeground(new Color(255, 255, 255));
        btnCancel.setBorderPainted(false);
        btnCancel.setFont(new Font(DEFAULT_FONT_NAME, BOLD, DEFAULT_BIG_FONT_SIZE));
        btnCancel.setBackground(new Color(79, 168, 202));
        btnCancel.addActionListener(actionEvent -> dispose());
        contentPane.add(btnCancel);
    }

    private void saveBoard() {
        String selectedCategoryName = (String) comboBox.getSelectedItem();
        String title = titleField.getText();
        String content = contentArea.getText();

        int categoryNo = categoryRepository.findCategoryNoByName(selectedCategoryName);

        boardRepository.create(Board.of(categoryNo, tokenInfo.getMemberNo(), title, content));

        boardListFrame.refreshBoardList();
        dispose();
    }
}
