package edu.uib.info323.controller;


import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.sleepycat.je.rep.util.ldiff.Record;

/**
 * Sample controller for going to the home page with a message
 */
@Controller
public class HomeController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(HomeController.class);




	/**
	 * Selects the home page and populates the model with a message
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView home() {
		List<String> urls = new LinkedList<String>();
		urls.add("black.jpg");
		urls.add("white.jpg");
		urls.add("flickr-images-1.jpg");
		urls.add("flickr-images-2.jpg");
		urls.add("flickr-images-3.jpg");
		Map<String, Object> model = new TreeMap<String, Object>();
		model.put("images", urls);
		ModelAndView mav = new ModelAndView("home", model);
		return mav;
	}

}
