package edu.uib.info323.dao;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import edu.uib.info323.image.test.AbstractCollosTest;
import edu.uib.info323.model.Image;
import edu.uib.info323.model.ImageImpl;

public class ImageDaoTest extends AbstractCollosTest {
	

	@Autowired
	private ImageDao dao;
	private EmbeddedDatabase database;
	
	private Image image;
	private Image image2;
	
	@Before
	public void setUp() {
		database = new EmbeddedDatabaseBuilder().addScript("schema.sql").setType(EmbeddedDatabaseType.DERBY).build();
		dao.setDataSource(database);
		image = new ImageImpl("image", "page");
	}
	
	@After
	public void tearDown() {
		database.shutdown();
	}
	
	@Test
	public void testInsertAndGetAll() {
		int startSize =  dao.getAllImages().size();
		dao.insert(image);
		assertEquals("Data source should have one item more.", startSize + 1, dao.getAllImages().size());
	}
	
	@Test
	public void testGetImageWithImageUri() {
		dao.insert(image);
		Image found = dao.getImageByImageUri(image.getImageUri());
		assertEquals(image, found);
	}
	

}
