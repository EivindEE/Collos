package edu.uib.info323.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import edu.uib.info323.dao.rowmapper.ColorFreqRowMapper;
import edu.uib.info323.model.ColorFreq;
import edu.uib.info323.model.ColorFreqFactory;
import edu.uib.info323.model.Image;

@Component
public class ColorFreqDaoMySql implements ColorFreqDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(ColorFreqDaoMySql.class);

	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	private ImageDao imageDao;

	@Autowired
	private ColorFreqRowMapper rowMapper;

	@Autowired
	private ColorFreqFactory freqFactory;

	@Autowired
	public void setDataSource(DataSource datasource) {
		jdbcTemplate = new NamedParameterJdbcTemplate(datasource);

	}

	public List<ColorFreq> getAllColors() {
		String sql = "SELECT * FROM color";
		return jdbcTemplate.query(sql,new HashMap<String, String>(), new RowMapper<ColorFreq>() {


			public ColorFreq mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				Image image = imageDao.getImageByImageUri(rs.getString("image_uri"));
				return freqFactory.createColorFreq(image, rs.getInt("color"), rs.getInt("relative_freq"));
			}

		});
	}

	public void insert(ColorFreq colorFreq) {
		String sql = "INSERT INTO color(image, color, relative_freq, compression) VALUES (:image, :color, :relative_freq, :compression)";
		jdbcTemplate.update(sql, this.getMapSqlParameterSource(colorFreq));
	}

	public List<ColorFreq> getImageColorFreqs(Image image) {
		String sql = "SELECT * FROM color WHERE image = :image";

		rowMapper.setImage(image);
		List<ColorFreq> colorFreqs = jdbcTemplate.query(sql,this.getMapSqlParameterSource(image), rowMapper);
		return colorFreqs;
	}

	public void insert(final List<ColorFreq> colorFreqs) {
		String sql = "INSERT INTO color(image, color, relative_freq, compression) " +
					 "VALUES ( :image, :color, :relative_freq, :compression) " +
					 "ON DUPLICATE KEY UPDATE relative_freq = :relative_freq, compression = :compression ";
		jdbcTemplate.batchUpdate(sql,this.getSqlParameterSource(colorFreqs) );
	}

	public void remove(ColorFreq colorFreq) {
		String sql = "DELETE FROM color WHERE image = :image";
		HashMap<String, Object> namedParameters = new HashMap<String, Object>();
		namedParameters.put("image_uri",colorFreq.getImage().getImageUri());
		jdbcTemplate.update(sql, namedParameters);
	}

	public void remove(final List<ColorFreq> colorFreqs) {
		String sql = "DELETE FROM color WHERE image = :image";
		jdbcTemplate.batchUpdate(sql, this.getSqlParameterSource(colorFreqs));
	}
	
	
	
	private SqlParameterSource[] getSqlParameterSource(List<ColorFreq> colorFreqs) {
		List<SqlParameterSource> parameters = new ArrayList<SqlParameterSource>();
		for(ColorFreq freq : colorFreqs) {
			parameters.add(this.getMapSqlParameterSource(freq));
		}
		return parameters.toArray(new SqlParameterSource[0]);
	}
	
	private MapSqlParameterSource getMapSqlParameterSource(ColorFreq colorFreq) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("image",colorFreq.getImage().getImageUri().hashCode());
		namedParameters.addValue("color",colorFreq.getColor());
		namedParameters.addValue("relative_freq",colorFreq.getRelativeFreq());
		namedParameters.addValue("compression",colorFreq.getCompression());
		return namedParameters;
	}

	private MapSqlParameterSource getMapSqlParameterSource(Image image) {
		MapSqlParameterSource parameterSource = new MapSqlParameterSource("image_uri",image.getImageUri());
		parameterSource.addValue("id", image.getImageUri().hashCode());
		return parameterSource;
	}
}
