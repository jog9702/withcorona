<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${ pageContext.request.contextPath }" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>COVID-19 | 게시글 상세 페이지</title>
<script src="https://code.jquery.com/jquery-3.6.0.js"></script>
<script>
	function goList() {
		location.href = "${ contextPath }/withcorona/qna";
	}

	function goUpdate() {
		location.href = "${ contextPath }/withcorona/qnaUpdate?boardId=${qna.boardId}";
	}

	function goDelete() {
		var isDel = window.confirm("정말 삭제하시겠습니까?");
		if (isDel) {
			location.href = "${ contextPath }/withcorona/qnaDelete?boardId=${qna.boardId}";
		}
	}

	function goReply() {
		location.href = "${ contextPath }/withcorona/qnaReply?boardParentno=${qna.boardId}";
	}
</script>
<style>
    #main_view{
        max-width: 100%;
        width: 100%;
    }
    
    .mgt{
        margin: auto;
        width:1000px;
        border: 1px solid gray;
        border-radius: 14px;
	    padding: 10px;
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
   
   .center{
   		width:800px;
   		margin:auto;
   }
   .right{
   		text-align:right;
   }
   .bright{
   	    width: 1000px;
    	text-align: right;
   }
   
   .width492{
   	width: 800px;
    text-align: right;
   }
   
   .ta{
   		width:800px;
   		border-radius: 14px;
   }
   .mh{
   	min-height: 300px;
   	border-radius: 14px;
   }
   
   table {
	border: 2px solid;
	width: 1000px;
	margin : auto;
	}
	
	th, td {
		border: 1px solid;
	}
	section{
		width: 1000px;
		margin: auto;
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
				<div>
					<a href="/withcorona/covidKorea">국내 상세</a>
				</div>
				<div>
					<a href="/withcorona/covidForeign">해외 상세</a>
				</div>
				<div>
					<a href="/withcorona/search">가까운 검사소 찾기</a>
				</div>
				<div>
					<a href="/withcorona/qna">문의/제보</a>
				</div>
			</div>
			<hr>
		</div>
	</header>
	<div class="mgt">
		<section>
			<c:if test="${ vo.userAuth == null}">
             로그인이 필요한 페이지입니다.<br>
				<a href="/withcorona/login"><input type="button" value="로그인"></a>
			</c:if>
			<c:if test="${ vo.userAuth != null }">
			작성자 : ${ qna.userId }<br> 제목 : ${ qna.boardTitle }<br> 내용 :
				<div class="mh" style="border: 1px black solid; padding: 10px;">${ qna.boardDesc }
				</div>
				<br>
				<div class="bright">
					<c:if test="${ qna.userId eq vo.userId || vo.userAuth eq '1'}">
						<input type="button" value="수정하기" onclick="goUpdate();">
						<input type="button" value="삭제하기" onclick="goDelete();">
						<input type="button" value="답글쓰기" onclick="goReply();">
						<input type="button" value="목록보기" onclick="goList();">
					</c:if>
					<c:if test="${ !(qna.userId eq vo.userId) && vo.userAuth eq '0'}">
						<input type="button" value="답글쓰기" onclick="goReply();">
						<input type="button" value="목록보기" onclick="goList();">
					</c:if>
				</div>
				<br>
				<hr>

		<table>
			<thred>
			<tr>
				<div class="center">
				<form action="/withcorona/comment">
					<input type="hidden" name="comment" value="${qna.boardId }"></input>
					<textarea class="ta" name="commentText" rows=5 cols=65 maxlength="4000"
						required></textarea>
					<div class="width492">
					<input class="right" type="submit" value="댓글 작성">
					</div>
				</form>
				</div>

				<br>


					<thred>
					<tr>
						<td colspan="4">댓글</td>
					</tr>
					</thred>
					<tbody>
						<c:choose>
							<c:when test="${ empty commentList }">
								<tr height="10">
									<td colspan="5">등록된 댓글이 없습니다</td>
								</tr>
							</c:when>
							<c:when test="${! empty commentList }">
								<c:forEach var="comment" items="${ commentList }"
									varStatus="commentNum">
									<tr align="center">
										<td width="5%">${ commentNum.count }</td>
										<td width="10%">${ comment.userId }</td>
										<td align="left" width="35%"><span
											style="padding-right: 30px"></span> <c:choose>
												<c:when test="${ comment.level > 1 }">
													<c:forEach begin="1" end="${ comment.level }" step="1">
														<span style="padding-right: 10px"></span>
													</c:forEach>
													<span style="font-size: 12px">[댓글]</span>
										${ comment.commentDesc }
										<form action="/withcorona/comment">
														<input type="hidden" name="comment"
															value="${qna.boardId }"></input> <input type="hidden"
															name="commentId" value="${comment.commentId }"></input> <input
															type="text" name="commentText" required> <input
															type="submit" value="댓글 작성">
													</form>
												</c:when>
												<c:otherwise>
										${ comment.commentDesc }
										<form action="/withcorona/comment">
														<input type="hidden" name="comment"
															value="${qna.boardId }"></input> <input type="hidden"
															name="commentId" value="${comment.commentId }"></input> <input
															type="text" name="commentText" required> <input
															type="submit" value="댓글 작성">
													</form>
												</c:otherwise>
											</c:choose></td>
										<td width="10%"><fmt:formatDate
												value="${ comment.commentTime }" /></td>
									</tr>
								</c:forEach>
							</c:when>
						</c:choose>
					</tbody>
				</table>
				</div>
			</c:if>
		</section>
</body>
</html>