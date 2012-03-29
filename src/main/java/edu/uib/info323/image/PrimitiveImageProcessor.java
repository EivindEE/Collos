package edu.uib.info323.image;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class PrimitiveImageProcessor {
	BufferedImage bufferedImage;
	Map<CompressedColor, Integer> colorFreq = new HashMap<CompressedColor, Integer>();
	double pixelCount;
	private double threshhold = 0.01;

	public void setImage(File f) throws IOException{
		try {

			bufferedImage = ImageIO.read(f);
		} catch (IOException e) {
			throw new IOException("Exception occured while reading image from file: " + f);
		}
	}

	public void readColors() {
		WritableRaster raster = bufferedImage.getRaster();
		int imageHeightInPixels = raster.getHeight();
		int imageWidthInPixels = raster.getWidth();
		pixelCount = imageHeightInPixels*imageWidthInPixels;
		for(int x = raster.getMinX(); x < raster.getWidth(); x++){
			for(int y = raster.getMinY(); y < raster.getHeight(); y++){
				int[] pixel = raster.getPixel(x, y, new int[3]);
				CompressedColor color = new CompressedColor(pixel[0], pixel[1], pixel[2]);
				Integer count = colorFreq.containsKey(color) ? colorFreq.get(color) : 0;
				colorFreq.put(color,++count);
			}
		}
	}

	public Map<CompressedColor, Integer> getColorFrequencies(){
		return colorFreq;
	}
	
	public static void main(String[] args) throws IOException {
		PrimitiveImageProcessor processor = new PrimitiveImageProcessor();
		processor.setImage(new File("src/main/webapp/resources/testimg/flickr-images-1.jpg"));
		processor.readColors();

		double relativeFreqSum = 0;
		System.out.println("Number of colorbuckets: " + processor.colorFreq.keySet().size());
		for(CompressedColor cc : processor.colorFreq.keySet()){
			double relativeFreq = processor.colorFreq.get(cc) / processor.pixelCount;
			relativeFreqSum += relativeFreq;
			if(relativeFreq > processor.threshhold){
				System.out.printf("Relative freq: ( %.3f), Freq : " + processor.colorFreq.get(cc) + ", for color: " + cc +"\n", relativeFreq);
			}
		}
		CompressedColor getColor = new CompressedColor(198, 243, 220);
		System.out.println(processor.colorFreq.get(getColor));
		System.out.println(getColor.hashCode());



		System.out.printf("Sum of relative frequensies: %.5f", relativeFreqSum);
	}
}