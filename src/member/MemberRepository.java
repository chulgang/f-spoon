package member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import config.CipherUtil;
import config.DbConnectionThreadLocal;

public class MemberRepository {

	Connection con = DbConnectionThreadLocal.getConnection();
	PreparedStatement pstmt1, pstmt2, pstmt3, pstmt4, pstmt5, pstmt6, pstmt7, pstmt8, pstmt9, pstmt10, pstmt11, pstm12;

	public boolean join(String email, String pwd, String confirmPwd ,String fullName, String phoneNumber, String birthDate, String role,
						int gender) {
		String sql1 = "insert into member(email, pwd, full_name, phone_number, birth_date, reg_date, role, gender) values(?,?,?,?,?,now(),?,?)";

		if(!pwd.equals(confirmPwd)) {
			JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다", "NOTICE", 1, null);
			return false;
		}

		try {
			String[] encryptPwds = CipherUtil.encryptCode(pwd);
			String encryptPwd = encryptPwds[0];
			String key1 = encryptPwds[1];
			System.out.println(encryptPwds[1]);
			pstmt1 = con.prepareStatement(sql1);
			pstmt1.setString(1, email);
			pstmt1.setString(2, encryptPwd);
			pstmt1.setString(3, fullName);
			pstmt1.setString(4, phoneNumber);
			pstmt1.setString(5, birthDate);
			pstmt1.setString(6, role);
			System.out.println(gender);
			pstmt1.setInt(7, gender);
			pstmt1.executeUpdate();
			long index = findById2(email);
			System.out.println("index: " + index);
			long memberNo = Long.valueOf(index);
			System.out.println("memberNo: " + memberNo);
			keyinsert(memberNo, key1);
			System.out.println("id: " + memberNo);
			JOptionPane.showMessageDialog(null, "회원가입에 성공했습니다", "NOTICE", 1, null);
			return true;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "정확한 정보를 입력해주세요", "NOTICE", 1, null);
			return false;
		}
	}
	public TokenInfo login(String email, String pwd) throws SQLException {
		String sql = "select email,pwd from member where email = " + "'" + email + "'";
		TokenInfo tokenInfo = new TokenInfo();
		ResultSet rs;
		long memberNo = findById2(email);
		String key1 = getKey(memberNo);
		System.out.println("key1: " + key1);
		String encryptedPwd = getencryptPwd(email);
		System.out.println("login() encryptedPwd: " + encryptedPwd);
		String decryptedPwd = CipherUtil.decryptCode(encryptedPwd, key1, CipherUtil.key2);
		System.out.println("login() decryptedPwd: " + decryptedPwd);
		pstmt10 = con.prepareStatement(sql);
		rs = pstmt10.executeQuery();
		do {
			if (!rs.next()) {
				JOptionPane.showMessageDialog(null, "해당하는 이메일이 없습니다.", "NOTICE", 1, null);
				System.out.println("해당하는 이메일이 없습니다.");
				return null;
			}
			String oldMemberEmail = rs.getString(1);
			String oldMemberPwd = rs.getString(2);
			System.out.println("oldMemberEmail: " + oldMemberEmail + "\n" + "oldMemberPwd: " + oldMemberPwd);
			String decryptedPwd2 = CipherUtil.decryptCode(oldMemberPwd, key1, CipherUtil.key2);
			System.out.println("\n decryptedPwd2: " + decryptedPwd2);
			if (oldMemberEmail.equals(email)) {
				if (decryptedPwd2.equals(pwd)) {
					String tokenSql = "insert into token values(?,?,?)";
					System.out.println("로그인에 성공했습니다.");
					// 기본키 , token_key, token
					String[] tokenArr = CipherUtil.encryptCode(email);
					String insertTokenSql = "insert into token(token_key, token, session_date) values (?,?, now())";
					pstmt11 = con.prepareStatement(insertTokenSql);
					pstmt11.setString(1, tokenArr[1]);
					pstmt11.setString(2, tokenArr[0]);
					System.out.println("token :" + tokenArr[0]);
					pstmt11.executeUpdate();
					System.out.println("해석 값 :" + CipherUtil.decryptCode(tokenArr[0], tokenArr[1], CipherUtil.key2));
					System.out.println("토큰넣기 성공!!");
					tokenInfo.setMemberNo(memberNo);
					tokenInfo.setToken(tokenArr[0]);
					JOptionPane.showMessageDialog(null, "로그인에 성공했습니다.", "NOTICE", 1, null);
					return tokenInfo;
				} else {
					JOptionPane.showMessageDialog(null, "비밀번호를 잘못 입력했습니다.", "NOTICE", 1, null);
					System.out.println("비밀번호를 잘못 입력했습니다.");
					return null;
				}
			}
		} while (rs.next());
		return null;
	}

	public long findById(String fullName) {
		String sql = "select * from member where full_name = " + "'" + fullName + "'";
		System.out.println("findById sql: " + sql);

		long pk = 0;
		try {
			pstmt2 = con.prepareStatement(sql);
			ResultSet rs = pstmt2.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
				System.out.println("pk: " + pk);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pk;
	}

	public long findById2(String email) {
		String sql = "select member_no from member where email = ?";
		System.out.println("findById2 sql: " + sql);
		long pk = 0;
		try {
			pstmt8 = con.prepareStatement(sql);
			pstmt8.setString(1, email);
			ResultSet rs = pstmt8.executeQuery();
			while (rs.next()) {
				pk = rs.getLong(1);
				System.out.println("pk: " + pk);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pk;
	}

	public void keyinsert(long memberNo, String key1) {
		String sql = "insert into enc_key values(? ,? )";
		System.out.println("keyInsert sql: " + sql);
		try {
			pstmt6 = con.prepareStatement(sql);
			pstmt6.setLong(1, memberNo);
			pstmt6.setString(2, key1);
			pstmt6.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getKey(long memberNo) {
		String sql = "select key1 from enc_key where member_no =" + memberNo;
		System.out.println("getKey sql: " + sql);
		String data = null;
		try {
			pstmt7 = con.prepareStatement(sql);
			ResultSet rs = pstmt7.executeQuery();
			rs.next();
			data = rs.getString(1);
			System.out.println("data: " + data);
		} catch (SQLException e) {
			System.out.println("예상하는에러 잘못된 아이디 넣음.");
			System.out.println(false);
			// e.printStackTrace();
		}
		return data;
	}

	public String getencryptPwd(String email) {
		String sql = "select pwd from member where email = " + "'" + email + "'";
		System.out.println("findById2 sql: " + sql);
		String encryptedPwd = "";
		try {
			pstmt9 = con.prepareStatement(sql);
			ResultSet rs = pstmt9.executeQuery();
			while (rs.next()) {
				encryptedPwd = rs.getString(1);
				System.out.println("encryptPwd: " + encryptedPwd);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return encryptedPwd;
	}

	public List<MemberDto> findByAll() {
		String sql = "select * from member";
		List<MemberDto> list = new ArrayList<MemberDto>();
		try {
			pstmt3 = con.prepareStatement(sql);
			ResultSet rs = pstmt3.executeQuery();
			while (rs.next()) {
				MemberDto member = new MemberDto();
				member.setMemberNo(rs.getLong("member_no"));
				member.setEmail(rs.getString("email"));
				member.setPwd(rs.getString("pwd"));
				member.setFullNname(rs.getString("full_name"));
				member.setPhoneNumber(rs.getString("phone_number"));
				member.setBirthNate(rs.getString("birth_date"));
				member.setRegDate(rs.getString("reg_date"));
				member.setRole(rs.getString("role"));
				member.setGender(rs.getInt("gender"));
				list.add(member);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

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

	public void updatePhoneNumber(long memberNo, String newPhoneNumber) {
		con = DbConnectionThreadLocal.getConnection();
		String sql = "UPDATE member SET phone_number = ? WHERE member_no = ?";

		try {PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, newPhoneNumber);
			pstmt.setLong(2, memberNo);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updatePwd(long memberNo, String newPwd) {
		Connection con = DbConnectionThreadLocal.getConnection();
		String[] encryptPwds = CipherUtil.encryptCode(newPwd);
		String encryptPwd = encryptPwds[0];
		String key1 = encryptPwds[1];
		String sql = "UPDATE member SET pwd = ? WHERE member_no = ?";

		try (PreparedStatement psmt = con.prepareStatement(sql)) {
			psmt.setString(1, encryptPwd);
			psmt.setLong(2, memberNo);

			int rowsUpdated = psmt.executeUpdate();
			if (rowsUpdated > 0) {
				keyUpdate(memberNo, key1);
			} else {
				System.out.println("업데이트 실패");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void keyUpdate ( long memberNo, String key1){
		String sql = "UPDATE enc_key SET key1 = ? WHERE member_no = ?";
		try {
			PreparedStatement pstmt20 = con.prepareStatement(sql);
			pstmt20.setString(1, key1);
			pstmt20.setLong(2, memberNo);
			pstmt20.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
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

	public static void main(String[] args) throws SQLException { // -- 메인 함수 추후 테스트시 사용 예정
		DbConnectionThreadLocal.initialize();
		MemberRepository m = new MemberRepository();
		// System.out.println("return pk: " +m.findById("조요한"));
		// System.out.println("return findByAll: " + m.findByAll()+"\n");
		m.login("whdygks486@naver.com", "asd123");
		// m.join("whdygks486@naver.com", "asd123", "조요한","01025959023","971219","USER",
		// 0);

	}

}