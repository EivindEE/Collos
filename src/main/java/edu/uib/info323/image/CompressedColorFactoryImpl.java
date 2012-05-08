package edu.uib.info323.image;

import java.awt.Color;

import org.springframework.stereotype.Component;

@Component
public class CompressedColorFactoryImpl implements CompressedColorFactory {
	private int compression = 32;

	/* (non-Javadoc)
	 * @see edu.uib.info323.image.CompressedColorFactory#createCompressedColor(int, int, int, int)
	 */
	public CompressedColorImpl createCompressedColor(int red, int green, int blue, int compression) {
		return new CompressedColorImpl(red, green, blue, compression);
	}
	
	/**
	 * Returns a new CompressedColor with the selected parameters, and the default compression rate
	 * If the default compression rate is not set by the user it has a sensible default({@value compression})
	 * @param red
	 * @param green
	 * @param blue
	 * @return
	 */
	public CompressedColorImpl createCompressedColor(int red, int green, int blue) {
		return this.createCompressedColor(red, green, blue, this.compression);
	}
	
	/* (non-Javadoc)
	 * @see edu.uib.info323.image.CompressedColorFactory#setDefaultCompression(int)
	 */
	public void setDefaultCompression(int compression) {
		this.compression = compression;
	}
	
	/* (non-Javadoc)
	 * @see edu.uib.info323.image.CompressedColorFactory#getDefaultCompression()
	 */
	public int getDefaultCompression() {
		return compression;
	}

	public CompressedColor createCompressedColor(Color color) {
		return this.createCompressedColor(color.getRed(), color.getGreen(), color.getBlue());
	}
}
