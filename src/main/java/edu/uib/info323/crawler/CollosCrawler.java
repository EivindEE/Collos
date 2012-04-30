	package edu.uib.info323.crawler;
	
	
	import java.io.BufferedWriter;
	import java.io.File;
	import java.io.FileWriter;
	import java.io.IOException;
	import java.util.HashMap;
	import java.util.List;
	import java.util.Map;
	import java.util.regex.Pattern;
	
	import org.slf4j.Logger;
	import org.slf4j.LoggerFactory;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.stereotype.Component;
	
	import edu.uci.ics.crawler4j.crawler.Page;
	import edu.uci.ics.crawler4j.crawler.WebCrawler;
	import edu.uci.ics.crawler4j.parser.HtmlParseData;
	import edu.uci.ics.crawler4j.url.WebURL;
	import edu.uib.info323.dao.ImageDao;
	import edu.uib.info323.model.Image;
	import edu.uib.info323.model.ImageImpl;
	
	public class CollosCrawler extends WebCrawler{
		
		@Autowired
		private ImageDao imageDao;
		
		private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" 
				+ "|png|tiff?|mid|mp2|mp3|mp4"
				+ "|wav|avi|mov|mpeg|ram|m4v|pdf" 
				+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
		
	    private static final Pattern IMG_PATTERNS = Pattern.compile(".*(\\.(bmp|gif|jpe?g|png|tiff?))$");
	
		private static final Logger LOGGER = LoggerFactory.getLogger(CollosCrawler.class);
		private Map<String,String> uriMap = new HashMap<String, String>();
		
		private File sqlStore = new File("src/main/resources/image.sql");
		private BufferedWriter writer;
	
		private int counter;
		public CollosCrawler() {
			try {
				sqlStore.createNewFile();
				writer = new BufferedWriter(new FileWriter(sqlStore));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	
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
							uriMap.put(webUrl.getURL(),url);
					}
					if(uriMap.size() >= 200) {
						try {
							for(String imageUri : uriMap.keySet()) {
								writer.append("INSERT INTO IMAGE (image_uri) VALUES ('" + imageUri + "');\n");
							}
							writer.flush();
							for(String imageUri : uriMap.keySet()) {
								writer.append("INSERT INTO IMAGE_PAGE (image_uri, page_uri) VALUES ('" + imageUri + "','" + uriMap.get(imageUri) + "');\n");
							}
							writer.flush();
							uriMap.clear();
							System.out.println("image.sql filled with test data");
							writer.close();
						}catch (Exception e) {
							// TODO: handle exception
						}
					}
				}
				
			}
			
		}
	}
