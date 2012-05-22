package edu.uib.info323.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import edu.uib.info323.dao.ImageDao;
import edu.uib.info323.model.Image;

/**
 * Sample controller for going to the home page with a message
 */
@Controller
public class HomeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private ImageDao imageDao;



	/**
	 * Selects the home page and populates the model with a message
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home() {
		return "home";
	}

	@RequestMapping(value = "/color", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody Map<String, Object> color(@RequestParam(required=true) String colors,@RequestParam(required=true) String freqs){
		long queryStart = System.currentTimeMillis();
		LOGGER.debug("Got request for colors and freq: " + colors +" and " + freqs);
		String[] colorArray = colors.split(",");
		List<String>  colorList = new ArrayList<String>();
		for(String color : colorArray) {
			colorList.add("0x" + color);
		}
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String[] freqArray = freqs.split(",");
		List<Integer> freqList = new ArrayList<Integer>();
		for(String freq : freqArray){
			freqList.add((int)Double.parseDouble(freq));
		}
		
		
		List<Image> images = imageDao.getImagesWithColor(colorList, freqList, 0, 100);
		LOGGER.debug("Found " +  images.size() + " images");
		long queryEnd = System.currentTimeMillis();
		responseMap.put("images", images);
		responseMap.put("queryTime", ((queryEnd - queryStart) / 1000.0));
		int pageCount = 0;
		StringBuilder imageUris = new StringBuilder();
		
		for(int i = 0; i < images.size() ; i++) {
			Image image = images.get(i);
			pageCount += image.getPageUris().size();
			float height = 200 * image.getHeight() / image.getWidth();
			
			imageUris.append("<div class='box'> <a class='gallery' id=" + i + " href='" + image.getImageUri() + "'><img width='200px' height='"+height+"px' src='" +  image.getImageUri() + "'></a></div>\n");
		}
		responseMap.put("pageCount", pageCount);
		responseMap.put("imageDivs", imageUris.toString());
		return responseMap;
	}

	@RequestMapping("/spring")
	public String redirectSpring() {
		return "redirect:/search";
	}


}
