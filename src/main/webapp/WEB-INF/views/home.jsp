<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false" %>
<!DOCTYPE html>
<html dir="ltr" lang="en">
<head>
	<meta charset="UTF-8" />
	<title>Collos - Colorfull image searching</title>
<!-- 	<link rel="shortcut icon" type="image/x-icon" href="resources/images/favicon.ico" /> -->
<!-- 	<link rel="icon" type="image/png" href="resources/images/favicon.png" /> -->
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>
	<link rel="stylesheet" type="text/css" href="resources/css/screen.css" />
</head>

<body class="home">
	<h1>HELLO WORLD!!!</h1>
	<c:forEach var="image" items="${images}">
		<img src="../resources/testimg/${image}">
	</c:forEach>
</body>
</html>
