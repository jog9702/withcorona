<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
%>
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

</style>
</head>
<body>
    <header>
    <div class="fixed">
        <div class="fs">
            <a href="/pot_220425/pot/covidHomepage">COVID-19</a>
        </div>
        <div class="flex">
            <div><a href="/pot_220425/pot/covidKorea">국내 상세</a></div>
            <div><a href="/pot_220425/pot/covidForeign">해외 상세</a></div>
            <div><a href="/pot_220425/pot/search">가까운 검사소 찾기</a></div>
            <div><a href="/pot_220425/pot/login">문의/제보</a></div>
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
            </div>
        </div>
        
        <div>
        	<div><a href="/pot_220425/pot/update">update</a></div>
        </div>
        
    </section>
</body>
</html>