package edu.uib.info323.image;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
/**
 * Basic class for analyzing which colors are in a given image.
 * @author eivindelseth
 *
 */
@Component
public interface ImageProcessor {

	public abstract void setImage(File f) throws IOException;
	
	public abstract void setImage(InputStream inputStream) throws IOException;

	public abstract HashMap<CompressedColor, Integer> getColorFrequencies();

}