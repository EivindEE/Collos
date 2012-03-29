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
import java.util.SortedMap;
import java.util.TreeMap;

import javax.imageio.ImageIO;

//TEST
public class ImageProcessor {
	BufferedImage bufferedImage;
	Map<CompressedColor, BigDecimal> colorFreq = new HashMap<CompressedColor, BigDecimal>();
	BigDecimal pixelCount;
	private BigDecimal threshhold = new BigDecimal("0.01");

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
		List<Map> mapList = new LinkedList<Map>();
		int numberOfRuns = 10;
		ImageProcessor processor = new ImageProcessor();

		System.out.println("Times for ImageProcessor:");
		long runTime = 0;
		long totalRunTime = 0;
		for(int i = 0; i < numberOfRuns; i++){
			long startTime = System.currentTimeMillis();
			processor.setImage(new File("img/IMG_8853.jpg"));
			processor.readColors();
			mapList.add(processor.colorFreq);

			processor.setImage(new File("img/IMG_2523.jpg"));
			processor.readColors();
			mapList.add(processor.colorFreq);

			processor.setImage(new File("img/images.jpeg"));
			processor.readColors();
			mapList.add(processor.colorFreq);

			processor.setImage(new File("img/black.png"));
			processor.readColors();
			mapList.add(processor.colorFreq);

			processor.setImage(new File("img/black.png"));
			processor.readColors();
			mapList.add(processor.colorFreq);

			long endTime = System.currentTimeMillis();
			runTime = (endTime-startTime) ;
			totalRunTime += runTime;
			System.out.printf("Time in seconds on run %d :%.5f \n",i , runTime / 1000.0 );
		}
		System.out.println("Average runtime in seconds: " + (totalRunTime/numberOfRuns) / 1000.0 + "\n\n");
		runTime = totalRunTime = 0;
		mapList = new LinkedList<Map>();
		PrimitiveImageProcessor primitiveProcessor = new PrimitiveImageProcessor();
		System.out.println("Times for PrimitiveImageProcessor:");

		for(int i = 0; i < numberOfRuns; i++){
			long startTime = System.currentTimeMillis();
			primitiveProcessor.setImage(new File("img/IMG_8853.jpg"));
			primitiveProcessor.readColors();
			mapList.add(primitiveProcessor.colorFreq);

			primitiveProcessor.setImage(new File("img/IMG_2523.jpg"));
			primitiveProcessor.readColors();
			mapList.add(primitiveProcessor.colorFreq);

			primitiveProcessor.setImage(new File("img/images.jpeg"));
			primitiveProcessor.readColors();
			mapList.add(primitiveProcessor.colorFreq);

			primitiveProcessor.setImage(new File("img/black.png"));
			primitiveProcessor.readColors();
			mapList.add(primitiveProcessor.colorFreq);

			primitiveProcessor.setImage(new File("img/black.png"));
			primitiveProcessor.readColors();
			mapList.add(primitiveProcessor.colorFreq);

			long endTime = System.currentTimeMillis();
			runTime = (endTime-startTime);
			totalRunTime += runTime;
			System.out.printf("Time in seconds on run %d :%.5f \n",i , runTime / 1000.0 );
		}
		System.out.println("Average runtime in seconds: " + (totalRunTime/numberOfRuns) / 1000.0);
	
		/*
				ImageProcessor processor = new ImageProcessor();
				//		CompressedColor color = new CompressedColor(340, 500, -122);
				processor.setImage(new File("img/IMG_8853.jpg"));
				processor.readColors();
				BigDecimal relativeFreqSum = BigDecimal.ZERO;
				System.out.println("Number of colorbuckets: " + processor.colorFreq.size());
				for(CompressedColor cc : processor.colorFreq.keySet()){
					BigDecimal relativeFreq = processor.colorFreq.get(cc).divide(processor.pixelCount, 4, BigDecimal.ROUND_HALF_EVEN);
					relativeFreqSum = relativeFreqSum.add(relativeFreq);
					if(relativeFreq.compareTo(processor.threshhold) >= 0){
						System.out.println("Relative freq: ("+ relativeFreq.multiply(new BigDecimal("100"))+ "), Freq : " + processor.colorFreq.get(cc) + ", for color: " + cc);
					}
				}
				CompressedColor getColor = new CompressedColor(198, 243, 220);
				System.out.println(processor.colorFreq.get(getColor));
				System.out.println(getColor.hashCode());
		
		
		
				System.out.println("Sum of relative frequensies: " + relativeFreqSum);

		 */
	}
}
