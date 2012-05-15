package edu.uib.info323.crawler;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.CustomFetchStatus;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.frontier.DocIDServer;
import edu.uci.ics.crawler4j.frontier.Frontier;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.parser.Parser;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
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
		System.out.println("Url: " + url);

	//	if (page.getParseData() instanceof CollosHtmlParseData) {
			CollosHtmlParseData htmlParseData = (CollosHtmlParseData) page.getParseData();
			for(WebURL webUrl : htmlParseData.getImgUrls()) {
				//if(IMG_PATTERNS.matcher(webUrl.getURL()).matches()) {
				//	imageList.add(imageFactory.createImage(webUrl.getURL(),url));
				//}
				//if(imageList.size() >= 200) {
				//	imageDao.insert(imageList);
				//	LOGGER.debug("Added images from crawler " + this);
				//	imageList.clear();
				//}
				System.out.println("Img: " + webUrl.getURL());
		//	}

		}

	}
	private void processPage(WebURL curURL) {
		if (curURL == null) {
			return;
		}
		PageFetchResult fetchResult = null;
		try {
			fetchResult = pageFetcher.fetchHeader(curURL);
			int statusCode = fetchResult.getStatusCode();
			handlePageStatusCode(curURL, statusCode, CustomFetchStatus.getStatusDescription(statusCode));
			if (statusCode != HttpStatus.SC_OK) {
				if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
					if (myController.getConfig().isFollowRedirects()) {
						String movedToUrl = fetchResult.getMovedToUrl();
						if (movedToUrl == null) {
							return;
						}
						int newDocId = docIdServer.getDocId(movedToUrl);
						if (newDocId > 0) {
							// Redirect page is already seen
							return;
						} else {
							WebURL webURL = new WebURL();
							webURL.setURL(movedToUrl);
							webURL.setParentDocid(curURL.getParentDocid());
							webURL.setParentUrl(curURL.getParentUrl());
							webURL.setDepth(curURL.getDepth());
							webURL.setDocid(-1);
							if (shouldVisit(webURL) && robotstxtServer.allows(webURL)) {
								webURL.setDocid(docIdServer.getNewDocID(movedToUrl));
								frontier.schedule(webURL);
							}
						}
					}
				} else if (fetchResult.getStatusCode() == CustomFetchStatus.PageTooBig) {
					logger.info("Skipping a page which was bigger than max allowed size: " + curURL.getURL());
				}
				return;
			}

			if (!curURL.getURL().equals(fetchResult.getFetchedUrl())) {
				if (docIdServer.isSeenBefore(fetchResult.getFetchedUrl())) {
					// Redirect page is already seen
					return;
				}
				curURL.setURL(fetchResult.getFetchedUrl());
				curURL.setDocid(docIdServer.getNewDocID(fetchResult.getFetchedUrl()));
			}

			Page page = new Page(curURL);
			int docid = curURL.getDocid();
			if (fetchResult.fetchContent(page) && parser.parse(page, curURL.getURL())) {
				ParseData parseData = page.getParseData();
				if (parseData instanceof CollosHtmlParseData) {
					CollosHtmlParseData htmlParseData = (CollosHtmlParseData) parseData;

					List<WebURL> toSchedule = new ArrayList<WebURL>();
					int maxCrawlDepth = myController.getConfig().getMaxDepthOfCrawling();
					for (WebURL webURL : htmlParseData.getOutgoingUrls()) {
						webURL.setParentDocid(docid);
						webURL.setParentUrl(curURL.getURL());
						int newdocid = docIdServer.getDocId(webURL.getURL());
						if (newdocid > 0) {
							// This is not the first time that this Url is
							// visited. So, we set the depth to a negative
							// number.
							webURL.setDepth((short) -1);
							webURL.setDocid(newdocid);
						} else {
							webURL.setDocid(-1);
							webURL.setDepth((short) (curURL.getDepth() + 1));
							if (maxCrawlDepth == -1 || curURL.getDepth() < maxCrawlDepth) {
								if (shouldVisit(webURL) && robotstxtServer.allows(webURL)) {
									webURL.setDocid(docIdServer.getNewDocID(webURL.getURL()));
									toSchedule.add(webURL);
								}
							}
						}
					}
					frontier.scheduleAll(toSchedule);
				}
				visit(page);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage() + ", while processing: " + curURL.getURL());
		} finally {
			if (fetchResult != null) {
				fetchResult.discardContentIfNotConsumed();
			}
		}
	}
}
