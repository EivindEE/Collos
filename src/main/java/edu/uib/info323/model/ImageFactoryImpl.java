package edu.uib.info323.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ImageFactoryImpl implements ImageFactory {

	public Image createImage(String imageUri) {
		return this.createImage(imageUri, new ArrayList<String>());
	}

	public Image createImage(String imageUri, List<String> pageUri) {
		return new ImageImpl(imageUri, pageUri);
	}
}
