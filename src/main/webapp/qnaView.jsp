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
<script src="https://code.jquery.com/jquery-3.6.0.js"></script>
<script>
	function goList(){
		location.href="${ contextPath }/withcorona/qna";
	}
	
	function goUpdate(){
		location.href="${ contextPath }/withcorona/qnaUpdate?boardId=${qna.boardId}";
	}
	
	function goDelete(){
		var isDel = window.confirm("정말 삭제하시겠습니까?");
		if(isDel){
			location.href="${ contextPath }/withcorona/qnaDelete?boardId=${qna.boardId}";
		}
	}
	
	function goComment(){
		location.href="${ contextPath }/withcorona/qnaComment?boardParentNO=${qna.boardId}";
	}
</script>
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
		제목 : ${ qna.boardTitle }<br> 
		내용 : <br>
		<div style="border:1px black solid; padding:10px;">
		${ qna.boardDesc }
		</div>
		<br><br>
		<div>
        	<c:if test="${ qna.userId eq vo.userId }">
        		<input type="button" value="수정하기" onclick="goUpdate();">
				<input type="button" value="삭제하기" onclick="goDelete();">
				<input type="button" value="답글쓰기" onclick="goComment();">
				<input type="button" value="목록보기" onclick="goList();">
			</c:if>
        	<c:if test="${ !(qna.userId eq vo.userId) }">
				<input type="button" value="답글쓰기" onclick="goComment();">
				<input type="button" value="목록보기" onclick="goList();">
			</c:if>
        </div>
    </section>
</body>
</html>