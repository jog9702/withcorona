<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="contextPath" value="${ pageContext.request.contextPath }"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>COVID-19 | 게시글 목록 페이지</title>
<style>
    #main_view{
        max-width: 100%;
        width: 100%;
    }
    
    .mgt{
        margin-top: 30px;
     }
    .mglr{
        margin: 0px 30px;
    }
    .flex{
        display: flex;
        justify-content: space-around;
        width: 100%;
        margin-top: 45px;
    }
    .flex1{
        display: flex;
        justify-content: center;
        width: 100%;
        margin-top: 30px;
    }
    .fs{
        font-size: 40px;
        width: 100%;
        text-align: center;
    }
    .fs1{
        font-size: 50px;
        width: 100%;
        text-align: center;
    }
    a:link {
       text-decoration: none;
       color: black;
   }
   
   a:visited {
       text-decoration: none;
       color: black;
   }
   
   a:hover {
       text-decoration: none;
       color: blue;
   }
   
   a:active {
       text-decoration: none;
   }
   .mg{
      margin-top: 100px;
      font-size: 10px;
      width:100%;
      text-align: right;
   }
   .login{
      float: right;
   }
   .center{
   		margin: auto;
    	width: 1000px;
    	text-align:right;
   }
   .bgcg{
   		background-color:rgba(225, 225, 225, 0.5);
   }

	table {
	border: 2px solid;
	width: 1000px;
	margin : auto;
   	border-radius: 10px;
   	margin-top : 45px;
	}
	
	th, td {
		border: 1px solid;
	}
</style>
</head>
<body>
    <header>
    <div class="fixed">
        <div class="fs">
            <a href="/withcorona/covidHomepage">COVID-19</a>
        </div>
        <div class="login">
        	<c:if test="${ vo.userAuth == null }">
				<a href="/withcorona/login"><input type="button" value="로그인"></a>
			</c:if>
        	<c:if test="${ vo.userAuth != null }">
				<a href="/withcorona/logout"><input type="button" value="로그아웃"></a>
			</c:if>
        </div>
        <div class="flex">
            <div><a href="/withcorona/covidKorea">국내 상세</a></div>
            <div><a href="/withcorona/covidForeign">해외 상세</a></div>
            <div><a href="/withcorona/search">가까운 검사소 찾기</a></div>
            <div><a href="/withcorona/qna">문의/제보</a></div>
        </div>
        <hr>
    </div>
    </header>
    <section>
    	<table>
			<thred>
				<tr class="bgcg">
					<td>글 번호</td>
					<td>작성자</td>
					<td>제목</td>
					<td>작성일</td>
				</tr>
			</thred>
			<tbody>
			<c:choose>
				<c:when test="${ empty qnaList }">
					<tr height="10">
						<td colspan="5">등록된 글이 없습니다</td>
					</tr>
				</c:when>
				<c:when test="${! empty qnaList }">
					<c:forEach var="qna" items="${ qnaList }" varStatus="qnaNum">
						<tr align="center">
							<td width="5%">${ qnaNum.count }</td>
							<td width="10%">${ qna.userId }</td>
							<td align="left" width="35%" class="level_${ qna.level }">
								<span style="padding-right:30px"></span>
								<c:choose>
									<c:when test="${ qna.level > 1 }">
										<c:forEach begin="1" end="${ qna.level }" step="1">
											<span style="padding-right:10px"></span>
										</c:forEach>
										<span style="font-size:12px">[답변]</span>
										<a href="${ contextPath }/withcorona/qnaView?boardId=${ qna.boardId }">${ qna.boardTitle }</a>
									</c:when>
									<c:otherwise>
										<a href="${ contextPath }/withcorona/qnaView?boardId=${ qna.boardId }">${ qna.boardTitle }</a>
									</c:otherwise>
								</c:choose>
							</td>
							<td width="10%">
								<fmt:formatDate value="${ qna.boardTime }"/>
							</td>
						</tr>
					</c:forEach>
				</c:when>
			</c:choose>
		</tbody>
    	</table>
    	<div style="text-align:center">
		<%
			int total = (int)request.getAttribute("total");
			int pageNum = (int)request.getAttribute("pageNum");
			int countPerPage = (int)request.getAttribute("countPerPage");

			double totalpaging = Math.ceil((double)total / countPerPage);
			
			final int section = 2;
			int postion = (int)Math.ceil((double)pageNum / section);
			int begin = ((postion - 1) * section) + 1;
			int end = begin + section - 1;
			if(end > totalpaging){
				end = (int)totalpaging;
			}
			String style = " style='color:red; font-weight:bold;'";
			pageContext.setAttribute("style", style);
		%>
		<%if(begin != 1){%>
			<a href="${ contextPath }/withcorona/qna?pageNum=<%= begin-1 %>&countPerPage=10" style="margin:10px;">[이전]</a>
		<% } %>
		<c:forEach begin="<%= begin %>" end="<%= end %>" var="paging1">
			<c:if test="${ pageNum == paging1 }">
				<a ${ sytle }href="${ contextPath }/withcorona/qna?pageNum=${paging1}&countPerPage=10" style="margin:10px;">[${ paging1 }]</a>			
			</c:if>
			<c:if test="${ pageNum != paging1 }">
				<a href="${ contextPath }/withcorona/qna?pageNum=${paging1}&countPerPage=10" style="margin:10px;">[${ paging1 }]</a>
			</c:if>
		</c:forEach>
		<%if(end != totalpaging){%>
			<a href="${ contextPath }/withcorona/qna?pageNum=<%= end+1 %>&countPerPage=10" style="margin:10px;">[다음]</a>
		<% } %>
	</div>
    </section>
    	<div class="center">
    	<a href="${ contextPath }/withcorona/qnaForm">
    	<button>
		글쓰기
		</button>
		</a>
		</div>
	</a>
</body>
</html>