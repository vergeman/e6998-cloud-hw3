<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html>

<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<link type="text/css" rel="stylesheet" media="all" href="./main.css" />
<title>Cloud Computing HW3 - akv2001</title>
</head>
<div id = "head">

	<h2>Cloud Computing HW3 - akv2001</h2>

	<h4><a href="https://github.com/vergeman/GAE_HW3">source</a></h4>
	
	<i>winner of the ugliest app engine site award! </i>
</div>
<hr>

<div id = "main">
<h2>Coldest 5</h2>

	<table id="coldest_tbl">
	
		<thead>
		<jsp:include page="/_tblHead.jsp" />
		</thead>

		
		<tbody id="coldest_tbl_body">
			<c:forEach var="w" items="${coldest}">
				<c:set var="w" value="${w}" scope="request"></c:set>
				<jsp:include page="/_tblData.jsp" />
				
			</c:forEach>
		</tbody>


	</table>


<h2>Hottest 5</h2>

<table id="hottest_tbl">
	
		<thead>
		<jsp:include page="/_tblHead.jsp" />
		</thead>

		
		<tbody id="hottest_tbl_body">
			<c:forEach var="w" items="${hottest}">
				<c:set var="w" value="${w}" scope="request"></c:set>
				<jsp:include page="/_tblData.jsp" />
				
			</c:forEach>
		</tbody>


</table>



<h2>All Locations</h2>


<table id="all_tbl">
	
		<thead>
		<jsp:include page="/_tblHead.jsp" />
		</thead>

		
		<tbody id="all_tbl_body">
			<c:forEach var="w" items="${all}">
				<c:set var="w" value="${w}" scope="request"></c:set>
				<jsp:include page="/_tblData.jsp" />
				
			</c:forEach>
		</tbody>


</table>


</div>

</html>