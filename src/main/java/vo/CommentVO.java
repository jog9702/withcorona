package vo;

public class CommentVO {
	
	// 댓글 정보
	private int commentId;
	private int boardId;
	private String userId;
	private String comment;
	private String commentDesc;
	private String commentTime;
	private int commentParentno;
	
	
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	public int getBoardId() {
		return boardId;
	}
	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCommentDesc() {
		return commentDesc;
	}
	public void setCommentDesc(String commentDesc) {
		this.commentDesc = commentDesc;
	}
	public String getCommentTime() {
		return commentTime;
	}
	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}
	public int getCommentParentno() {
		return commentParentno;
	}
	public void setCommentParentno(int commentParentno) {
		this.commentParentno = commentParentno;
	}
}
