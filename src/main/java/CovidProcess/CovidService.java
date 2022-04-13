package CovidProcess;

import java.util.List;

import BoardVo.ClinicVo;

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
	
	
}
