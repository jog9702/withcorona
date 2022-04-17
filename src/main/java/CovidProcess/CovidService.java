package CovidProcess;

import java.util.ArrayList;
import java.util.List;

import BoardVo.ClinicVo;
import vo.*;

public class CovidService {

	CovidDao covidDao;
	
	CovidService(){
		covidDao = new CovidDao();
	}
	
	public int todayConfirmedCase() {
		return covidDao.todayConfirmedCase(0.9);
	}
	
	public int monthConfirmedCase() {
		return covidDao.todayConfirmedCase(30);
	}
	
	public int yearConfirmedCase() {
		return covidDao.todayConfirmedCase(365);
	}
	
	public int koreaLocConfirmedCase(String loc) {
		return covidDao.koreaLocConfirmedCase(loc);
	}
	
	public int ForeignLocConfirmedCase(String loc) {
		return covidDao.ForeignLocConfirmedCase(loc);
	}
	
	public List<ClinicVo> getClinicInfo() {
		return covidDao.clinic();
	}
	
	public List<ClinicVo> getSearchClinicInfo(String str) {
		return covidDao.searchClinic(str);
	}
	
	public String loginCheck(String id, String pw) {
		return covidDao.loginCheck(id, pw);
	}
	
	public void updateConfirmedCase() {
		covidDao.updateConfirmedCase();
	}
	
	public void qnaUpdate(BoardVO boardVO) {
		covidDao.qnaUpdate(boardVO);
	}
	
	public int qnaTotal() {
		return covidDao.qnaTotal();
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
		List<BoardVO> articlesList = covidDao.selectQna(pageNum, countPerPage);

		// list는 그냥 모든 row
		recursive(0, articlesList);

		List list = articlesList;
		List resultList = new ArrayList();
		
		for(int i=0; i<list.size(); i++) {
			BoardVO boardVo = (BoardVO) list.get(i);
			
			if(boardVo.getBoardParentno() == 0) {
				resultList.add(boardVo);
				int articleNo = boardVo.getBoardId();
				
			}
		}
		return articlesList;
	}
	
}
