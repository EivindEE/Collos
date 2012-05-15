package edu.uib.info323.crawler;

import java.util.List;

import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class CollosHtmlParseData extends HtmlParseData {
	private String html;
	private String text;
	private String title;

	private List<WebURL> outgoingUrls;
	private List<WebURL> imgUrls;

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<WebURL> getOutgoingUrls() {
		return outgoingUrls;
	}

	public void setOutgoingUrls(List<WebURL> outgoingUrls) {
		this.outgoingUrls = outgoingUrls;
	}
	
	public List<WebURL> getImgUrls() {
		return imgUrls;
	}
	
	public void setImgUrls(List<WebURL> imgUrls) {
		this.imgUrls = imgUrls;
	}
	
	@Override
	public String toString() {
		return text;
	}
}
