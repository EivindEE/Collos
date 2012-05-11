package edu.uib.info323.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;

import edu.uib.info323.model.ColorFreq;
import edu.uib.info323.model.ColorFreqFactory;
import edu.uib.info323.model.Image;
import edu.uib.info323.model.ImageFactory;
import edu.uib.info323.model.ImageImpl;

@Scope("prototype")
@Component
public class ImageProcessorImpl implements ImageProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageProcessorImpl.class);

	@Autowired
	private CompressedColorFactory colorFactory;

	@Autowired
	private ColorFreqFactory freqFactory;

	private int imageHeight;
	private int imageWidth;

	private Integer numberOfPixels;
	private Image image;
	private int threshold = 5;

	private BufferedImage bufferedImage;

	/* (non-Javadoc)
	 * @see edu.uib.info323.image.ImageProcessor#readColors()
	 */
	private HashMap<CompressedColor, Integer> getColorFrequencies() {
		HashMap<CompressedColor, Integer> colorFreq = new HashMap<CompressedColor, Integer>();

		for(int x = bufferedImage.getMinX(); x < this.imageWidth; x++){
			for(int y = bufferedImage.getMinY(); y < this.imageHeight; y++){
				int pixel = bufferedImage.getRGB(x, y);
				int transparency = ((pixel & 0xff000000) >> 24);
				if(transparency != 0x00) {
					CompressedColor color = colorFactory.createCompressedColor(new Color(pixel));
					Integer count = colorFreq.containsKey(color) ? colorFreq.get(color) : 0;
					colorFreq.put(color,++count);
				}
			}
		}
		return colorFreq;
	}



	/**
	 * Sets the compressionrate of the RGB color space.
	 * Accepted values are � 4
	 * @param imageCompressionRate
	 */
	public void setImageCompression(int imageCompressionRate) {
		if(imageCompressionRate >= 4) {
			colorFactory.setDefaultCompression(imageCompressionRate);
		}
		else {
			throw new InvalidParameterException("Compression rate must be �4 but was " + imageCompressionRate );
		}
	}

	/**
	 * Sets the compressionrate of the RGB color space.
	 * Accepted values are � 4
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
			this.imageHeight = bufferedImage.getHeight();
			this.imageWidth = bufferedImage.getWidth();
			if( this.imageWidth < 100 || this.imageHeight < 100) {
				throw new InvalidParameterException("Image less that 100 x 100 px. To small for db.");
			}
			this.numberOfPixels = this.imageHeight * this.imageWidth;
		}catch (IOException e) {
			throw new InvalidParameterException("Could not open stream for image " + image);
		}

	}



	public ArrayList<ColorFreq> getColorFreqs() {
		Map<CompressedColor, Integer> colorMap = getColorFrequencies();
		ArrayList<ColorFreq> colorFreqs = new ArrayList<ColorFreq>();
		for(CompressedColor color : colorMap.keySet()) {
			Integer freq = colorMap.get(color);
			int relativeFreq = (int) ((freq.doubleValue() / numberOfPixels.doubleValue()) * 100);
			if(relativeFreq >= threshold) {
				if(relativeFreq > 100) {
					LOGGER.error("Relative freq > 100 for color in " + image.getImageUri());
				}
				colorFreqs.add(freqFactory.createColorFreq(image, color.getColor(), relativeFreq));
			}

		}
		return colorFreqs;
	}


	public static void main(String... args) {
		ApplicationContext context = new FileSystemXmlApplicationContext(new String[] {"src/main/webapp/WEB-INF/spring/app/model-context.xml", "src/main/webapp/WEB-INF/spring/app/db-context.xml"});   
		ImageProcessor imageProcessor = context.getBean(ImageProcessor.class);
		ImageFactory imageFactory = context.getBean(ImageFactory.class);
		Image image = imageFactory.createImage("http://upload.wikimedia.org/wikipedia/commons/thumb/d/d5/Lojban_flag_public_domain.svg/300px-Lojban_flag_public_domain.svg.png","");
		imageProcessor.setImage(image);
		imageProcessor.setColorFactory(new CompressedColorFactoryImpl());
		List<ColorFreq> colors = imageProcessor.getColorFreqs();
		for(ColorFreq color : colors) {
			System.out.println(color);
		}
	}



	public int getImageHeight() {
		return this.imageHeight;
	}



	public int getImageWidth() {
		return this.imageWidth;
	}
}