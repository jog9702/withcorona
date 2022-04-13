package CovidVo;

import java.sql.Date;

public class CountVO {

	private int id;
	private int info;
	private int death;
	private String local;
	private int local_info;
	private Date time;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getInfo() {
		return info;
	}
	public void setInfo(int info) {
		this.info = info;
	}
	public int getDeath() {
		return death;
	}
	public void setDeath(int death) {
		this.death = death;
	}
	public String getLocal() {
		return local;
	}
	public void setLocal(String local) {
		this.local = local;
	}
	public int getLocal_info() {
		return local_info;
	}
	public void setLocal_info(int local_info) {
		this.local_info = local_info;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
	
	
}
