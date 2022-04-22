package CovidProcess;

import java.util.ArrayList;
import java.util.List;

import vo.*;

public class CovidService {

	CovidDao covidDao;
	
	CovidService(){
		covidDao = new CovidDao();
	}
	
	public int todayConfirmedCase() {
		return covidDao.todayConfirmedCase(1);
	}
	
	public int monthConfirmedCase() {
		return covidDao.todayConfirmedCase(30);
	}
	
	public int yearConfirmedCase() {
		return covidDao.todayConfirmedCase(365);
	}
	
	public int todayDeath() {
		return covidDao.todayDeath();
	}
	
	public int todayDeath(String loc) {
		return covidDao.todayDeath(loc);
	}
	
	public int koreaLocConfirmedCase(String loc) {
		return covidDao.koreaLocConfirmedCase(loc);
	}
	
	public int ForeignLocConfirmedCase(String loc) {
		return covidDao.ForeignLocConfirmedCase(loc);
	}
	
	//Controller의 signUpSuccess에서 쓰는 Service - 남모세
	public void signUpSuccess(UserVO vo) {
		covidDao.signUp(vo);
	}
		
	//Controller의 signUpCheck에서 쓰는 Service - 남모세
	public boolean signUpCheck(String id) {
		UserVO vo = new UserVO();
		vo = covidDao.signUpCheck(id);
		System.out.println("Service => String id: " + id);
		System.out.println("Service => getUserId: " + vo.getUserId());
			
		if(vo.getUserId() == null) {
			return true;
		} else {
			return false;
		}
	}
	
	//Controller의 loginCheck에서 쓰는 Service - 남모세
	public UserVO loginCheck(String id, String pwd) {
		UserVO vo = covidDao.loginCheck(id, pwd);
		return vo;
	}
	
	public void updateDBtoDate(String before) {
		covidDao.dropTable();
		covidDao.createTable();
		covidDao.updateDBtoDate(before);
	}
	
	public void updateToAuto() {
		covidDao.updateToAuto();
	}
	
	public void foreignUpdateDBtoDate(String before) {
		covidDao.foreignDropTable();
		covidDao.foreignCreateTable();
		covidDao.foreignUpdateDBtoDate(before);
	}
	
	public void foreignUpdateToAuto() {
		covidDao.foreignUpdateToAuto();
	}
	
	public int foreignTodayConfirmedCase(String loc) {
		return covidDao.foreignTodayConfirmedCase(loc);
	}
	
	public int foreignTodayDeathCase(String loc) {
		return covidDao.foreignTodayDeathCase(loc);
	}
	
	
	
	List recursive(int pId, List list) {
		List resultList = new ArrayList();
		
		for(int i=0; i<list.size(); i++) {
			BoardVO boardVo = (BoardVO)list.get(i);
			if(boardVo.getBoardParentno() == pId) {
				resultList.add(boardVo);
				
				List tempList = recursive(boardVo.getBoardId(), list);
				resultList.add(tempList);
			}
		}
		return resultList;
	}
	
	public List<BoardVO> qnaList(int pageNum, int countPerPage){
		List<BoardVO> qnaList = covidDao.qnaSelect(pageNum, countPerPage);

		// list는 그냥 모든 row
		recursive(0, qnaList);

		List list = qnaList;
		List resultList = new ArrayList();
		
		for(int i=0; i<list.size(); i++) {
			BoardVO boardVo = (BoardVO) list.get(i);
			
			if(boardVo.getBoardParentno() == 0) {
				resultList.add(boardVo);
				int articleNo = boardVo.getBoardId();
				
			}
		}
		return qnaList;
	}
	
	public BoardVO qnaView(int boardId) {
		BoardVO boardVo = covidDao.qnaView(boardId);
		
		return boardVo;
	}
	
	public void qnaInsert(BoardVO boardVO) {
		covidDao.qnaInsert(boardVO);
	}
	
	public void qnaUpdate(BoardVO boardVO) {
		covidDao.qnaUpdate(boardVO);
	}
	
	public void qnaDelete(int boardId) {
		covidDao.qnaDelete(boardId);
	}

	public int qnaTotal() {
		return covidDao.qnaTotal();
	}
	
}
