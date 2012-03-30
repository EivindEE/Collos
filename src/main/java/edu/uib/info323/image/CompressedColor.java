package edu.uib.info323.image;

import java.security.InvalidParameterException;
/**
 * CompressedColor contains information about a color in a reduced RGB color space given a certain compression.
 * Accepts RGB color ranges (0-255), and comression rates ³4. 
 *
 */

public class CompressedColor implements Comparable<CompressedColor> {
	private final static int RGB_MAX = 255;
	private final static int RGB_MIN = 0;

	private int red;
	private int green;
	private int blue;
	private int compression;

	@Override
	public String toString() {
		return "CompressedColor [red=" + getRange(red) + ", green=" + getRange(green) + ", blue="
				+ getRange(blue) + "]";
	}
	
	private String getRange(int color){
		return new String("(" + (color * compression) + "-" + (((color + 1) * compression) - 1 ) + ")");
	}
	
	/**
	 * Creates a new compressed color object.
	 * Allowed ranges for colors is 0-255.
	 * Allowed range for compression is ³4 or greater
	 * @param red (0-255)
	 * @param green (0-255)
	 * @param blue (0-255)
	 * @param compression (Must be ³4 )
	 */
	public CompressedColor(int red, int green, int blue, int compression) {
		String exeptionString = "";
		if(red > RGB_MAX || red < RGB_MIN){
			exeptionString = "Red: " + red + ".";
		}
		if(green > RGB_MAX || green < RGB_MIN){
			exeptionString += "Green: " + green + ".";
		}
		if(blue > RGB_MAX || blue < RGB_MIN){
			exeptionString += "Blue: " + blue + ".";
		}
		if(compression < 4) {
			exeptionString += "Compression rate: " + compression;
		}
		
		if(!exeptionString.equals("")){
			castInputException(exeptionString);
		}
		this.compression = compression;
		this.red = red / this.compression;
		this.green = green / this.compression;
		this.blue = blue / this.compression;
	}

	private void castInputException(String incorrectInputsString) {
		String exceptionString = "Parameter(s) outside of expected range: " 
				+ incorrectInputsString.replace(".", ", ");
		if(exceptionString.endsWith(", "))
			exceptionString = exceptionString.substring(0, exceptionString.length()-2);
		
		throw new InvalidParameterException(exceptionString);
	}

	/**
	 * Returns the compressed red value.
	 * Range 0 - (255 / compression)
	 * @return compressed red value
	 */
	public int getRed() {
		return red;
	}

	/**
	 * Returns the compressed green value.
	 * Range 0 - (255 / compression)
	 * @return compressed green value
	 */
	public int getGreen() {
		return green;
	}

	/**
	 * Returns the compressed blue value.
	 * Range 0 - (255 / compression)
	 * @return compressed blue value
	 */
	public int getBlue() {
		return blue;
	}


	/**
	 * Returns the compression rate of the object
	 * @return
	 */
	public int getCompression() {
		return compression;
	}

	/**
	 * Provides a hashCode that is unique for all possible RGB values if compression rate is 4 or more.
	 * @return hashValue
	 */
	@Override
	public int hashCode() {
		int result = 0;
		result += blue;
		result += green * RGB_MAX;
		result += red * (RGB_MAX * RGB_MAX); // RGB_MAX squared
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompressedColor other = (CompressedColor) obj;
		if (blue != other.blue)
			return false;
		if (green != other.green)
			return false;
		if (red != other.red)
			return false;
		return true;
	}



	public int compareTo(CompressedColor o) {
		if(this.equals(o)){
			return 0;
		}
		if(red != o.red){
			return red - o.red;
		}
		else if(green != o.green){
			return green - o.green;
		}
		else{
			return blue - o.blue;
		}
	}
}
