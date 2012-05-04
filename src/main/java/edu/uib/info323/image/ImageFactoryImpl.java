package edu.uib.info323.image;

import java.util.ArrayList;
import java.util.List;

import edu.uib.info323.model.Image;
import edu.uib.info323.model.ImageFactory;
import edu.uib.info323.model.ImageImpl;

public class ImageFactoryImpl implements ImageFactory {

	public Image createImage(String imageUri) {
		return this.createImage(imageUri, new ArrayList<String>());
	}

	public Image createImage(String imageUri, List<String> pageUri) {
		return new ImageImpl(imageUri, pageUri);
	}
}
