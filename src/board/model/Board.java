package board.model;

import util.FormatConverter;

import java.time.LocalDateTime;

public class Board {
    private Long boardNo;
    private int categoryNo;
    private Long memberNo;
    private String title;
    private String content;
    private LocalDateTime writeDate;
    private LocalDateTime updateDate;
    private int viewCount;
    private int likeCount;
    private int dislikeCount;

    private Board(int categoryNo, Long memberNo, String title, String content) {
        this.categoryNo = categoryNo;
        this.memberNo = memberNo;
        this.title = title;
        this.content = content;
    }

    private Board(Long boardNo, int categoryNo, Long memberNo, String title, String content) {
        this.boardNo = boardNo;
        this.categoryNo = categoryNo;
        this.memberNo = memberNo;
        this.title = title;
        this.content = content;
    }

    private Board(Long boardNo, int categoryNo, Long memberNo, String title, String content,
                  LocalDateTime writeDate, LocalDateTime updateDate, int viewCount, int likeCount, int dislikeCount) {
        this.boardNo = boardNo;
        this.categoryNo = categoryNo;
        this.memberNo = memberNo;
        this.title = title;
        this.content = content;
        this.writeDate = writeDate;
        this.updateDate = updateDate;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
    }

    public static Board from(String[] data) {
        Long boardNo = Long.parseLong(data[0]);
        int categoryNo = Integer.parseInt(data[1]);
        Long memberNo = Long.parseLong(data[2]);
        String title = data[3];
        String content = data[4];
        LocalDateTime writeDate = FormatConverter.parseToDateTime(data[5]);
        LocalDateTime updateDate = FormatConverter.parseToDateTime(data[6]);
        int viewCount = Integer.parseInt(data[7]);
        int like = Integer.parseInt(data[8]);
        int dislike = Integer.parseInt(data[9]);

        return new Board(boardNo, categoryNo, memberNo, title, content, writeDate, updateDate, viewCount, like, dislike);
    }

    public static Board of(int categoryNo, Long memberNo, String title, String content) {
        return new Board(categoryNo, memberNo, title, content);
    }

    public static Board of(Long boardNo, int categoryNo, Long memberNo, String title, String content) {
        return new Board(boardNo, categoryNo, memberNo, title, content);
    }

    public Long getBoardNo() {
        return boardNo;
    }

    public int getCategoryNo() {
        return categoryNo;
    }

    public Long getMemberNo() {
        return memberNo;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getWriteDate() {
        return writeDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public int getViewCount() {
        return viewCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public int getDislikeCount() {
        return dislikeCount;
    }
}
