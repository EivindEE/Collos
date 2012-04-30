package edu.uib.info323.crawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class CollosCrawlerController {
	public static void main(String[] args) throws Exception {
		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder("Collos/crawl");

		//         config.setCrawlStorageFolder(rootFolder);

		/*
		 * Since images are binary content, we need to set this parameter to
		 * true to make sure they are included in the crawl.
		 */
		config.setIncludeBinaryContentInCrawling(true);

		String[] crawlDomains = new String[] { "http://www.flickr.com" };

		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
		for (String domain : crawlDomains) {
			controller.addSeed(domain);
		}

		//         CollosCrawler.configure(crawlDomains, storageFolder);
		controller.start(CollosCrawler.class, 1);
	}
	
}
