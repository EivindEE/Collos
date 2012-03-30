package edu.uib.info323.image.test;

import java.security.InvalidParameterException;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.uib.info323.image.CompressedColor;
import edu.uib.info323.image.CompressedColorFactory;
import edu.uib.info323.image.CompressedColorImpl;


public class CompressedColorTest extends AbstractCollosTest{
	private int rgbMax = 255;
	private int rgbMin= 0;
	
	@Autowired
	private CompressedColorFactory colorFactory;
	
	private int compressionRate = 8;
	
	@Before
	public void setUp() {
//		colorFactory = new CompressedColorFactoryImpl();
		colorFactory.setDefaultCompression(compressionRate);
	}

	@Test
	public void testConstructor(){
		Exception exception = null;
		CompressedColor cc = null;
		try{
			cc = new CompressedColorImpl(0, 0, 0, 8);
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
			cc = new CompressedColorImpl(255, 255, 255, 8);
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
			cc = new CompressedColorImpl(-1, 255, 255, 8);
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
			cc = new CompressedColorImpl(256, 255, 255, 8);
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
			cc = new CompressedColorImpl(255, 255, 255, 3);
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

	@Test
	public void testGetters(){
		int redValue = 12;
		int greenValue = 5;
		int blueValue = 123;
		int compression = 8;
		CompressedColor cc = new CompressedColorImpl(redValue, greenValue, blueValue, compression);
		assertEquals("Compression should be " + compression + " but was" + cc.getCompression(), compression, cc.getCompression());
		assertEquals("Color value should be "+ (redValue/cc.getCompression()) + "but was " + cc.getRed(),redValue/cc.getCompression(), cc.getRed());
		assertEquals("Color value should be "+ (greenValue/cc.getCompression()) + "but was " + cc.getGreen(),greenValue/cc.getCompression(), cc.getGreen());
		assertEquals("Color value should be "+ (blueValue/cc.getCompression()) + "but was " + cc.getBlue(),blueValue/cc.getCompression(), cc.getBlue());
	}
	@Test
	public void testHashCode(){
		int compression = 8;

		Map<Integer,Integer> hashKeyCounter = new TreeMap<Integer, Integer>();
		for(int red = rgbMin; red <= rgbMax; red += compression){
			for(int green = rgbMin; green <= rgbMax; green += compression){
				for(int blue = rgbMin; blue <= rgbMax; blue += compression){
					CompressedColor color = new CompressedColorImpl(red, green, blue, compression); 
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
