package admin.dto;

public class SessionInfo {
    private long tokenNo;
    private String token;
    private String sessionDate;

    public String getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(String sessionDate) {
        this.sessionDate = sessionDate;
    }

    public long getTokenNo() {
        return tokenNo;
    }

    public void setTokenNo(long tokenNo) {
        this.tokenNo = tokenNo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}