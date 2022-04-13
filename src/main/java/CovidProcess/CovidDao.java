package CovidProcess;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import BoardVo.ClinicVo;
import CovidVo.CountVO;
import CovidVo.UserVO;

public class CovidDao {

	private PreparedStatement pstmt;
	private Connection con;
	private DataSource dataFactory;

	DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	Date date = new Date();        
	String dateToStrMinor = dateFormat.format(date);
	String dateToStr = dateFormat.format(date);
	
	public CovidDao() {
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context)ctx.lookup("java:comp/env");
			dataFactory = (DataSource)envContext.lookup("jdbc/oracle");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// xml로 데이터 받아서 update버튼 누르면 그날 확진자 수 따오기
	// openApi xml 데이터 데이터 베이스에 넣는 작업
	public void updateConfirmedCase () {
		
		String url ="http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19SidoInfStateJson?serviceKey=sIYnCEa6cv0btJb%2Bc2pWvDc76iYuhohbaoz%2B9bwsx8R2C8sPhrIivNMS3HHDCkVoBKoCktxoml4HN%2Bih04AWPQ%3D%3D&pageNo=1&numOfRows=10&";
		url += "startCreateDt="+dateToStr+"&endCreateDt=" +dateToStr;
		String result = getStringFromURL(url);
		
		try {
			List<CountVO> list = new ArrayList();
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			StringReader sr = new StringReader(result);
			InputSource is = new InputSource(sr);
			
			Document doc = builder.parse(is);
			
			NodeList nodeList = doc.getElementsByTagName("item");
			for(int i=0; i<nodeList.getLength(); i++) {
				Element el = (Element)nodeList.item(i);
				CountVO vo = new CountVO();
				
				
				NodeList nodes = el.getChildNodes();
				Node node3 = nodes.item(3);
				Element childElement3 = (Element) node3;
				vo.setLocal(childElement3.getTextContent());
				System.out.println(childElement3.getTextContent());
				
				Node node1 = nodes.item(1);
				Element childElement1 = (Element) node1;
				vo.setDeath(Integer.parseInt(childElement1.getTextContent()));
				System.out.println(childElement1.getTextContent());
				
				Node node7 = nodes.item(7);
				Element childElement7 = (Element) node7;
				vo.setLocal_info(Integer.parseInt(childElement7.getTextContent()));
				System.out.println(childElement7.getTextContent());
				
//				Date type 삽입
//				Node node0 = nodes.item(0);
//				Element childElement0 = (Element) node0;
//				vo.setDate(childElement0.getTextContent());
				list.add(vo);	  
			}
			
			
			con = dataFactory.getConnection();
			
			// 쿼리에 오늘 일일 확진자 수 뽑아 낼 수 있는 쿼리문 입력
			for(int i=0; i<list.size(); i++) {
				String query="insert into korea (korea_id, korea_death, korea_local, korea_local_info) values (korea_seq.nextval, ?,?,?)";
				System.out.println(query);
				
				CountVO list_i = list.get(i);
				
//				java.sql.Date date = (java.sql.Date) list_i.get(0);
				int deathCount = (int) list_i.getDeath();
				System.out.println(deathCount);
				String local = (String) list_i.getLocal();
				System.out.println(local);
				int localInfo = (int) list_i.getLocal_info();
				System.out.println(localInfo);
				
				
				pstmt = con.prepareStatement(query);
				pstmt.setInt(1, deathCount);
				pstmt.setString(2, local);
				pstmt.setInt(3, localInfo);
//				pstmt.setDate(4, date);
				
				pstmt.executeQuery();
			}
			
			if(pstmt != null) {
				pstmt.close();
			}
			if(con != null) {
				con.close();
			}
			if(sr != null) {
				sr.close();
			}			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// 홈페이지에 표시되는 일일, 월별, 연별 확진자 수
	// 박정희
	public int todayConfirmedCase (double a) {
		int count = 0;
		try {
			con = dataFactory.getConnection();
			
			// 쿼리에 오늘 일일 확진자 수 뽑아 낼 수 있는 쿼리문 입력
			String query="select korea_info as tot from korea where korea_time between sysdate -? and sysdate";
			System.out.println(query);
			
			pstmt = con.prepareStatement(query);
			pstmt.setDouble(1, a);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				 count += rs.getInt("tot"); 
			}
			if(rs != null) {
				rs.close();
			}
			if(pstmt != null) {
				pstmt.close();
			}
			if(con != null) {
				con.close();
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
	
	// 국내 지역을 받아서 DB 접속 -> 그 지역의 확진자 수를 표시한다
	public int koreaLocConfirmedCase (String loc) {
		int count =0;

		try {
			con = dataFactory.getConnection();
			
			String query="select korea_local_info from korea where korea_local = ? and korea_time > sysdate - 1";
			System.out.println(query);
			
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, loc);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				count = rs.getInt("korea_local_info");
			}
			if(rs != null) {
				rs.close();
			}
			if(pstmt != null) {
				pstmt.close();
			}
			if(con != null) {
				con.close();
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
	}
	
	// 해외 지역을 받아서 DB 접속 -> 그 지역의 확진자 수를 표시한다
	public int ForeignLocConfirmedCase (String loc) {
		int count =0;

		try {
			con = dataFactory.getConnection();
			
			String query="select foreign_local_info from foreign where foreign_local = ? and foreign_time > sysdate - 1";
			System.out.println(query);
			
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, loc);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				count = rs.getInt("foreign_local_info");
			}
			if(rs != null) {
				rs.close();
			}
			if(pstmt != null) {
				pstmt.close();
			}
			if(con != null) {
				con.close();
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
	}
	
	// 보건소 정보 표시 (openApi로 변경?)
	public List<ClinicVo> clinic() {
		
		List<ClinicVo> list = new ArrayList<ClinicVo>();
		
		try {
			con = dataFactory.getConnection();
			
			String query = "";
			query += "SELECT * ";
			query += " FROM clinic";
			
			pstmt = con.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ClinicVo vo = new ClinicVo();
				
				vo.setClinic_id(rs.getInt("clinic_id"));
				vo.setClinic_local(rs.getString("clinic_local"));
				vo.setClinic_name(rs.getString("clinic_name"));
				vo.setClinic_info(rs.getString("clinic_info"));
				vo.setClinic_tel(rs.getString("clinic_tel"));
				
				list.add(vo);
			}
			if(rs != null) {
				rs.close();
			}
			if(pstmt != null) {
				pstmt.close();
			}
			if(con != null) {
				con.close();
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
		
	}
	
	// 보건소 검색 (openApi로 변경?)
	public List<ClinicVo> searchClinic(String str) {
		
		List<ClinicVo> list = new ArrayList<ClinicVo>();
		
		try {
			con = dataFactory.getConnection();
			
			String query = "";
			query += "SELECT * ";
			query += " FROM clinic where clinic_local like ? or clinic_name like ? or clinic_info like ?";
			
			pstmt = con.prepareStatement(query);
			
			pstmt.setString(1, "%"+str+"%");
			pstmt.setString(2, "%"+str+"%");
			pstmt.setString(3, "%"+str+"%");
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ClinicVo vo = new ClinicVo();
				
				vo.setClinic_id(rs.getInt("clinic_id"));
				vo.setClinic_local(rs.getString("clinic_local"));
				vo.setClinic_name(rs.getString("clinic_name"));
				vo.setClinic_info(rs.getString("clinic_info"));
				vo.setClinic_tel(rs.getString("clinic_tel"));
				
				list.add(vo);
			}
			if(rs != null) {
				rs.close();
			}
			if(pstmt != null) {
				pstmt.close();
			}
			if(con != null) {
				con.close();
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
		
	}
	
	public String loginCheck(String id, String pw) {

		String authcode ="";
		
		try {
			con = dataFactory.getConnection();
			
			String query = "";
			query += "SELECT user_id, user_password ";
			query += " from user";
			
			pstmt = con.prepareStatement(query);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				if(id.equals(rs.getString("user_id")) && pw.equals(rs.getString("user_password"))) {
					authcode = rs.getString("user_auth");
				}
			}
			if(rs != null) {
				rs.close();
			}
			if(pstmt != null) {
				pstmt.close();
			}
			if(con != null) {
				con.close();
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return authcode;
	}
	
	public String getStringFromURL(String url) {
		StringBuffer result = new StringBuffer();
		
		try {
			URL urlObj = new URL(url);
			HttpURLConnection conn = null;
			conn = (HttpURLConnection) urlObj.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(10000);
			conn.setRequestMethod("GET");
			conn.connect();
			
			InputStream is = conn.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			do {
				line = br.readLine();
				if(line != null) {
					result.append(line);
				}	
			}while(line != null);

//			while( ( line = br.readLine() ) != null ) {
//				result.append(line);
//			}
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result.toString();
	}
}
















