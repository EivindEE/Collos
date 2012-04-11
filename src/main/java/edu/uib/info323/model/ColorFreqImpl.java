package edu.uib.info323.model;

import org.springframework.stereotype.Component;

@Component
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

}
