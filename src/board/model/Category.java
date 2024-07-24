package board.model;

public enum Category {
    POPULARITY(0, "인기"),
    NOTIFICATION(1, "공지"),
    ELEMENTARY_SCHOOL(2, "초등"),
    MIDDLE_SCHOOL(3, "중등"),
    HIGH_SCHOOL(4, "고등");

    private final String description;
    private final int categoryNo;

    Category(int categoryNo, String description) {
        this.categoryNo = categoryNo;
        this.description = description;
    }

    public static Category get(int idx) {
        return Category.values()[idx];
    }

    public int getCategoryNo() {
        return categoryNo;
    }

    public String getDescription() {
        return description;
    }

    public boolean isNotification() {
        return this == NOTIFICATION;
    }

    public boolean isPopularity() {
        return this == POPULARITY;
    }
}
