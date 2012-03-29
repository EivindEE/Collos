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
	<script type="text/javascript" src="resources/javascript/jquery.masonry.min.js"></script>
</head>

<body class="home">
	<h1>HELLO WORLD!!!</h1>
	<div id="container" style="position: width:200px">
	<c:forEach var="image" items="${images}">
	<div class="box">
		<img src="../resources/testimg/${image}">
	</div>
	</c:forEach>
	
	</div>
	
	<script>
	 $(function(){

		    var $container = $('#container');
		  
		    $container.imagesLoaded( function(){
		      $container.masonry({
		        itemSelector : '.box',
		        columnWidth : 500,
 		        animationOptions : {
 		        	duration: 750,
 		        	easing: 'linear',
 		        	queue: false
 		        	}
		        
		      });
		    });
		  
		  });
	</script>
</body>
</html>
