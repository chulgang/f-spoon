package image.dbconnection;

import config.DbConnectionThreadLocal;

import javax.sql.rowset.serial.SerialBlob;
import javax.swing.*;
import java.sql.*;

public class ImageRepository {
    Connection con;
    Statement stmt;
    PreparedStatement pstmt;

    public ImageRepository() {

    }

    public ImageIcon findImageFileByMemnberNo(Long memberNo) {
        con = DbConnectionThreadLocal.getConnection();
        String sql = "select IMAGE_FILE from IMAGE where MEMBER_NO = " + memberNo;
        ResultSet rs = null;

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Blob blob = rs.getBlob(1);
                byte[] imageData = blob.getBytes(1, (int) blob.length());

                return new ImageIcon(imageData);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException se) {
            }
        }

        return null;
    }

    public void create(Long memberNo, byte[] image) {
        con = DbConnectionThreadLocal.getConnection();
        String sql = "insert into IMAGE values(? , ?)";

        try {
            Blob imageBlob = new SerialBlob(image);

            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, memberNo);
            pstmt.setBlob(2, imageBlob);
            pstmt.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void update(Long memberNo, byte[] image) {
        con = DbConnectionThreadLocal.getConnection();
        String sql = "update IMAGE set IMAGE_FILE = ? where MEMBER_NO = " + memberNo;

        try {
            Blob imageBlob = new SerialBlob(image);

            pstmt = con.prepareStatement(sql);
            pstmt.setBlob(1, imageBlob);
            pstmt.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}
