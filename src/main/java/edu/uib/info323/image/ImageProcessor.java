package edu.uib.info323.image;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TEST
public class ImageProcessor {
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageProcessor.class); 
	BufferedImage bufferedImage;
	Map<CompressedColor, BigDecimal> colorFreq = new HashMap<CompressedColor, BigDecimal>();
	BigDecimal pixelCount;
	// private BigDecimal threshhold = new BigDecimal("0.01"); // TODO

	public void setImage(File f) throws IOException{
		try {

			bufferedImage = ImageIO.read(f);
		} catch (IOException e) {
			throw new IOException("Exception occured while reading image from file: " + f);
		}
	}

	public void readColors() {
		WritableRaster raster = bufferedImage.getRaster();
		BigDecimal imageHeightInPixels = new BigDecimal(new Integer(raster.getHeight()).toString());
		BigDecimal imageWidthInPixels = new BigDecimal(new Integer(raster.getWidth()).toString());
		LOGGER.debug("Image height in pixels: " + imageHeightInPixels);
		LOGGER.debug("Image width in pixels: " + imageWidthInPixels);
		//		System.out.println("Number of pixels in width:" + imageWidthInPixels);
		//		System.out.println("Number of pixels in height:" + imageHeightInPixels);
		pixelCount = imageHeightInPixels.multiply(imageWidthInPixels);
		//		System.out.println("Total number of pixels:" + pixelCount);
		for(int x = raster.getMinX(); x < raster.getWidth(); x++){
			for(int y = raster.getMinY(); y < raster.getHeight(); y++){
				int[] pixel = raster.getPixel(x, y, new int[3]);
				CompressedColor color = new CompressedColor(pixel[0], pixel[1], pixel[2]);
				BigDecimal count = colorFreq.containsKey(color) ? colorFreq.get(color) : new BigDecimal("0");
				colorFreq.put(color,count.add(new BigDecimal("1")));
			}
		}
	}

	public static void main(String[] args) throws IOException {
		List<Map<CompressedColor,BigDecimal>> mapList = new LinkedList<Map<CompressedColor,BigDecimal>>();
		int numberOfRuns = 10;
		ImageProcessor processor = new ImageProcessor();

		System.out.println("Times for ImageProcessor:");
		long runTime = 0;
		long totalRunTime = 0;
		for(int i = 0; i < numberOfRuns; i++){
			long startTime = System.currentTimeMillis();
			processor.setImage(new File("img/flickr-images-1.jpg"));
			processor.readColors();
			mapList.add(processor.colorFreq);

			processor.setImage(new File("img/flickr-images-2.jpg"));
			processor.readColors();
			mapList.add(processor.colorFreq);

			processor.setImage(new File("img/flickr-images-3.jpg"));
			processor.readColors();
			mapList.add(processor.colorFreq);

			processor.setImage(new File("img/black.jpg"));
			processor.readColors();
			mapList.add(processor.colorFreq);

			processor.setImage(new File("img/black.jpg"));
			processor.readColors();
			mapList.add(processor.colorFreq);

			long endTime = System.currentTimeMillis();
			runTime = (endTime-startTime) ;
			totalRunTime += runTime;
			System.out.printf("Time in seconds on run %d :%.5f \n",i , runTime / 1000.0 );
		}
		System.out.println("Average runtime in seconds: " + (totalRunTime/numberOfRuns) / 1000.0 + "\n\n");
		runTime = totalRunTime = 0;
		List<Map<CompressedColor,Integer>> primitiveMapList = new LinkedList<Map<CompressedColor,Integer>>();
		PrimitiveImageProcessor primitiveProcessor = new PrimitiveImageProcessor();
		System.out.println("Times for PrimitiveImageProcessor:");

		for(int i = 0; i < numberOfRuns; i++){
			long startTime = System.currentTimeMillis();
			primitiveProcessor.setImage(new File("img/flickr-images-1.jpg"));
			primitiveProcessor.readColors();
			primitiveMapList.add(primitiveProcessor.colorFreq);

			primitiveProcessor.setImage(new File("img/flickr-images-2.jpg"));
			primitiveProcessor.readColors();
			primitiveMapList.add(primitiveProcessor.colorFreq);

			primitiveProcessor.setImage(new File("img/flickr-images-3.jpg"));
			primitiveProcessor.readColors();
			primitiveMapList.add(primitiveProcessor.colorFreq);

			primitiveProcessor.setImage(new File("img/black.jpg"));
			primitiveProcessor.readColors();
			primitiveMapList.add(primitiveProcessor.colorFreq);

			primitiveProcessor.setImage(new File("img/black.jpg"));
			primitiveProcessor.readColors();
			primitiveMapList.add(primitiveProcessor.colorFreq);

			long endTime = System.currentTimeMillis();
			runTime = (endTime-startTime);
			totalRunTime += runTime;
			System.out.printf("Time in seconds on run %d :%.5f \n",i , runTime / 1000.0 );
		}
		System.out.println("Average runtime in seconds: " + (totalRunTime/numberOfRuns) / 1000.0);
	}
}
