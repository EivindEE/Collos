package edu.uib.info323.crawler;

import org.springframework.stereotype.Component;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;

@Component
public class CollosPageFetcher extends PageFetcher {

	
	public CollosPageFetcher() {
		this(new CollosCrawlConfig());
	}
	public CollosPageFetcher(CrawlConfig config) {
		super(config);
	}

}
