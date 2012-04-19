package edu.uib.info323.image;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import edu.uib.info323.model.ColorFreq;
import edu.uib.info323.model.Image;
/**
 * Basic class for analyzing which colors are in a given image.
 * @author eivindelseth
 *
 */
@Component
public interface ImageProcessor {

	/**
	 * Sets the image to be analyzed to the image
	 * @param imageUrl
	 */
	public abstract void setImage(Image image);
	
//	public abstract void setImage(File f) throws IOException;
//	
//	public abstract void setImage(InputStream inputStream) throws IOException;

//	public abstract HashMap<CompressedColor, Integer> getColorFrequencies();
	
	public abstract List<ColorFreq> getColorFreqs();

	public abstract void setColorFactory(CompressedColorFactory colorFactory);

}