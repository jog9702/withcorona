package vo;

import java.sql.Date;

public class BoardVO {
	
	// 게시판 정보
	private int level;
	private int boardId;
	private String userId;
	private String boardTitle;
	private String boardDesc;
	private Date boardTime;
	private int boardParentno;
	
	public BoardVO() {
		
	}
	public BoardVO(int level, int boardId, String userId, String boardTitle, String boardDesc, Date boardTime,
			int boardParentno) {
		super();
		this.level = level;
		this.boardId = boardId;
		this.userId = userId;
		this.boardTitle = boardTitle;
		this.boardDesc = boardDesc;
		this.boardTime = boardTime;
		this.boardParentno = boardParentno;
	}
	
	
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
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
	public String getBoardTitle() {
		return boardTitle;
	}
	public void setBoardTitle(String boardTitle) {
		this.boardTitle = boardTitle;
	}
	public String getBoardDesc() {
		return boardDesc;
	}
	public void setBoardDesc(String boardDesc) {
		this.boardDesc = boardDesc;
	}
	public Date getBoardTime() {
		return boardTime;
	}
	public void setBoardTime(Date boardTime) {
		this.boardTime = boardTime;
	}
	public int getBoardParentno() {
		return boardParentno;
	}
	public void setBoardParentno(int boardParentno) {
		this.boardParentno = boardParentno;
	}
}
