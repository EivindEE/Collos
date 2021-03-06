package edu.uib.info323.dao;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import edu.uib.info323.model.Image;
import edu.uib.info323.model.ImageImpl;

public class ImageDaoTest extends AbstractDaoCollosTest{
	

	@Autowired
	private ImageDao dao;
	private Image image;
	
	@Before
	public void setUp() {
		super.setUp();
		dao.setDataSource(database);
		image = new ImageImpl("image", Arrays.asList("page"));
	}
	
	@Test
	public void testInsertAndGetAll() {
		int startSize =  dao.getAllImages().size();
		dao.insert(image);
		assertEquals("Data source should have one item more.", startSize + 1, dao.getAllImages().size());
	}
	@Test
	public void testBatchInsert() {
		List<Image> expectedImageList = new LinkedList<Image>();
		expectedImageList.add(image);
		expectedImageList.add(new ImageImpl("image2", Arrays.asList("page2")));
		expectedImageList.add(new ImageImpl("image3", Arrays.asList("page3")));
		expectedImageList.add(new ImageImpl("image4", Arrays.asList("page4")));
		dao.insert(expectedImageList);
		assertTrue(dao.getAllImages().containsAll(expectedImageList));

		// DAO Should not cause duplicates
		dao.insert(expectedImageList);
		assertTrue(dao.getAllImages().containsAll(expectedImageList));
	}
	@Test
	public void testGetImageWithImageUri() {
		dao.insert(image);
		Image found = dao.getImageByImageUri(image.getImageUri());
		assertEquals(image, found);
	}
	
	@Test
	public void testGetImagesWithColor() {
		
	}
	

}
