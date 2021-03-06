package edu.uib.info323.model;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import edu.uib.info323.image.test.AbstractCollosTest;

public class ColorFreqTest extends AbstractCollosTest {
	private Image image;
	private int color = 200;
	private int relativeFreq = 5;
	
	@Autowired
	private ColorFreqFactory freqFactory;
	
	@Before
	public void setUp() {
		image = new ImageImpl("imageUri", Arrays.asList("pageUri"));
	}

	@Test
	public void testContructorAndGet() {
		ColorFreq colorFreq = freqFactory.createColorFreq(image, color, relativeFreq);
		assertEquals(image, colorFreq.getImage());
		assertEquals(color, colorFreq.getColor());
		assertEquals(relativeFreq, colorFreq.getRelativeFreq());
	}
}
