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
    .mgt-{
        margin-top: 30px;
        font-size: 30px;
    }
    .select{
        width: 800px;
        text-align: center;
    }



</style>
</head>
<body>
    <header>
    <div class="fixed">
        <div class="fs">
            <a href="/withcorona/covidHomepage">COVID-19</a>
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
            <div class="fs1">일일 확진자수 : ${ todayCount } </div>
            <div class="flex1">
                <div class="mglr">연간 확진자수 : ${ monthCount }</div>
                <div class="mglr">월간 확진자수 : ${ yearCount }</div>
            </div>
        </div>
        <div>
            <div class="mgt- select">
        	<% 
        	
        		if(request.getAttribute("koreaLocCount")== null){
        			out.println("지역을 입력하세요");
        		}else{
        			%> ${ loc } : ${ koreaLocCount } 명 <%        		
        		}
        	%> 
            </div>

            <div class="select">
        	<form action="/withcorona/koreaSelection">
				<select name="loc">
					<option value="seoul" selected>서울</option>
					<option value="gyeonggi">경기</option>
					<option value="gangwon">강원</option>
					<option value="chungcheongN">충북</option>
					<option value="chungcheongS">충남</option>
					<option value="jeollaN">전북</option>
					<option value="jeollaS">전남</option>
					<option value="gyeongsangN">경북</option>
					<option value="gyeongsangS">경남</option>
				</select>
				<input type="submit" value="조회" />
        	</form>
        </div>

        </div>
    </section>
</body>
</html>