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
		
		
		$("#collos").click(function(){
			
		});
		
		$("#palette").click(function(e) {
 	color.push(colorPalette.current_display_color);
 	console.log(colorPalette.current_display_color);
 	writeHtml(color);
 	
 	
	
		
		
		});
		
	$('.delete_color').live('click',function() { 
	var id = $(this).parent().index();
	console.log("index" +id);
	$(this).parent().remove();
	color.splice(id,1);
	
	console.log(color.length);
	});
	
	jQuery.ajaxSettings.traditional = true;	
});
	function writeHtml(color){
		$.ajax({
			  type: "GET",
			  url: "search/",
			  data: { color:color }
	 		  
			}).sucess(function() {
			  alert( "Data Saved: ");
			});
	$('#col').html('');
 	for(var i = 0; i < color.length; i++) {
       var item = "<div class=\"color\" style=\"width:160px;height:50px;background:#"+color[i]+"\"><img class=\"delete_color\" id=\"delete_color_"+i+"\" src=\"resources/images/delete.png\" title=\"Delete this color\"></div>";
       console.log(color);
	  $('#col').append(item);	
		}
	console.log(color.length);
	
	};

</script>
</head>

<body>
	<div id="top">
	
	<img src="resources/images/colloslogo.png" alt="Collos" id="collos"/>
	
	<div id="palette_container">
	
		<div id="palette">
				
			&nbsp;
		
		</div>
		
		</div>
	</div>
	
		<div id="col">
		</div>
	<div Id="pictures">
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
		
</body>
</html>
