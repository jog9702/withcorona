<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>COVID-19 | 게시글 등록 페이지</title>
<script>
	function goList(){
		location.href="${ contextPath }/withcorona/qna";
	}
</script>
<style>
	section{
		width: 491px;
    	margin: auto;
	}
    #main_view{
        max-width: 100%;
        width: 100%;
    }
    
    .mgt{
            margin-top: 130px;
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
		<c:if test="${ vo.userAuth == null }">
    		<script>
    			alert("로그인을 해야합니다.");
    			location.href="/withcorona/login";
    		</script>
    		
    	</c:if>
    	<c:if test="${ vo.userAuth != null }">
    		<h1 class="mgt">새글 쓰기</h1>
			<form name="qnaForm" method="post" action="${ contextPath }/withcorona/qnaInsert">
			제목 : <input type="text" placeholder="제목을 입력하세요" name="title" required><br>
			내용 : <br>
			<textarea name="desc" rows=10 cols=65 maxlength="4000" placeholder="내용을 입력하세요" required></textarea>
			<br><br>
			<input type="submit" value="글쓰기">
			<input type="button" value="목록보기" onclick="goList();">
		</form>
    	</c:if>
	</section>
</body>
</html>