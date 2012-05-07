package edu.uib.info323.model;

import java.util.List;


public class ImageImpl implements Image {
	private String imageUri;
	private List<String> pageUris;
	private int height;
	private int width;
	
	public ImageImpl(String imageUri, List<String> pageUri){
		this(imageUri, pageUri, 0, 0);
	}
	
	public ImageImpl(String imageUri, List<String> pageUris, int height, int width) {
		this.imageUri = imageUri;
		this.pageUris = pageUris;
		this.height = height;
		this.width = width;
	}

	/* (non-Javadoc)
	 * @see edu.uib.info323.model.Image#getImageUri()
	 */
	public String getImageUri() {
		return imageUri;
	}
	/* (non-Javadoc)
	 * @see edu.uib.info323.model.Image#setImageUri(java.lang.String)
	 */
	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}
	/* (non-Javadoc)
	 * @see edu.uib.info323.model.Image#getPageUri()
	 */
	public List<String> getPageUris() {
		return pageUris;
	}
	/* (non-Javadoc)
	 * @see edu.uib.info323.model.Image#setPageUri(java.util.List<String>)
	 */
	public void setPageUri(List<String> pageUri) {
		this.pageUris = pageUri;
	}
	
	@Override
	public String toString() {
		return "Image [imageUri=" + imageUri + ", pageUri=" + pageUris + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((imageUri == null) ? 0 : imageUri.hashCode());
		result = prime * result + ((pageUris == null) ? 0 : pageUris.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Image))
			return false;
		Image other = (Image) obj;
		if (imageUri == null) {
			if (other.getImageUri() != null)
				return false;
		} else if (!imageUri.equals(other.getImageUri()))
			return false;
		if (pageUris == null) {
			if (other.getPageUris() != null)
				return false;
		} else if (!pageUris.equals(other.getPageUris()))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see edu.uib.info323.model.Image#addPageUri(java.lang.String)
	 */
	public void addPageUri(String pageUri) {
		this.pageUris.add(pageUri);
	}

	/* (non-Javadoc)
	 * @see edu.uib.info323.model.Image#addPageUri(java.util.List<String>)
	 */
	public void addPageUri(List<String> pageUris) {
		this.pageUris.addAll(pageUris);
	}

	public int getHeight() {
		return this.height;
	}

	public int getWidth() {
		return this.width;
	}

	public void setHeight(int imageHeight) {
		this.height = imageHeight;
	}

	public void setWidth(int imageWidth) {
		this.width = imageWidth;
	}
}
