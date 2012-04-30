package edu.uib.info323.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.uib.info323.dao.ColorFreqDao;
import edu.uib.info323.dao.ImageDao;
import edu.uib.info323.image.ImageProcessor;
import edu.uib.info323.model.ColorFreq;
import edu.uib.info323.model.Image;

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
		List<Image> unprocessed = imageDao.getUnprocessedImages();
		LOGGER.debug("Images to process: "  + unprocessed.size());
		for(Image image : unprocessed) {
			LOGGER.debug("Starting to process ");
			LOGGER.debug(image.getImageUri());
			try {
			imageProcessor.setImage(image);
			List<ColorFreq> freqs = imageProcessor.getColorFreqs();
			colorFreqDao.batchInsert(freqs);
			LOGGER.debug("Finished processing, found " + freqs.size() + " colors above threshold. Starting to write SQL to file");
			for(ColorFreq freq : freqs) {
				writer.append("INSERT INTO COLOR_FREQ (image_uri, color, relative_freq) " +
						"VALUES ('" + freq.getImage().getImageUri() + "'," +
								"" + freq.getColor() + "," + freq.getRelativeFreq() + ");\n");
			}
			writer.flush();
			LOGGER.debug("Wrote SQL to file.");
			}
			catch(Exception e) {
				LOGGER.error("Got " + e.getClass() + "exception for image " + image.getImageUri() + ". Deleting image from DB");
				imageDao.delete(image);
			}
		}

	}
	
}
