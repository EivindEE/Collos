package edu.uib.info323.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.uib.info323.image.CompressedColorFactory;

@Component
public class ColorFreqFactoryImpl implements ColorFreqFactory{

	@Autowired
	CompressedColorFactory colorFactory;
	
	public ColorFreq createColorFreq(Image image, int color, int relativeFreq) {
		return new ColorFreqImpl(image, color, relativeFreq, colorFactory.getDefaultCompression());
	}

}
