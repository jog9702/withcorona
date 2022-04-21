<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
            <div class="fs1">일일 확진자수 : ${ todayCount }</div>
            <div class="flex1">
                <div class="mglr">연간 확진자수 : ${ monthCount }</div>
                <div class="mglr">월간 확진자수 : ${ yearCount }</div>
                <div class="mglr">사망자수 : ${ todayDeath }</div>
            </div>
        </div>
        
        <div>
            <div class="mgt- select">
	        	<c:choose>
	 	       		<c:when test="${ koreaLocCount == null }">
	 	       			<div>지역을 입력하세요</div>
	 	       		</c:when>
	 	       		<c:otherwise>
	 	       			<div>${ loc } 확진자수 : ${ koreaLocCount } 명</div>
	 	       			<div class="fs--">사망자수 : ${ locDeath } 명</div>
	 	       		</c:otherwise>
	        	</c:choose>
            </div>

            <div class="select">
        	<form action="/withcorona/koreaSelection">
				<select name="loc">
					<option value="서울">서울</option>
					<option value="경기">경기</option>
					<option value="강원">강원</option>
					<option value="충북">충북</option>
					<option value="충남">충남</option>
					<option value="전북">전북</option>
					<option value="전남">전남</option>
					<option value="경북">경북</option>
					<option value="경남">경남</option>
					<option value="제주">제주</option>
				</select>
				<input type="submit" value="조회" />
        	</form>
        </div>

        </div>
        
        <form action="/withcorona/updateToDate">
        	<input type="text" name="before" value="yyyymmdd">
        	<input type="submit" value="업데이트">
        </form>
    </section>
</body>
</html>