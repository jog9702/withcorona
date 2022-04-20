package vo;

public class CoronaVO {
	
	// 국내 정보
	private int koreaId;
	private int koreaDeath;
	private String koreaLocal;
	private int koreaLocalInfo;
	private String koreaTime;
	
	// 해외 정보
	private int foreignId;
	private int foreignInfo;
	private int foreignDeath;
	private String foreignLocal;
	private int foreignLocalInfo;
	private String foreignTime;
	
	// 유저 정보
	private String userId;
	private int userPassword;
	private String userName;
	private String userGender;
	private String userEmail;
	private String userAuth;
	
	// 게시판 정보
	private int boardId;
//	private String userId;
	private String boardTitle;
	private String boardDesc;
	private String boardTime;
	private int boardParentno;
	
	// 댓글 정보
	private int commentId;
//	private int boardId;
//	private String userId;
	private String comment;
	private String commentDesc;
	private String commentTime;
	private int commentParentno;
	
	public int getKoreaId() {
		return koreaId;
	}
	public void setKoreaId(int koreaId) {
		this.koreaId = koreaId;
	}
	public int getKoreaDeath() {
		return koreaDeath;
	}
	public void setKoreaDeath(int koreaDeath) {
		this.koreaDeath = koreaDeath;
	}
	public String getKoreaLocal() {
		return koreaLocal;
	}
	public void setKoreaLocal(String koreaLocal) {
		this.koreaLocal = koreaLocal;
	}
	public int getKoreaLocalInfo() {
		return koreaLocalInfo;
	}
	public void setKoreaLocalInfo(int koreaLocalInfo) {
		this.koreaLocalInfo = koreaLocalInfo;
	}
	public String getKoreaTime() {
		return koreaTime;
	}
	public void setKoreaTime(String koreaTime) {
		this.koreaTime = koreaTime;
	}
	public int getForeignId() {
		return foreignId;
	}
	public void setForeignId(int foreignId) {
		this.foreignId = foreignId;
	}
	public int getForeignInfo() {
		return foreignInfo;
	}
	public void setForeignInfo(int foreignInfo) {
		this.foreignInfo = foreignInfo;
	}
	public int getForeignDeath() {
		return foreignDeath;
	}
	public void setForeignDeath(int foreignDeath) {
		this.foreignDeath = foreignDeath;
	}
	public String getForeignLocal() {
		return foreignLocal;
	}
	public void setForeignLocal(String foreignLocal) {
		this.foreignLocal = foreignLocal;
	}
	public int getForeignLocalInfo() {
		return foreignLocalInfo;
	}
	public void setForeignLocalInfo(int foreignLocalInfo) {
		this.foreignLocalInfo = foreignLocalInfo;
	}
	public String getForeignTime() {
		return foreignTime;
	}
	public void setForeignTime(String foreignTime) {
		this.foreignTime = foreignTime;
	}
}
