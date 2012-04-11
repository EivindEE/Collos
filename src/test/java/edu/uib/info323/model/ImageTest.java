package edu.uib.info323.model;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import edu.uib.info323.image.test.AbstractCollosTest;

public class ImageTest extends AbstractCollosTest{
	private Image image;
	private Image otherImage;
	private String imageUri = "http://example.org/someImageUri.jpg";
	private String pageUri = "http://example.org/";
	
	@Test
	public void testConstructorAndGet() {
		image = new ImageImpl(imageUri, pageUri);
		assertEquals("Image URI should be " + imageUri + " but was " + image.getImageUri(),imageUri, image.getImageUri());
		assertEquals("Page URI should be " + pageUri + " but was " + image.getPageUri(),pageUri, image.getPageUri());
	}
	
	@Test
	public void testEquals() {
		image = new ImageImpl(imageUri, pageUri);
		otherImage = new ImageImpl(imageUri, pageUri);
		assertEquals(image, otherImage);
	}
}
