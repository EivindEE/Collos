package edu.uib.info323.dao;

import java.awt.Color;
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

public abstract class ImageDaoDerbyImpl implements ImageDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageDaoDerbyImpl.class);
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	CompressedColorFactory colorFactory;


	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/* (non-Javadoc)
	 * @see edu.uib.info323.dao.ImageDao#insert(edu.uib.info323.model.Image)
	 */
	public void insert(Image image) {
		String sql = "INSERT INTO image (image_uri) VALUES (?)";
		jdbcTemplate.update(sql, new Object[] {image.getImageUri()});

		sql = "INSERT INTO image_page (image_uri,page_uri) VALUES (?,?)";
		jdbcTemplate.update(sql, new Object[] {image.getImageUri(), image.getPageUri()});
		
		LOGGER.debug("Inserted " + image + " into database");
	}

	/* (non-Javadoc)
	 * @see edu.uib.info323.dao.ImageDao#getImageByImageUri(java.lang.String)
	 */
	public Image getImageByImageUri(String imageUri) {
		String sql = "SELECT image_uri, page_uri FROM IMAGE_PAGE WHERE image_uri = ?";

		return this.jdbcTemplate.query(sql, new Object[] {imageUri}, new ResultSetExtractor<Image>() {

			public Image extractData(ResultSet rs) throws SQLException,
			DataAccessException {
				rs.next();
				
				return new ImageImpl(rs.getString("image_uri"), rs.getString("page_uri"));
			}});
	}

	/* (non-Javadoc)
	 * @see edu.uib.info323.dao.ImageDao#getAllImages()
	 */
	public List<Image> getAllImages(){
		List<Image> images = new ArrayList<Image>();
		String sql = "SELECT image_uri, page_uri FROM IMAGE_PAGE";
		images = this.jdbcTemplate.query(sql, new RowMapper<Image>() {

			public Image mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new ImageImpl(rs.getString("image_uri"), rs.getString("page_uri"));
			}

		});
		return images;
	}

	public void batchInsert(final List<Image> imageList) {
		String sql = "INSERT INTO IMAGE(image_uri)(" +
				"SELECT ? AS image_uri" +
				"FROM IMAGE " +
				"WHERE image_uri = ?" +
				"HAVING COUNT(*)=0 " +
				")";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Image image = imageList.get(i);
				ps.setString(1, image.getImageUri());
				ps.setString(2, image.getPageUri());
				ps.setString(3, image.getImageUri());
				ps.setString(4, image.getPageUri());
			}
			public int getBatchSize() {
				return imageList.size();
			}
		});

		sql = "INSERT INTO IMAGE_PAGE(image_uri, page_uri)(" +
				"SELECT ? AS image_uri, ? AS page_uri " +
				"FROM IMAGE " +
				"WHERE image_uri = ? AND page_uri = ? " +
				"HAVING COUNT(*)=0 " +
				")";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Image image = imageList.get(i);
				ps.setString(1, image.getImageUri());
				ps.setString(2, image.getPageUri());
				ps.setString(3, image.getImageUri());
				ps.setString(4, image.getPageUri());
			}
			public int getBatchSize() {
				return imageList.size();
			}
		});

	}

	public List<Image> getUnprocessedImages() {
		String sql = "SELECT * FROM IMAGE WHERE date_analyzed IS NULL";

		return 	jdbcTemplate.query(sql, new RowMapper<Image>() {

			public Image mapRow(ResultSet rs, int rowNum) throws SQLException { 
				return new ImageImpl(rs.getString("image_uri"),null);
			}
		});
	}

	public void delete(Image image) {
		// TODO Auto-generated method stub
		
	}

	public List<Image> getImagesWithColor(String color) {
		Color decodedColor = Color.decode(color);
		int colorValue = colorFactory.createCompressedColor(decodedColor.getRed(), decodedColor.getGreen(), decodedColor.getBlue()).getColor();
		String sql = "SELECT IMAGE_PAGE.image_uri, IMAGE_PAGE.page_uri " +
				"FROM IMAGE_PAGE, COLOR_FREQ " +
				"WHERE IMAGE_PAGE.image_uri = COLOR_FREQ.image_uri " +
				"AND color = ?";
		
		
		return jdbcTemplate.query(sql,new Object[] {colorValue}, new RowMapper<Image>() {

			public Image mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new ImageImpl(rs.getString("image_uri"), rs.getString("page_uri"));
			}
			
		});
	}
}
