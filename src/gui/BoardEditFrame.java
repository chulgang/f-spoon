package gui;

import board.dbconnection.BoardRepository;
import board.dbconnection.CategoryRepository;
import board.model.Board;
import board.model.Category;
import member.TokenInfo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

import static config.GUI.*;

public class BoardEditFrame extends JFrame {
    private JPanel contentPane;
    private JTextField titleField;
    private JTextArea contentArea;
    private JComboBox<String> comboBox;
    private Long boardNo;
    private gui.BoardListFrame boardListFrame;
    private BoardRepository boardRepository;
    private CategoryRepository categoryRepository;
    private TokenInfo tokenInfo;

    public BoardEditFrame(
            Long boardNo, Category category, String title, String content, BoardListFrame boardListFrame,
            BoardRepository boardRepository, CategoryRepository categoryRepository, TokenInfo tokenInfo
    ) {
        this.boardRepository = boardRepository;
        this.categoryRepository = categoryRepository;
        this.boardListFrame = boardListFrame;
        this.boardNo = boardNo;
        this.tokenInfo = tokenInfo;

        setSize(DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT);
        setLocation(DEFAULT_FRAME_POINT_X, DEFAULT_FRAME_POINT_Y);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(79, 168, 202));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        List<String> categoryNames = categoryRepository.findAllNames();
        contentPane.setLayout(null);
        comboBox = new JComboBox<>(categoryNames.toArray(new String[0]));
        comboBox.setBounds(27, 81, 318, 23);
        comboBox.setSelectedIndex(category.getCategoryNo() - 1);
        contentPane.add(comboBox);

        JLabel lblTitle = new JLabel("제목:");
        lblTitle.setBounds(27, 108, 240, 23);
        lblTitle.setForeground(new Color(255, 255, 255));
        // lblTitle.setBorderPainted(false);
        lblTitle.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_BIG_FONT_SIZE));
        lblTitle.setBackground(new Color(79, 168, 202));
        contentPane.add(lblTitle);

        titleField = new JTextField(title);
        titleField.setBounds(27, 141, 318, 23);
        contentPane.add(titleField);
        titleField.setColumns(10);

        JLabel lblContent = new JLabel("내용:");
        lblContent.setBounds(27, 174, 240, 23);
        lblContent.setForeground(new Color(255, 255, 255));
        // lblContent.setBorderPainted(false);
        lblContent.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_BIG_FONT_SIZE));
        lblContent.setBackground(new Color(79, 168, 202));
        contentPane.add(lblContent);

        contentArea = new JTextArea(content);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setBorder(new LineBorder(Color.BLACK));

        JScrollPane contentScrollPane = new JScrollPane(contentArea);
        contentScrollPane.setBounds(27, 201, 318, 306);
        contentScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentPane.add(contentScrollPane);

        JButton btnSave = new JButton("저장");
        btnSave.setBounds(127, 531, 65, 23);
        btnSave.setForeground(new Color(255, 255, 255));
        btnSave.setBorderPainted(false);
        btnSave.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_BIG_FONT_SIZE));
        btnSave.setBackground(new Color(79, 168, 202));

        btnSave.addActionListener(actionEvent -> saveChanges());
        contentPane.add(btnSave);

        JButton btnCancel = new JButton("취소");
        btnCancel.setBounds(202, 531, 65, 23);
        btnCancel.setForeground(new Color(255, 255, 255));
        btnCancel.setBorderPainted(false);
        btnCancel.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_BIG_FONT_SIZE));
        btnCancel.setBackground(new Color(79, 168, 202));
        btnCancel.addActionListener(actionEvent -> dispose());
        contentPane.add(btnCancel);

        JLabel titleLabel = new JLabel("게시판 수정");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, 20));
        titleLabel.setBounds(91, 46, 200, 23);
        contentPane.add(titleLabel);
    }

    private void saveChanges() {
        String selectedCategoryName = (String) comboBox.getSelectedItem();
        String newTitle = titleField.getText();
        String newContent = contentArea.getText();

        int categoryNo = categoryRepository.findCategoryNoByName(selectedCategoryName);

        boardRepository.update(Board.of(boardNo, categoryNo, 1L, newTitle, newContent));

        boardListFrame.refreshBoardList();
        dispose();
    }
}
