package board.dbconnection;

import config.DbConnectionThreadLocal;
import util.StreamContainer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {
    Connection con;
    Statement stmt;

    public List<String> findAllNames() {
        con = DbConnectionThreadLocal.getConnection();
        String sql = "select NAME from CATEGORY";
        ResultSet rs = null;
        List<String> columnNames = new ArrayList<>();

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                columnNames.add(rs.getString(1));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        StreamContainer.close(rs);

        return columnNames;
    }

    public int findCategoryNoByName(String selectedCategoryName) {
        con = DbConnectionThreadLocal.getConnection();
        String sql = "select CATEGORY_NO from CATEGORY where NAME = '" + selectedCategoryName + "'";
        ResultSet rs = null;

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                return Integer.parseInt(rs.getString(1));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        StreamContainer.close(rs);

        return -1;
    }
}
