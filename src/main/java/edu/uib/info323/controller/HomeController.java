package edu.uib.info323.controller;


import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import edu.uib.info323.dao.ImageDao;
import edu.uib.info323.dao.ImageDaoImpl;
import edu.uib.info323.model.Image;
import edu.uib.info323.model.ImageImpl;

/**
 * Sample controller for going to the home page with a message
 */
@Controller
public class HomeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private DataSource dataSource;
	
	
	/**
	 * Selects the home page and populates the model with a message
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home() {
		return "home";
	}
	
	/**
	 * Selects the home page and populates the model with a message
	 */
	@RequestMapping(value = "/search")
	@ResponseStatus(value = HttpStatus.OK)
	public ModelAndView search(@RequestParam(required=false) String color) {
		LOGGER.error("Returning color: " + color);
		LOGGER.error(color);
		//		EmbeddedDatabase database = new EmbeddedDatabaseBuilder().addScript("schema.sql").addScript("test-data.sql").build();
		ImageDao daoImpl = new ImageDaoImpl();
		daoImpl.setDataSource(dataSource);
		List<Image> urls = daoImpl.getAllImages();
		//LOGGER.error("Returning file list: " + urls);
		Map<String, Object> model = new TreeMap<String, Object>();
		model.put("images", urls);
		ModelAndView mav = new ModelAndView("home", model);
		return mav;
	}
	
	@RequestMapping(value = "/color")
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public String color(@RequestParam(required=false) String color){
		LOGGER.error("Returning color: " + color);

	return(color);
	}
	
	@RequestMapping("/spring")
	public String redirectSpring() {
		ImageDao daoImpl = new ImageDaoImpl();
		daoImpl.setDataSource(dataSource);
		
		File imageFolder = new File("src/main/webapp/resources/testimg");
		for(File f :imageFolder.listFiles()){
			Image img = new ImageImpl("resources/testimg/" + f.getName(),"http://example.org/" + f.getName());
			daoImpl.insert(img);
		}
		return "redirect:/search";
	}
//	public static void main(String[] args) {
//		HomeController controller = new HomeController();
//		controller.home();
//	}

}
