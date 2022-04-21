<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>확진자 상세</title>
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
    .mgt-{
        margin-top: 30px;
        font-size: 30px;
    }
    .select{
        width: 800px;
        text-align: center;
    }
    .fs--{
    	font-size: 15px;
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
        <div class="mgt">
            <div class="fs1">
            	한국 ${ kor }<br>
            	중국 ${ chi }<br>
            	일본 ${ jap }<br>
            </div>
        </div>
        
        <div>
            <div class="mgt- select">
	        	<c:choose>
	 	       		<c:when test="${ empty to }">
	 	       			<div>지역을 입력하세요</div>
	 	       		</c:when>
	 	       		<c:otherwise>
	 	       			<div>${ loc } 확진자수 : ${ pick } 명</div>
	 	       			<div class="fs--">사망자수 : ${ pickD } 명</div>
	 	       		</c:otherwise>
	        	</c:choose>
            </div>

            <div class="select">
        	<form action="/withcorona/foreignSelection">
				<input type="text" value="국가를 입력하세요" name="loc">
				<input type="submit" value="조회" />
        	</form>
        </div>

        </div>
        
        <c:if test="${ vo.userAuth eq '1' }">
	        <form action="/withcorona/fUpdate">
	        	<input type="text" name="before2" value="yyyymmdd">
	        	<input type="submit" value="업데이트">
	        </form>
        </c:if>
    </section>
</body>
</html>