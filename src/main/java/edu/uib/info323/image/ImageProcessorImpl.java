package edu.uib.info323.image;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageProcessorImpl implements ImageProcessor {
	BufferedImage bufferedImage;
	Map<CompressedColor, Integer> colorFreq = new HashMap<CompressedColor, Integer>();
	double pixelCount;
	private double threshhold = 0.01;

	/* (non-Javadoc)
	 * @see edu.uib.info323.image.ImageProcessor#setImage(java.io.File)
	 */
	public void setImage(File f) throws IOException{
		this.setImage(new FileInputStream(f));
	}

	/* (non-Javadoc)
	 * @see edu.uib.info323.image.ImageProcessor#readColors()
	 */
	public void readColors() {
		WritableRaster raster = bufferedImage.getRaster();
		int imageHeightInPixels = raster.getHeight();
		int imageWidthInPixels = raster.getWidth();
		pixelCount = imageHeightInPixels*imageWidthInPixels;
		for(int x = raster.getMinX(); x < raster.getWidth(); x++){
			for(int y = raster.getMinY(); y < raster.getHeight(); y++){
				int[] pixel = raster.getPixel(x, y, new int[3]);
				CompressedColor color = new CompressedColor(pixel[0], pixel[1], pixel[2], 8);
				Integer count = colorFreq.containsKey(color) ? colorFreq.get(color) : 0;
				colorFreq.put(color,++count);
			}
		}
	}

	/* (non-Javadoc)
	 * @see edu.uib.info323.image.ImageProcessor#getColorFrequencies()
	 */
	public Map<CompressedColor, Integer> getColorFrequencies(){
		return colorFreq;
	}

	public static void main(String[] args) throws IOException {
		ImageProcessorImpl processor = new ImageProcessorImpl();
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
		CompressedColor getColor = new CompressedColor(198, 243, 220, 8);
		System.out.println(processor.colorFreq.get(getColor));
		System.out.println(getColor.hashCode());



		System.out.printf("Sum of relative frequensies: %.5f", relativeFreqSum);
	}

	public void setImage(InputStream inputStream) throws IOException {
		try {
			bufferedImage = ImageIO.read(inputStream);
		}
		catch (IOException e) {
			throw new IOException("Exception occured while reading image from stream: " + inputStream);
		}
	}
}