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
			Context envContext = (Context) ctx.lookup("java:comp/env");
			dataFactory = (DataSource) envContext.lookup("jdbc/oracle");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 홈페이지 접속하면 db를 확인 -> 최근날짜부터 오늘날짜까지 업데이트
	// db에 데이터가 없을시 작동 안하니 db초기화 먼저 진행
	// update 버튼 필요없이 자동 업데이트
	// 박정희
	public void updateToAuto() {

		String token = null;

		try {
			con = dataFactory.getConnection();
			String tmpQuery = "SELECT Max(korea_time) as korea_time FROM   korea_info";
			pstmt = con.prepareStatement(tmpQuery);
			pstmt.executeQuery();

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				token = rs.getString("korea_time");
			}

			if (pstmt != null) {
				pstmt.close();
			}
			if (con != null) {
				con.close();
			}
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (token == null) {
			System.out.println("db를 초기화 해 주세요");
		} else {

			System.out.println(token);
			int tmpDate = (Integer.parseInt(token.substring(8, 10)) + 1);
			System.out.println(Integer.parseInt(token.substring(8, 10)));
			System.out.println(tmpDate);
			String tmpDate2 = Integer.toString(tmpDate);
			System.out.println(tmpDate2);

			token = (token.substring(0, 8) + tmpDate2).replace("-", "");
			System.out.println(token);

			if (Integer.parseInt(token) <= Integer.parseInt(dateToStr)) {

				String url = "http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19SidoInfStateJson?serviceKey=sIYnCEa6cv0btJb%2Bc2pWvDc76iYuhohbaoz%2B9bwsx8R2C8sPhrIivNMS3HHDCkVoBKoCktxoml4HN%2Bih04AWPQ%3D%3D&pageNo=1&numOfRows=10&";
				url += "startCreateDt=" + token + "&endCreateDt=" + dateToStr;
				String result = getStringFromURL(url);

				try {
					List<KoreaVO> list = new ArrayList();

					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					StringReader sr = new StringReader(result);
					InputSource is = new InputSource(sr);

					Document doc = builder.parse(is);

					NodeList nodeList = doc.getElementsByTagName("item");
					for (int i = 0; i < nodeList.getLength(); i++) {
						Element el = (Element) nodeList.item(i);
						KoreaVO vo = new KoreaVO();

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
					for (int i = 0; i < list.size(); i++) {
						String query = "insert into korea_info (korea_id, korea_death, korea_local, korea_local_info, korea_time) values (korea_seq.nextval, ?,?,?,?)";
						System.out.println(query);

						KoreaVO list_i = list.get(i);

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

					if (pstmt != null) {
						pstmt.close();
					}
					if (con != null) {
						con.close();
					}
					if (sr != null) {
						sr.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	public void foreignUpdateToAuto() {

		String token = null;

		try {
			con = dataFactory.getConnection();
			String tmpQuery = "SELECT Max(foreign_time) as foreign_time FROM foreign_info";
			pstmt = con.prepareStatement(tmpQuery);
			pstmt.executeQuery();

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				token = rs.getString("foreign_time");
			}

			if (pstmt != null) {
				pstmt.close();
			}
			if (con != null) {
				con.close();
			}
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (token == null) {
			System.out.println("db를 초기화 해 주세요");
		} else {

			System.out.println(token);
			int tmpDate = (Integer.parseInt(token.substring(8, 10)) + 1);
			System.out.println(Integer.parseInt(token.substring(8, 10)));
			System.out.println(tmpDate);
			String tmpDate2 = Integer.toString(tmpDate);
			System.out.println(tmpDate2);

			token = (token.substring(0, 8) + tmpDate2).replace("-", "");
			System.out.println(token);

			if (Integer.parseInt(token) <= Integer.parseInt(dateToStr)) {

				String url = "http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19NatInfStateJson?serviceKey=sIYnCEa6cv0btJb%2Bc2pWvDc76iYuhohbaoz%2B9bwsx8R2C8sPhrIivNMS3HHDCkVoBKoCktxoml4HN%2Bih04AWPQ%3D%3D&pageNo=1&numOfRows=10&";
				url += "startCreateDt=" + token + "&endCreateDt=" + dateToStr;
				String result = getStringFromURL(url);

				try {
					List<ForeignVO> list = new ArrayList();

					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					StringReader sr = new StringReader(result);
					InputSource is = new InputSource(sr);

					Document doc = builder.parse(is);

					NodeList nodeList = doc.getElementsByTagName("item");
					con = dataFactory.getConnection();

					// 쿼리에 오늘 일일 확진자 수 뽑아 낼 수 있는 쿼리문 입력
					for (int i = 0; i < list.size(); i++) {
						String query = "insert into foreign_info (foreign_id, foreign_death, foreign_local_O, foreign_local_I, foreign_local_info, foreign_time) values (foreign_info_seq.nextval, ?,?,?,?,?)";
						System.out.println(query);

						ForeignVO list_i = list.get(i);

//						java.sql.Date date = (java.sql.Date) list_i.get(0);
						int deathCount = (int) list_i.getForeignDeath();
						System.out.println(deathCount);
						String localO = (String) list_i.getForeignLocalO();
						System.out.println(localO);
						String localI = (String) list_i.getForeignLocalI();
						System.out.println(localI);
						int localInfo = (int) list_i.getForeignLocalInfo();
						System.out.println(localInfo);
						String time = list_i.getForeignTime();

						pstmt = con.prepareStatement(query);
						pstmt.setInt(1, deathCount);
						pstmt.setString(2, localO);
						pstmt.setString(3, localI);
						pstmt.setInt(4, localInfo);
						pstmt.setString(5, time);

						pstmt.executeQuery();
					}

					if (pstmt != null) {
						pstmt.close();
					}
					if (con != null) {
						con.close();
					}
					if (sr != null) {
						sr.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	// 날짜를 입력받아 DB를 초기화 할때 사용되는 드롭문
	// 박정희
	public void dropTable() {
		try {
			con = dataFactory.getConnection();
			String query = "drop table korea_info";
			pstmt = con.prepareStatement(query);
			pstmt.executeQuery();

			if (pstmt != null) {
				pstmt.close();
			}
			if (con != null) {
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
			String query = "create table korea_info(korea_id number(10) primary key, korea_death number(10), korea_local varchar2(1000), korea_local_info number(10), korea_time date)";
			pstmt = con.prepareStatement(query);
			pstmt.executeQuery();

			if (pstmt != null) {
				pstmt.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 입력받은 날짜 부터 오늘 까지의 xml정보로 DB초기화
	// 박정희
	public void updateDBtoDate(String sDay) {

		String url = "http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19SidoInfStateJson?serviceKey=sIYnCEa6cv0btJb%2Bc2pWvDc76iYuhohbaoz%2B9bwsx8R2C8sPhrIivNMS3HHDCkVoBKoCktxoml4HN%2Bih04AWPQ%3D%3D&pageNo=1&numOfRows=10&";
		url += "startCreateDt=" + sDay + "&endCreateDt=" + dateToStr;
		String result = getStringFromURL(url);

		try {
			List<KoreaVO> list = new ArrayList();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			StringReader sr = new StringReader(result);
			InputSource is = new InputSource(sr);

			Document doc = builder.parse(is);

			NodeList nodeList = doc.getElementsByTagName("item");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element el = (Element) nodeList.item(i);
				KoreaVO vo = new KoreaVO();

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
			for (int i = 0; i < list.size(); i++) {
				String query = "insert into korea_info (korea_id, korea_death, korea_local, korea_local_info, korea_time) values (korea_info_seq.nextval, ?,?,?,?)";
				System.out.println(query);

				KoreaVO list_i = list.get(i);

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

			if (pstmt != null) {
				pstmt.close();
			}
			if (con != null) {
				con.close();
			}
			if (sr != null) {
				sr.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// foreign 테일블 드랍
	public void foreignDropTable() {
		System.out.println("foreign drop");
		try {
			con = dataFactory.getConnection();
			String query = "drop table foreign_info";
			pstmt = con.prepareStatement(query);
			pstmt.executeQuery();

			if (pstmt != null) {
				pstmt.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// foreign 테이블 생성
	public void foreignCreateTable() {
		System.out.println("foreign create");
		try {
			con = dataFactory.getConnection();
			String query = "create table foreign_info(foreign_id number(10) primary key, foreign_death number(10), foreign_local_o varchar2(1000), foreign_local_i varchar2(1000), foreign_local_info number(10), foreign_time date)";
			pstmt = con.prepareStatement(query);
			pstmt.executeQuery();

			if (pstmt != null) {
				pstmt.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 입력받은 날짜 부터 오늘 까지의 xml정보로 DB초기화
	public void foreignUpdateDBtoDate(String sDay) {
		System.out.println("foreign update");

		String url = "http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19NatInfStateJson?serviceKey=sIYnCEa6cv0btJb%2Bc2pWvDc76iYuhohbaoz%2B9bwsx8R2C8sPhrIivNMS3HHDCkVoBKoCktxoml4HN%2Bih04AWPQ%3D%3D&pageNo=1&numOfRows=10&";
		url += "startCreateDt=" + sDay + "&endCreateDt=" + dateToStr;
		String result = getStringFromURL(url);

		try {
			List<ForeignVO> list = new ArrayList();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			StringReader sr = new StringReader(result);
			InputSource is = new InputSource(sr);

			Document doc = builder.parse(is);

			NodeList nodeList = doc.getElementsByTagName("item");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element el = (Element) nodeList.item(i);
				ForeignVO vo = new ForeignVO();

				NodeList nodes = el.getChildNodes();

				Node node0 = nodes.item(0);
				Element childElement0 = (Element) node0;
				vo.setForeignLocalO(childElement0.getTextContent());
				System.out.println(childElement0.getTextContent());

				if (childElement0.getTextContent().equals("아시아")) {
					Node node7 = nodes.item(7);
					Element childElement7 = (Element) node7;
					vo.setForeignLocalI(childElement7.getTextContent());
					System.out.println(childElement7.getTextContent());

					Node node4 = nodes.item(4);
					Element childElement4 = (Element) node4;
					vo.setForeignDeath(Integer.parseInt(childElement4.getTextContent()));
					System.out.println(childElement4.getTextContent());

					Node node6 = nodes.item(6);
					Element childElement6 = (Element) node6;
					vo.setForeignLocalInfo(Integer.parseInt(childElement6.getTextContent()));
					System.out.println(childElement6.getTextContent());

					Node node3 = nodes.item(3);
					Element childElement3 = (Element) node3;
					String timeBefore = childElement3.getTextContent();
					System.out.println(timeBefore);
					String time = timeBefore.substring(2, 10).replace("-", "/");
					vo.setForeignTime(time);
					System.out.println(time);

//				Date type 삽입
//				Node node0 = nodes.item(0);
//				Element childElement0 = (Element) node0;
//				vo.setDate(childElement0.getTextContent());
					list.add(vo);
				}

			}

			con = dataFactory.getConnection();

			// 쿼리에 오늘 일일 확진자 수 뽑아 낼 수 있는 쿼리문 입력
			for (int i = 0; i < list.size(); i++) {
				String query = "insert into foreign_info (foreign_id, foreign_death, foreign_local_O, foreign_local_I, foreign_local_info, foreign_time) values (foreign_info_seq.nextval, ?,?,?,?,?)";
				System.out.println(query);

				ForeignVO list_i = list.get(i);

//				java.sql.Date date = (java.sql.Date) list_i.get(0);
				int deathCount = (int) list_i.getForeignDeath();
				System.out.println(deathCount);
				String localO = (String) list_i.getForeignLocalO();
				System.out.println(localO);
				String localI = (String) list_i.getForeignLocalI();
				System.out.println(localI);
				int localInfo = (int) list_i.getForeignLocalInfo();
				System.out.println(localInfo);
				String time = list_i.getForeignTime();

				pstmt = con.prepareStatement(query);
				pstmt.setInt(1, deathCount);
				pstmt.setString(2, localO);
				pstmt.setString(3, localI);
				pstmt.setInt(4, localInfo);
				pstmt.setString(5, time);

				pstmt.executeQuery();
			}

			if (pstmt != null) {
				pstmt.close();
			}
			if (con != null) {
				con.close();
			}
			if (sr != null) {
				sr.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 홈페이지에 표시되는 일일, 월별, 연별 확진자 수
	// 박정희
	public int todayConfirmedCase(int a) {
		int count = 0;
		try {
			con = dataFactory.getConnection();

			// 쿼리에 오늘 일일 확진자 수 뽑아 낼 수 있는 쿼리문 입력
			if (a == 1) {
				
				String token = "";
				
				String tmpQuery = "SELECT Max(korea_time) as korea_time FROM korea_info";
				pstmt = con.prepareStatement(tmpQuery);
				pstmt.executeQuery();

				ResultSet rs1 = pstmt.executeQuery();
				
				while(rs1.next()) {
					token = rs1.getString("korea_time");
				}if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
				
				if(token == null) {
					count = 0;
				}else {
					int tmpDate = (Integer.parseInt(token.substring(8, 10)));
					System.out.println(Integer.parseInt(token.substring(8, 10)));
					System.out.println(tmpDate);
					String tmpDate2 = Integer.toString(tmpDate);
					System.out.println(tmpDate2);

					token = (token.substring(0, 8) + tmpDate2).replace("-", "");
					System.out.println(token);
					
					con = dataFactory.getConnection();
					if(token.equals(dateToStr)) {
						System.out.println("오늘자");
						String query = "SELECT sum(distinct korea_local_info) as korea_local_info FROM   korea_info WHERE  korea_id = (SELECT Max(korea_id) FROM   korea_info WHERE  korea_time BETWEEN sysdate - ? AND sysdate AND korea_local = '합계')";
						System.out.println(query);

						pstmt = con.prepareStatement(query);
						pstmt.setInt(1, a);

						ResultSet rs = pstmt.executeQuery();

						while (rs.next()) {
							count = rs.getInt("korea_local_info");
						}
						if (rs != null) {
							rs.close();
						}
						if (pstmt != null) {
							pstmt.close();
						}
						if (con != null) {
							con.close();
						}
					}else {
						System.out.println("어제자");
						String query = "SELECT sum(distinct korea_local_info) as korea_local_info FROM   korea_info WHERE  korea_id = (SELECT Max(korea_id) FROM   korea_info WHERE  korea_time BETWEEN sysdate - 2 AND sysdate - 1 AND korea_local = '합계')";
						System.out.println(query);

						pstmt = con.prepareStatement(query);

						ResultSet rs = pstmt.executeQuery();

						while (rs.next()) {
							count = rs.getInt("korea_local_info");
						}
						if (rs != null) {
							rs.close();
						}
						if (pstmt != null) {
							pstmt.close();
						}
						if (con != null) {
							con.close();
						}
					}
				}
			} else {
				String query = "select sum(distinct korea_local_info) as korea_local_info from korea_info where korea_local = '합계' and korea_time BETWEEN sysdate - ? AND sysdate";
				System.out.println(query);

				pstmt = con.prepareStatement(query);
				pstmt.setInt(1, a);

				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) {
					count = rs.getInt("korea_local_info");
				}
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return count;
	}

	// 국가별 확진자 수
	public int foreignTodayConfirmedCase(String loc) {
		int count = 0;
		try {
			con = dataFactory.getConnection();
			String token = "";
			
			String tmpQuery = "SELECT Max(foreign_time) as foreign_time FROM foreign_info";
			pstmt = con.prepareStatement(tmpQuery);
			pstmt.executeQuery();

			ResultSet rs1 = pstmt.executeQuery();
			
			while(rs1.next()) {
				token = rs1.getString("foreign_time");
			}if (pstmt != null) {
				pstmt.close();
			}
			if (con != null) {
				con.close();
			}
			
			if(token == null) {
				count = 0;
			}else {
				int tmpDate = (Integer.parseInt(token.substring(8, 10)));
				System.out.println(Integer.parseInt(token.substring(8, 10)));
				System.out.println(tmpDate);
				String tmpDate2 = Integer.toString(tmpDate);
				System.out.println(tmpDate2);

				token = (token.substring(0, 8) + tmpDate2).replace("-", "");
				System.out.println(token);
				
				con = dataFactory.getConnection();
				if(token.equals(dateToStr)) {
					System.out.println("오늘자");
					
					// 쿼리에 오늘 일일 확진자 수 뽑아 낼 수 있는 쿼리문 입력
					String query = "select (select foreign_local_info from foreign_info where foreign_local_i = ? and foreign_time between sysdate - 1 and sysdate)-(select foreign_local_info from foreign_info where foreign_local_i = ? and foreign_time between sysdate - 2 and sysdate - 1) as foreign_local_info from dual";
					System.out.println(query);
					
					pstmt = con.prepareStatement(query);
					pstmt.setString(1, loc);
					pstmt.setString(2, loc);
					
					ResultSet rs = pstmt.executeQuery();
					
					while (rs.next()) {
						count = rs.getInt("foreign_local_info");
					}
					if (rs != null) {
						rs.close();
					}
					if (pstmt != null) {
						pstmt.close();
					}
					if (con != null) {
						con.close();
					}
				}else {
					System.out.println("어제자");
					
					// 쿼리에 오늘 일일 확진자 수 뽑아 낼 수 있는 쿼리문 입력
					String query = "select (select foreign_local_info from foreign_info where foreign_local_i = ? and foreign_time between sysdate - 2 and sysdate - 1)-(select foreign_local_info from foreign_info where foreign_local_i = ? and foreign_time between sysdate - 3 and sysdate - 2) as foreign_local_info from dual";
					System.out.println(query);
					
					pstmt = con.prepareStatement(query);
					pstmt.setString(1, loc);
					pstmt.setString(2, loc);
					
					ResultSet rs = pstmt.executeQuery();
					
					while (rs.next()) {
						count = rs.getInt("foreign_local_info");
					}
					if (rs != null) {
						rs.close();
					}
					if (pstmt != null) {
						pstmt.close();
					}
					if (con != null) {
						con.close();
					}
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return count;
	}
	
	public int foreignTodayDeathCase(String loc) {
		int count = 0;
		try {
			con = dataFactory.getConnection();
			
			// 쿼리에 오늘 일일 확진자 수 뽑아 낼 수 있는 쿼리문 입력
			String query = "select (select foreign_death from foreign_info where foreign_local_i = ? and foreign_time between sysdate - 1 and sysdate)-(select foreign_death from foreign_info where foreign_local_i = ? and foreign_time between sysdate - 2 and sysdate - 1) as foreign_death from dual";
			System.out.println(query);
			
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, loc);
			pstmt.setString(2, loc);
			
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				count = rs.getInt("foreign_death");
			}
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (con != null) {
				con.close();
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
	}

	// 일일 사망자
	// 박정희
	public int todayDeath() {
		int count = 0;
		try {
			con = dataFactory.getConnection();

			// 쿼리에 오늘 일일 확진자 수 뽑아 낼 수 있는 쿼리문 입력
			String query = "select (select korea_death from korea_info where korea_time between sysdate-1 and sysdate and korea_local = '합계')-(select korea_death from korea_info where korea_time between sysdate - 2 and sysdate - 1 and korea_local = '합계') as korea_death from dual";
			System.out.println(query);

			pstmt = con.prepareStatement(query);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				count = rs.getInt("korea_death");
			}
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (con != null) {
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
	public int todayDeath(String loc) {
		int count = 0;
		try {
			con = dataFactory.getConnection();

			// 쿼리에 오늘 일일 확진자 수 뽑아 낼 수 있는 쿼리문 입력
			String query = "select (select korea_death from korea_info where korea_time between sysdate-1 and sysdate and korea_local = ?)-(select korea_death from korea_info where korea_time between sysdate - 2 and sysdate - 1 and korea_local = ?) as korea_death from dual";
			System.out.println(query);

			pstmt = con.prepareStatement(query);
			pstmt.setString(1, loc);
			pstmt.setString(2, loc);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				count = rs.getInt("korea_death");
			}
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (con != null) {
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
	public int koreaLocConfirmedCase(String loc) {
		int count = 0;

		try {
			con = dataFactory.getConnection();

			String query = "SELECT korea_local_info FROM   korea_info WHERE  korea_id = (SELECT Max(korea_id) FROM   korea_info WHERE  korea_time BETWEEN sysdate - 1 AND sysdate AND korea_local = ?)";
			System.out.println(query);

			pstmt = con.prepareStatement(query);
			pstmt.setString(1, loc);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				count = rs.getInt("korea_local_info");
			}
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (con != null) {
				con.close();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return count;
	}

	// 해외 지역을 받아서 DB 접속 -> 그 지역의 확진자 수를 표시한다
	public int ForeignLocConfirmedCase(String loc) {
		int count = 0;

		try {
			con = dataFactory.getConnection();

			String query = "select foreign_local_info from foreign_info where foreign_local = ? and foreign_time > sysdate - 1";
			System.out.println(query);

			pstmt = con.prepareStatement(query);
			pstmt.setString(1, loc);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				count = rs.getInt("foreign_local_info");
			}
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (con != null) {
				con.close();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return count;
	}

	// 로그인 시 id pwd 확인 - 남모세
	public UserVO loginCheck(String id, String pwd) {

		UserVO vo = new UserVO();

		try {
			con = dataFactory.getConnection();
			
			String query = "";
			query += " SELECT * ";
			query += " FROM user_info ";
			query += " WHERE user_id = ? ";
			query += " AND user_password = ? ";

			pstmt = con.prepareStatement(query);

			pstmt.setString(1, id);
			pstmt.setString(2, pwd);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				vo.setUserId(rs.getString("user_id"));
				vo.setUserPassword(rs.getString("user_password"));
				vo.setUserName(rs.getString("user_name"));
				vo.setUserGender(rs.getString("user_gender"));
				vo.setUserEmail(rs.getString("user_email"));
				vo.setUserAuth(rs.getString("user_auth"));

			}
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (con != null) {
				con.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return vo;
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
				if (line != null) {
					result.append(line);
				}
			} while (line != null);

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
	public List<BoardVO> qnaSelect(int pageNum, int countPerPage){
		List<BoardVO> qnaList = new ArrayList();
		
		try {
			con = dataFactory.getConnection();
			
			String query = "";
			query += "select * from (";
			query += " select rownum as rnum, level, board_id, board_parentno, board_title, board_desc, board_time, u.user_id";
			query += " from board_info b, user_info u";
			query += " where b.user_id = u.user_id";
			query += " start with board_parentno = 0";
			query += " connect by prior board_id = board_parentno";
			query += " order siblings by board_id desc";
			query += " ) tmp";
			query += " where rnum > ? and rnum <= ?";
			
			int offset = (pageNum - 1) * countPerPage;
			int to = offset + countPerPage;
			
			System.out.println(offset);
			System.out.println(to);
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, offset);
			pstmt.setInt(2, to);
			ResultSet rs = pstmt.executeQuery();
			System.out.println(query);
			
			while(rs.next()) {
				BoardVO boardVo = new BoardVO();
				boardVo.setLevel(rs.getInt("level"));
				boardVo.setBoardId(rs.getInt("board_id"));
				boardVo.setBoardParentno(rs.getInt("board_parentno"));
				boardVo.setBoardTitle(rs.getString("board_title"));
				boardVo.setBoardDesc(rs.getString("board_desc"));
				boardVo.setBoardTime(rs.getDate("board_time"));
				boardVo.setUserId(rs.getString("user_id"));
				
				System.out.println("level : " + boardVo.getLevel());
				
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
	
	// 댓글 조회
	public List<CommentVO> commentSelect(int pageNum, int countPerPage){
		List<CommentVO> commentList = new ArrayList();
		
		try {
			con = dataFactory.getConnection();
			
			String query = "";
			query += "select * from (";
			query += " select rownum as rnum, level, comment_id, comment_parentno, comment_desc, comment_time, u.user_id";
			query += " from comment_info b, user_info u";
			query += " where b.user_id = u.user_id";
			query += " start with comment_parentno = 0";
			query += " connect by prior comment_id = comment_parentno";
			query += " order siblings by comment_id desc";
			query += " ) tmp";
			query += " where rnum > ? and rnum <= ?";
			
			int offset = (pageNum - 1) * countPerPage;
			int to = offset + countPerPage;
			
			
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, offset);
			pstmt.setInt(2, to);
			ResultSet rs = pstmt.executeQuery();
			System.out.println(query);
			
			while(rs.next()) {
				CommentVO vo = new CommentVO();
				vo.setLevel(rs.getInt("level"));
				vo.setCommentId(rs.getInt("comment_id"));
				vo.setCommentParentno(rs.getInt("comment_parentno"));
				vo.setCommentDesc(rs.getString("comment_desc"));
				vo.setCommentTime(rs.getDate("comment_time"));
				vo.setUserId(rs.getString("user_id"));
				
				commentList.add(vo);
			}
			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(con != null) con.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return commentList;
	}
	
	// 게시판 입력, 등록
	public void qnaInsert(BoardVO boardVo) {
		
		try {
			con = dataFactory.getConnection();
			String query = "";
			query += "insert into board_info (board_id, user_id, board_parentno, board_title, board_desc, board_time)";
			query += " values(board_info_seq.nextval, ?, ?, ?, ?, sysdate)";
			pstmt = con.prepareStatement(query);
			
			pstmt.setString(1, boardVo.getUserId());
			pstmt.setInt(2, boardVo.getBoardParentno());
			pstmt.setString(3, boardVo.getBoardTitle());
			pstmt.setString(4, boardVo.getBoardDesc());
			
			int result = pstmt.executeUpdate();
			System.out.println("새글등록 : result : " + result);
			
			if(pstmt != null) pstmt.close();
			if(con != null) con.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 댓글 입력, 등록
	public void insertComment(CommentVO vo) {
		
		try {
			con = dataFactory.getConnection();
			String query = "";
			query += "insert into comment_info (comment_id, board_id, user_id, comment_desc, comment_time, comment_parentno)";
			query += " values(comment_info_seq.nextval, ?, ?, ?, sysdate, ?)";
			pstmt = con.prepareStatement(query);
			
			pstmt.setInt(1, vo.getBoardId());
			pstmt.setString(2, vo.getUserId());
			pstmt.setString(3, vo.getCommentDesc());
			pstmt.setInt(4, vo.getCommentParentno());
			
			int result = pstmt.executeUpdate();
			System.out.println("새글등록 : result : " + result);
			
			if(pstmt != null) pstmt.close();
			if(con != null) con.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 게시판 상세조회
	public BoardVO qnaView(int boardId) {
		BoardVO boardVo = new BoardVO();
		
		try {
			con = dataFactory.getConnection();
			String query = "select b.*, u.user_id";
			query += " from board_info b, user_info u";
			query += " where b.board_id = ? and b.user_id = u.user_id";
			
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, boardId);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			
			boardVo.setBoardId(rs.getInt("board_id"));
			boardVo.setUserId(rs.getString("user_id"));
			boardVo.setBoardParentno(rs.getInt("board_parentno"));
			boardVo.setBoardTitle(rs.getString("board_title"));
			boardVo.setBoardDesc(rs.getString("board_desc"));
			boardVo.setBoardTime(rs.getDate("board_time"));	
			
			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(con != null) con.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return boardVo;
	}
	
	// 게시판 수정
	public void qnaUpdate(BoardVO boardVo) {
		
		try {
			con = dataFactory.getConnection();
			
			String query = "update board_info set";
			query += " board_title = ?, board_desc = ?";
			query += " where board_id = ?";
			
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, boardVo.getBoardTitle());
			pstmt.setString(2, boardVo.getBoardDesc());
			pstmt.setInt(3, boardVo.getBoardId());
			pstmt.executeUpdate();
			
			if(pstmt != null) pstmt.close();
			if(con != null) con.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 게시판 삭제
	public void qnaDelete(int boardId) {

		try {
			con = dataFactory.getConnection();
			
			String query = "delete from board_info";
			query += " where board_id = ?";
			
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, boardId);
			pstmt.executeUpdate();
			
			if(pstmt != null) pstmt.close();
			if(con != null) con.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 게시판 글 보여주는 제한걸기
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
			
			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(con != null) con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return total;
	}
	
	
	// 댓글 조회
	public List<CommentVO> commentSelect(int boardId){
		List<CommentVO> commentList = new ArrayList();
		CommentVO commentVo = new CommentVO();
		
		try {
			con = dataFactory.getConnection();
			
			String query = "select * from ( select level, comment_id, comment_parentno, comment_desc, comment_time, u.user_id from comment_info b, user_info u where board_id = ? start with comment_parentno = 0 connect by prior comment_id = comment_parentno order siblings by comment_id desc) tmp";
			
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, boardId);
			ResultSet rs = pstmt.executeQuery();
			System.out.println(query);
			
			while(rs.next()) {
				commentVo = new CommentVO();
				commentVo.setLevel(rs.getInt("level"));
				commentVo.setCommentId(rs.getInt("comment_id"));
				commentVo.setBoardId(boardId);
				commentVo.setCommentParentno(rs.getInt("comment_parentno"));
				commentVo.setCommentDesc(rs.getString("comment_desc"));
				commentVo.setCommentTime(rs.getDate("comment_time"));
				commentVo.setUserId(rs.getString("user_id"));
				
				commentList.add(commentVo);
			}
			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(con != null) con.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return commentList;
	}
	
	// 댓글 입력, 등록
	public void commentInsert(CommentVO commentVo) {
		
		try {
			con = dataFactory.getConnection();
			String query = "";
			query += "insert into comment_info (comment_id, board_id, user_id, comment_parentno, comment_desc, comment_time)";
			query += " values(comment_info_seq.nextval, ?, ?, ?, ?, sysdate)";
			pstmt = con.prepareStatement(query);
			
			pstmt.setInt(1, commentVo.getBoardId());
			pstmt.setString(2, commentVo.getUserId());
			pstmt.setInt(3, commentVo.getCommentParentno());
			pstmt.setString(4, commentVo.getCommentDesc());
			
			int result = pstmt.executeUpdate();
			System.out.println("새댓글등록 : result : " + result);
			
			if(pstmt != null) pstmt.close();
			if(con != null) con.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 댓글 수정
	public void commentUpdate(CommentVO commentVo) {
		
		try {
			con = dataFactory.getConnection();
			
			String query = "update comment_info set";
			query += " comment_desc = ?";
			query += " where comment_id = ?";
			
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, commentVo.getCommentDesc());
			pstmt.setInt(2, commentVo.getCommentId());
			pstmt.executeUpdate();
			
			if(pstmt != null) pstmt.close();
			if(con != null) con.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 댓글 삭제
	public void commentDelete(int commentId) {

		try {
			con = dataFactory.getConnection();
			
			String query = "delete from comment_info";
			query += " where board_id = ?";
			
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, commentId);
			pstmt.executeUpdate();
			
			if(pstmt != null) pstmt.close();
			if(con != null) con.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	// 회원가입 시 DB에 회원정보 넣는 메소드 - 남모세(Service의 signUpSuccess에서 호출함)
	public void signUp(UserVO vo) {

		try {
			con = dataFactory.getConnection();

			String query = " INSERT INTO user_info (user_id, user_password, user_name, user_gender, user_email, user_auth)";
			query += " VALUES (?, ?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, vo.getUserId());
			pstmt.setString(2, vo.getUserPassword());
			pstmt.setString(3, vo.getUserName());
			pstmt.setString(4, vo.getUserGender());
			pstmt.setString(5, vo.getUserEmail());
			pstmt.setString(6, vo.getUserAuth());

			int result = pstmt.executeUpdate();
			System.out.println("signUp 성공: " + result);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {

				if (con != null)
					con.close();
				if (pstmt != null)
					pstmt.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

	}

	// 회원가입 시 중복되는 id가 있는지 확인 - 남모세
	public UserVO signUpCheck(String id) {

		System.out.println("Dao => String id: " + id);
		UserVO vo = new UserVO();

		try {
			con = dataFactory.getConnection();

			String query = "";
			query += " SELECT user_id ";
			query += " FROM user_info ";
			query += " WHERE user_id = ? ";
			System.out.println(query);

			pstmt = con.prepareStatement(query);
			pstmt.setString(1, id);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {

				String userId = rs.getString("user_id");

				vo.setUserId(userId);
				System.out.println("Dao => vo.getUserId: " + vo.getUserId());
			}
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (con != null) {
				con.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return vo;
	}

	public UserVO loginCheck(String id) {
		// Service에서 호출하는 loginCheck메소드 - 남모세
		UserVO vo = new UserVO();

		try {
			con = dataFactory.getConnection();

			String query = "";
			query += " SELECT * ";
			query += " FROM user_info ";
			query += " WHERE user_id = ? ";
			System.out.println(query);

			pstmt = con.prepareStatement(query);
			pstmt.setString(1, id);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {

				String userId = rs.getString("user_id");
				String userPassword = rs.getString("user_password");
				String userName = rs.getString("user_name");
				String userGender = rs.getString("user_gender");
				String userEmail = rs.getString("user_email");
				String userAuth = rs.getString("user_auth");

				vo.setUserId(userId);
				vo.setUserPassword(userPassword);
				vo.setUserName(userName);
				vo.setUserGender(userGender);
				vo.setUserEmail(userEmail);
				vo.setUserAuth(userAuth);

			}
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (con != null) {
				con.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return vo;
	}

}
