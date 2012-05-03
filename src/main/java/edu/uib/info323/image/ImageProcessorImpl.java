package edu.uib.info323.image;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.InputStream;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.uib.info323.model.ColorFreq;
import edu.uib.info323.model.ColorFreqImpl;
import edu.uib.info323.model.Image;
import edu.uib.info323.model.ImageImpl;

@Component
public class ImageProcessorImpl implements ImageProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageProcessorImpl.class);
	private BufferedImage bufferedImage;
	
	@Autowired
	private CompressedColorFactory colorFactory;
	
	private Integer numberOfPixels;
	private Image image;
	private int threshold = 5;

	/* (non-Javadoc)
	 * @see edu.uib.info323.image.ImageProcessor#readColors()
	 */
	private HashMap<CompressedColor, Integer> getColorFrequencies() {
		HashMap<CompressedColor, Integer> colorFreq = new HashMap<CompressedColor, Integer>();
		WritableRaster raster = bufferedImage.getRaster();
		numberOfPixels = raster.getHeight() * raster.getWidth();
		for(int x = raster.getMinX(); x < raster.getWidth(); x++){
			for(int y = raster.getMinY(); y < raster.getHeight(); y++){
				int[] pixel = raster.getPixel(x, y, new int[3]);
				CompressedColor color = colorFactory.createCompressedColor(pixel[0], pixel[1], pixel[2]);
				Integer count = colorFreq.containsKey(color) ? colorFreq.get(color) : 0;
				colorFreq.put(color,++count);
			}
		}
		return colorFreq;
	}



	/**
	 * Sets the compressionrate of the RGB color space.
	 * Accepted values are ³ 4
	 * @param imageCompressionRate
	 */
	public void setImageCompression(int imageCompressionRate) {
		if(imageCompressionRate >= 4) {
			colorFactory.setDefaultCompression(imageCompressionRate);
		}
		else {
			throw new InvalidParameterException("Compression rate must be ³4 but was " + imageCompressionRate );
		}
	}

	/**
	 * Sets the compressionrate of the RGB color space.
	 * Accepted values are ³ 4
	 * @param imageCompressionRate
	 */
	public int getImageCompression() {
		return colorFactory.getDefaultCompression();
	}



	public CompressedColorFactory getColorFactory() {
		return colorFactory;
	}



	public void setColorFactory(CompressedColorFactory colorFactory) {
		this.colorFactory = colorFactory;
	}



	public void setImage(Image image) {
		try {
			this.image = image;
			InputStream inputStream = new URL(image.getImageUri()).openStream();
			bufferedImage = ImageIO.read(inputStream);
		}catch (Exception e) {
			LOGGER.error("Could not open stream for image " + image + ". Got exception of type: " + e.getClass());
			throw new InvalidParameterException("Could not open stream for image " + image);
		}

	}



	public ArrayList<ColorFreq> getColorFreqs() {
		Map<CompressedColor, Integer> colorMap = getColorFrequencies();
		ArrayList<ColorFreq> colorFreqs = new ArrayList<ColorFreq>();
		for(CompressedColor color : colorMap.keySet()) {
			Integer freq = colorMap.get(color);
			int relativeFreq = (int) ((freq.doubleValue() / numberOfPixels.doubleValue()) * 100);
			if(relativeFreq > threshold) {
				colorFreqs.add(new ColorFreqImpl(image, color.getColor(), relativeFreq));
			}

		}
		return colorFreqs;
	}

	public static void main(String[] args) {
		ImageProcessor imageProcessor = new ImageProcessorImpl();
		Image image = new ImageImpl("http://pritisprettyblog.files.wordpress.com/2012/01/ying-yang.jpg", "http://pritisprettyblog.wordpress.com/2012/01/08/ying-yang/");
		imageProcessor.setImage(image);
		imageProcessor.setColorFactory(new CompressedColorFactoryImpl());
		List<ColorFreq> colors = imageProcessor.getColorFreqs();
		for(ColorFreq color : colors) {
			System.out.println(color);
		}
	}
}