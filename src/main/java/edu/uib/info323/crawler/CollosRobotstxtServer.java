package edu.uib.info323.crawler;

import org.springframework.stereotype.Component;

import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

@Component
public class CollosRobotstxtServer extends RobotstxtServer {
	public CollosRobotstxtServer() {
		this(new RobotstxtConfig(), new CollosPageFetcher());
	}
	
	public CollosRobotstxtServer(RobotstxtConfig config, PageFetcher pageFetcher) {
		super(config, pageFetcher);
	}

}
