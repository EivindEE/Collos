package edu.uib.info323.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uib.info323.crawler.CollosCrawler;

@Scope("prototype")
@Controller
public class CrawlerController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerController.class);
	@Autowired
	CrawlController controller;

	public CrawlerController() {
		LOGGER.debug(this.getClass().getName() + " bean created");
	}
	
	@RequestMapping(value = "/crawl", method = RequestMethod.GET)
	public void crawl() throws Exception {
		/*
		 * Since images are binary content, we need to set this parameter to
		 * true to make sure they are included in the crawl.
		 */

		String[] crawlDomains = new String[] { "http://photobucket.com/images/colorful/", "http://www.flickr.com/groups/best100only/pool/", "http://www.reddit.com/r/photography/","http://www.reddit.com/r/PictureChallenge","http://www.reddit.com/r/photos","http://www.nationalgeographic.com/","http://www.sciencephoto.com/","http://www.google.com/search?client=safari&rls=en&q=animal+images&ie=UTF-8&oe=UTF-8" };


		for (String domain : crawlDomains) {
			controller.addSeed(domain);
		}

		//         CollosCrawler.configure(crawlDomains, storageFolder);
		controller.start(CollosCrawler.class, 8);
	}
}

