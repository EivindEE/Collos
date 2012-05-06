package edu.uib.info323.model;

import java.util.Arrays;

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
		image = new ImageImpl(imageUri, Arrays.asList(pageUri));
		assertEquals("Image URI should be " + imageUri + " but was " + image.getImageUri(),imageUri, image.getImageUri());
		assertEquals("Page URI should be " + pageUri + " but was " + image.getPageUris(),pageUri, image.getPageUris());
	}
	
	@Test
	public void testEquals() {
		image = new ImageImpl(imageUri, Arrays.asList(pageUri));
		otherImage = new ImageImpl(imageUri, Arrays.asList(pageUri));
		assertEquals(image, otherImage);
	}
}
