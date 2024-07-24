package member;

import config.CipherUtil;
import config.DbConnectionThreadLocal;

import java.sql.*;

public class MemberRep {
    Connection con = DbConnectionThreadLocal.getConnection();
    CipherUtil c = new CipherUtil();
    String sql1 = "insert into member(email, pwd, full_name, phone_number, birth_date, reg_date, role, gender) values(?,?,?,?,?,now(),?,?)";
    final String key2 = "DFSGsf#@F#$#$WSs4535";

    PreparedStatement pstmt1, pstmt2, pstmt3, pstmt4, pstmt5, pstmt6, pstmt7, pstmt8, pstmt9, pstmt10, pstmt11;

    public MemberDto findByMemberNo(Long memberNo) {
        con = DbConnectionThreadLocal.getConnection();
        String sql = "select * from MEMBER where MEMBER_NO = " + memberNo;
        ResultSet rs = null;
        Statement stmt = null;

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
                
                MemberDto member = new MemberDto();
                member.setMemberNo(Long.valueOf(data[0]));
                member.setEmail(data[1]);
                member.setPwd(data[2]);
                member.setFullNname(data[3]);
                member.setPhoneNumber(data[4]);
                member.setBirthNate(data[5]);
                member.setRegDate(data[6]);
                member.setRole(data[7]);
                member.setGender(Long.valueOf(data[8]));

                return member;
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

    public void updatePhoneNumber(Long memberNo, String newPhoneNumber) {
        con = DbConnectionThreadLocal.getConnection();
        String sql = "update MEMBER set PHONE_NUMBER = ? where MEMBER_NO = ?";

        try {
            pstmt1 = con.prepareStatement(sql);
            pstmt1.setString(1, newPhoneNumber);
            pstmt1.setLong(2, memberNo);
            pstmt1.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
    
    public void deleteByMemberNo(Long memberNo) {
        con = DbConnectionThreadLocal.getConnection();
        Statement stmt = null;
        String sql = "delete from MEMBER where MEMBER_NO = " + memberNo;

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

    public static void main(String[] args) {

        MemberRep m = new MemberRep();
        m.updatePhoneNumber(3L, "010-1234-1243");
    }
}
