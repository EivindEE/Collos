package edu.uib.info323.model;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import edu.uib.info323.image.test.AbstractCollosTest;

public class ColorFreqTest extends AbstractCollosTest {
	@Autowired
	private Image image;
	private int color = 200;
	private int relativeFreq = 5;
	
	

	@Test
	public void testContructorAndGet() {
		ColorFreq colorFreq = new ColorFreqImpl(image, color, relativeFreq);
		assertEquals(image, colorFreq.getImage());
		assertEquals(color, colorFreq.getColor());
		assertEquals(relativeFreq, colorFreq.getRelativeFreq());
	}
}
