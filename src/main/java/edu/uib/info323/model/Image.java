package edu.uib.info323.model;

import org.springframework.stereotype.Component;

@Component
public interface Image {
	
	public abstract String getImageUri();

	public abstract String getPageUri();

}