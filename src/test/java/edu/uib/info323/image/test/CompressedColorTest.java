package edu.uib.info323.image.test;

import java.security.InvalidParameterException;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.TestCase;
import edu.uib.info323.image.CompressedColor;

public class CompressedColorTest extends TestCase {
	private int rgbMax = 255;
	private int rgbMin= 0;


	public void testConstructor(){
		Exception exception = null;
		CompressedColor cc = null;
		try{
			cc = new CompressedColor(0, 0, 0, 8);
		}
		catch (InvalidParameterException e) {
			exception = e;
		}
		finally{
			assertNull(exception);
			assertNotNull(cc);
			cc = null;
		}
		try{
			cc = new CompressedColor(255, 255, 255, 8);
		}
		catch (InvalidParameterException e) {
			exception = e;
		}
		finally{
			assertNull(exception);
			assertNotNull(cc);
			cc = null;
		}
		try{
			cc = new CompressedColor(-1, 255, 255, 8);
		}
		catch (InvalidParameterException e) {
			exception = e;
		}
		finally{
			assertNotNull(exception);
			assertNull(cc);
			assertEquals(InvalidParameterException.class, exception.getClass());
			cc = null;
		}
		try{
			cc = new CompressedColor(256, 255, 255, 8);
		}
		catch (InvalidParameterException e) {
			exception = e;
		}
		finally{
			assertNotNull(exception);
			assertNull(cc);
			assertEquals(InvalidParameterException.class, exception.getClass());
			cc = null;
		}
		
		try{
			cc = new CompressedColor(255, 255, 255, 3);
		}
		catch (InvalidParameterException e) {
			exception = e;
		}
		finally{
			assertNotNull(exception);
			assertNull(cc);
			assertEquals(InvalidParameterException.class, exception.getClass());
			cc = null;
		}
	}

	public void testGetters(){
		int redValue = 12;
		int greenValue = 5;
		int blueValue = 123;
		int compression = 8;
		CompressedColor cc = new CompressedColor(redValue, greenValue, blueValue, compression);
		assertEquals("Compression should be " + compression + " but was" + cc.getCompression(), compression, cc.getCompression());
		assertEquals("Color value should be "+ (redValue/cc.getCompression()) + "but was " + cc.getRed(),redValue/cc.getCompression(), cc.getRed());
		assertEquals("Color value should be "+ (greenValue/cc.getCompression()) + "but was " + cc.getGreen(),greenValue/cc.getCompression(), cc.getGreen());
		assertEquals("Color value should be "+ (blueValue/cc.getCompression()) + "but was " + cc.getBlue(),blueValue/cc.getCompression(), cc.getBlue());
	}

	public void testHashCode(){
		int compression = 8;

		Map<Integer,Integer> hashKeyCounter = new TreeMap<Integer, Integer>();
		for(int red = rgbMin; red <= rgbMax; red += compression){
			for(int green = rgbMin; green <= rgbMax; green += compression){
				for(int blue = rgbMin; blue <= rgbMax; blue += compression){
					CompressedColor color = new CompressedColor(red, green, blue, compression); 
					int hashKey = color.hashCode();
					int count = hashKeyCounter.containsKey(hashKey) ? hashKeyCounter.get(hashKey) : 0;
					count += 1;
					hashKeyCounter.put(hashKey, count);
				}
			}
		}
		Integer expectedKeyCount = 1;
		for(Integer key : hashKeyCounter.keySet()){
			assertEquals(expectedKeyCount, hashKeyCounter.get(key));
		}
	}
}
