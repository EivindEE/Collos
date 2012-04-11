<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page session="false"%>
<!DOCTYPE html>
<html dir="ltr" lang="en">
<head>
<meta charset="UTF-8" />
<title>Collos - Colorfull image searching</title>
<!-- 	<link rel="shortcut icon" type="image/x-icon" href="resources/images/favicon.ico" /> -->
<!-- 	<link rel="icon" type="image/png" href="resources/images/favicon.png" /> -->
<link rel="stylesheet" type="text/css" href="resources/css/screen.css" />
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>
<script type="text/javascript" src="resources/javascript/jquery.masonry.min.js"></script>
<script type="text/javascript" src="resources/javascript/colorutils.js"></script>
 <script type="text/javascript">
 
	jQuery(document).ready(function() {
		var colorPalette = new ColorPalette($("#palette"));
		var color = new Array();
		$("#palette").click(function(e) {
 	color.push(colorPalette.current_display_color);
 
		  $('#col').append("<div style=\"width:160px;height:50px;background:#"+colorPalette.current_display_color+"\">TEST</div>");
 	});
})
</script>
</head>

<body>
	<div id="">
	
	<img src="resources/images/colloslogo.png" alt="Collos" id="collos"/>
	</div>
	<div Id="leftrow">
	<div id="container" class="transitions-enabled clearfix masonry">
		<c:forEach var="image" items="${images}">
			<div class="box">
			<a href="${image.pageUri}"><img src="${image.imageUri}"></a>
			</div>
		</c:forEach>
		
		</div>
		</div>
		<script>
			$(function() {

				var $container = $('#container');

				$container.imagesLoaded(function() {
					$container.masonry({
						itemSelector : '.box',
						//isFitWidth: true,
//						columnWidth : 100,
 						isAnimated : true,
// 						animationOptions : {
// 							duration : 100,
// 							easing : 'linear',
// 							queue : false
// 						}

					});
				});

			});
		</script>
		<div class="right">
			<div id="palette">
				
			&nbsp;
			</div>
		<div id="col">
		
		</div>
		</div>
</body>
</html>
