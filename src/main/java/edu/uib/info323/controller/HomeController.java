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
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

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
	public ModelAndView home() {
		
//		EmbeddedDatabase database = new EmbeddedDatabaseBuilder().addScript("schema.sql").addScript("test-data.sql").build();
		ImageDaoImpl daoImpl = new ImageDaoImpl();
		daoImpl.setDataSource(dataSource);

		File imageFolder = new File("src/main/webapp/resources/testimg");
		for(File f :imageFolder.listFiles()){
			Image img = new ImageImpl();
			img.setImageUri("../resources/testimg/" + f.getName());
			img.setPageUri("http://example.org/" + f.getName());
			daoImpl.insert(img);
		}

		List<ImageImpl> urls = daoImpl.getAllImages();
		LOGGER.error("Returning file list: " + urls);
		Map<String, Object> model = new TreeMap<String, Object>();
		model.put("images", urls);
		ModelAndView mav = new ModelAndView("home", model);
		return mav;
	}
	
	public static void main(String[] args) {
		HomeController controller = new HomeController();
		controller.home();
	}

}
