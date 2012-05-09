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
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.4/jquery-ui.js"></script>
<link rel="stylesheet" type="text/css" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.4/themes/overcast/jquery-ui.css">

<script src="resources/javascript/modernizr-transitions.js"></script>
<script type="text/javascript" src="resources/javascript/jquery.masonry.min.js"></script>
<script type="text/javascript" src="resources/javascript/colorutils.js"></script>
<script type="text/javascript" src="resources/javascript/jquery.colorbox.js"></script>
<link rel="stylesheet" type="text/css" href="resources/css/colorbox.css" />
<script type="text/javascript">
		var imagesArray;
	jQuery(document).ready(function() {
		var colorPalette = new ColorPalette($("#palette"));
		var color = new Array();
		
		
		$("#collos").click(function(){
			
		});
		
		$("#palette").click(function(e) {
 	color.push(colorPalette.current_display_color);
 	console.log(colorPalette.current_display_color);
 	writeHtml(color);
 	/*  $.ajax({
		  type: "GET",
		  url: "search/",
		  data: { color:colorPalette.current_display_color }
 		  
		}).sucess(function(mydata){
		  alert( "Data Saved: "+ mydata);
		}); */
		$('.color').resizable({
			handles: 'e',
			maxHeight: '50',
			maxWidth: $('.color').parent().width(),
			resize:function(){
				var resizeWidth = $(this).width();
				var parentWidth = $(this).parent().width();
				var percent = 100*resizeWidth/parentWidth;
				var numberSiblings = $(this).siblings().size();
				var restPercent = 100-percent;
				var siblingPercent = restPercent/numberSiblings;
				//console.log(this.offsetParent());
				console.log(resizeWidth);
				console.log(parentWidth);
				console.log(percent);
				$(this).css('width',percent+'%')
				$(this).siblings().css('width',siblingPercent+'%')
				
				
			}
		});	
		
		
		
		$.getJSON("/Collos/color?colors=" + colorPalette.current_display_color,
				function(data){
			writeImages(data);
			imagesArray = data;
		}
	
			);
		
		});
		
		
		
	$('.delete_color').live('click',function() { 
	var id = $(this).parent().index();
	console.log("index" +id);
	$(this).parent().remove();
	color.splice(id,1);
	var width = 100/color.length
	  $('.color').css('width',width+'%');
	console.log(color.length);
	});
	
	
});
	function writeHtml(color){
	$('#col').html('');
 	for(var i = 0; i < color.length; i++) {
       var item = "<div class=\"color ui-resizable\" style=\"background:#"+color[i]+"\"><img class=\"delete_color\" id=\"delete_color_"+i+"\" src=\"resources/images/delete.png\" title=\"Delete this color\"></div>";
       console.log(color);
	  $('#col').append(item);	
		}
 	var width = 100/color.length
	  $('.color').css('width',width+'%');
	console.log(color.length);
	console.log(width);
	};
	
	function writeImages(images){
		$('#container').html('');
		
		for(var i = 0; i < images.length; i++){
			var $imagebox = $("<div class='box'> <a class='gallery' id='"+i+"' href='" + images[i].imageUri + "'><img style='width:100px;height:auto' src='" +  images[i].imageUri + "'></a>");
			$('#container').append($imagebox)
			
			
			
		}
		
		$('#container').masonry('reload');	
		$('a.gallery').colorbox({
			next:"Next",
			previous:"Previous",
			title: function(){
				var i = $(this).attr('id');
				var pageUrisList = imagesArray[i].pageUris;
				var pageUris ="<div><div style='float:left'>Source:</div>";
				console.log(pageUrisList);
				for(var j = 0; j <pageUrisList.length && j < 20; j++){
					console.log(pageUrisList[j]);
				pageUris = pageUris + '<a style="float:left" href="' + pageUrisList[j] + '" target="_blank">'+(j+1)+' &nbsp;</a>'
				}
				console.log(pageUris);
				return pageUris+ "</div>";
			}
		});
	};

</script>
</head>

<body>
	<div id="top">

		<img src="resources/images/colloslogo.png" alt="Collos" id="collos" />

		<div id="palette_container">

			<div id="palette">&nbsp;</div>

		</div>
	</div>
<!-- 	<div id="resizable" style=" width: 150px; height: 150px; padding: 0.5em; " class="ui-widget-content">
    <h3 class="ui-widget-header">Resizable</h3>
</div> -->
	<script>
	
	</script>
	<div id="col"></div>
	<div Id="pictures">
		<div id="container" class="transitions-enabled clearfix masonry">
		</div>
	</div>
	
	<script>
			$(function() {

				var $container = $('#container');

				$container.imagesLoaded(function() {
					$container.masonry({
						itemSelector : '.box',
						isFitWidth: true,
 						isAnimated : true,
 					 	animationOptions : {
					    duration : 300,
 						easing : 'linear',
						queue : false
						} 

					});
				});

			});
		</script>

</body>
</html>
