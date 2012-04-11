package edu.uib.info323.model;

import org.springframework.stereotype.Component;

@Component
public interface Image {

	public abstract String getImageUri();

	public abstract void setImageUri(String imageUri);

	public abstract String getPageUri();

	public abstract void setPageUri(String pageUri);

}