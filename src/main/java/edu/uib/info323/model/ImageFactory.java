package edu.uib.info323.model;

import java.util.List;

public interface ImageFactory {
	public Image createImage(String imageUri);
	
	public Image createImage(String imageUri, List<String> pageUri);
}
