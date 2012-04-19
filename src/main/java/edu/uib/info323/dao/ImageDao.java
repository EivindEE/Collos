package edu.uib.info323.dao;

import java.util.List;

import javax.sql.DataSource;

import edu.uib.info323.model.Image;
import edu.uib.info323.model.ImageImpl;

public interface ImageDao {

	public abstract void setDataSource(DataSource dataSource);

	public abstract void insert(Image image);

	public abstract Image getImageByImageUri(String imageUri);

	public abstract List<Image> getAllImages();

	public abstract void batchInsert(List<Image> imageList);

}