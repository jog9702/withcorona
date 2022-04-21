<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>COVID-19 | 회원가입 페이지</title>
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
            <div><a href="/withcorona/login">문의/제보</a></div>
        </div>
        <hr>
    </div>
    </header>
    <section>
    	<form action="/withcorona/signUpResult" method="post">
	    	<table>
	    		<tr>
	    			<td colspan=2><h1>회원가입</h1></td>
	    		</tr>
	    		<tr>
	    			<td>아이디</td>
	    			<td><input type="text" name="id"></td>
	    		</tr>
	    		<tr>
	    			<td>비밀번호</td>
	    			<td><input type="password" name="pwd"></td>
	    		</tr>
	    		<tr>
	    			<td>이름</td>
	    			<td><input type="text" name="name"></td>
	    		</tr>
	    		<tr>
	    			<td>성별</td>
	    			<td>
	    				<input type="radio" name="gender" value="male" checked>남자<br>
	    				<input type="radio" name="gender" value="female">여자
	    			</td>
	    		</tr>
	    		<tr>
	    			<td>이메일</td>
	    			<td><input type="email" name="email"></td>
	    		</tr>
    		</table>
    		<input type="submit" value="가입하기">
	    	<input type="reset" value="다시입력">
    	</form>
    </section>
</body>
</html>