package gui;

import admin.dbconnection.AdminRepository;
import admin.dto.BoardInfo;
import admin.dto.CommentInfo;
import admin.dto.MemberInfo;
import admin.dto.SessionInfo;
import config.DbConnectionThreadLocal;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class AdminPageFrame extends JFrame {

    private JPanel contentPane;
    private JTable memberTable;
    private JTable boardTable;
    private JTable commentTable;
    private JTable tokenTable;
    private AdminRepository adminRepository = new AdminRepository();
    private DefaultTableModel memberTableModel;
    private DefaultTableModel boardTableModel;
    private DefaultTableModel commentTableModel;
    private DefaultTableModel tokenTableModel;
    private JTabbedPane tabbedPane;
    private JButton deleteButton;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    DbConnectionThreadLocal.initialize();
                    AdminPageFrame frame = new AdminPageFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    DbConnectionThreadLocal.setSqlError(true);
                } finally {
                    DbConnectionThreadLocal.reset();
                }
            }
        });
    }

    public AdminPageFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());

        JLabel lblNewLabel = new JLabel("관리자 페이지");
        lblNewLabel.setFont(new Font("굴림", Font.PLAIN, 16));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(lblNewLabel, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        contentPane.add(splitPane, BorderLayout.CENTER);

        JPanel statisticsPanel = new JPanel();
        statisticsPanel.setLayout(new GridLayout(1, 2));
        statisticsPanel.add(createRegistrationPanel());
        statisticsPanel.add(createGenderRatioPanel());
        splitPane.setTopComponent(statisticsPanel);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        splitPane.setBottomComponent(tabbedPane);

        showMemberInfo();
        showBoardInfo();
        showCommentInfo();
        showTokenInfo();

        JPanel buttonPanel = new JPanel();
        deleteButton = new JButton("회원 삭제");
        deleteButton.addActionListener(e -> deleteSelectedMember());
        buttonPanel.add(deleteButton);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        tabbedPane.addChangeListener(e -> updateDeleteButton());

        splitPane.setDividerLocation(400);
    }

    private JPanel createRegistrationPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        Map<String, Integer> dailyRegistrations = adminRepository.findDailyRegistrations();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, Integer> entry : dailyRegistrations.entrySet()) {
            dataset.addValue(entry.getValue(), "data", entry.getKey());
        }

        JFreeChart chart = ChartFactory.createLineChart("날짜별 가입자 수 추이", "일자", "가입자수", dataset,
                org.jfree.chart.plot.PlotOrientation.VERTICAL, true, true, false);

        Font titleFont = new Font("나눔바른고딕", Font.BOLD, 14);
        Font labelFont = new Font("나눔바른고딕", Font.PLAIN, 12);

        chart.setTitle(new TextTitle(chart.getTitle().getText(), titleFont));

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.getDomainAxis().setLabelFont(labelFont);
        plot.getDomainAxis().setTickLabelFont(labelFont);
        plot.getRangeAxis().setLabelFont(labelFont);
        plot.getRangeAxis().setTickLabelFont(labelFont);

        chart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        ChartPanel chartPanel = new ChartPanel(chart);
        panel.add(chartPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createGenderRatioPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        Map<String, Integer> genderRatio = adminRepository.findGenderRatio();
        DefaultPieDataset dataset = new DefaultPieDataset();

        for (Map.Entry<String, Integer> entry : genderRatio.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart chart = ChartFactory.createPieChart3D("부모 비율", dataset, true, true, false);

        Font titleFont = new Font("나눔바른고딕", Font.BOLD, 14);
        chart.setTitle(new TextTitle(chart.getTitle().getText(), titleFont));

        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        plot.setLabelFont(new Font("나눔바른고딕", Font.PLAIN, 14));
        plot.setNoDataMessage("데이터가 없습니다.");
        plot.setExplodePercent("부", 0.1);
        plot.setExplodePercent("모", 0.1);

        ChartPanel chartPanel = new ChartPanel(chart);
        panel.add(chartPanel, BorderLayout.CENTER);

        return panel;
    }

    private void updateDeleteButton() {
        int selectedIndex = tabbedPane.getSelectedIndex();
        String tabTitle = tabbedPane.getTitleAt(selectedIndex);

        switch (tabTitle) {
            case "회원":
                deleteButton.setText("회원 삭제");
                deleteButton.setActionCommand("회원 삭제");
                deleteButton.addActionListener(e -> deleteSelectedMember());
                break;
            case "게시글":
                deleteButton.setText("게시글 삭제");
                deleteButton.setActionCommand("게시글 삭제");
                deleteButton.addActionListener(e -> deleteSelectedBoard());
                break;
            case "댓글":
                deleteButton.setText("댓글 삭제");
                deleteButton.setActionCommand("댓글 삭제");
                deleteButton.addActionListener(e -> deleteSelectedComment());
                break;
            case "토큰":
                deleteButton.setText("토큰 삭제");
                deleteButton.setActionCommand("토큰 삭제");
                deleteButton.addActionListener(e -> deleteSelectedToken());
                break;
            default:
                deleteButton.setText("");
                deleteButton.setActionCommand("");
                break;
        }
    }

    private void deleteSelectedMember() {
        int selectedRow = memberTable.getSelectedRow();
        if (selectedRow >= 0) {
            long memberId = (long) memberTable.getValueAt(selectedRow, 0);
            boolean success = adminRepository.deleteMemberById(memberId);
            if (success) {
                memberTableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "회원이 성공적으로 삭제되었습니다.");
            } else {
                JOptionPane.showMessageDialog(this, "회원 삭제에 실패했습니다.");
            }
        }
    }

    private void deleteSelectedBoard() {
        int selectedRow = boardTable.getSelectedRow();
        if (selectedRow >= 0) {
            long boardNo = (long) boardTable.getValueAt(selectedRow, 0);
            boolean success = adminRepository.deleteBoardById(boardNo);
            if (success) {
                boardTableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "게시글이 성공적으로 삭제되었습니다.");
            } else {
                JOptionPane.showMessageDialog(this, "게시글 삭제에 실패했습니다.");
            }
        }
    }

    private void deleteSelectedComment() {
        int selectedRow = commentTable.getSelectedRow();
        if (selectedRow >= 0) {
            long commentNo = (long) commentTable.getValueAt(selectedRow, 0);
            boolean success = adminRepository.deleteCommentById(commentNo);
            if (success) {
                commentTableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "댓글이 성공적으로 삭제되었습니다.");
            } else {
                JOptionPane.showMessageDialog(this, "댓글 삭제에 실패했습니다.");
            }
        }
    }

    private void deleteSelectedToken() {
        int selectedRow = tokenTable.getSelectedRow();
        if (selectedRow >= 0) {
            long tokenNo = (long) tokenTable.getValueAt(selectedRow, 0);
            boolean success = adminRepository.deleteSessionById(tokenNo);
            if (success) {
                tokenTableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "토큰이 성공적으로 삭제되었습니다.");
            } else {
                JOptionPane.showMessageDialog(this, "토큰 삭제에 실패했습니다.");
            }
        }
    }

    private void showMemberInfo() {
        String[] memberColumnNames = {"유저번호", "아이디", "이름", "연락처", "성별", "생년월일", "가입일"};
        List<MemberInfo> memberInfoList = adminRepository.findAllMemberInfo();
        Object[][] memberData = new Object[memberInfoList.size()][7];

        for (int i = 0; i < memberInfoList.size(); i++) {
            MemberInfo memberInfo = memberInfoList.get(i);
            memberData[i][0] = memberInfo.getMemberNo();
            memberData[i][1] = memberInfo.getEmail();
            memberData[i][2] = memberInfo.getFullName();
            memberData[i][3] = memberInfo.getPhoneNumber();
            memberData[i][4] = memberInfo.getGender();
            memberData[i][5] = memberInfo.getBirthDate();
            memberData[i][6] = memberInfo.getRegDate();
        }

        memberTableModel = new DefaultTableModel(memberData, memberColumnNames);
        memberTable = new JTable(memberTableModel);
        JScrollPane memberScrollPane = new JScrollPane(memberTable);
        tabbedPane.add("회원", memberScrollPane);
    }

    private void showBoardInfo() {
        String[] boardColumnNames = {"게시번호", "카테고리", "작성자", "제목", "작성일", "조회수", "좋아요", "싫어요"};
        List<BoardInfo> boardInfoList = adminRepository.findAllBoardInfo();
        Object[][] boardData = new Object[boardInfoList.size()][8];

        for (int i = 0; i < boardInfoList.size(); i++) {
            BoardInfo boardInfo = boardInfoList.get(i);
            boardData[i][0] = boardInfo.getBoardNo();
            boardData[i][1] = boardInfo.getCategoryName();
            boardData[i][2] = boardInfo.getMemberName();
            boardData[i][3] = boardInfo.getTitle();
            boardData[i][4] = boardInfo.getWriteDate();
            boardData[i][5] = boardInfo.getViewCount();
            boardData[i][6] = boardInfo.getLikeCount();
            boardData[i][7] = boardInfo.getDislikeCount();
        }

        boardTableModel = new DefaultTableModel(boardData, boardColumnNames);
        boardTable = new JTable(boardTableModel);
        JScrollPane boardScrollPane = new JScrollPane(boardTable);
        tabbedPane.add("게시글", boardScrollPane);
    }

    private void showCommentInfo() {
        String[] commentColumnNames = {"댓글번호", "게시글번호", "작성자", "내용", "작성일"};
        List<CommentInfo> commentInfoList = adminRepository.findAllCommentInfo();
        Object[][] commentData = new Object[commentInfoList.size()][5];

        for (int i = 0; i < commentInfoList.size(); i++) {
            CommentInfo commentInfo = commentInfoList.get(i);
            commentData[i][0] = commentInfo.getCommentNo();
            commentData[i][1] = commentInfo.getBoardTitle();
            commentData[i][2] = commentInfo.getMemberName();
            commentData[i][3] = commentInfo.getContent();
            commentData[i][4] = commentInfo.getWriteDate();
        }

        commentTableModel = new DefaultTableModel(commentData, commentColumnNames);
        commentTable = new JTable(commentTableModel);
        JScrollPane commentScrollPane = new JScrollPane(commentTable);
        tabbedPane.add("댓글", commentScrollPane);
    }

    private void showTokenInfo() {
        String[] tokenColumnNames = {"토큰번호", "토큰", "세션 생성 일시"};
        List<SessionInfo> tokenInfoList = adminRepository.findAllSessionInfo();
        Object[][] tokenData = new Object[tokenInfoList.size()][3];

        for (int i = 0; i < tokenInfoList.size(); i++) {
            SessionInfo sessionInfo = tokenInfoList.get(i);
            tokenData[i][0] = sessionInfo.getTokenNo();
            tokenData[i][1] = sessionInfo.getToken();
            tokenData[i][2] = sessionInfo.getSessionDate();
        }

        tokenTableModel = new DefaultTableModel(tokenData, tokenColumnNames);
        tokenTable = new JTable(tokenTableModel);
        JScrollPane tokenScrollPane = new JScrollPane(tokenTable);
        tabbedPane.add("토큰", tokenScrollPane);
    }
}
