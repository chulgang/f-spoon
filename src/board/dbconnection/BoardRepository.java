package board.dbconnection;

import board.model.Board;
import config.DbConnectionThreadLocal;
import util.StreamContainer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BoardRepository {
    Connection con;
    Statement stmt;
    PreparedStatement pstmt;

    public BoardRepository() {
        DbConnectionThreadLocal.initialize();
    }

    public List<Board> findAllByCategoryNoDesc(int categoryNo) {
        if (categoryNo == 0) {
            return findAllByLikesOverTen();
        }

        con = DbConnectionThreadLocal.getConnection();
        String sql = "select * from BOARD where CATEGORY_NO = " + categoryNo + " order by BOARD_NO desc";
        ResultSet rs = null;
        List<Board> boards = new ArrayList<>();

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int cc = rsmd.getColumnCount();

            while (rs.next()) {
                String[] board = new String[cc];
                for (int i = 0; i < cc; i++) {
                    board[i] = rs.getString(i + 1);

                    if (i == 3) {
                        board[i] = cutTitleLengthIfOverCount(board[i], 23);
                    }
                }
                boards.add(Board.from(board));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        StreamContainer.close(rs);

        return boards;
    }

    public List<Board> findFiveByCategoryNoDesc(int categoryNo) {
        if (categoryNo == 0) {
            return findFiveByLikesOverTen();
        }

        con = DbConnectionThreadLocal.getConnection();
        String sql = "select * from BOARD where CATEGORY_NO = '" + categoryNo + "' order by BOARD_NO desc limit 5";
        ResultSet rs = null;
        List<Board> boards = new ArrayList<>();

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int cc = rsmd.getColumnCount();

            while (rs.next()) {
                String[] board = new String[cc];
                for (int i = 0; i < cc; i++) {
                    board[i] = rs.getString(i + 1);

                    if (i == 3) {
                        board[i] = cutTitleLengthIfOverCount(board[i], 25);
                    }
                }
                boards.add(Board.from(board));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        StreamContainer.close(rs);

        return boards;
    }

    private List<Board> findAllByLikesOverTen() {
        con = DbConnectionThreadLocal.getConnection();
        String sql = "select * from BOARD where LIKES >= 20 order by BOARD_NO desc";
        ResultSet rs = null;
        List<Board> boards = new ArrayList<>();

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int cc = rsmd.getColumnCount();

            while (rs.next()) {
                String[] board = new String[cc];
                for (int i = 0; i < cc; i++) {
                    board[i] = rs.getString(i + 1);

                    if (i == 3) {
                        board[i] = cutTitleLengthIfOverCount(board[i], 23);
                    }
                }
                boards.add(Board.from(board));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        StreamContainer.close(rs);

        return boards;
    }

    private List<Board> findFiveByLikesOverTen() {
        con = DbConnectionThreadLocal.getConnection();
        String sql = "select * from BOARD where LIKES >= 20 order by BOARD_NO desc limit 5";
        ResultSet rs = null;
        List<Board> boards = new ArrayList<>();

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int cc = rsmd.getColumnCount();

            while (rs.next()) {
                String[] board = new String[cc];
                for (int i = 0; i < cc; i++) {
                    board[i] = rs.getString(i + 1);
                    if (i == 3) {
                        board[i] = cutTitleLengthIfOverCount(board[i], 25);
                    }
                }
                boards.add(Board.from(board));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        StreamContainer.close(rs);

        return boards;
    }

    private String cutTitleLengthIfOverCount(String str, int count) {
        if (str.length() > count) {
            return str.substring(0, count) + "...";
        }

        return str;
    }

    public Board findByBoardNo(Long boardNo) {
        con = DbConnectionThreadLocal.getConnection();
        String sql = "select * from BOARD where BOARD_NO = " + boardNo;
        ResultSet rs = null;

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int cc = rsmd.getColumnCount();
            String[] data = new String[cc];

            while (rs.next()) {
                for (int i = 0; i < cc; i++) {
                    data[i] = rs.getString(i + 1);
                }

                return Board.from(data);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        StreamContainer.close(rs);

        return null;
    }

    public void create(Board board) {
        con = DbConnectionThreadLocal.getConnection();
        String sql = "insert into BOARD(CATEGORY_NO, MEMBER_NO, TITLE, CONTENT, WRITE_DATE, UPDATE_DATE, VIEW) "
                + "values(?, ?, ?, ?, now(), now(), 0)";

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, board.getCategoryNo());
            pstmt.setLong(2, board.getMemberNo());
            pstmt.setString(3, board.getTitle());
            pstmt.setString(4, board.getContent());
            pstmt.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void update(Board board) {
        con = DbConnectionThreadLocal.getConnection();
        String sql = "update BOARD set TITLE = ?, CONTENT = ? where BOARD_NO = ?";

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, board.getTitle());
            pstmt.setString(2, board.getContent());
            pstmt.setLong(3, board.getBoardNo());
            pstmt.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void deleteByBoardNo(Long boardNo) {
        con = DbConnectionThreadLocal.getConnection();
        String sql = "delete from BOARD where BOARD_NO = " + boardNo;

        try {
            stmt = con.createStatement();
        } catch (SQLException se) {
            se.printStackTrace();
        }

        try {
            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void incrementViewByBoardNo(Long boardNo) {
        con = DbConnectionThreadLocal.getConnection();
        String sql = "update BOARD set VIEW = (select VIEW from BOARD where BOARD_NO = ?) + 1 where BOARD_NO = ?";

        try {
            stmt = con.createStatement();
        } catch (SQLException se) {
            se.printStackTrace();
        }

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, boardNo);
            pstmt.setLong(2, boardNo);
            pstmt.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void incrementLikeByBoardNo(Long boardNo) {
        con = DbConnectionThreadLocal.getConnection();
        String sql = "update BOARD set LIKES = (select LIKES from BOARD where BOARD_NO = ?) + 1 where BOARD_NO = ?";

        try {
            stmt = con.createStatement();
        } catch (SQLException se) {
            se.printStackTrace();
        }

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, boardNo);
            pstmt.setLong(2, boardNo);
            pstmt.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void incrementDislikeByBoardNo(Long boardNo) {
        con = DbConnectionThreadLocal.getConnection();
        String sql = "update BOARD set DISLIKES = (select DISLIKES from BOARD where BOARD_NO = ?) + 1 where BOARD_NO = ?";

        try {
            stmt = con.createStatement();
        } catch (SQLException se) {
            se.printStackTrace();
        }

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, boardNo);
            pstmt.setLong(2, boardNo);
            pstmt.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}
