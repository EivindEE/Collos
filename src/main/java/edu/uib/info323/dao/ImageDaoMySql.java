package edu.uib.info323.dao;

import java.awt.Color;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import edu.uib.info323.image.CompressedColorFactory;
import edu.uib.info323.model.Image;
import edu.uib.info323.model.ImageImpl;

@Component
public class ImageDaoMySql implements ImageDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageDaoMySql.class);
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private CompressedColorFactory colorFactory;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void insert(Image image) {
		String sql = "INSERT INTO image (image_uri) VALUES (?) ON DUPLICATE KEY UPDATE image_uri = image_uri";
		jdbcTemplate.update(sql, new Object[] {image.getImageUri()});

		sql = "INSERT INTO image_page (image_uri,page_uri) VALUES (?,?) ON DUPLICATE KEY UPDATE image_uri = image_uri";
		jdbcTemplate.update(sql, new Object[] {image.getImageUri(), image.getPageUri()});
		
		LOGGER.debug("Inserted " + image + " into database");

	}

	public Image getImageByImageUri(String imageUri) {
		String sql = "SELECT image_uri, page_uri " +
					 "FROM image_page " +
					 "WHERE image_uri = ?";

		return this.jdbcTemplate.query(sql, new Object[] {imageUri}, new ResultSetExtractor<Image>() {

			public Image extractData(ResultSet rs) throws SQLException,
			DataAccessException {
				rs.next();
				
				return new ImageImpl(rs.getString("image_uri"), rs.getString("page_uri"));
			}});
	}

	public List<Image> getAllImages() {
		List<Image> images = new ArrayList<Image>();
		String sql = "SELECT image_uri, page_uri FROM image_page";
		images = this.jdbcTemplate.query(sql, new RowMapper<Image>() {

			public Image mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new ImageImpl(rs.getString("image_uri"), rs.getString("page_uri"));
			}

		});
		return images;
	}

	

	public void batchInsert(final List<Image> imageList) {
		String sql = "INSERT INTO image (image_uri) VALUES (?) ON DUPLICATE KEY UPDATE image_uri = image_uri";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Image image = imageList.get(i);
				ps.setString(1, image.getImageUri());
			}
			public int getBatchSize() {
				return imageList.size();
			}
		});

		sql = "INSERT INTO image_page (image_uri,page_uri) VALUES (?,?) ON DUPLICATE KEY UPDATE image_uri = image_uri";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Image image = imageList.get(i);
				ps.setString(1, image.getImageUri());
				ps.setString(2, image.getPageUri());
			}
			public int getBatchSize() {
				return imageList.size();
			}
		});

	}

	public List<Image> getUnprocessedImages() {
		String sql = "SELECT * FROM image WHERE date_analyzed IS NULL LIMIT 0, 100";

		return 	jdbcTemplate.query(sql, new RowMapper<Image>() {

			public Image mapRow(ResultSet rs, int rowNum) throws SQLException { 
				return new ImageImpl(rs.getString("image_uri"),null);
			}
		});
	}
	
	public void updateAnalysedDate(final List<Image> images) {
		String sql = "UPDATE image SET date_analyzed = ? WHERE image_uri = ?";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Image image = images.get(i);
				ps.setDate(1, new Date(System.currentTimeMillis()));
				ps.setString(2, image.getImageUri());
			}
			
			public int getBatchSize() {
				return images.size();
			}
		});
	}

	public void delete(Image image) {
		String sql = "DELETE FROM image WHERE image_uri = ?";
		jdbcTemplate.update(sql, new Object[] {image.getImageUri()});

	}

	public List<Image> getImagesWithColor(String color) {
		Color decodedColor = Color.decode(color);
		int colorValue = colorFactory.createCompressedColor(decodedColor.getRed(), decodedColor.getGreen(), decodedColor.getBlue()).getColor();
		String sql = "SELECT image_page.image_uri, image_page.page_uri " +
				"FROM image_page, color " +
				"WHERE image_page.image_uri = color.image_uri " +
				"AND color = ? " +
				"ORDER BY color.relative_freq DESC " +
				"LIMIT 0, 100";
		
		
		return jdbcTemplate.query(sql,new Object[] {colorValue}, new RowMapper<Image>() {

			public Image mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new ImageImpl(rs.getString("image_uri"), rs.getString("page_uri"));
			}
			
		});
	}
}
