package edu.uib.info323.dao;

import javax.sql.DataSource;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

public class ColorFreqDaoTest extends AbstractDaoCollosTest {
	
	@Autowired
	private ColorFreqDao dao;
	
	@Before
	public void setUp() {
		dao.setDataSource((DataSource)database);
	}
}
