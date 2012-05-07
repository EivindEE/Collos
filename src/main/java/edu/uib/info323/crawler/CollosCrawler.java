package edu.uib.info323.crawler;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import edu.uib.info323.dao.ImageDao;
import edu.uib.info323.model.Image;
import edu.uib.info323.model.ImageFactory;
import edu.uib.info323.model.ImageImpl;

@Scope("prototype")
@Component
public class CollosCrawler extends WebCrawler{

	@Autowired
	private ImageDao imageDao;
	@Autowired
	private ImageFactory imageFactory;

	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" 
			+ "|png|tiff?|mid|mp2|mp3|mp4"
			+ "|wav|avi|mov|mpeg|ram|m4v|pdf" 
			+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	private static final Pattern IMG_PATTERNS = Pattern.compile(".*(\\.(bmp|gif|jpe?g|png|tiff?))$");

	private static final Logger LOGGER = LoggerFactory.getLogger(CollosCrawler.class);
	private List<Image> imageList = new ArrayList<Image>();



	/**
	 * You should implement this function to specify whether
	 * the given url should be crawled or not (based on your
	 * crawling logic).
	 */
	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();
		return !FILTERS.matcher(href).matches();
	}

	/**
	 * This function is called when a page is fetched and ready 
	 * to be processed by your program.
	 */
	@Override
	public void visit(Page page) {          
		String url = page.getWebURL().getURL();
		//		System.out.println("URL: " + url);

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			for(WebURL webUrl : htmlParseData.getOutgoingUrls()) {
				if(IMG_PATTERNS.matcher(webUrl.getURL()).matches()) {
					imageList.add(imageFactory.createImage(webUrl.getURL(),url));
				}
				if(imageList.size() >= 200) {
					imageDao.insert(imageList);
					LOGGER.debug("Added images from crawler " + this);
					imageList.clear();
				}
			}

		}

	}
}
