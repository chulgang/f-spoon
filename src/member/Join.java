package member;

import config.DbConnectionThreadLocal;

import java.sql.*;


public class Join {
	Statement stmt;
	Connection con;
	Join(){
 		 con = DbConnectionThreadLocal.getConnection();

 		 findByBoardNo("1");
 	}
	
	
	
	
	
	
 
	private void findByBoardNo(String boardNo) {
    	con = DbConnectionThreadLocal.getConnection();
        String sql = "select * from BOARD where BOARD_NO = " + boardNo;
        ResultSet rs = null;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int cc = rsmd.getColumnCount();
            for (int i = 1; i <= cc; i++) {
                String cn = rsmd.getColumnName(i);
                p(cn + "\t");
            }
            pln("");
            for (int i = 1; i <= cc; i++) {
                p("-----------");
            }
            pln("");
            while (rs.next()) {
                for (int i = 1; i <= cc; i++) {
                    String data = rs.getString(i);
                    p(data + "\t");
                }
                pln("");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException se) {
            }
        }
        pln("");
    }
 
	private void p(String str) {
	        System.out.print(str);
	    }
	private void pln(String str) {
	        System.out.println(str);
	    }
	public static void main(String[] args) {
	        Join boardRepository = new Join();

	    }
}
