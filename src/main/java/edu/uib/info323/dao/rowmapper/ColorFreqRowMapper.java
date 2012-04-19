package edu.uib.info323.dao.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import edu.uib.info323.model.ColorFreq;
import edu.uib.info323.model.ColorFreqImpl;
import edu.uib.info323.model.Image;

@Component
public class ColorFreqRowMapper implements RowMapper<ColorFreq>{
	private Image image;
	
	public ColorFreq mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new ColorFreqImpl(image, rs.getInt("color"), rs.getInt("relative_freq"));
	}
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}

}