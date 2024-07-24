package board.model;

import util.FormatConverter;

import java.time.LocalDateTime;

public class Comment {
    private Long commentNo;
    private Long boardNo;
    private Long memberNo;
    private String content;
    private LocalDateTime writeDate;
    private LocalDateTime updateDate;

    private Comment(Long commentNo, String content) {
        this.commentNo = commentNo;
        this.content = content;
    }

    public Comment(Long boardNo, Long memberNo, String content, LocalDateTime writeDate, LocalDateTime updateDate) {
        this.boardNo = boardNo;
        this.memberNo = memberNo;
        this.content = content;
        this.writeDate = writeDate;
        this.updateDate = updateDate;
    }

    public Comment(Long commentNo, Long boardNo, Long memberNo,
                   String content, LocalDateTime writeDate, LocalDateTime updateDate) {
        this.commentNo = commentNo;
        this.boardNo = boardNo;
        this.memberNo = memberNo;
        this.content = content;
        this.writeDate = writeDate;
        this.updateDate = updateDate;
    }

    public static Comment of(Long commentNo, String newContent) {
        return new Comment(commentNo, newContent);
    }

    public static Comment from(String[] data) {
        Long commentNo = Long.parseLong(data[0]);
        Long boardNo = Long.parseLong(data[1]);
        Long memberNo = Long.parseLong(data[2]);
        String content = data[3];
        LocalDateTime writeDate = FormatConverter.parseToDateTime(data[4]);
        LocalDateTime updateDate = FormatConverter.parseToDateTime(data[5]);

        return new Comment(commentNo, boardNo, memberNo, content, writeDate, updateDate);
    }

    public Long getCommentNo() {
        return commentNo;
    }

    public Long getBoardNo() {
        return boardNo;
    }

    public Long getMemberNo() {
        return memberNo;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getWriteDate() {
        return writeDate;
    }
}
