package edu.uib.info323.model;

import java.util.List;

public interface ImageFactory {
	public Image createImage(String imageUri);
	
	public Image createImage(String imageUri, int id);
	
	public Image createImage(String imageUri, String pageUri);
	
	public Image createImage(String imageUri, String pageUri, int id);
	
	public Image createImage(String imageUri, List<String> pageUris);
	
	public Image createImage(String imageUri, List<String> pageUris, int id);

	public Image createImage(String imageUri, int height, int width);
	
	public Image createImage(String imageUri, int height, int width, int id);
	
	public Image createImage(String imageUri, List<String> pageUris, int height, int width, int id);

	public Image createImage(String imageUri, String pageUri, int height, int width);
}
