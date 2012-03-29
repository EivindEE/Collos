package edu.uib.info323.image;

import java.security.InvalidParameterException;

public class CompressedColor implements Comparable<CompressedColor>{
	private final static int RGB_MAX = 255;
	private final static int RGB_MIN = 0;

	private int red;
	private int green;
	private int blue;

	private static int COMPRESSION = 31;

	private int compression = COMPRESSION;

	@Override
	public String toString() {
		return "CompressedColor [red=" + getRange(red) + ", green=" + getRange(green) + ", blue="
				+ getRange(blue) + "]";
	}

	private String getRange(int color){
		return new String("(" + (color * compression) + "-" + (((color + 1) * compression) - 1 ) + ")");
	}

	public CompressedColor(int red, int green, int blue) {
		String exeptionString = "";
		if(red > RGB_MAX || red < RGB_MIN){
			exeptionString = "Red ";
		}
		if(green > RGB_MAX || green < RGB_MIN){
			exeptionString += "Green ";
		}
		if(blue > RGB_MAX || blue < RGB_MIN){
			exeptionString += "Blue";
		}
		if(!exeptionString.equals("")){
			castInputException(exeptionString);
		}
		
		this.red = red / compression;
		this.green = green / compression;
		this.blue = blue / compression;
	}

	private void castInputException(String incorrectInputsString) {
		throw new InvalidParameterException("Color parameter outside of expected range: " 
				+ incorrectInputsString.replace(" ", ", "));
	}

	public int getRed() {
		return red;
	}

	public int getGreen() {
		return green;
	}

	public int getBlue() {
		return blue;
	}

	public int getCompression() {
		return compression;
	}

	public static int getDefaultCompression(){
		return COMPRESSION;
	}

	/**
	 * Provides a hashCode that is unique for all possible RGB values
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
