package edu.uib.info323.model;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import edu.uib.info323.image.test.AbstractCollosTest;

public class ImageTest extends AbstractCollosTest{
	@Autowired
	private Image image;
	private String imageUri = "http://example.org/someImageUri.jpg";
	private String pageUri = "http://example.org/";
	
	@Test
	public void testGetAndSetImageUri() {
		assertNull("Should default to null but was " + image.getImageUri() ,image.getImageUri());
		image.setImageUri(imageUri);
		assertEquals("Image URI should be " + imageUri + " but was " + image.getImageUri(),imageUri, image.getImageUri());
	}
	
	@Test
	public void testGetAndSetPageUri() {
		assertNull("Should default to null but was " + image.getPageUri() ,image.getPageUri());
		image.setPageUri(pageUri);
		assertEquals("Page URI should be " + pageUri + " but was " + image.getPageUri(),pageUri, image.getPageUri());
	}



}
