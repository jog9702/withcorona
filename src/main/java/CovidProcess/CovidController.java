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

import vo.*;

@WebServlet("/withcorona/*")
public class CovidController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

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
				
				session.setAttribute("action", action);

				nextPage = "/homepage.jsp";
				
			} else if(action.equals("/covidKorea")){
				
				covidService.updateToAuto();
				
				session.setAttribute("action", action);

				nextPage = "/covidKorea.jsp";
				
			}else if(action.equals("/koreaSelection")){
				
				String loc = request.getParameter("loc");
				int koreaLocCount = covidService.koreaLocConfirmedCase(loc);
				request.setAttribute("koreaLocCount", koreaLocCount);
				request.setAttribute("loc", loc);
				
				int locDeath = covidService.todayDeath(loc);
				request.setAttribute("locDeath", locDeath);
				
				session.setAttribute("action", action);

				nextPage = "/covidKorea.jsp";
				
			}else if(action.equals("/covidForeign")){
				covidService.foreignUpdateToAuto();
				
				DecimalFormat fmt = new DecimalFormat("###,###");
				
				request.setAttribute("kor", fmt.format(covidService.foreignTodayConfirmedCase("한국")));
				request.setAttribute("chi", fmt.format(covidService.foreignTodayConfirmedCase("중국")));
				request.setAttribute("jap", fmt.format(covidService.foreignTodayConfirmedCase("일본")));
				
				session.setAttribute("action", action);

				nextPage = "/covidForeign.jsp";
				
			}else if(action.equals("/foreignSelection")){ 
				DecimalFormat fmt = new DecimalFormat("###,###");
				
				request.setAttribute("kor", fmt.format(covidService.foreignTodayConfirmedCase("한국")));
				request.setAttribute("chi", fmt.format(covidService.foreignTodayConfirmedCase("중국")));
				request.setAttribute("jap", fmt.format(covidService.foreignTodayConfirmedCase("일본")));
				
				String loc = request.getParameter("loc");
				request.setAttribute("pick", fmt.format(covidService.foreignTodayConfirmedCase(loc)));
				request.setAttribute("pickD", fmt.format(covidService.foreignTodayDeathCase(loc)));
				request.setAttribute("to", "to");
				
				session.setAttribute("action", action);

				nextPage = "/covidForeign.jsp";
				
			}else if(action.equals("/updateToDate")){
				
				covidService.updateDBtoDate(request.getParameter("before"));
				nextPage="/covidKorea.jsp";
				
			}else if(action.equals("/fUpdate")){
				//해결
				covidService.foreignUpdateDBtoDate(request.getParameter("before2"));
				System.out.println("123");
				nextPage="/covidForeign.jsp";

			}else if (action.equals("/search")) {
				//보건소 정보 검색하는 페이지로 이동 - 남모세
				session.setAttribute("action", action);
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
				//로그인 페이지로 이동 - 남모세
				nextPage="/login.jsp";
				
			}else if(action.equals("/logout")) {
				//로그아웃 하기 - 남모세
				session.invalidate();
				
				nextPage="/withcorona/covidHomepage";
				
			}else if(action.equals("/loginCheck")) {
				//로그인 시 가입된 id인지 확인 하는 Controller - 남모세
				//integer.parseInt 때문에 NumberFormatException 날까봐 try catch로 묶음 - 남모세
				try {
					
					String id = request.getParameter("id");
					String pwd = request.getParameter("pwd");
					System.out.println("Controller String id: " + id);
					
					UserVO vo = new UserVO();
					vo = covidService.loginCheck(id, pwd);
					if(vo.getUserAuth() != null) {
						request.setAttribute("msg", "로그인 성공");
						request.getSession().setAttribute("userId", vo.getUserId());
//						request.getSession().setAttribute("userPassword", vo.getUserPassword());
//						request.getSession().setAttribute("userName", vo.getUserName());
//						request.getSession().setAttribute("userGender", vo.getUserGender());
//						request.getSession().setAttribute("userEmail", vo.getUserEmail());
//						request.getSession().setAttribute("userAuth", vo.getUserAuth());
						request.getSession().setAttribute("vo", vo);
						
					} else {
						request.setAttribute("msg", "로그인 실패");
					}
					
					nextPage="/loginResult.jsp";
				
				} catch(Exception e) {
					e.printStackTrace();
				}
				
			}else if(action.equals("/signUp")) {
				//회원가입 페이지로 이동 - 남모세
				
				nextPage="/signUp.jsp";

			}else if(action.equals("/signUpResult")) {
				//회원가입 페이지에서 입력한 값들은 받아서 UserVO에 넣는 Controller - 남모세

				//integer.parseInt 때문에 NumberFormatException 날까봐 try catch로 묶음 - 남모세
				String id = request.getParameter("id");
				System.out.println("con => id: " + id);
				if(covidService.signUpCheck(id)) {
					
					try {
						id = request.getParameter("id");
						String pwd = request.getParameter("pwd");
						String name = request.getParameter("name");
						String gender = request.getParameter("gender");
						String email = request.getParameter("email");
						String auth = "0";
						
						UserVO vo = new UserVO();
						vo.setUserId(id);
						vo.setUserPassword(pwd);
						vo.setUserName(name);
						vo.setUserGender(gender);
						vo.setUserEmail(email);
						vo.setUserAuth(auth);
						
						covidService.signUpSuccess(vo);
						
						request.setAttribute("signUpMsg", "회원가입 성공");

						nextPage="/signUpResult.jsp";

					} catch(Exception e) {
						e.printStackTrace();
					}
					
				} else {
					request.setAttribute("signUpMsg", "회원가입 실패");

					nextPage="/signUpResult.jsp";

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
				
				session.setAttribute("action", action);
				
				nextPage = "/qna.jsp";
				
				
				// 게시판 등록창이동
			}else if(action.equals("/qnaForm")){
				
				nextPage = "/qnaForm.jsp";
				
				// 게시판 등록
			}else if(action.equals("/qnaInsert")){
				
				BoardVO	boardVo = new BoardVO();
				UserVO userVo = new UserVO();
				
				String title = request.getParameter("title");
				String desc = request.getParameter("desc");
				String userId = (String) session.getAttribute("userId");		
				
				boardVo = new BoardVO();
				boardVo.setBoardParentno(0);
				boardVo.setUserId(userId);
				boardVo.setBoardTitle(title);
				boardVo.setBoardDesc(desc);
				
				covidService.qnaInsert(boardVo);
				
				nextPage = "";
				response.sendRedirect("/withcorona/qna");					
	
				// 게시판 상세 조회
			}else if(action.equals("/qnaView")){
				BoardVO	boardVo = new BoardVO();
				CommentVO commentVo = new CommentVO();
				String boardId = request.getParameter("boardId");
				
				boardVo = covidService.qnaView(Integer.parseInt(boardId));
				
				// 텍스트 에어리어 줄넘김
				String desc = boardVo.getBoardDesc();
				if(desc != null && desc.length() > 0) {
					desc = desc.replaceAll("\n", "<br>");
					boardVo.setBoardDesc(desc);					
				}
				
				List<CommentVO> commenetList = new ArrayList<CommentVO>();
				
				commenetList = covidService.commentList(Integer.parseInt(boardId));
				
				request.setAttribute("commenetList", commenetList);
				
				request.setAttribute("qna", boardVo);
				request.setAttribute("comment", commentVo);
				
				session.setAttribute("action", action);
				
				nextPage = "/qnaView.jsp";
				
				// 게시판 수정페이지 이동
			}else if(action.equals("/qnaUpdate")){
				BoardVO	boardVo = new BoardVO();
				String boardId = request.getParameter("boardId");
				boardVo = covidService.qnaView(Integer.parseInt(boardId));
				request.setAttribute("qna", boardVo);
				
				nextPage = "/qnaUpdate.jsp";
				
				// 게시판 수정
			}else if(action.equals("/qnaUpdateResult")){
				BoardVO	boardVo = new BoardVO();
				String boardId = request.getParameter("boardId");
				String title = request.getParameter("title");
				String desc = request.getParameter("desc");
				
				boardVo.setBoardId(Integer.parseInt(boardId));
				boardVo.setBoardTitle(title);
				boardVo.setBoardDesc(desc);
				
				covidService.qnaUpdate(boardVo);
				
				nextPage = "";
				response.sendRedirect("/withcorona/qnaView?boardId=" + boardId);
				
				// 게시판 삭제
			}else if(action.equals("/qnaDelete")){
				
				String boardId = request.getParameter("boardId");
				covidService.qnaDelete(Integer.parseInt(boardId));
				
				nextPage = "";
				response.sendRedirect("/withcorona/qna");
				
				// 게시판 답글 페이지 이동
			}else if(action.equals("/qnaReply")){
				
				String boardParentno = request.getParameter("boardParentno");
				request.setAttribute("boardParentno", boardParentno);
				
				nextPage = "/qnaReply.jsp";	
				
				// 게시판 답글 
			}else if(action.equals("/qnaReplyResult")){
				
				String boardParentno = request.getParameter("boardParentno");
				String title = request.getParameter("title");
				String desc = request.getParameter("desc");
				String userId = (String) session.getAttribute("userId");	
				
				BoardVO boardVo = new BoardVO();
				boardVo.setBoardTitle(title);
				boardVo.setBoardDesc(desc);
				boardVo.setUserId(userId);
				boardVo.setBoardParentno(Integer.parseInt(boardParentno));
				
				covidService.qnaReply(boardVo);
				
				nextPage = "";
				response.sendRedirect("/withcorona/qna");
				

				
				
				
				
				// 댓글 등록
			}else if(action.equals("/commentInsert")){
				
				CommentVO commentVo = new CommentVO();
				UserVO userVo = new UserVO();
				
				String boardId = request.getParameter("boardId");
				String commentDesc = request.getParameter("commentDesc");
				String userId = (String) session.getAttribute("userId");		
				
				commentVo.setCommentParentno(0);
				commentVo.setBoardId(Integer.parseInt(boardId));
				commentVo.setCommentDesc(commentDesc);
				commentVo.setUserId(userId);
				
				covidService.commentInsert(commentVo);
				
				nextPage = "";
				response.sendRedirect("/withcorona/qnaView?boardId=" + boardId);			
				
				// 댓글 수정
			}else if(action.equals("/qnaUpdateResult")){
				BoardVO	boardVo = new BoardVO();
				String boardId = request.getParameter("boardId");
				String title = request.getParameter("title");
				String desc = request.getParameter("desc");
				
				boardVo.setBoardId(Integer.parseInt(boardId));
				boardVo.setBoardTitle(title);
				boardVo.setBoardDesc(desc);
				
				covidService.qnaUpdate(boardVo);
				
				nextPage = "";
				response.sendRedirect("/withcorona/qnaView?boardId=" + boardId);
				
				// 댓글 삭제
			}else if(action.equals("/qnaDelete")){
				
				String boardId = request.getParameter("boardId");
				covidService.qnaDelete(Integer.parseInt(boardId));
				
				nextPage = "";
				response.sendRedirect("/withcorona/qna");
				
				// 댓글 답글 
			}else if(action.equals("/qnaReplyResult")){
				
				String boardParentno = request.getParameter("boardParentno");
				String title = request.getParameter("title");
				String desc = request.getParameter("desc");
				String userId = (String) session.getAttribute("userId");	
				
				BoardVO boardVo = new BoardVO();
				boardVo.setBoardTitle(title);
				boardVo.setBoardDesc(desc);
				boardVo.setUserId(userId);
				boardVo.setBoardParentno(Integer.parseInt(boardParentno));
				
				covidService.qnaReply(boardVo);
				
				nextPage = "";
				response.sendRedirect("/withcorona/qna");
				
				
				
				
				
				
			}else {	
				nextPage = "/deny.jsp";
			}
			
			if(!nextPage.equals("")) {
				RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
				dispatch.forward(request, response);				
			}
			
			
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
