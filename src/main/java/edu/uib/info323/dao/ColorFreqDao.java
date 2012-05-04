package edu.uib.info323.dao;

import java.util.List;

import javax.sql.DataSource;

import edu.uib.info323.model.ColorFreq;
import edu.uib.info323.model.Image;

public interface ColorFreqDao {

	public void setDataSource(DataSource datasource);

	public List<ColorFreq> getAllColors();

	public void insert(ColorFreq colorFreq);

	public List<ColorFreq> getImageColorFreqs(Image image);

	public void batchInsert(List<ColorFreq> colorList);
	
	public abstract void remove(ColorFreq colorFreq);
	
	public abstract void remove(List<ColorFreq> colorFreqs);

}