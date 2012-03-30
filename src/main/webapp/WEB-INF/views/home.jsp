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
 $(document).ready(function() {
//		process_hash();
		// Initialize color palette       
		var colorPalette = new ColorPalette($("#palette0"));
	});
	jQuery(document).ready(function() {
		$("#palette0img").click(function(e) {
			colorPalette.set_palette_color_for(100,100)
		// $('#status2').html(e.pageX +', '+ e.pageY);

	});
})
</script>
</head>

<body class="home">
	<h1>HELLO WORLD!!!</h1>
	<div id="container" style="position: width:200px">
		<c:forEach var="image" items="${images}">
			<div class="box">
				<img src="../resources/testimg/${image}">
			</div>
		</c:forEach>
		<script>
			$(function() {

				var $container = $('#container');

				$container.imagesLoaded(function() {
					$container.masonry({
						itemSelector : '.box',
						columnWidth : 500,
						isAnimated : true,
						animationOptions : {
							duration : 100,
							easing : 'linear',
							queue : false
						}

					});
				});

			});
		</script>
		<div id="palatte">
			<div id="palette0" style="float: right">
				<img id="palette0img" src="../resources/images/palette.png"
					width="160" height="160" alt="pallete" onclick="">

				
			</div>
			<h2 id="status2">0, 0</h2>
		</div>
		<!-- <div style="width: 100px; height: 100px; background:#ccc;" id="special"> -->
		<!-- Click me anywhere! -->
		<!-- </div> -->
</body>
</html>
