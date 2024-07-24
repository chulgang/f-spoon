package gui;

import config.DbConnectionThreadLocal;
import message.dbconnection.MessageRepository;
import message.dto.NoticeInfo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import static config.GUI.*;

public class NoticeInfoFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTable noticeTable;
    private MessageRepository messageRepository = new MessageRepository();

    public NoticeInfoFrame(long receiverId) {
        setTitle("메세지 알림");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT);
        setLocation(DEFAULT_FRAME_POINT_X, DEFAULT_FRAME_POINT_Y);
        setLocationRelativeTo(null);

        List<NoticeInfo> noticeInfoList = messageRepository.getNoticeInfo(receiverId);

        String[] columnNames = {"보낸사람", "Sender ID", "Receiver ID", "메세지"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (NoticeInfo notice : noticeInfoList) {
            Object[] row = {
                    notice.getSenderName(),
                    notice.getSenderNo(),
                    notice.getReceiverNo(),
                    notice.getCount()
            };
            tableModel.addRow(row);
        }

        noticeTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(noticeTable);
        add(scrollPane, BorderLayout.CENTER);

        noticeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = noticeTable.rowAtPoint(e.getPoint());
                int col = noticeTable.columnAtPoint(e.getPoint());
                if (col == 0) {
                    long senderId = (long) noticeTable.getValueAt(row, 1);
                    openMessageFrame(receiverId, senderId);
                }
            }
        });
    }

    private void openMessageFrame(long senderId, long receiverId) {
        MessageFrame messageFrame = new MessageFrame(senderId, receiverId);
        messageFrame.setVisible(true);
    }
}
