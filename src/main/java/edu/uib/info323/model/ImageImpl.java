package edu.uib.info323.model;

import org.springframework.stereotype.Component;

@Component
public class ImageImpl implements Image {
	private String imageUri;
	private String pageUri;
	
	public ImageImpl() {
		
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
	public String getPageUri() {
		return pageUri;
	}
	/* (non-Javadoc)
	 * @see edu.uib.info323.model.Image#setPageUri(java.lang.String)
	 */
	public void setPageUri(String pageUri) {
		this.pageUri = pageUri;
	}
	
	@Override
	public String toString() {
		return "Image [imageUri=" + imageUri + ", pageUri=" + pageUri + "]";
	}
	

}
