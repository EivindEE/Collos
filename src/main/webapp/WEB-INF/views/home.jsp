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
<link rel="icon" type="image/png" href="resources/images/favicon.png">
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.4/jquery-ui.js"></script>
<link rel="stylesheet" type="text/css"
	href="resources/css/jquery-ui.css">
<link rel="stylesheet" type="text/css" href="resources/css/screen.css" />

<script src="resources/javascript/modernizr-transitions.js"></script>
<script type="text/javascript"
	src="resources/javascript/jquery.masonry.min.js"></script>
<script type="text/javascript" src="resources/javascript/colorutils.js"></script>
<script type="text/javascript"
	src="resources/javascript/jquery.colorbox.js"></script>
<link rel="stylesheet" type="text/css" href="resources/css/colorbox.css" />
<script type="text/javascript" src="resources/javascript/farbtastic.js"></script>
<link rel="stylesheet" type="text/css"
	href="resources/css/farbtastic.css" />

<script type="text/javascript">
	var imagePageMap;
	var request =$.ajax();
	var imageDivs;
	var color = new Array();
	var relativeFreqs = new Array();
	var pageCount;

	jQuery(document).ready(function() {
		var colorPalette = new ColorPalette($("#palette"));
		var pickedColor;
		
		$("#palette").click(function(e) {
			if(color.length <= 4){
			color.push(colorPalette.current_display_color);
			console.log(colorPalette.current_display_color);
			$('#picker').hide();
			writeHtml(color);
			instruction_text();
			var startWidth = 0;
			$('.color_box').resizable({
				handles : 'e',
				maxHeight : '50',
				minWidth  : '0',
				maxWidth : $('.color_box').parent().width(),
				start: function(){
					startWidth = $(this).width();				
				},
				resize : function() {
					var resizeWidth = $(this).width();
					var parentWidth = $(this).parent().width();
					var percent = 100 * resizeWidth / parentWidth;
					var restPercent = 100 - percent;
					
					var numberSiblings = $(this).siblings().size();
					var siblingPercent = restPercent / numberSiblings;
					$(this).css('width', percent + '%');
					var intWidth = Math.round( percent );
					$(this).children('.freq').html(intWidth + '%');
					var next = $(this).next();
					var siblingsWidth = 0;
					$(next).siblings().each(function(i,el){
						siblingsWidth += $(el).width();
					});
					var nextWidth = parentWidth - siblingsWidth;
					var nextPercent = (100 * nextWidth / parentWidth);
					if($(next).width() <= 5){
						var id = $(next).index();
						color.splice(id, 1);
						$(next).remove();
						instruction_text();
					}
					if($(this).width() <= 5){
						var id = $(this).index();
						color.splice(id, 1);
						$(this).remove();
						instruction_text();
					}
					if($(this).width() == $(this).parent().width()){
						$('.color_box').resizeable('disable');
					}
					$(next).css('width', nextPercent + '%	');
					intWidth = Math.round( nextPercent );
					$(next).children('.freq').html(intWidth + '%');
				
					
				},
				stop: function(){
					getImages(color);
				}
			
			});
		getImages(color);
			}else{
			var info_box = $('#info_box').html('');
			info_box.append("You have selected to many colors. Please remove one before choosing a new color");
			}
		});
	
// end of palette click
		
		$('.delete_color').live('click', function() {
			var id = $(this).parents('.color_box').index();
			console.log("index" + id);
			$(this).parents('.color_box').remove();
			color.splice(id, 1);
			var width = 100 / color.length;
			$('.color_box').css('width', width + '%');
			var intWidth = Math.round( width );
			$('.freq').each(function(){$(this).html(intWidth + '%')});
			instruction_text();
			getImages(color);
		});
			var color_picker_hidden = true;
		
		$('.pick_color').live('click',function(){
			if(color_picker_hidden){
			var id = $(this).parents('.color_box').index();
			pickedColor ="#"+color[id];
			change_color = "#"+color[id];
			$('#picker').show();
			$('#color').val(change_color);
			$('#colorpicker').farbtastic('#color');
			color_picker_hidden=false;
			}else{
				color_picker_hidden=true;
				$('#picker').hide();
				
			}
		});
		
		$('#change_color').live('click',function(){
			var change_color = $('#color').attr('value');
			var colorToReplace = color.indexOf(pickedColor.replace("#",""));
			color[colorToReplace] = $('#color').attr('value').replace("#","");
			var colorBox = $(pickedColor);
			console.log("Colorbox object style:" + colorBox.css('background-color'));
			colorBox.css("background-color",change_color);
			colorBox.attr("id","" + $('#color').attr('value').replace("#",""));
			$('#picker').hide();
			getImages(color);
			
		});
		
	});
	

	function getImages(color) {
		request.abort();
		clearInfoBox();
		removeImages();
		if (color.length !== 0) {
			showLoading();
			request = $.getJSON("/Collos/color?colors=" + color + "&freqs="
					+ getFreqs(), function(data) {

				//console.log(data.imagePages);
				pageCount = data.pageCount;
				writeImages(data.imageDivs);
				writeQueryTime(data.pageCount, data.queryTime);
				imagePageMap = data.imagePages;
			});
		} else {
			hideLoading();
			request.abort();
			console.log("Request aborted");
		}
	}

	function writeHtml(color) {
		$('#col').html('');
		for ( var i = 0; i < color.length; i++) {
			var item = "<div id='" + color[i] + "' class='color_box ui-resizable' style='background:#" + color[i] + "'><div class='color_box_element close_button' ><img class='delete_color' id='delete_color_"+i+"' src='resources/images/delete-1.png' title='Delete this color'></div><div class='color_box_element'><img class='pick_color' id='pick_color_" + i + "' src='resources/images/palette-small.png' title='Change this color'></div><div class='freq color_box_element'>100%</div></div>";
			console.log(color);
			$('#col').append(item);
		}
		var width = 100 / color.length;
		$('.color_box').css('width', width + '%');
		var intWidth = Math.round( width );
		$('.freq').each(function(){$(this).html(intWidth + '%')});
		console.log(color.length);
		console.log(width);
	};

	function getFreqs() {
		relativeFreqs = new Array();
		var parentWidth = $('#col').width();
		$('#col').children().each(function(i, el) {
			var percent = 100 * $(el).width() / parentWidth;
			relativeFreqs.push(percent);
		});
		console.log(relativeFreqs);
		return relativeFreqs;
	}

	function writeImages(images) {
		$('#container').html('');
		if (images !== "") {

			$('#container').append(images)


			$('#container').masonry('reload');
			addColorbox();

		
		} else {
			console.log("No images found")
		}

	};
	
	function addColorbox(){
		$('a.gallery')
		.colorbox(
				{
					next : "Next",
					previous : "Previous",
					width : 500,
					title : function() {
						console.log("this:" + this);
						var pageUrisList = imagePageMap[this];
						var pageUris = "<div><div style='float:left'>Source:</div>";
						console.log(pageUrisList);
						var count = 0;
						for (var pageUri in pageUrisList) {
							pageUris = pageUris
									+ '<a style="float:left" href="' + pageUrisList[pageUri] + '" target="_blank">'
									+ (count + 1) + ' &nbsp;</a>';
									count++
						}
						console.log(pageUris);
						return pageUris + "</div>";
					}

				});
		
	}

	function writeQueryTime(pageCount, queryTime) {
		var qt = $('#info_box').html('');
		var numImages = $('#container').children('.box').length;
		qt.append('Found ' + numImages + ' images from ' + pageCount
				+ ' pages in ' + queryTime + ' seconds');
	}
	function clearInfoBox() {
		$('#info_box').html('');
	}
	
	function removeImages(){
		 $('#container').html('');
	}
	
	function showLoading() {
		var loading = $('#container');
		$('#info_box').html('Loading please wait.')
		loading
				.append('<div id="loading" style="visibility: show"> <img id="loadingImg" src="resources/images/loading.gif"/> </div>');
	}

	function hideLoading() {
		var load = $('#loading');
		load.css("visibility", "hidden");
	}
	
	function instruction_text(){
		if(color.length == 1){
			$('#instruction_text').html('Click another color to add more colors to your search');
			
		}else if(color.length > 1){
			$('#instruction_text').html('Click another color to add more colors to your search or to adjust the color ratio drag the black line between the color boxes');
			$('#instruction_text').css('width','380px');
		}else {
			$('#instruction_text').html('Click on the color palette to select color for your search');	
			$('#instruction_text').css('width','162px');

		}
		
	}
