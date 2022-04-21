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
<title>COVID-19</title>
<style>
    .fixed{
        position: fixed;
        margin-top: -230px;
    }
    .mgt{
        margin-top: 240px;
     }
    .mglr{
        margin: 0px 80px;
    }
    .flex{
        display: flex;
        justify-content: space-around;
        width: 800px;
        margin-top: 45px;
    }
    .flex1{
        display: flex;
        justify-content: center;
        width: 800px;
        margin-top: 30px;
    }
    .fs{
        font-size: 40px;
        width: 800px;
        text-align: center;
    }
    .fs1{
        font-size: 50px;
        width: 800px;
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
		width:800px;
		text-align: right;
	}
	table{
		margin-top: 240px;
	}
	.login{
		float: right;
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
				<tr>
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
							<td align="left" width="35%">
								<span style="padding-right:30px"></span>
								<c:choose>
									<c:when test="${ qna.boardId > 1 }">
										<c:forEach begin="1" end="${ qna.boardId }" step="1">
											<span style="padding-right:20px"></span>
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
								<fmt:formatDate value="${ qna.boardTime }" pattern="yyyy-mm-dd hh:mm:ss"/>
							</td>
						</tr>
					</c:forEach>
				</c:when>
			</c:choose>
		</tbody>
    	</table>
    </section>
    	<a href="${ contextPath }/withcorona/qnaForm">
		<p>글쓰기</p>
	</a>
</body>
</html>