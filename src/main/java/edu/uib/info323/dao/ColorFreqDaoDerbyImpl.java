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
import edu.uib.info323.model.ColorFreqFactory;
import edu.uib.info323.model.ColorFreqImpl;
import edu.uib.info323.model.Image;


public abstract class ColorFreqDaoDerbyImpl implements ColorFreqDao {
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private ColorFreqRowMapper rowMapper;
	@Autowired
	private ImageDao imageDao;
	@Autowired
	private ColorFreqFactory freqFactory;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ColorFreqDaoDerbyImpl.class);
	
	@Autowired
	public void setDataSource(DataSource datasource) {
		this.jdbcTemplate = new JdbcTemplate(datasource);
		this.imageDao.setDataSource(datasource);
	}

	public List<ColorFreq> getAllColors() {
		String sql = "SELECT * FROM COLOR_FREQ";
		return jdbcTemplate.query(sql, new RowMapper<ColorFreq>() {

			public ColorFreq mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				Image image = imageDao.getImageByImageUri(rs.getString("image_uri"));
				return freqFactory.createColorFreq(image, rs.getInt("color"), rs.getInt("relative_freq"));
			}
			
		});
		
	}

	public void insert(ColorFreq colorFreq) {
		String sql = "INSERT INTO COLOR_FREQ(image_uri, color, relative_freq) VALUES (?,?,?)";
		jdbcTemplate.update(sql, new Object[] {colorFreq.getImage().getImageUri(),colorFreq.getColor(),colorFreq.getRelativeFreq()});
	}

	public List<ColorFreq> getImageColorFreqs(Image image) {
		String sql = "SELECT * FROM COLOR_FREQ WHERE image_uri = ?";
		rowMapper.setImage(image);
		List<ColorFreq> colorFreqs = jdbcTemplate.query(sql,new Object[] {image.getImageUri()}, rowMapper);
		LOGGER.debug(colorFreqs.toString());
		return colorFreqs;
	}

	/**
	 * Ignores duplicates
	 */
	public void insert(final List<ColorFreq> colorList) {
		String sql = "INSERT INTO COLOR_FREQ (" +
				"SELECT ? AS image_uri, ? AS color, ? AS relative_freq " +
				"FROM COLOR_FREQ " +
				"WHERE image_uri = ? AND color = ? AND relative_freq = ? " +
				"HAVING COUNT(*) = 0" +
				")";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ColorFreq colorFreq = colorList.get(i);
				ps.setString(1, colorFreq.getImage().getImageUri());
				ps.setInt(2, colorFreq.getColor());
				ps.setInt(3, colorFreq.getRelativeFreq());
				ps.setString(4, colorFreq.getImage().getImageUri());
				ps.setInt(5, colorFreq.getColor());
				ps.setInt(6, colorFreq.getRelativeFreq());
			}
			
			public int getBatchSize() {
				return colorList.size();
			}
		});
	}
	
	

}
