package board.model;

import util.FormatConverter;

public class BoardData {
    private Long boardNo;
    private Category category;
    private Long authorNo;
    private String authorName;
    private String title;
    private String content;
    private String writeDate;
    private String updateDate;
    private int viewCount;

    private BoardData(Long boardNo, Category category, Long authorNo, String authorName, String title,
                      String content, String writeDate, String updateDate, int viewCount) {
        this.boardNo = boardNo;
        this.category = category;
        this.authorNo = authorNo;
        this.authorName = authorName;
        this.title = title;
        this.content = content;
        this.writeDate = writeDate;
        this.updateDate = updateDate;
        this.viewCount = viewCount;
    }

    public static BoardData from(Board board, Category category) {
        String writeDate = FormatConverter.parseToString(board.getWriteDate());
        String updateDate = FormatConverter.parseToString(board.getUpdateDate());

        return new BoardData(
                board.getBoardNo(), category, board.getMemberNo(), "홍길동", board.getTitle(),
                board.getContent(), writeDate, updateDate, board.getViewCount()
        );
    }

    public Long getBoardNo() {
        return boardNo;
    }

    public Category getCategory() {
        return category;
    }

    public Long getAuthorNo() {
        return authorNo;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getWriteDate() {
        return writeDate;
    }

    public int getViewCount() {
        return viewCount;
    }
}