</script>
</head>

<body>
	<div id="top">

		<a href="/Collos"><img src="resources/images/colloslogo.png" alt="Collos" id="collos" /></a>

		<div id="palette_container">

			<div id="palette">&nbsp;</div>

		</div>
		<div id="instruction_text">Click on the color palette to select
			color for your search.</div>

	</div>

	<script>
		
	</script>

	<div id="col"></div>
	<div id="info_box">&nbsp;</div>
	<div id="picker">
		<div id="close_picker">
			<input type="image" src="resources/images/delete-1.png"
				onclick="$('#picker').hide();" width="20px" height="20px" />
		</div>
		<div id="color_input">
			<form>
				<input type="text" id="color" name="color" value="" />
			</form>
		</div>
		<div id="colorpicker"></div>
		<div id="button_container">
			<button id="change_color">Change color</button>
		</div>
	</div>
	<div Id="pictures">
		<div id="container" class="transitions-enabled infinite-scroll clearfix masonry"></div>
	</div>

	<script>
		var $container = $('#container');

		$container.imagesLoaded(function() {
			$container.masonry({
				itemSelector : '.box',
				isFitWidth : true,
				isAnimated : true,
				animationOptions : {
					duration : 300,
					easing : 'linear',
					queue : false
				}

			});
		});
	</script>
	<script type="text/javascript">
	var requestPending = false;
	$(window).scroll(function()
	{
    	if($(window).scrollTop() == $(document).height() - $(window).height() && !requestPending && pageCount % 100 == 0){
			var image_count = $('#container').children('.box').length;
			if(image_count >0){
				if (color.length !== 0) {
					requestPending = true;
					hideLoading();
					showLoading();
					request = $.getJSON("/Collos/color?colors="+color+"&freqs="+relativeFreqs+"&pageCount=" + pageCount, function(data) {
						var $newImages = $(data.imageDivs);
						hideLoading();	
						$('#container').append( $newImages ).masonry( 'appended', $newImages, true );
						addColorbox();		
						pageCount += data.pageCount;
						writeQueryTime(pageCount, data.queryTime);
						console.log("data imagePages length" + Object.keys(data.imagePages).length);
						console.log("Before length" + Object.keys(imagePageMap).length);
						var properties = '';
					for(property in data.imagePages){
						imagePageMap[property]= data.imagePages[property]; 
						properties += property + ', ' + data.imagePages[property] + ' \n';
					}
					console.log("Properties:" + properties);
					console.log("After length" + Object.keys(imagePageMap).length);
				requestPending = false;
				});
				} else {
				request.abort();
				console.log("Request aborted");
				}
	    	}
		}
	});
</script>

</body>
</html>
