package edu.uib.info323.crawler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.uci.ics.crawler4j.parser.ExtractedUrlAnchorPair;
import edu.uci.ics.crawler4j.parser.HtmlContentHandler;

public class CollosHtmlContentHandler extends HtmlContentHandler {
	private enum Element {
		A, AREA, LINK, IFRAME, FRAME, EMBED, IMG, BASE, META, BODY
	}

	private static class HtmlFactory {
		private static Map<String, Element> name2Element;

		static {
			name2Element = new HashMap<String, Element>();
			for (Element element : Element.values()) {
				name2Element.put(element.toString().toLowerCase(), element);
			}
		}

		public static Element getElement(String name) {
			return name2Element.get(name);
		}
	}

	private String base;
	private String metaRefresh;
	private String metaLocation;

	private boolean isWithinBodyElement;
	private StringBuilder bodyText;

	private List<ExtractedUrlAnchorPair> outgoingUrls;
	private List<ExtractedUrlAnchorPair> imgUrls;
	
	private ExtractedUrlAnchorPair curUrl = null;
	private boolean anchorFlag = false;
	private StringBuilder anchorText = new StringBuilder();

	public CollosHtmlContentHandler() {
		isWithinBodyElement = false;
		bodyText = new StringBuilder();
		outgoingUrls = new ArrayList<ExtractedUrlAnchorPair>();
		imgUrls = new ArrayList<ExtractedUrlAnchorPair>();
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		Element element = HtmlFactory.getElement(localName);

		if (element == Element.A || element == Element.AREA || element == Element.LINK) {
			String href = attributes.getValue("href");
			if (href != null) {
				anchorFlag = true;
				curUrl = new ExtractedUrlAnchorPair();
				curUrl.setHref(href);
				outgoingUrls.add(curUrl);
			}
			return;
		}

		if (element == Element.IMG) {
			String imgSrc = attributes.getValue("src");
			if (imgSrc != null) {
				curUrl = new ExtractedUrlAnchorPair();
				curUrl.setHref(imgSrc);
				outgoingUrls.add(curUrl);
				imgUrls.add(curUrl);
			}
			return;
		}

		if (element == Element.IFRAME || element == Element.FRAME || element == Element.EMBED) {
			String src = attributes.getValue("src");
			if (src != null) {
				curUrl = new ExtractedUrlAnchorPair();
				curUrl.setHref(src);
				outgoingUrls.add(curUrl);
			}
			return;
		}

		if (element == Element.BASE) {
			if (base != null) { // We only consider the first occurrence of the
								// Base element.
				String href = attributes.getValue("href");
				if (href != null) {
					base = href;
				}
			}
			return;
		}

		if (element == Element.META) {
			String equiv = attributes.getValue("http-equiv");
			String content = attributes.getValue("content");
			if (equiv != null && content != null) {
				equiv = equiv.toLowerCase();

				// http-equiv="refresh" content="0;URL=http://foo.bar/..."
				if (equiv.equals("refresh") && (metaRefresh == null)) {
					int pos = content.toLowerCase().indexOf("url=");
					if (pos != -1) {
						metaRefresh = content.substring(pos + 4);
					}
				}

				// http-equiv="location" content="http://foo.bar/..."
				if (equiv.equals("location") && (metaLocation == null)) {
					metaLocation = content;
				}
			}
			return;
		}

		if (element == Element.BODY) {
			isWithinBodyElement = true;
		}
	}
	
	public List<ExtractedUrlAnchorPair> getImgUrls() {
		return imgUrls;
	}	
}
