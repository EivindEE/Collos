package edu.uib.info323.controller;

import java.io.File;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.uib.info323.dao.ImageDao;
import edu.uib.info323.dao.ImageDaoImpl;
import edu.uib.info323.model.Image;
import edu.uib.info323.model.ImageImpl;

@Component
public class DBSetup {
	@Autowired
	private DataSource dataSource;


	@Autowired
	public void setUp() {
		ImageDao daoImpl = new ImageDaoImpl();
		daoImpl.setDataSource(dataSource);

		File imageFolder = new File("src/main/webapp/resources/testimg");
		for(File f :imageFolder.listFiles()){
			Image img = new ImageImpl("resources/testimg/" + f.getName(),"http://example.org/" + f.getName());
			daoImpl.insert(img);
		}
	}
}
