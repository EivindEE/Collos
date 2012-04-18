package edu.uib.info323.dao;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class ColorFreqDaoImpl implements ColorFreqDao {
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource datasource) {
		this.jdbcTemplate = new JdbcTemplate(datasource);
	}
	
	

}
