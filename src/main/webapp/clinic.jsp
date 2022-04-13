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
<title>검사소 찾기</title>
<style>
    .fixed{
        position: fixed;
        margin-top: -145px;
    }
    .mgt{
        margin-top: 155px;
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
	.tb{
		width: 800px;
	}
	.mgt-30{
		margin-top: 30px;
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
            <div><a href="/pot_220425/pot/qna">문의/제보</a></div>
        </div>
        <hr>
    </div>
    </header>
    <section>
    
       
        <div class="mgt">
        	<form action="selectClinic">
				<input type="text" name="loc">
				<input type="submit" value="조회" />
        	</form>
        	
        	<c:choose>
        		<c:when test="${ empty searchClinicInfo }">
		        	<c:choose>
		        		<c:when test="${ !empty clinicInfo }">
		        			<div class="mgt-30">
		        				<table border='1' class="tb">
			        				<tr>
			        					<td>지역명</td>
			        					<td>보건소명</td>
			        					<td>보건소 주소</td>
			        					<td>전화번호</td>
			        				</tr>
					        		<c:forEach var="i" items="${ clinicInfo }">
							       		<tr>
							       			<td>${ i.clinic_local }</td>
							       			<td>${ i.clinic_name }</td>
							       			<td>${ i.clinic_info }</td>
							       			<td>${ i.clinic_tel }</td>
							       		</tr>
					        		</c:forEach>
			        			</table>
		        			</div>
		        		</c:when>
		        		<c:when test="${ empty clinicInfo }">
		        			목록이 없습니다
		        		</c:when>
		        	</c:choose>
        		</c:when>
        		<c:when test="${ !empty searchClinicInfo }">
        			<div class="mgt-30">
		        				<table border='1' class="tb">
			        				<tr>
			        					<td>지역명</td>
			        					<td>보건소명</td>
			        					<td>보건소 주소</td>
			        					<td>전화번호</td>
			        				</tr>
					        		<c:forEach var="i" items="${ searchClinicInfo }">
					       			<tr>
					        			<td>${ i.clinic_local}</td>
					        			<td>${ i.clinic_name}</td>
					        			<td>${ i.clinic_info}</td>
					        			<td>${ i.clinic_tel}</td>
					        		</tr>
					        	</c:forEach>
			        		</table>
		        		</div>
        		</c:when>
        	</c:choose>
        </div>	
    </section>
</body>
</html>