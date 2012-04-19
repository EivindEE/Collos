package edu.uib.info323.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ColorFreqImpl implements ColorFreq {
	private static final Logger LOGGER = LoggerFactory.getLogger(ColorFreqImpl.class);
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
//		LOGGER.debug("Comparing " + this + " to " + obj);
		if (this == obj) {
//			LOGGER.debug("TRUE: this == obj");
			return true;
		}
		if (obj == null) {
//			LOGGER.debug("FALSE: obj == null");
			return false;
		}
		if (!(obj instanceof ColorFreq)) {
//			LOGGER.debug("FALSE: !(" + obj + " instanceof ColorFreq)");
			return false;
		}
		ColorFreq other = (ColorFreq) obj;
		if (color != other.getColor()) {
//			LOGGER.debug("FALSE:" + this.color + " != " + other.getColor());
			return false;
		}
		if (image == null) {
			if (other.getImage() != null) {
//				LOGGER.debug("FALSE: One image is null");
				return false;
			}
		} else if (!image.equals(other.getImage())) {
			LOGGER.debug("FALSE: !"+this.image + ".equals(" + other.getImage() +")");			
			return false;
		}
		if (relativeFreq != other.getRelativeFreq()) {
//			LOGGER.debug("FALSE: " + this.relativeFreq + " != " + other.getRelativeFreq());
			return false;
		}
//		LOGGER.debug("TRUE: "+ this +" equals "  + other);
		return true;
	}

	@Override
	public String toString() {
		return "ColorFreqImpl [image=" + image + ", color=" + color
				+ ", relativeFreq=" + relativeFreq + "]";
	}





}
