package admin.dbconnection;

import admin.dto.BoardInfo;
import admin.dto.CommentInfo;
import admin.dto.MemberInfo;
import admin.dto.SessionInfo;
import config.DbConnectionThreadLocal;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminRepository {

    public List<MemberInfo> findAllMemberInfo() {
        List<MemberInfo> memberInfoList = new ArrayList<>();
        Connection connection = DbConnectionThreadLocal.getConnection();
        String sql = "SELECT * FROM MEMBER";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                MemberInfo memberInfo = new MemberInfo();
                memberInfo.setMemberNo(rs.getLong("member_no"));
                memberInfo.setFullName(rs.getString("full_name"));
                memberInfo.setEmail(rs.getString("email"));
                memberInfo.setPhoneNumber(rs.getString("phone_number"));
                memberInfo.setRegDate(rs.getString("reg_date"));
                memberInfo.setBirthDate(rs.getString("birth_date").substring(0, 10));
                String gender = (rs.getInt("gender") == 0) ? "남성" : "여성";
                memberInfo.setGender(gender);
                memberInfoList.add(memberInfo);
            }
        } catch (SQLException e) {
            DbConnectionThreadLocal.setSqlError(true);
        }
        return memberInfoList;
    }

    public List<BoardInfo> findAllBoardInfo() {
        List<BoardInfo> boardInfoList = new ArrayList<>();
        Connection connection = DbConnectionThreadLocal.getConnection();
        String sql = "SELECT b.board_no, c.name AS category_name, m.full_name AS member_name, b.title, b.write_date, b.view, b.likes, b.dislikes "
                + "FROM board b "
                + "JOIN category c ON b.category_no = c.category_no "
                + "JOIN member m ON m.member_no = b.member_no";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                BoardInfo boardInfo = new BoardInfo();
                boardInfo.setBoardNo(rs.getLong("b.board_no"));
                boardInfo.setCategoryName(rs.getString("category_name"));
                boardInfo.setMemberName(rs.getString("member_name"));
                boardInfo.setTitle(rs.getString("b.title"));
                boardInfo.setWriteDate(rs.getString("b.write_date"));
                boardInfo.setViewCount(rs.getInt("b.view"));
                boardInfo.setLikeCount(rs.getInt("b.likes"));
                boardInfo.setDislikeCount(rs.getInt("b.dislikes"));
                boardInfoList.add(boardInfo);
            }
        } catch (SQLException e) {
            DbConnectionThreadLocal.setSqlError(true);
        }
        return boardInfoList;
    }

    public List<CommentInfo> findAllCommentInfo() {
        List<CommentInfo> commentInfoList = new ArrayList<>();
        Connection connection = DbConnectionThreadLocal.getConnection();
        String sql = "SELECT c.comment_no, b.title AS board_title, c.content, m.full_name AS member_name, c.write_date "
                + "FROM COMMENTS c "
                + "JOIN BOARD b ON b.BOARD_NO = c.BOARD_NO "
                + "JOIN MEMBER m ON m.MEMBER_NO = c.MEMBER_NO";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                CommentInfo commentInfo = new CommentInfo();
                commentInfo.setCommentNo(rs.getLong("c.comment_no"));
                commentInfo.setBoardTitle(rs.getString("board_title"));
                commentInfo.setContent(rs.getString("c.content"));
                commentInfo.setMemberName(rs.getString("member_name"));
                commentInfo.setWriteDate(rs.getString("c.write_date"));
                commentInfoList.add(commentInfo);
            }
        } catch (SQLException e) {
            DbConnectionThreadLocal.setSqlError(true);
        }
        return commentInfoList;
    }

    public List<SessionInfo> findAllSessionInfo() {
        List<SessionInfo> sseionInfoList = new ArrayList<>();
        Connection connection = DbConnectionThreadLocal.getConnection();
        String sql = "SELECT token_no, session_date, token FROM token";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                SessionInfo sessionInfo = new SessionInfo();
                sessionInfo.setTokenNo(rs.getLong("token_no"));
                sessionInfo.setToken(rs.getString("token"));
                sessionInfo.setSessionDate(rs.getString("session_date"));
                sseionInfoList.add(sessionInfo);
            }
        } catch (SQLException e) {
            DbConnectionThreadLocal.setSqlError(true);
        }
        return sseionInfoList;
    }

    public Map<String, Integer> findDailyRegistrations() {
        Map<String, Integer> dailyRegistrations = new HashMap<>();
        Connection connection = DbConnectionThreadLocal.getConnection();
        String sql = "SELECT DATE(reg_date) AS reg_date, COUNT(*) AS count FROM MEMBER "
                + "WHERE reg_date >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH) "
                + "GROUP BY DATE(reg_date) "
                + "ORDER BY DATE(reg_date)";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String date = rs.getString("reg_date").substring(8, 10);
                int count = rs.getInt("count");
                dailyRegistrations.put(date, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DbConnectionThreadLocal.setSqlError(true);
        }
        return dailyRegistrations;
    }

    public Map<String, Integer> findGenderRatio() {
        Map<String, Integer> genderRatio = new HashMap<>();
        Connection connection = DbConnectionThreadLocal.getConnection();
        String sql = "SELECT gender, COUNT(*) AS count FROM MEMBER GROUP BY gender";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String gender = rs.getInt("gender") == 0 ? "부" : "모";
                int count = rs.getInt("count");
                genderRatio.put(gender, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DbConnectionThreadLocal.setSqlError(true);
        }
        return genderRatio;
    }

    public boolean deleteMemberById(long memberId) {
        Connection connection = DbConnectionThreadLocal.getConnection();
        String sql = "DELETE FROM MEMBER WHERE member_no = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, memberId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            DbConnectionThreadLocal.setSqlError(true);
        }
        return false;
    }

    public boolean deleteBoardById(long boardNo) {
        Connection connection = DbConnectionThreadLocal.getConnection();
        String sql = "DELETE FROM BOARD WHERE board_no = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, boardNo);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            DbConnectionThreadLocal.setSqlError(true);
        }
        return false;
    }

    public boolean deleteCommentById(long commentNo) {
        Connection connection = DbConnectionThreadLocal.getConnection();
        String sql = "DELETE FROM COMMENTS WHERE comment_no = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, commentNo);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            DbConnectionThreadLocal.setSqlError(true);
        }
        return false;
    }

    public boolean deleteSessionById(long tokenNo) {
        Connection connection = DbConnectionThreadLocal.getConnection();
        String sql = "DELETE FROM token WHERE token_no = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, tokenNo);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            DbConnectionThreadLocal.setSqlError(true);
        }
        return false;
    }

}
