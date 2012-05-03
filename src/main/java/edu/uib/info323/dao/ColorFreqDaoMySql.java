package edu.uib.info323.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import edu.uib.info323.dao.rowmapper.ColorFreqRowMapper;
import edu.uib.info323.model.ColorFreq;
import edu.uib.info323.model.ColorFreqImpl;
import edu.uib.info323.model.Image;

@Component
public class ColorFreqDaoMySql implements ColorFreqDao {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ColorFreqDaoMySql.class);

	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ImageDao imageDao;

	private ColorFreqRowMapper rowMapper;

	@Autowired
	public void setDataSource(DataSource datasource) {
		jdbcTemplate = new JdbcTemplate(datasource);

	}

	public List<ColorFreq> getAllColors() {
		String sql = "SELECT * FROM color";
		return jdbcTemplate.query(sql, new RowMapper<ColorFreq>() {


			public ColorFreq mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				Image image = imageDao.getImageByImageUri(rs.getString("image_uri"));
				return new ColorFreqImpl(image, rs.getInt("color"), rs.getInt("relative_freq"));
			}
			
		});
	}

	public void insert(ColorFreq colorFreq) {
		String sql = "INSERT INTO color(image_uri, color, relative_freq) VALUES (?,?,?)";
		jdbcTemplate.update(sql, new Object[] {colorFreq.getImage().getImageUri(),colorFreq.getColor(),colorFreq.getRelativeFreq()});
	}

	public List<ColorFreq> getImageColorFreqs(Image image) {
		String sql = "SELECT * FROM color WHERE image_uri = ?";
		rowMapper.setImage(image);
		List<ColorFreq> colorFreqs = jdbcTemplate.query(sql,new Object[] {image.getImageUri()}, rowMapper);
		LOGGER.debug(colorFreqs.toString());
		return colorFreqs;
	}

	public void batchInsert(final List<ColorFreq> colorList) {
		String sql = "INSERT INTO color(image_uri, color, relative_freq) VALUES ( ?, ? ,?) ON DUPLICATE KEY UPDATE relative_freq = ? ";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ColorFreq colorFreq = colorList.get(i);
				ps.setString(1, colorFreq.getImage().getImageUri());
				ps.setInt(2, colorFreq.getColor());
				ps.setInt(3, colorFreq.getRelativeFreq());
				ps.setInt(4, colorFreq.getRelativeFreq());
			}
			
			public int getBatchSize() {
				return colorList.size();
			}
		});

	}

}
