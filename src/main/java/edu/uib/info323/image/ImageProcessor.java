package edu.uib.info323.image;

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
	
	public abstract List<ColorFreq> getColorFreqs();

	public abstract void setColorFactory(CompressedColorFactory colorFactory);
	
	public abstract int getImageHeight();
	
	public abstract int getImageWidth();

}