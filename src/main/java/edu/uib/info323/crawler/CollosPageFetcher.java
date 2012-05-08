package edu.uib.info323.crawler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;

@Component
public class CollosPageFetcher extends PageFetcher {

	@Autowired
	public CollosPageFetcher(CrawlConfig config) {
		super(config);
	}

}
