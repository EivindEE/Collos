package edu.uib.info323.dao;

import java.util.List;

import javax.sql.DataSource;

import edu.uib.info323.model.Image;

public interface ImageDao {

	public abstract void setDataSource(DataSource dataSource);

	public abstract void insert(Image image);

	public abstract Image getImageByImageUri(String imageUri);

	public abstract List<Image> getAllImages();

	public abstract void insert(List<Image> imageList);

	public abstract List<Image> getUnprocessedImages();

	public abstract void delete(Image image);

	public abstract List<Image> getImagesWithColor(String color);
	
	public abstract void updateAnalysedDate(List<Image> images);

	public abstract void update(List<Image> images);
}