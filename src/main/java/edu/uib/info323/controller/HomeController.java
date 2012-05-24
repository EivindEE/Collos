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

	private Integer maxPages = 100;
	private Integer limit = 100;



	/**
	 * Selects the home page and populates the model with a message
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home() {
		return "home";
	}

	@RequestMapping(value = "/color", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody Map<String, Object> color(@RequestParam(required=true) String colors,@RequestParam(required=true) String freqs,
			@RequestParam(required=false) Integer pageCount){
		Map<String, Object> responseMap = new HashMap<String, Object>();
		long queryStart = System.currentTimeMillis();
		LOGGER.debug("Got request for colors and freq: " + colors +" and " + freqs + ", page count:" + pageCount);

		pageCount = validateLimitLow(pageCount);

		List<String> colorList = toColorList(colors);

		List<Integer> freqList = toFreqList(freqs);

		List<Image> images = imageDao.getImagesWithColor(colorList, freqList, limit, pageCount); 
		Map<String,List<String>> imagePages = getPageUris(images);

		//		LOGGER.debug("ImagePages" + imagePages);
		long queryEnd = System.currentTimeMillis();
		responseMap.put("imagePages", imagePages);
		responseMap.put("queryTime", ((queryEnd - queryStart) / 1000.0));



		pageCount = 0; 
		pageCount = getPageCount(images);

		StringBuilder imageDivs = createImageDivs(pageCount, images);

		responseMap.put("pageCount", pageCount);
		LOGGER.debug("Found " +  images.size() + " images on " + pageCount + " pages");
		responseMap.put("imageDivs", imageDivs.toString());
		return responseMap;
	}

	private StringBuilder createImageDivs(Integer pageCount, List<Image> images) {
		StringBuilder imageUris = new StringBuilder();
		for(int i = 0; i < images.size() ; i++) {
			Image image = images.get(i);

			float height = 200 * image.getHeight() / image.getWidth();

			imageUris.append("<div class='box'> <a class='gallery' id=" + (pageCount + i) + " href='" + image.getImageUri() + "'><img width='200px' height='"+height+"px' src='" +  image.getImageUri() + "'></a></div>\n");
		}
		return imageUris;
	}

	private int getPageCount(List<Image> images) {
		int pageCount = 0;
		LOGGER.debug("Getting page count for list of size:" + images.size());
		for(Image image : images){
			pageCount += image.getPageUris().size();
		}
		return pageCount;
	}

	private Map<String, List<String>> getPageUris(List<Image> images) {
		Map<String, List<String>> imagePages = new HashMap<String,List<String>>(); 
		for(Image image : images){
			imagePages.put(image.getImageUri(), image.getPageUris());
		}
		return imagePages;
	}

	private List<Integer> toFreqList(String freqs) {
		List<Integer> freqList = new ArrayList<Integer>();
		String[] freqArray = freqs.split(",");
		for(String freq : freqArray){
			freqList.add((int)Double.parseDouble(freq));
		}
		return freqList;
	}

	private List<String> toColorList(String colors) {
		String[] colorArray = colors.split(",");
		List<String>  colorList = new ArrayList<String>();
		for(String color : colorArray) {
			colorList.add("0x" + color);
		}
		return colorList;
	}

	private Integer validateLimitLow(Integer limitLow) {
		if(limitLow == null) {
			limitLow = 0;
		}
		return limitLow;
	}

	private Integer validateLimitHight(Integer limitHigh) {
		if(limitHigh == null) {
			limitHigh = maxPages;
		}
		return limitHigh;
	}

	@RequestMapping("/spring")
	public String redirectSpring() {
		return "redirect:/search";
	}


}
