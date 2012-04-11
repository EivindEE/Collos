package edu.uib.info323.dao;

import org.junit.After;
import org.junit.Before;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import edu.uib.info323.image.test.AbstractCollosTest;

public abstract class AbstractDaoCollosTest extends AbstractCollosTest {
	protected EmbeddedDatabase database;
	
	
	@Before
	public void setUp() {
		database = new EmbeddedDatabaseBuilder().addScript("schema.sql").setType(EmbeddedDatabaseType.DERBY).build();
	}

	@After
	public void tearDown() {
		database.shutdown();
	}
}
