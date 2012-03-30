package edu.uib.info323.image;

import java.security.InvalidParameterException;

import org.springframework.stereotype.Component;
/**
 * CompressedColor contains information about a color in a reduced RGB color space given a certain compression.
 * Accepts RGB color ranges (0-255), and comression rates ³4. 
 *
 */

public class CompressedColorImpl implements CompressedColor {
	private final static int RGB_MAX = 255;
	private final static int RGB_MIN = 0;

	private int red;
	private int green;
	private int blue;
	private int compression;

	/* (non-Javadoc)
	 * @see edu.uib.info323.image.CompressedColor#toString()
	 */
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
	public CompressedColorImpl(int red, int green, int blue, int compression) {
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

	/* (non-Javadoc)
	 * @see edu.uib.info323.image.CompressedColor#getRed()
	 */
	public int getRed() {
		return red;
	}

	/* (non-Javadoc)
	 * @see edu.uib.info323.image.CompressedColor#getGreen()
	 */
	public int getGreen() {
		return green;
	}

	/* (non-Javadoc)
	 * @see edu.uib.info323.image.CompressedColor#getBlue()
	 */
	public int getBlue() {
		return blue;
	}


	/* (non-Javadoc)
	 * @see edu.uib.info323.image.CompressedColor#getCompression()
	 */
	public int getCompression() {
		return compression;
	}

	/* (non-Javadoc)
	 * @see edu.uib.info323.image.CompressedColor#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = 0;
		result += blue;
		result += green * RGB_MAX;
		result += red * (RGB_MAX * RGB_MAX); // RGB_MAX squared
		return result;
	}

	/* (non-Javadoc)
	 * @see edu.uib.info323.image.CompressedColor#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompressedColorImpl other = (CompressedColorImpl) obj;
		if (blue != other.blue)
			return false;
		if (green != other.green)
			return false;
		if (red != other.red)
			return false;
		return true;
	}



	/* (non-Javadoc)
	 * @see edu.uib.info323.image.CompressedColor#compareTo(edu.uib.info323.image.CompressedColorImpl)
	 */
	public int compareTo(CompressedColor o) {
		if(this.equals(o)){
			return 0;
		}
		if(red != o.getRed()){
			return red - o.getRed();
		}
		else if(green != o.getGreen()){
			return green - o.getGreen();
		}
		else{
			return blue - o.getBlue();
		}
	}
}
