CREATE TABLE image(
	image_uri VARCHAR(500),
	date_analyzed DATE,
	suspended INT,
	PRIMARY KEY(image_uri)
);


CREATE TABLE image_page(
	image_uri VARCHAR(500),
	page_uri VARCHAR(500),
	FOREIGN KEY (image_uri) REFERENCES image (image_uri) ON DELETE CASCADE ON UPDATE CASCADE ,
	PRIMARY KEY (image_uri , page_uri)
);
  
CREATE TABLE color(
	image_uri VARCHAR(500),
	color INT,
	relative_freq INT,
	FOREIGN KEY (image_uri) REFERENCES image (image_uri) ON DELETE CASCADE ON UPDATE CASCADE ,
	PRIMARY KEY(image_uri, color)
);