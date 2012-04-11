package edu.uib.info323.model;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import edu.uib.info323.image.test.AbstractCollosTest;

public class ImageModelTest extends AbstractCollosTest{
	
	@Autowired
	private Image imageModel;
	private String imageUri = "http://example.org/someImageUri.jpg";
	private String pageUri = "http://example.org/";
	
	@Test
	public void testGetAndSetImageUri() {
		assertNull("Should default to null but was " + imageModel.getImageUri() ,imageModel.getImageUri());
		imageModel.setImageUri(imageUri);
		assertEquals("Image URI should be " + imageUri + " but was " + imageModel.getImageUri(),imageUri, imageModel.getImageUri());
	}
	
	@Test
	public void testGetAndSetPageUri() {
		assertNull("Should default to null but was " + imageModel.getPageUri() ,imageModel.getPageUri());
		imageModel.setPageUri(pageUri);
		assertEquals("Page URI should be " + pageUri + " but was " + imageModel.getPageUri(),pageUri, imageModel.getPageUri());
	}



}
