package edu.uib.info323.model;



public class ColorFreqImpl implements ColorFreq {
	private Image image;
	private int color;
	private int relativeFreq;
	
	public ColorFreqImpl() {
	}
	
	public ColorFreqImpl(Image image, int color, int relativeFreq) {
		this.image = image;
		this.color = color;
		this.relativeFreq = relativeFreq;
	}
	
	/* (non-Javadoc)
	 * @see edu.uib.info323.model.ColorFreq#getImage()
	 */
	public Image getImage() {
		return image;
	}

	/* (non-Javadoc)
	 * @see edu.uib.info323.model.ColorFreq#getColor()
	 */
	public int getColor() {
		return color;
	}

	/* (non-Javadoc)
	 * @see edu.uib.info323.model.ColorFreq#getRelativeFreq()
	 */
	public int getRelativeFreq() {
		return relativeFreq;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + color;
		result = prime * result + ((image == null) ? 0 : image.hashCode());
		result = prime * result + relativeFreq;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ColorFreqImpl))
			return false;
		ColorFreqImpl other = (ColorFreqImpl) obj;
		if (color != other.color)
			return false;
		if (image == null) {
			if (other.image != null)
				return false;
		} else if (!image.equals(other.image))
			return false;
		if (relativeFreq != other.relativeFreq)
			return false;
		return true;
	}
	
	

}
