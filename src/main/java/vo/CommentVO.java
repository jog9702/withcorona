package vo;

import java.sql.Date;

public class CommentVO {
	
	// 댓글 정보
	private int commentId;
	private int boardId;
	private String userId;
	private String commentDesc;
	private Date commentTime;
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
	public String getCommentDesc() {
		return commentDesc;
	}
	public void setCommentDesc(String commentDesc) {
		this.commentDesc = commentDesc;
	}
	public Date getCommentTime() {
		return commentTime;
	}
	public void setCommentTime(Date commentTime) {
		this.commentTime = commentTime;
	}
	public int getCommentParentno() {
		return commentParentno;
	}
	public void setCommentParentno(int commentParentno) {
		this.commentParentno = commentParentno;
	}
}
