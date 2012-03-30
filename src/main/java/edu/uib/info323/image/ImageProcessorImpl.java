package edu.uib.info323.image;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

@Component
public class ImageProcessorImpl implements ImageProcessor {

	private BufferedImage bufferedImage;
	private CompressedColorFactory colorFactory;

	/* (non-Javadoc)
	 * @see edu.uib.info323.image.ImageProcessor#readColors()
	 */
	public HashMap<CompressedColor, Integer> getColorFrequencies() {
		HashMap<CompressedColor, Integer> colorFreq = new HashMap<CompressedColor, Integer>();
		WritableRaster raster = bufferedImage.getRaster();
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



	/* (non-Javadoc)
	 * @see edu.uib.info323.image.ImageProcessor#setImage(java.io.File)
	 */
	public void setImage(File f) throws IOException{
		this.setImage(new FileInputStream(f));
	}

	public void setImage(InputStream inputStream) throws IOException {
		try {
			bufferedImage = ImageIO.read(inputStream);
		}
		catch (IOException e) {
			throw new IOException("Exception occured while reading image from stream: " + inputStream);
		}
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
}