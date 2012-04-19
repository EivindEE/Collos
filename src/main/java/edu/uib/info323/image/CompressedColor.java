package edu.uib.info323.image;

import org.springframework.stereotype.Component;

@Component
public interface CompressedColor extends Comparable<CompressedColor>{

	public abstract String toString();

	/**
	 * Returns the compressed red value.
	 * Range 0 - (255 / compression)
	 * @return compressed red value
	 */
	public abstract int getRed();

	/**
	 * Returns the compressed green value.
	 * Range 0 - (255 / compression)
	 * @return compressed green value
	 */
	public abstract int getGreen();

	/**
	 * Returns the compressed blue value.
	 * Range 0 - (255 / compression)
	 * @return compressed blue value
	 */
	public abstract int getBlue();

	/**
	 * Returns the compression rate of the object
	 * @return
	 */
	public abstract int getCompression();

	/**
	 * Provides a hashCode that is unique for all possible RGB values if compression rate is 4 or more.
	 * @return hashValue
	 */
	public abstract int hashCode();

	public abstract boolean equals(Object obj);

	public abstract int compareTo(CompressedColor o);
	
	public abstract int getColor();

}