package CovidProcess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vo.BoardVO;
import vo.ClinicVO;

/**
 * Servlet implementation class potController
 */
@WebServlet("/withcorona/*")
public class CovidController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}
		
	protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nextPage = "";
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		String action = request.getPathInfo();
		
		HttpSession session = request.getSession();
		
		CovidService covidService = new CovidService();
//		BoardVO	boardVO = new BoardVO();
		

		
		try {
			if (action.equals("/covidHomepage")) {
				
				covidService.updateToAuto();
				
				DecimalFormat fmt = new DecimalFormat("###,###");
				
				String todayConfirmedCase = fmt.format(covidService.todayConfirmedCase());
				String monthConfirmedCase = fmt.format(covidService.monthConfirmedCase());
				String yearConfirmedCase = fmt.format(covidService.yearConfirmedCase());
				String todayDeath = fmt.format(covidService.todayDeath());
				
				session.setAttribute("todayCount", todayConfirmedCase);
				session.setAttribute("monthCount", monthConfirmedCase);
				session.setAttribute("yearCount", yearConfirmedCase);
				session.setAttribute("todayDeath", todayDeath);
				
				nextPage = "/homepage.jsp";
				
				nextPage = "/homepage.jsp";
			} else if(action.equals("/covidKorea")){
				
				nextPage = "/covidKorea.jsp";
				
			}else if(action.equals("/koreaSelection")){
				
				String loc = request.getParameter("loc");
				int koreaLocCount = covidService.koreaLocConfirmedCase(loc);
				request.setAttribute("koreaLocCount", koreaLocCount);
				request.setAttribute("loc", loc);
				
				int locDeath = covidService.todayDeath(loc);
				request.setAttribute("locDeath", locDeath);
				
				nextPage = "/covidKorea.jsp";
				
			}else if(action.equals("/covidForeign")){
				
				covidService.foreignUpdateToAuto();
				
				nextPage = "/covidForeign.jsp";
				
			}else if(action.equals("/foreignSelection")){
				
				String loc = request.getParameter("loc");
				int foreignLocCount = covidService.ForeignLocConfirmedCase(loc);
				request.setAttribute("foreignLocCount", foreignLocCount);
				request.setAttribute("loc", loc);
				
				nextPage = "/covidForeign.jsp";

			}else if (action.equals("/search")) {
				//보건소 정보 검색하는 페이지로 이동 - 남모세
				nextPage = "/clinicSearch.jsp";
				
			}else if(action.equals("/selectClinic")){
				//검색한 보건소 정보를 json에서 바로 표시해주는 페이지 - 남모세
				
				String loc = request.getParameter("loc");
				
				String url = "http://api.odcloud.kr/api/3072692/v1/uddi:9d420e87-8e70-4fb0-a54a-be1244249b2e_201909271427?page=1&perPage=3564&serviceKey=sI726jPqcVCuRbjGbsSgId%2BsznYVz20Kk7JJ0RJ7R09QQlrhYyYeeRWxOYXQqeWZXt2jQggrOrj5K2JytdxpsQ%3D%3D";
				String result = getStringFromURL(url);
				//lib에 json-simple.jar파일 넣어야함 - 남모세
				JSONParser jsonParser = new JSONParser();
				List<ClinicVO> list = new ArrayList();

				try {
					Object obj = jsonParser.parse(result);

					if(obj instanceof JSONObject) {
						JSONObject json = new JSONObject();
						json = (JSONObject) obj;
						
						JSONArray json_arr = (JSONArray) json.get("data");
						
						for(int i=0; i<json_arr.size(); i++) {
							JSONObject item = (JSONObject) json_arr.get(i);
							String local = (String) item.get("시도");
							String name = (String) item.get("보건기관명");
							String info = (String) item.get("주소");
							String tel = (String) item.get("대표전화번호");
							
							if(info.contains(loc)) {
								ClinicVO vo = new ClinicVO();
								
								vo.setClinicLocal(local);
								vo.setClinicName(name);
								vo.setClinicInfo(info);
								vo.setClinicTel(tel);
								list.add(vo);
							}
						}
						
					} else if(obj instanceof JSONArray) {
						
					}
					
				} catch(Exception e) {
					e.printStackTrace();
				}
				
				request.setAttribute("list", list);
				nextPage = "/clinicInfo.jsp";
				
			}else if(action.equals("/login")) {
				
				nextPage="/login.jsp";
				
			}else if(action.equals("/loginCheck")) {
				
				String auth = covidService.loginCheck((String)request.getParameter("id"), (String)request.getParameter("password"));
				
				if(auth == null) {
					nextPage="/login.jsp";
				}else {
					session.setAttribute("auth", auth);
					nextPage="/qna.jsp";
				}
			
				// 게시판 조회
			}else if(action.equals("/qna")) {
				List<BoardVO> qnaList = new ArrayList<BoardVO>();
				
				int pageNum = 1;
				int countPerPage = 5;
				String strPageNum = request.getParameter("pageNum");
				String strCountPerPage = request.getParameter("countPerPage");
				
				if(strPageNum != null) {
					pageNum = Integer.parseInt(strPageNum);
				}
				if(strCountPerPage != null) {
					countPerPage = Integer.parseInt(strCountPerPage);
				}
				
				qnaList = covidService.qnaList(pageNum, countPerPage);
				request.setAttribute("qnaList", qnaList);
				
				
				int total = covidService.qnaTotal();
				request.setAttribute("total", total);
				request.setAttribute("pageNum", pageNum);
				request.setAttribute("countPerPage", countPerPage);
				
				nextPage = "/qna.jsp";
				
				
				// 게시판 등록창이동
			}else if(action.equals("/qnaForm.do")){
				
				nextPage = "/withcorona/qnaForm.jsp";
				
				// 게시판 등록
//			}else if(action.equals("/qnaUpdate")){
//				
//				BoardVO	boardVo = new BoardVO();
//				String title = request.getParameter("title");
//				String desc = request.getParameter("desc");
//				
//				Integer id = (Integer) session.getAttribute("empno");
//				if(id == null) id = 7839;
//						
//				boardVo = new BoardVO();
//				boardVo.setBoardParentno(0);
//				boardVo.setBoardId(id);
//				boardVo.setBoardTitle(title);
//				boardVo.setBoardDesc(desc);
//				
//				covidService.qnaUpdate(boardVo);
//				
//				nextPage = "/board/listArticles.do";
				
				
				
			}else if(action.equals("/updateToDate")){
				
				covidService.updateDBtoDate(request.getParameter("before"));
				nextPage="/covidKorea.jsp";
				
			}else if(action.equals("/fUpdate")){
				//해결
				covidService.foreignUpdateDBtoDate(request.getParameter("before2"));
				System.out.println("123");
				nextPage="/covidForeign.jsp";
				
			}else {	
				nextPage = "/deny.jsp";
			}
			
			RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
			dispatch.forward(request, response);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	//보건소 정보를 가져오기위한 메소드 - 남모세
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
			
			} catch (Exception e) {
				
			e.printStackTrace();
		}
		return result.toString();
	}

}
