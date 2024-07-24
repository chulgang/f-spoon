package member;

public class MemberDto {
	private long memberNo;
	private String email;
	private String pwd;
	private String fullNname;
	private String phoneNumber;
	private String birthDate;
	private String regDate;
	private Role role;
	private int gender;
	
	public long getMemberNo() {
		return memberNo;
	}
	public void setMemberNo(long memberNo) {
		this.memberNo = memberNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getFullNname() {
		return fullNname;
	}
	public void setFullNname(String fullNname) {
		this.fullNname = fullNname;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getBirthNate() {
		return birthDate;
	}
	public void setBirthNate(String birthNate) {
		this.birthDate = birthNate;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = Role.valueOf(role);
	}
	public long getGender() {
		return memberNo;
	}
	public void setGender(long memberNo) {
		this.memberNo = memberNo;
	}

	public boolean isRoleAdmin() {
		return role.isAdmin();
	}
}
