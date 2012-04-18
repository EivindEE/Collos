package edu.uib.info323.dao;

import java.util.List;

import javax.sql.DataSource;

import edu.uib.info323.model.ColorFreq;

public interface ColorFreqDao {

	public void setDataSource(DataSource datasource);

	public List<ColorFreq> getAllColors();

	public void insert(ColorFreq colorFreq);

}