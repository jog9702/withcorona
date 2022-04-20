<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
	import = "java.util.List"  
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>COVID-19 | 검사소 결과</title>
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
		border: 2px solid;
		width:800px
	}
	th, td{
		border: 1px solid;
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
            <div><a href="/withcorona/login">문의/제보</a></div>
        </div>
        <hr>
    </div>
    </header>
   
	<section>
		<table class="table">
			<tr>
				<th>지역명</th>
				<th>보건소명</th>
				<th>주소</th>
				<th>전화번호</th>
			</tr>
			<c:forEach var="list" items="${ list }">
				<tr>
					<td>${ list.clinicLocal }</td>
					<td>${ list.clinicName }</td>
					<td>${ list.clinicInfo }</td>
					<td>${ list.clinicTel }</td>
				</tr>
			</c:forEach>
		</table>
	</section>
	
</body>
</html>