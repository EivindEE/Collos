package edu.uib.info323.model;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public interface Image {
	
	public abstract String getImageUri();

	public abstract List<String> getPageUris();
	
	public abstract void addPageUri(String pageUri);
	
	public abstract void addPageUri(List<String> pageUris);

}