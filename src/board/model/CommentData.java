package board.model;

import util.FormatConverter;


public class CommentData {
    private Long commentNo;
    private Long authorNo;
    private String content;
    private String authorName;
    private String writeDate;

    public CommentData(Long commentNo, Long authorNo, String content, String authorName, String writeDate) {
        this.commentNo = commentNo;
        this.authorNo = authorNo;
        this.content = content;
        this.authorName = authorName;
        this.writeDate = writeDate;
    }

    public static CommentData from(Comment comment, String authorName) {
        String writeDate = FormatConverter.parseToString(comment.getWriteDate());

        return new CommentData(
                comment.getCommentNo(), comment.getMemberNo(), comment.getContent(), authorName, writeDate
        );
    }

    public Long getCommentNo() {
        return commentNo;
    }

    public Long getAuthorNo() {
        return authorNo;
    }

    public String getContent() {
        return content;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getWriteDate() {
        return writeDate;
    }
}
