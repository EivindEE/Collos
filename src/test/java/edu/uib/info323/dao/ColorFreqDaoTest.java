package edu.uib.info323.dao;

import java.util.Collections;

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

	private ColorFreq colorFreq;

	private String imageUri = "http://www.example.org/image.jpg";
	private String pageUri = "http://www.example.org/";

	private Image image = new ImageImpl(imageUri, pageUri);
	private int color = 0;
	private int relativeFreq = 30;

	@Before
	public void setUp() {
		super.setUp();
		LOGGER.warn(database.toString());
		dao.setDataSource((DataSource)database);
		imageDao.insert(image);
		colorFreq = new ColorFreqImpl(image, color, relativeFreq);
	}

	@Test
	public void testInsertAndGetColor() {
		assertEquals(Collections.emptyList(), dao.getAllColors());
		dao.insert(colorFreq);
		assertEquals(1, dao.getAllColors().size());
	}
}
