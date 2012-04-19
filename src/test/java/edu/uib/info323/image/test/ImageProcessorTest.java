package edu.uib.info323.image.test;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.stream.FileImageInputStream;

import org.apache.commons.logging.Log;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import edu.uib.info323.image.ImageProcessor;



public class ImageProcessorTest extends AbstractCollosTest {
	
	@Autowired
	private ImageProcessor imageProcessor;
	
//	@Test
//	public void testSetImage() throws FileNotFoundException {
//		File file = new File("");
//		Exception exception = null;
//		try {
//			imageProcessor.setImage(file);
//		}
//		catch (Exception e) {
//			exception = e;
//		}
//		assertTrue(exception instanceof IOException);
//		exception = null;
//		
//		InputStream inputStream = null;
//		try {
//			imageProcessor.setImage(inputStream);
//		}
//		catch (Exception e) {
//			exception = e;
//		}
//		assertTrue("Exception should be of type IOException but was of type "  + exception, exception instanceof IOException);
//		exception = null;
//	}
	
	
}
