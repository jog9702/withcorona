package vo;

public class BoardVO {
	
	// 게시판 정보
	private int boardId;
	private String userId;
	private String boardTitle;
	private String boardDesc;
	private String boardTime;
	private int boardParentno;
	
	
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
	public String getBoardTime() {
		return boardTime;
	}
	public void setBoardTime(String boardTime) {
		this.boardTime = boardTime;
	}
	public int getBoardParentno() {
		return boardParentno;
	}
	public void setBoardParentno(int boardParentno) {
		this.boardParentno = boardParentno;
	}
}