package edu.uib.info323.image;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
/**
 * Basic class for analyzing which colors are in a given image.
 * @author eivindelseth
 *
 */
public interface ImageProcessor {

	public abstract void setImage(File f) throws IOException;
	
	public abstract void setImage(InputStream inputStream) throws IOException;
	

	public abstract void readColors();

	public abstract Map<CompressedColor, Integer> getColorFrequencies();

}