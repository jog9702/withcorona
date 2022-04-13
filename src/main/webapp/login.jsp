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
            <div><a href="/pot_220425/board/search">가까운 검사소 찾기</a></div>
            <div><a href="/pot_220425/board/qna">문의/제보</a></div>
        </div>
        <hr>
    </div>
    </header>
    <section>
    	<div class="mgt">
	    	<form action="/loginCheck">
	    		<input type="text" name="id"><br>
	    		<input type="password" name="password"><br><hr>
	    		<input type="submit" value="login">
	    		<input type="button" href="/join" value="join">
	    	</form>
    	</div>
    </section>
</body>
</html>