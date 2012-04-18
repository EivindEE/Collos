package edu.uib.info323.dao;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import edu.uib.info323.model.ColorFreq;
import edu.uib.info323.model.ColorFreqImpl;
import edu.uib.info323.model.Image;
import edu.uib.info323.model.ImageImpl;

public class ColorFreqDaoTest extends AbstractDaoCollosTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(ColorFreqDaoTest.class);
	@Autowired
	private ColorFreqDao dao;
	@Autowired
	private ImageDao imageDao;

	private ColorFreq colorFreq1;
	private ColorFreq colorFreq2;

	private String imageUri = "http://www.example.org/image.jpg";
	private String pageUri = "http://www.example.org/";
	
	private String imageUri2 = "http://www.example.org/image2.jpg";
	private String pageUri2 = "http://www.example.com/";
	
	private String imageUri3 = "http://www.example.org/image3.jpg";
	private String pageUri3 = "http://www.example.net/";

	private Image image1 = new ImageImpl(imageUri, pageUri);
	private Image image2 = new ImageImpl(imageUri2, pageUri2);
	private Image image3 = new ImageImpl(imageUri3, pageUri3);

	private int color1 = 0;
	private int relativeFreq1 = 30;
	private int color2 = 500;
	private int relativeFreq2 = 60;

	@Before
	public void setUp() {
		super.setUp();
		dao.setDataSource((DataSource)database);
		imageDao.insert(image1);
		colorFreq1 = new ColorFreqImpl(image1, color1, relativeFreq1);
		colorFreq2 = new ColorFreqImpl(image1, color2, relativeFreq2);
	}

	@Test
	public void testInsertAndGetColor() {
		assertEquals(Collections.emptyList(), dao.getAllColors());
		dao.insert(colorFreq1);
		assertEquals(1, dao.getAllColors().size());
	}

	@Test
	public void testGetColorFreqsForImage() {
		dao.insert(colorFreq1);
		dao.insert(colorFreq2);
		List<ColorFreq> expectedColorFreqs = new LinkedList<ColorFreq>();
		List<ColorFreq> actualColorFreqs = new LinkedList<ColorFreq>();
		expectedColorFreqs.add(colorFreq1);
		expectedColorFreqs.add(colorFreq2);
		
		actualColorFreqs = dao.getImageColorFreqs(image1);
		LOGGER.debug(actualColorFreqs.toString());
		assertTrue(actualColorFreqs.containsAll(expectedColorFreqs));
		assertTrue(expectedColorFreqs.containsAll(actualColorFreqs));
	}
	
	@Test
	public void getImagesWithColor() {
		
	}
}
