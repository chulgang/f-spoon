package board.dbconnection;

import board.model.Comment;
import config.DbConnectionThreadLocal;
import util.StreamContainer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentRepository {
    Connection con;
    Statement stmt;
    PreparedStatement pstmt;

    public List<Comment> findAllByBoardNoAsc(Long boardNo) {
        con = DbConnectionThreadLocal.getConnection();
        String sql = "select * from COMMENTS where BOARD_NO = " + boardNo + " order by COMMENT_NO";
        ResultSet rs = null;
        List<Comment> comments = new ArrayList<>();

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int cc = rsmd.getColumnCount();

            while (rs.next()) {
                String[] data = new String[cc];
                for (int i = 0; i < cc; i++) {
                    data[i] = rs.getString(i + 1);
                }
                comments.add(Comment.from(data));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        StreamContainer.close(rs);

        return comments;
    }

    public void create(Comment comment) {
        con = DbConnectionThreadLocal.getConnection();
        String sql = "insert into COMMENTS(BOARD_NO, MEMBER_NO, CONTENT, WRITE_DATE, UPDATE_DATE)"
                + "values(?, ?, ?, now(), now())";

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, comment.getBoardNo());
            pstmt.setLong(2, comment.getMemberNo());
            pstmt.setString(3, comment.getContent());
            pstmt.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void updateContent(Comment comment) {
        con = DbConnectionThreadLocal.getConnection();
        String sql = "update COMMENTS set CONTENT = ? where COMMENT_NO = ?";

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, comment.getContent());
            pstmt.setLong(2, comment.getCommentNo());
            pstmt.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void deleteByCommentNo(Long commentNo) {
        con = DbConnectionThreadLocal.getConnection();
        String sql = "delete from COMMENTS where COMMENT_NO = " + commentNo;

        try {
            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}
