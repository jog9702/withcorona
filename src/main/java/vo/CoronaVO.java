package vo;

public class CoronaVO {
	
	// 국내 정보
	private int koreaId;
	private int koreaInfo;
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
	
}
