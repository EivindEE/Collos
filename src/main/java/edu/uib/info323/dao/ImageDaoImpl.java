package edu.uib.info323.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import edu.uib.info323.model.Image;
import edu.uib.info323.model.ImageImpl;

@Component
public class ImageDaoImpl implements ImageDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageDaoImpl.class);
	private JdbcTemplate jdbcTemplate;

	//	private DataSource dataSource;

	/* (non-Javadoc)
	 * @see edu.uib.info323.dao.ImageDao#setDataSource(javax.sql.DataSource)
	 */
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/* (non-Javadoc)
	 * @see edu.uib.info323.dao.ImageDao#insert(edu.uib.info323.model.Image)
	 */
	public void insert(Image image) {
		String sql = "INSERT INTO IMAGE (image_uri,page_uri) VALUES (?,?)";
		jdbcTemplate.update(sql, new Object[] {image.getImageUri(), image.getPageUri()});
	}

	/* (non-Javadoc)
	 * @see edu.uib.info323.dao.ImageDao#getImageByImageUri(java.lang.String)
	 */
	public Image getImageByImageUri(String imageUri) {
		String sql = "SELECT image_uri, page_uri FROM IMAGE WHERE image_uri = ?";
		
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
		String sql = "SELECT image_uri, page_uri FROM IMAGE";
		images = this.jdbcTemplate.query(sql, new RowMapper<Image>() {

			public Image mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new ImageImpl(rs.getString("image_uri"), rs.getString("page_uri"));
			}

		});
		return images;
	}

	public void batchInsert(final List<Image> imageList) {
		String sql = "INSERT INTO IMAGE(image_uri, page_uri)(" +
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
}
