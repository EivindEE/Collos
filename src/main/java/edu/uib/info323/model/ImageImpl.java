package edu.uib.info323.model;


public class ImageImpl implements Image {
	private String imageUri;
	private String pageUri;
	
	public ImageImpl(String imageUri, String pageUri){
		this.imageUri = imageUri;
		this.pageUri = pageUri;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((imageUri == null) ? 0 : imageUri.hashCode());
		result = prime * result + ((pageUri == null) ? 0 : pageUri.hashCode());
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
		if (pageUri == null) {
			if (other.getPageUri() != null)
				return false;
		} else if (!pageUri.equals(other.getPageUri()))
			return false;
		return true;
	}
	

	

}
