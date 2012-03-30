package edu.uib.info323.image.test;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import edu.uib.info323.image.CompressedColor;
import edu.uib.info323.image.CompressedColorFactory;

public class CompressedColorFactoryTest extends AbstractCollosTest {
	@Autowired
	private CompressedColorFactory colorFactory;
	private int compression = 8;
	private int red = 120;
	private int green = 120;
	private int blue = 120;
	
	@Before
	public void setUp() {
		
	}
	
	@Test
	public void testGetAndSet() {
		
		assertEquals("Default compression should be 64 but was " + colorFactory.getDefaultCompression(), 
				64, 
				colorFactory.getDefaultCompression());
		
		colorFactory.setDefaultCompression(compression);
		assertEquals("Compression should be " + compression + " but was " +  colorFactory.getDefaultCompression(), compression, colorFactory.getDefaultCompression());
	}
	
	@Test
	public void testCreateCompressedColor() {

		CompressedColor color = colorFactory.createCompressedColor(red, green, blue);
		assertEquals(red / color.getCompression(), color.getRed());
		assertEquals(green / color.getCompression(), color.getGreen());
		assertEquals(blue / color.getCompression(), color.getBlue());
		
		colorFactory.setDefaultCompression(compression);
		
		color = colorFactory.createCompressedColor(red, green, blue, compression);
		assertEquals(red / compression, color.getRed());
		assertEquals(green / compression, color.getGreen());
		assertEquals(blue / compression, color.getBlue());
	}

}
