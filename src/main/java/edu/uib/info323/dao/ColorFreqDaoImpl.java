package edu.uib.info323.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import edu.uib.info323.model.ColorFreq;
import edu.uib.info323.model.ColorFreqImpl;
import edu.uib.info323.model.Image;

@Component
public class ColorFreqDaoImpl implements ColorFreqDao {
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ImageDao imageDao;
	
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
				return new ColorFreqImpl(image, rs.getInt("color"), rs.getInt("relative_freq"));
			}
			
		});
		
	}

	public void insert(ColorFreq colorFreq) {
		String sql = "INSERT INTO COLOR_FREQ(image_uri, color, relative_freq) VALUES (?,?,?)";
		jdbcTemplate.update(sql, new Object[] {colorFreq.getImage().getImageUri(),colorFreq.getColor(),colorFreq.getRelativeFreq()});
	}
	
	

}
