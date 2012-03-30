package edu.uib.info323.image;

import org.springframework.stereotype.Component;

@Component
public interface CompressedColorFactory {

	/**
	 * Returns a new CompressedColor with the selected parameters
	 * @param red
	 * @param green
	 * @param blue
	 * @param compression
	 * @return a CompressedColor with the selected parameters
	 */
	public abstract CompressedColor createCompressedColor(int red, int green,
			int blue, int compression);

	/**
	 * Returns a new CompressedColor with the selected parameters, and the default compression rate
	 * If the default compression rate is not set by the user it has a sensible default({@value compression})
	 * @param red
	 * @param green
	 * @param blue
	 * @return
	 */
	public abstract CompressedColor createCompressedColor(int red, int green,
			int blue);

	/**
	 *	Sets the default compression for CompressedColors created by the factory  
	 * @param compression
	 */
	public abstract void setDefaultCompression(int compression);

	public abstract int getDefaultCompression();

}