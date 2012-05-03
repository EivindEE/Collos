CREATE TABLE image(
  image_uri VARCHAR2(2000),
  date_analyzed DATE,
  suspended INT,
  PRIMARY KEY(image_uri)
  );


  CREATE TABLE image_page(
  image_uri VARCHAR2(2000),
  page_uri VARCHAR2(2000),
	FOREIGN KEY (image_uri) REFERENCES image (image_uri),
  PRIMARY KEY (image_uri , page_uri)
);
  
  CREATE TABLE color(
  image_uri VARCHAR2(2000),
  color INT,
  relative_freq INT,
  FOREIGN KEY (image_uri) REFERENCES image (image_uri),
  PRIMARY KEY(image_uri, color)
);