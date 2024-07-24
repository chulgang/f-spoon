package board.dbconnection;

import config.DbConnectionThreadLocal;
import util.StreamContainer;

import java.sql.*;

public class DislikeRepository {
    Connection con;
    Statement stmt;
    PreparedStatement pstmt;

    public int findCountByMemberNo(Long memberNo) {
        con = DbConnectionThreadLocal.getConnection();
        String sql = "select count(DISLIKE_NO) from DISLIKES where MEMBER_NO = " + memberNo;
        ResultSet rs = null;

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        StreamContainer.close(rs);

        return -1;
    }

    public int findCountByMemberNoAndBoardNo(Long memberNo, Long boardNo) {
        con = DbConnectionThreadLocal.getConnection();
        String sql = "select count(DISLIKE_NO) from DISLIKES where MEMBER_NO = ? and BOARD_NO = ? ";
        ResultSet rs = null;

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, memberNo);
            pstmt.setLong(2, boardNo);
            rs = pstmt.executeQuery();

            rs.next();
            return rs.getInt(1);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        StreamContainer.close(rs);

        return -1;
    }

    public void create(Long boardNo, Long memberNo) {
        con = DbConnectionThreadLocal.getConnection();
        String sql = "insert into DISLIKES(BOARD_NO, MEMBER_NO) " + "values(?, ?)";

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, boardNo);
            pstmt.setLong(2, memberNo);
            pstmt.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}
