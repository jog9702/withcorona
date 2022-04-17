package vo;

public class ForeignVO {
	
	// 해외 정보
	private int foreignId;
	private String foreignLocal;
	private int foreignLocalInfo;
	private int foreignDeath;
	private String foreignTime;
	
	
	public int getForeignId() {
		return foreignId;
	}
	public void setForeignId(int foreignId) {
		this.foreignId = foreignId;
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
