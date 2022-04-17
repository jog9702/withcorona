package CovidProcess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import BoardVo.ClinicVo;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vo.*;

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
				
				int todayConfirmedCase = covidService.todayConfirmedCase();
				int monthConfirmedCase = covidService.monthConfirmedCase();
				int yearConfirmedCase = covidService.yearConfirmedCase();
				session.setAttribute("todayCount", todayConfirmedCase);
				session.setAttribute("monthCount", monthConfirmedCase);
				session.setAttribute("yearCount", yearConfirmedCase);
				
				nextPage = "/homepage.jsp";
				
			} else if(action.equals("/covidKorea")){
				
				nextPage = "/covidKorea.jsp";
				
			}else if(action.equals("/koreaSelection")){
				
				String loc = request.getParameter("loc");
				int koreaLocCount = covidService.koreaLocConfirmedCase(loc);
				request.setAttribute("koreaLocCount", koreaLocCount);
				
				switch(loc) {
				case "seoul":
					request.setAttribute("loc", "서울");
					break;
				case "gyeonggi":
					request.setAttribute("loc", "경기도");
					break;
				case "gangwon":
					request.setAttribute("loc", "강원도");
					break;
				case "chungcheongN":
					request.setAttribute("loc", "충청북도");
					break;
				case "chungcheongS":
					request.setAttribute("loc", "충청남도");
					break;
				case "jeollaN":
					request.setAttribute("loc", "전라북도");
					break;
				case "jeollaS":
					request.setAttribute("loc", "전라남도");
					break;
				case "gyeongsangN":
					request.setAttribute("loc", "경상북도");
					break;
				case "gyeongsangS":
					request.setAttribute("loc", "경상남도");
					break;
			}
				nextPage = "/covidKorea.jsp";
				
			}else if(action.equals("/covidForeign")){
				
				nextPage = "/covidForeign.jsp";
				
			}else if(action.equals("/foreignSelection")){
				
				String loc = request.getParameter("loc");
				int foreignLocCount = covidService.ForeignLocConfirmedCase(loc);
				request.setAttribute("foreignLocCount", foreignLocCount);
				request.setAttribute("loc", loc);
				
				nextPage = "/covidForeign.jsp";
				
			}else if (action.equals("/search")) {
				
				
				List<ClinicVo> clinicInfo = covidService.getClinicInfo();
				
				request.setAttribute("clinicInfo", clinicInfo);
				
				nextPage = "/clinic.jsp";
				
				
			}else if(action.equals("/selectClinic")){
				
				String str = (String) request.getParameter("loc");
				List<ClinicVo> searchClinicInfo = covidService.getSearchClinicInfo(str);
				request.setAttribute("searchClinicInfo", searchClinicInfo);
				
				
				nextPage = "/clinic.jsp";
				
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
				
				
			}else if(action.equals("/update")){
				covidService.updateConfirmedCase();
				
				
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
				
				
				
			}else {	
				nextPage = "/deny.jsp";
			}
			
			RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
			dispatch.forward(request, response);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	

}
