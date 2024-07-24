package config;

import member.TokenInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthenticationFilter {

	public static int authenticationFilter(String token) {
		Connection con = DbConnectionThreadLocal.getConnection();

		String sql = "select count(*) as tok from token where token = ?";

		int result = 0;
		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setString(1, token);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getInt("tok");
			}
			return result;
		}
		catch (SQLException e) {
			e.printStackTrace();
			DbConnectionThreadLocal.setSqlError(true);
		}
		return 0;
	}

	public static int authorizationFilter(long memberNo, TokenInfo tokenInfo) {
		Connection con = DbConnectionThreadLocal.getConnection();
		String sql = "SELECT token_key AS tokenKey FROM token WHERE token = ?";
		String tokenKey = "";
		String email;

		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setString(1, tokenInfo.getToken());
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					tokenKey = rs.getString("tokenKey");
				}
			}

			email = CipherUtil.decryptCode(tokenInfo.getToken(), tokenKey, CipherUtil.key2);

			return verificationEmail(memberNo, email);
		} catch (SQLException e) {
			e.printStackTrace();
			DbConnectionThreadLocal.setSqlError(true);
		}
		return 0;
	}

	private static int verificationEmail(long memberNo, String email) {
		Connection con = DbConnectionThreadLocal.getConnection();
		String sql = "SELECT member_no AS newUserNo, role FROM member WHERE email = ?";
		long loginUserNo = 0;
		String userRole = "";
		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setString(1, email);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					loginUserNo = rs.getLong("newUserNo");
					userRole = rs.getString("role");
				}
			}
			if(userRole.equals("ROLE_ADMIN") || (loginUserNo == memberNo)){
				return 1;
			} else {
				return 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			DbConnectionThreadLocal.setSqlError(true);
		}
		return 0;
	}
}
