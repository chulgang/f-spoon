package message.dbconnection;

import config.DbConnectionThreadLocal;
import message.dto.MessageInfo;
import message.dto.NoticeInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageRepository {

    public boolean sendMessage(long senderId, long receiverId, String content) {
        String sql = "INSERT INTO message (sender_no, receiver_no, content, send_date, receive_check) VALUES (?, ?, ?, now(), 1)";
        Connection con = DbConnectionThreadLocal.getConnection();
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setLong(1, senderId);
            pstmt.setLong(2, receiverId);
            pstmt.setString(3, content);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            DbConnectionThreadLocal.setSqlError(true);
            return false;
        }
    }

    public List<MessageInfo> getMessages(long receiverId, long senderId) {
        List<MessageInfo> messages = new ArrayList<>();
        String sql = "SELECT * FROM message M "
                + "JOIN Member SM on SM.MEMBER_NO = M.SENDER_NO "
                + "JOIN MEMBER RM ON RM.MEMBER_NO = M.RECEIVER_NO "
                + "WHERE (receiver_no = ? and sender_no = ?) OR (receiver_no = ? AND sender_no = ?) "
                + "ORDER BY send_date";
        Connection con = DbConnectionThreadLocal.getConnection();

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setLong(1, receiverId);
            pstmt.setLong(2, senderId);
            pstmt.setLong(3, senderId);
            pstmt.setLong(4, receiverId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    MessageInfo messageInfo = new MessageInfo();
                    messageInfo.setMessageNo(rs.getInt("message_no"));
                    messageInfo.setSenderName(rs.getString("SM.FULL_NAME"));
                    messageInfo.setReceiverName(rs.getString("RM.FULL_NAME"));
                    messageInfo.setContent(rs.getString("content"));
                    messageInfo.setTimestamp(rs.getTimestamp("send_date"));
                    messageInfo.setSenderId(rs.getLong("sender_no"));
                    messages.add(messageInfo);
                }
            }
        } catch (SQLException e) {
            DbConnectionThreadLocal.setSqlError(true);
        }

        return messages;
    }

    public int getCountNoticeMessage(long receiverId) {
        String sql = "SELECT COUNT(DISTINCT sender_no) AS count FROM message WHERE receiver_no = ? AND receive_check = 1";
        Connection con = DbConnectionThreadLocal.getConnection();
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setLong(1, receiverId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count");
                }
            }
        } catch (SQLException e) {
            DbConnectionThreadLocal.setSqlError(true);
        }

        return 0;
    }

    public List<NoticeInfo> getNoticeInfo(long receiverId) {

        List<NoticeInfo> notices = new ArrayList<>();
        String sql = "SELECT mem.full_name, msg.sender_no, msg.receiver_no, COUNT(*) AS count " + "FROM message msg "
                + "JOIN member mem ON mem.member_no = msg.sender_no "
                + "WHERE msg.receiver_no = ? "
                + "AND msg.receive_check = 1 "
                + "GROUP BY mem.full_name, msg.sender_no, msg.receiver_no";
        Connection con = DbConnectionThreadLocal.getConnection();

        try (PreparedStatement pstmt = con.prepareStatement(sql)){
            pstmt.setLong(1, receiverId);
            try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                    NoticeInfo noticeInfo = new NoticeInfo();
                    noticeInfo.setSenderName(rs.getString("full_name"));
                    noticeInfo.setSenderNo(rs.getLong("sender_no"));
                    noticeInfo.setReceiverNo(rs.getLong("receiver_no"));
                    noticeInfo.setCount(rs.getInt("count"));
                    notices.add(noticeInfo);
                }
            }
        } catch (SQLException e) {
            DbConnectionThreadLocal.setSqlError(true);
        }
        return notices;
    }

    public void updateReceiveCheckToZero(long receiverId) {
        String updateSql = "UPDATE message SET receive_check = 0 WHERE receiver_no = ? AND receive_check = 1";
        Connection con = DbConnectionThreadLocal.getConnection();
        try (PreparedStatement pstmt = con.prepareStatement(updateSql)) {
            pstmt.setLong(1, receiverId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            DbConnectionThreadLocal.setSqlError(true);
        }
    }
}
