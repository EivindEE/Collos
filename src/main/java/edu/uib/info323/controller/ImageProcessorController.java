package edu.uib.info323.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.uib.info323.dao.ColorFreqDao;
import edu.uib.info323.dao.ImageDao;
import edu.uib.info323.image.ImageProcessor;
import edu.uib.info323.model.ColorFreq;
import edu.uib.info323.model.Image;
import edu.uib.info323.model.ImageFactory;

@Scope("prototype")
@Controller
public class ImageProcessorController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageProcessorController.class);

	@Autowired
	ImageDao imageDao;

	@Autowired
	ColorFreqDao colorFreqDao;

	@Autowired
	ImageProcessor imageProcessor;

	@Autowired
	ImageFactory imageFactory;
//	File colorFreqFile = new File("src/main/resources/color_freq.sql");


	public ImageProcessorController(){
		LOGGER.debug(this.getClass().getName() + " bean created");
	}


	@RequestMapping(value="/process")
	public void process() throws IOException {
		LOGGER.debug("Starting to process images.");
		while(true) {
			LOGGER.debug("Starting processing loop");
			List<Image> images = imageDao.getUnprocessedImages();
			imageDao.updateAnalysedDate(images);
			List<ColorFreq> freqs = new LinkedList<ColorFreq>();
			for(Image image : images) {
				try {
					imageProcessor.setImage(image);
					image.setHeight(imageProcessor.getImageHeight());
					image.setWidth(imageProcessor.getImageWidth());
					freqs.addAll(imageProcessor.getColorFreqs());
				}
				catch(Exception e) {
					LOGGER.error("Got " + e.getClass() + "exception with message "+ e.getLocalizedMessage() + " for image " + image.getImageUri() + ". Deleting image from DB");
					try{
						imageDao.delete(image);
						LOGGER.debug("Image deleted"); 
					}
					catch (Exception ee) {
						LOGGER.error("Image not deleted, got exception " + e.getClass() );
					}
				}
			}
			colorFreqDao.insert(freqs);
			imageDao.update(images);
		}

	}

}
