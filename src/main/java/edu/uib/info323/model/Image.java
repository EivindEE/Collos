package edu.uib.info323.model;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public interface Image {
	
	/**
	 * Returns the uri the image is located
	 * @return the image uri
	 */
	public abstract String getImageUri();

	/**
	 * Returns a list of pages containing the image
	 * @return a list of pages containing the image
	 */
	public abstract List<String> getPageUris();
	
	/**
	 * Add the uri of a page to the list of pages which includes the image.
	 * The methods does not check if the input is valid.
	 * @param pageUri - a string representation of an uri of a page containing the image
	 */
	public abstract void addPageUri(String pageUri);
	
	/**
	 * Add a list of uris of pages to the list of pages which includes the image.
	 * The methods does not check if the input is valid.
	 * @param pageUri - a list of string representations of an uris of pages containing the image
	 */
	public abstract void addPageUri(List<String> pageUris);
	
	/**
	 * Returns the height of the image in pixels
	 * @return the height of the image
	 */
	public abstract int getHeight();
	
	/**
	 * Returns the width of the image in pixels
	 * @return the width of the image
	 */
	public abstract int getWidth();

	public abstract void setHeight(int imageHeight);

	public abstract void setWidth(int imageWidth);
	
	public abstract int getId();

}