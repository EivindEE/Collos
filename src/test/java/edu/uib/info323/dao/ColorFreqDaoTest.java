package edu.uib.info323.dao;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import edu.uib.info323.model.ColorFreq;
import edu.uib.info323.model.ColorFreqFactory;
import edu.uib.info323.model.Image;
import edu.uib.info323.model.ImageImpl;

public class ColorFreqDaoTest extends AbstractDaoCollosTest {

	@Autowired
	private ColorFreqDao dao;

	@Autowired
	private ImageDao imageDao;
	
	@Autowired
	ColorFreqFactory freqFactory;

	private ColorFreq colorFreq1;
	private ColorFreq colorFreq2;


	private String imageUri = "http://www.example.org/image.jpg";
	private String pageUri = "http://www.example.org/";

	private Image image1 = new ImageImpl(imageUri, Arrays.asList(pageUri));

	private int color1 = 0;
	private int relativeFreq1 = 30;
	private int color2 = 500;
	private int relativeFreq2 = 60;

	@Before
	public void setUp() {
		super.setUp();
		dao.setDataSource((DataSource)database);
		imageDao.insert(image1);
		colorFreq1 = freqFactory.createColorFreq(image1, color1, relativeFreq1);
		colorFreq2 = freqFactory.createColorFreq(image1, color2, relativeFreq2);
	}

	@Test
	public void testInsertAndGetColor() {
		assertEquals(Collections.emptyList(), dao.getAllColors());
		dao.insert(colorFreq1);
		assertEquals(1, dao.getAllColors().size());
	}
	
	@Test
	public void testBatchInsert() {
		assertEquals(Collections.emptyList(), dao.getAllColors());
		List<ColorFreq> expectedColorList = new LinkedList<ColorFreq>();
		dao.insert(expectedColorList);
		assertEquals(Collections.emptyList(), dao.getAllColors());
		expectedColorList.add(colorFreq1);
		expectedColorList.add(colorFreq2);
		dao.insert(expectedColorList);
		List<ColorFreq> actualColorList = dao.getAllColors();
		assertTrue(actualColorList + " should contain all the elements in " + expectedColorList, actualColorList.containsAll(expectedColorList));
		dao.insert(expectedColorList);
		actualColorList = dao.getAllColors();
		assertTrue(actualColorList + " should contain all the elements in " + expectedColorList, actualColorList.containsAll(expectedColorList));
		
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
		assertTrue(actualColorFreqs.containsAll(expectedColorFreqs));
		assertTrue(expectedColorFreqs.containsAll(actualColorFreqs));
	}
}
