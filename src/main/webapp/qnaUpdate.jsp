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
<title>COVID-19 | 게시판 수정 페이지</title>
<style>
    .fixed{
        position: fixed;
        margin-top: -230px;
    }
    .mgt{
        margin-top: 240px;
     }
    .mglr{
        margin: 0px 30px;
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
		작성자 : ${ qna.userId }<br>
		<form name="qnaForm" method="post" action="${ contextPath }/withcorona/qnaUpdateResult">
			제목 : <input name="title" type="text" value="${ qna.boardTitle }"><br> 
			내용 : <br>
			<textarea name="desc" rows="10" cols="65" maxlength="4000">${ qna.boardDesc }</textarea>
			<br><br>
			<input type="hidden" name="boardId" value="${ qna.boardId }">
			<input type="submit" value="수정하기">
			<input type="button" value="취소하기" onclick="history.back(-1)">
		</form>
	</section>
</body>
</html>