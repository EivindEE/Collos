package edu.uib.info323.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ImageFactoryImpl implements ImageFactory {

	public Image createImage(String imageUri) {
		return this.createImage(imageUri, new ArrayList<String>());
	}

	public Image createImage(String imageUri, String pageUri) {
		List<String> pageUris = new ArrayList<String>();
		pageUris.add(pageUri);
		return this.createImage(imageUri, pageUris);
	}
	
	public Image createImage(String imageUri, String pageUri, int height, int width) {
		List<String> pageUris = new ArrayList<String>();
		pageUris.add(pageUri);
		return this.createImage(imageUri, pageUris, height, width);
	}
	
	
	public Image createImage(String imageUri, List<String> pageUris, int height, int width) {
		return this.createImage(-1, imageUri, pageUris, height, width);
	}

	public Image createImage(String imageUri, List<String> pageUris) {
		return this.createImage(imageUri, pageUris, 0, 0);
	}

	public Image createImage(String imageUri, int height, int width) {
		return this.createImage(imageUri, new ArrayList<String>(), height, width);
	}
	
	public Image createImage(int id, String imageUri, List<String> pageUris, int height, int width) {
		return new ImageImpl(id, imageUri, pageUris, height, width);
	}

}
