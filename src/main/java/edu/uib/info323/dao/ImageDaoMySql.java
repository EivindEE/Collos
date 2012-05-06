package edu.uib.info323.dao;

import java.awt.Color;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import edu.uib.info323.image.CompressedColorFactory;
import edu.uib.info323.model.Image;
import edu.uib.info323.model.ImageFactory;

@Component
public class ImageDaoMySql implements ImageDao{

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageDaoMySql.class);
	private NamedParameterJdbcTemplate jdbcTemplate;
	@Autowired
	private CompressedColorFactory colorFactory;
	
	@Autowired
	private ImageFactory imageFactory;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	public void insert(Image image) {
		String sql = "INSERT INTO image (image_uri) VALUES (:image_uri) ON DUPLICATE KEY UPDATE image_uri = image_uri";
		MapSqlParameterSource mapSqlParameterSource = this.getMapSqlParameterSource(image);
		jdbcTemplate.update(sql, mapSqlParameterSource);

		sql = "INSERT INTO image_page (image_uri,page_uri) VALUES (:image_uri,:page_uri) ON DUPLICATE KEY UPDATE image_uri = image_uri";
		jdbcTemplate.update(sql, mapSqlParameterSource);
		
		LOGGER.debug("Inserted " + image + " into database");

	}

	public Image getImageByImageUri(String imageUri) {
		String sql = "SELECT image_uri, page_uri " +
					 "FROM image_page " +
					 "WHERE image_uri = :image_uri";

		Image image =  this.jdbcTemplate.query(sql, this.getMapSqlParameterSource(imageFactory.createImage(imageUri)), new ResultSetExtractor<Image>() {

			public Image extractData(ResultSet rs) throws SQLException,
			DataAccessException {
				rs.next();
				
				return imageFactory.createImage(rs.getString("image_uri"), rs.getString("page_uri"));
			}});
		image.addPageUri(this.getPagesForImage(image));
		return image;
	}

	public List<Image> getAllImages() {
		List<Image> images = new ArrayList<Image>();
		String sql = "SELECT image_uri FROM image";
		images = this.jdbcTemplate.query(sql, new MapSqlParameterSource() ,new RowMapper<Image>() {

			public Image mapRow(ResultSet rs, int rowNum) throws SQLException {
				return imageFactory.createImage(rs.getString("image_uri"));
			}

		});
		
		return this.getPagesForImages(images);
	}

	

	public void batchInsert(final List<Image> images) {
		String sql = "INSERT INTO image (image_uri) VALUES (:image_uri) ON DUPLICATE KEY UPDATE image_uri = image_uri";
		SqlParameterSource[] parameterSource = this.getSqlParameterSource(images);
		jdbcTemplate.batchUpdate(sql, parameterSource);

		sql = "INSERT INTO image_page (image_uri,page_uri) VALUES (:image_uri,:page_uri) ON DUPLICATE KEY UPDATE image_uri = image_uri";
		jdbcTemplate.batchUpdate(sql, parameterSource);

	}

	public List<Image> getUnprocessedImages() {
		String sql = "SELECT * FROM image WHERE date_analyzed IS NULL LIMIT 0, 100";

		return 	jdbcTemplate.query(sql,new MapSqlParameterSource(), new RowMapper<Image>() {

			public Image mapRow(ResultSet rs, int rowNum) throws SQLException { 
				return imageFactory.createImage(rs.getString("image_uri"));
			}
		});
	}
	
	public void updateAnalysedDate(final List<Image> images) {
		String sql = "UPDATE image SET date_analyzed = :date_analyzed WHERE image_uri = :image_uri";
		
		jdbcTemplate.batchUpdate(sql, getSqlParameterSource(images));
		
	}

	public void delete(Image image) {
		String sql = "DELETE FROM image WHERE image_uri = :image_uri";
		jdbcTemplate.update(sql, this.getMapSqlParameterSource(image));

	}

	public List<Image> getImagesWithColor(String color) {
		Color decodedColor = Color.decode(color);
		LOGGER.debug("Retrieving images with color " + color);
		int colorValue = colorFactory.createCompressedColor(decodedColor.getRed(), decodedColor.getGreen(), decodedColor.getBlue()).getColor();
		String sql = "SELECT image.image_uri " +
				"FROM image, color " +
				"WHERE image.image_uri = color.image_uri " +
				"AND color = :color " +
				"ORDER BY color.relative_freq DESC " +
				"LIMIT 0, 100";
		MapSqlParameterSource parameterSource = new MapSqlParameterSource("color", colorValue);
		
		List<Image> imagesWithoutPages = jdbcTemplate.query(sql,parameterSource, new RowMapper<Image>() {

			public Image mapRow(ResultSet rs, int rowNum) throws SQLException {
				return imageFactory.createImage(rs.getString("image_uri"));
			}
			
		});
		LOGGER.debug("Found " + imagesWithoutPages.size() + " images");
		return this.getPagesForImages(imagesWithoutPages);
	}
	
	private List<Image> getPagesForImages(List<Image> images) {
		LOGGER.debug("Getting the pages for the images");
		for(Image image : images) {
			image.addPageUri(this.getPagesForImage(image));
		}
		LOGGER.debug("Pages found");
		return images;
	}
	
	private List<String> getPagesForImage(Image image) {
		String sql = "SELECT page_uri FROM image_page WHERE image_uri = :image_uri";
		List<String> pages = Collections.emptyList(); 
		try {
			pages = jdbcTemplate.queryForList(sql, this.getMapSqlParameterSource(image), String.class);
		}catch (Throwable t) {
			LOGGER.error("Got throwable " + t.getLocalizedMessage());
		}
		return pages;
	}

	private SqlParameterSource[] getSqlParameterSource(List<Image> images) {
		List<SqlParameterSource> parameters = new ArrayList<SqlParameterSource>();
		for(Image image : images) {
			parameters.add(this.getMapSqlParameterSource(image));
		}
		return parameters.toArray(new SqlParameterSource[0]);
	}


	private MapSqlParameterSource getMapSqlParameterSource(Image image) {
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue("image_uri", image.getImageUri());
		parameterSource.addValue("page_uri", image.getPageUris());
		parameterSource.addValue("date_analyzed", new Date(System.currentTimeMillis()));
		return parameterSource; 
	}
}
