package edu.uib.info323.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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

	File colorFreqFile = new File("src/main/resources/color_freq.sql");



	BufferedWriter writer;

	public ImageProcessorController() throws IOException {
		writer = new BufferedWriter(new FileWriter(colorFreqFile,true));
	}


	@RequestMapping(value="/process")
	public void process() throws IOException {
		LOGGER.debug("Starting to process images.");
		while(true) {
			List<Image> unprocessed = imageDao.getUnprocessedImages();
			LOGGER.debug("Setting images as analyzed");
			imageDao.updateAnalysedDate(unprocessed);
			for(Image image : unprocessed) {
				try {
					imageProcessor.setImage(image);
					List<ColorFreq> freqs = imageProcessor.getColorFreqs();
					colorFreqDao.batchInsert(freqs);
				}
				catch(Exception e) {
					LOGGER.error("Got " + e.getClass() + "exception for image " + image.getImageUri() + ". Deleting image from DB");
					try{
						imageDao.delete(image);
						LOGGER.debug("Image deleted"); 
					}
					catch (Exception ee) {
						LOGGER.error("Image not deleted, got exception " + e.getClass() );
					}
				}
			}
		}

	}

}
