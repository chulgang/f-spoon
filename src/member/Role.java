package member;

public enum Role {
    ROLE_ADMIN("관리자"),
    ROLE_USER("일반 회원");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public boolean isAdmin() {
        return this == ROLE_ADMIN;
    }
}
