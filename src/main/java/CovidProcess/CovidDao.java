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

import vo.CoronaVO;
import vo.*;

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
	
	// xml로 데이터 받아서 update버튼 누르면 그날 국내 확진자 수 따오기
	// openApi xml 데이터 데이터 베이스에 넣는 작업
	// 박정희
	public void updateConfirmedCase () {
		
		String url ="http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19SidoInfStateJson?serviceKey=sIYnCEa6cv0btJb%2Bc2pWvDc76iYuhohbaoz%2B9bwsx8R2C8sPhrIivNMS3HHDCkVoBKoCktxoml4HN%2Bih04AWPQ%3D%3D&pageNo=1&numOfRows=10&";
		url += "startCreateDt="+dateToStr+"&endCreateDt=" +dateToStr;
		String result = getStringFromURL(url);
		String token = null;
		
		
		try {
			con = dataFactory.getConnection();
			String tmpQuery = "select korea_local from korea where korea_time between sysdate-1 and sysdate and korea_local = '합계'"; 
			pstmt = con.prepareStatement(tmpQuery);
			pstmt.executeQuery();
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				token = rs.getString("korea_local"); 
			}
			
			if(pstmt != null) {
				pstmt.close();
			}
			if(con != null) {
				con.close();
			}
			if(rs != null) {
				rs.close();
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		
		if(token == null) {
			try {
				List<CoronaVO> list = new ArrayList();
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				StringReader sr = new StringReader(result);
				InputSource is = new InputSource(sr);
				
				Document doc = builder.parse(is);
				
				NodeList nodeList = doc.getElementsByTagName("item");
				for(int i=0; i<nodeList.getLength(); i++) {
					Element el = (Element)nodeList.item(i);
					CoronaVO vo = new CoronaVO();
					
					
					NodeList nodes = el.getChildNodes();
					Node node3 = nodes.item(3);
					Element childElement3 = (Element) node3;
					vo.setKoreaLocal(childElement3.getTextContent());
					System.out.println(childElement3.getTextContent());
					
					Node node1 = nodes.item(1);
					Element childElement1 = (Element) node1;
					vo.setKoreaDeath(Integer.parseInt(childElement1.getTextContent()));
					System.out.println(childElement1.getTextContent());
					
					Node node7 = nodes.item(7);
					Element childElement7 = (Element) node7;
					vo.setKoreaLocalInfo(Integer.parseInt(childElement7.getTextContent()));
					System.out.println(childElement7.getTextContent());
					
					Node node0 = nodes.item(0);
					Element childElement0 = (Element) node0;
					String timeBefore = childElement0.getTextContent();
					String time = timeBefore.substring(1, 10).replace("-", "/");
					vo.setKoreaTime(time);
					System.out.println(time);
					
//				Date type 삽입
//				Node node0 = nodes.item(0);
//				Element childElement0 = (Element) node0;
//				vo.setDate(childElement0.getTextContent());
					list.add(vo);	  
				}
				
				
				con = dataFactory.getConnection();
				
				// 쿼리에 오늘 일일 확진자 수 뽑아 낼 수 있는 쿼리문 입력
				for(int i=0; i<list.size(); i++) {
					String query="insert into korea (korea_id, korea_death, korea_local, korea_local_info, korea_time) values (korea_seq.nextval, ?,?,?,?)";
					System.out.println(query);
					
					CoronaVO list_i = list.get(i);
					
//				java.sql.Date date = (java.sql.Date) list_i.get(0);
					int deathCount = (int) list_i.getKoreaDeath();
					System.out.println(deathCount);
					String local = (String) list_i.getKoreaLocal();
					System.out.println(local);
					int localInfo = (int) list_i.getKoreaLocalInfo();
					System.out.println(localInfo);
					String time = list_i.getKoreaTime();
					System.out.println(time);
					
					
					pstmt = con.prepareStatement(query);
					pstmt.setInt(1, deathCount);
					pstmt.setString(2, local);
					pstmt.setInt(3, localInfo);
					pstmt.setString(4, time);
					
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
		}else {
			System.out.println("이미 최신 데이터 입니다");
		}
	}
	
	// 날짜를 입력받아 DB를 초기화 할때 사용되는 드롭문
	// 박정희
	public void dropTable() {
		try {
			con = dataFactory.getConnection();
			String query="drop table korea";
			pstmt = con.prepareStatement(query);
			pstmt.executeQuery();
			
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
	}
	
	// 날짜를 입력받아 DB를 초기화 할때 사용되는 크리에이트문
	// 박정희
	public void createTable() {
		try {
			con = dataFactory.getConnection();
			String query="create table korea(korea_id number(10) primary key, korea_info number(10), korea_death number(10), korea_local varchar2(1000), korea_local_info number(10), korea_time date)";
			pstmt = con.prepareStatement(query);
			pstmt.executeQuery();
			
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
	}
	
	// 입력받은 날짜 부터 오늘 까지의 xml정보로 DB초기화
	// 박정희
	public void updateDBtoDate (String sDay) {
		
		String url ="http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19SidoInfStateJson?serviceKey=sIYnCEa6cv0btJb%2Bc2pWvDc76iYuhohbaoz%2B9bwsx8R2C8sPhrIivNMS3HHDCkVoBKoCktxoml4HN%2Bih04AWPQ%3D%3D&pageNo=1&numOfRows=10&";
		url += "startCreateDt="+sDay+"&endCreateDt=" +dateToStr;
		String result = getStringFromURL(url);
		
		try {
			List<CoronaVO> list = new ArrayList();
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			StringReader sr = new StringReader(result);
			InputSource is = new InputSource(sr);
			
			Document doc = builder.parse(is);
			
			NodeList nodeList = doc.getElementsByTagName("item");
			for(int i=0; i<nodeList.getLength(); i++) {
				Element el = (Element)nodeList.item(i);
				CoronaVO vo = new CoronaVO();
				
				
				NodeList nodes = el.getChildNodes();
				Node node3 = nodes.item(3);
				Element childElement3 = (Element) node3;
				vo.setKoreaLocal(childElement3.getTextContent());
				System.out.println(childElement3.getTextContent());
				
				Node node1 = nodes.item(1);
				Element childElement1 = (Element) node1;
				vo.setKoreaDeath(Integer.parseInt(childElement1.getTextContent()));
				System.out.println(childElement1.getTextContent());
				
				Node node7 = nodes.item(7);
				Element childElement7 = (Element) node7;
				vo.setKoreaLocalInfo(Integer.parseInt(childElement7.getTextContent()));
				System.out.println(childElement7.getTextContent());
				
				Node node0 = nodes.item(0);
				Element childElement0 = (Element) node0;
				String timeBefore = childElement0.getTextContent();
				String time = timeBefore.substring(1, 10).replace("-", "/");
				vo.setKoreaTime(time);
				System.out.println(time);
				
//				Date type 삽입
//				Node node0 = nodes.item(0);
//				Element childElement0 = (Element) node0;
//				vo.setDate(childElement0.getTextContent());
				list.add(vo);	  
			}
		
			con = dataFactory.getConnection();
			
			// 쿼리에 오늘 일일 확진자 수 뽑아 낼 수 있는 쿼리문 입력
			for(int i=0; i<list.size(); i++) {
				String query="insert into korea (korea_id, korea_death, korea_local, korea_local_info, korea_time) values (korea_seq.nextval, ?,?,?,?)";
				System.out.println(query);
				
				CoronaVO list_i = list.get(i);
				
//				java.sql.Date date = (java.sql.Date) list_i.get(0);
				int deathCount = (int) list_i.getKoreaDeath();
				System.out.println(deathCount);
				String local = (String) list_i.getKoreaLocal();
				System.out.println(local);
				int localInfo = (int) list_i.getKoreaLocalInfo();
				System.out.println(localInfo);
				String time = list_i.getKoreaTime();
				
				
				pstmt = con.prepareStatement(query);
				pstmt.setInt(1, deathCount);
				pstmt.setString(2, local);
				pstmt.setInt(3, localInfo);
				pstmt.setString(4, time);
				
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
	public int todayConfirmedCase (int a) {
		int count = 0;
		try {
			con = dataFactory.getConnection();
			
			// 쿼리에 오늘 일일 확진자 수 뽑아 낼 수 있는 쿼리문 입력
			if(a == 1) {
				String query="SELECT sum(distinct korea_local_info) as korea_local_info FROM   korea WHERE  korea_id = (SELECT Max(korea_id) FROM   korea WHERE  korea_time BETWEEN sysdate - ? AND sysdate AND korea_local = '합계')";
				System.out.println(query);
				
				pstmt = con.prepareStatement(query);
				pstmt.setInt(1, a);
				
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
			}else {
				String query="select sum(distinct korea_local_info) as korea_local_info from korea where korea_local = '합계' and korea_time BETWEEN sysdate - ? AND sysdate";
				System.out.println(query);
				
				pstmt = con.prepareStatement(query);
				pstmt.setInt(1, a);
				
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
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return count;
	}
	
	// 일일 사망자
	// 박정희
	public int todayDeath () {
		int count = 0;
		try {
			con = dataFactory.getConnection();
			
			// 쿼리에 오늘 일일 확진자 수 뽑아 낼 수 있는 쿼리문 입력
			String query="select (select korea_death from korea where korea_time between sysdate-1 and sysdate and korea_local = '합계')-(select korea_death from korea where korea_time between sysdate - 2 and sysdate - 1 and korea_local = '합계') as korea_death from dual";
			System.out.println(query);
			
			pstmt = con.prepareStatement(query);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				count = rs.getInt("korea_death"); 
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
	
	// 지역별 일일 사망자
	// 박정희
	public int todayDeath (String loc) {
		int count = 0;
		try {
			con = dataFactory.getConnection();
			
			// 쿼리에 오늘 일일 확진자 수 뽑아 낼 수 있는 쿼리문 입력
			String query="select (select korea_death from korea where korea_time between sysdate-1 and sysdate and korea_local = ?)-(select korea_death from korea where korea_time between sysdate - 2 and sysdate - 1 and korea_local = ?) as korea_death from dual";
			System.out.println(query);
			
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, loc);
			pstmt.setString(2, loc);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				count = rs.getInt("korea_death"); 
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
	// 박정희
	public int koreaLocConfirmedCase (String loc) {
		int count =0;

		try {
			con = dataFactory.getConnection();
			
			String query="SELECT korea_local_info FROM   korea WHERE  korea_id = (SELECT Max(korea_id) FROM   korea WHERE  korea_time BETWEEN sysdate - 1 AND sysdate AND korea_local = ?)";
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
			
			String query="select foreign_local_info from foreign_info where foreign_local = ? and foreign_time > sysdate - 1";
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
	
	// 게시판 모두 조회
	public List<BoardVO> selectQna(int pageNum, int countPerPage){
		List<BoardVO> qnaList = new ArrayList();
		
		try {
			con = dataFactory.getConnection();
			
			String query = "";
			query += "select * from (";
			query += " select rownum as rnum, board_id, board_parentno, board_title, board_desc, board_time, u.user_id";
			query += " from board_info b, user_info u";
			query += " where b.user_id = u.user_id";
			query += " start with board_parentno = 0";
			query += " connect by prior board_id = board_parentno";
			query += " order siblings by board_id desc";
			query += " ) tmp";
			query += " where rnum > ? and rnum <= ?";
			
			int offset = (pageNum - 1) * countPerPage;
			int to = offset + countPerPage;
			
			
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, offset);
			pstmt.setInt(2, to);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				BoardVO boardVo = new BoardVO();
				boardVo.setBoardId(rs.getInt("board_id"));
				boardVo.setBoardParentno(rs.getInt("board_parentno"));
				boardVo.setBoardTitle(rs.getString("board_title"));
				boardVo.setBoardDesc(rs.getString("board_desc"));
				boardVo.setBoardTime(rs.getString("board_time"));
				boardVo.setUserId(rs.getString("user_id"));
				
				qnaList.add(boardVo);
			}
			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(con != null) con.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return qnaList;
	}
	
	// 게시판 입력, 등록
	public void qnaUpdate(BoardVO boardVo) {
		
		try {
			con = dataFactory.getConnection();
			String query = "";
			query += "insert into board_info (board_id, board_title, board_desc, board_parentno)";
			query += " values(board_info_seq.nextval, ?, ?, ?)";
			pstmt = con.prepareStatement(query);
			
			pstmt.setInt(1, boardVo.getBoardId());
			pstmt.setString(2, boardVo.getBoardTitle());
			pstmt.setString(3, boardVo.getBoardDesc());
			pstmt.setInt(4, boardVo.getBoardParentno());
			
			int result = pstmt.executeUpdate();
			System.out.println("새글등록 : result : " + result);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int qnaTotal() {
		
		int total = 0;
		
		try {
			con = dataFactory.getConnection();
			String query = "select count(*) as total from board_info";
			pstmt = con.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				total = rs.getInt("total");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return total;
	}
	
	
}
















