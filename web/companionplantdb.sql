/*This database holds the information about plants and their companions
the plant table holds information on species, type (vegetable, fruit, herb, etc.), and the 
seasons they grow insert
companion holdds information of the cpmpanion plants to the primary plant
all plants that are in the plant table are in the companion table at least once
and all plants in the companion table are in the plant table
This database is modeled after a recursive many to many relationship*/

/* creates database*/
CREATE DATABASE companionplant;
USE companionplant;

/* Creates pllant table with a plantname, species, type (vegetable or fruit), and season*/
CREATE TABLE plant(
	plantname varchar(30),
	species varchar(30),
	type varchar(30),
	season varchar(50),
	PRIMARY KEY (plantname)	
);

/* Creates companion table witht he plant name as the foreign key connecting the two tables
only has companion plant's name and id*/
CREATE TABLE companion(
	plantname varchar(30),
	companionname varchar(30),
	compplantid int,
	PRIMARY KEY (compplantid),
	FOREIGN KEY (plantname) REFERENCES plant(plantname),
	FOREIGN KEY (companionname) REFERENCES plant(plantname)
);


/*creates each plant in the database*/
INSERT INTO plant VALUES( 'beetroot', 'beta vulgaris','vegetable', 'spring/summer');
INSERT INTO plant VALUES( 'cabbage', 'brassica oleracea', 'vegetable', 'annual');
INSERT INTO plant VALUES( 'carrot', 'daucus carota', 'vegetable', 'spring/fall');
INSERT INTO plant VALUES( 'potato', 'solanum tuberosum', 'vegetable', 'summer');
INSERT INTO plant VALUES( 'tomato', 'solanum  lycopersicum', 'fruit', 'summer');
INSERT INTO plant VALUES( 'onion', 'allium cepa', 'vegetable', 'fall');
INSERT INTO plant VALUES( 'radish', 'raphanus raphanistrum', 'vegetable', 'spring/fall');
INSERT INTO plant VALUES( 'swiss chard', 'beta vulgaris', 'vegetable', 'summer/fall/spring');
INSERT INTO plant VALUES( 'peas', 'Pisum sativum', 'vegetable', 'winter');
INSERT INTO plant VALUES( 'cucumber', 'cucumis sativus', 'vegetable', 'spring/summer' );
INSERT INTO plant VALUES( 'lettuce', 'lactuca sativa', 'vegetable', 'annual');
INSERT INTO plant VALUES( 'parsnip ','pastinaca sativa', 'vegetable', 'fall/winter');
INSERT INTO plant VALUES('corn', 'zea mays', 'vegetable', 'summer');
INSERT INTO plant VALUES( 'spinach', 'spinacia oleracea', 'vegetable', 'annual/spring/fall');
INSERT INTO plant VALUES( 'turnip', 'brassica rapa', 'vegetable', 'summer');
INSERT INTO plant VALUES( 'leek', 'allium ampeloprasum', 'vegetable', 'winter');
INSERT INTO plant VALUES( 'broccoli', 'brassica oleracea', 'vegetable', 'annual');
INSERT INTO plant VALUES( 'brussel sprouts', 'brassica oleracea', 'vegetable', 'annual');
INSERT INTO plant VALUES( 'kale', 'brassica oleracea', 'vegetable', 'fall/winter');
INSERT INTO plant VALUES( 'squash', 'cucurbita', 'vegetable', 'summer');

/*companion plants divided based on their primary plants*/
INSERT INTO companion VALUES( 'beetroot', 'brussel sprouts', 03);
INSERT INTO companion VALUES( 'beetroot', 'broccoli', 04);
INSERT INTO companion VALUES( 'beetroot', 'onion', 05);
INSERT INTO companion VALUES( 'beetroot', 'cabbage', 01);
INSERT INTO companion VALUES( 'beetroot', 'swiss chard', 02);

INSERT INTO companion VALUES( 'cabbage', 'kale', 16);
INSERT INTO companion VALUES( 'cabbage', 'broccoli', 15);
INSERT INTO companion VALUES( 'cabbage', 'swiss chard', 14);
INSERT INTO companion VALUES( 'cabbage', 'spinach', 13);
INSERT INTO companion VALUES( 'cabbage', 'brussel sprouts', 12);
INSERT INTO companion VALUES( 'cabbage', 'tomato', 11);

INSERT INTO companion VALUES( 'carrot', 'peas', 26);
INSERT INTO companion VALUES( 'carrot', 'leek', 25);
INSERT INTO companion VALUES( 'carrot', 'onion', 24);
INSERT INTO companion VALUES( 'carrot', 'lettuce', 23);
INSERT INTO companion VALUES( 'carrot', 'radish', 22);
INSERT INTO companion VALUES( 'carrot', 'cabbage', 21);

INSERT INTO companion VALUES( 'potato', 'peas', 34);
INSERT INTO companion VALUES( 'potato', 'corn', 33);
INSERT INTO companion VALUES( 'potato', 'cabbage', 32);
INSERT INTO companion VALUES( 'potato', 'squash', 31);

INSERT INTO companion VALUES( 'tomato', 'carrot', 43);
INSERT INTO companion VALUES( 'tomato', 'onion', 42);
INSERT INTO companion VALUES( 'tomato', 'cabbage', 41);

INSERT INTO companion VALUES( 'onion', 'lettuce', 55);
INSERT INTO companion VALUES( 'onion', 'carrot', 54);
INSERT INTO companion VALUES( 'onion', 'parsnip', 53);
INSERT INTO companion VALUES( 'onion', 'cabbage', 52);
INSERT INTO companion VALUES( 'onion', 'beetroot', 51);

INSERT INTO companion VALUES( 'radish', 'peas', 66);
INSERT INTO companion VALUES( 'radish', 'lettuce', 65);
INSERT INTO companion VALUES( 'radish', 'spinach', 64);
INSERT INTO companion VALUES( 'radish', 'parsnip', 63);
INSERT INTO companion VALUES( 'radish', 'carrot', 62);
INSERT INTO companion VALUES( 'radish', 'cucumber', 61);

INSERT INTO companion VALUES( 'swiss chard', 'onion', 73);
INSERT INTO companion VALUES( 'swiss chard', 'beetroot', 72);
INSERT INTO companion VALUES( 'swiss chard', 'cabbage', 71);

INSERT INTO companion VALUES( 'peas', 'turnip', 85);
INSERT INTO companion VALUES( 'peas', 'corn', 84);
INSERT INTO companion VALUES( 'peas', 'radish', 83);
INSERT INTO companion VALUES( 'peas', 'carrot', 82);
INSERT INTO companion VALUES( 'peas', 'cucumber', 81);

INSERT INTO companion VALUES( 'cucumber', 'corn', 93);
INSERT INTO companion VALUES( 'cucumber', 'cabbage', 92);
INSERT INTO companion VALUES( 'cucumber', 'radish', 91);

INSERT INTO companion VALUES( 'lettuce', 'parsnip', 104);
INSERT INTO companion VALUES( 'lettuce', 'carrot', 103);
INSERT INTO companion VALUES( 'lettuce', 'radish', 102);
INSERT INTO companion VALUES( 'lettuce', 'beetroot', 101);

INSERT INTO companion VALUES( 'parsnip', 'lettuce',113);
INSERT INTO companion VALUES( 'parsnip', 'onion', 112);
INSERT INTO companion VALUES( 'parsnip', 'onion', 111);

INSERT INTO companion VALUES( 'corn', 'potato', 123);
INSERT INTO companion VALUES( 'corn', 'peas', 122);
INSERT INTO companion VALUES( 'corn', 'cucumber', 121);

INSERT INTO companion VALUES( 'spinach', 'cabbage', 132);
INSERT INTO companion VALUES( 'spinach', 'radish', 131);

INSERT INTO companion VALUES( 'turnip', 'peas', 141);

INSERT INTO companion VALUES( 'leek', 'carrot', 151);

INSERT INTO companion VALUES( 'broccoli', 'beetroot', 162);
INSERT INTO companion VALUES( 'broccoli', 'cabbage', 161);

INSERT INTO companion VALUES( 'brussel sprouts', 'beetroot', 172);
INSERT INTO companion VALUES( 'brussel sprouts', 'cabbage', 171);

INSERT INTO companion VALUES( 'kale', 'cabbage', 181);

/*test queries to check if everything was recorded and added to the table correctly*/ 
SELECT * FROM plant;
SELECT * FROM companion;

/*selects the companion plants for a specified primary plant*/
SELECT companionname FROM companion WHERE plantname=(SELECT plantname FROM plant WHERE plantname='swiss chard');
/*counts the number of companion pants the primary plant has*/
SELECT COUNT(companionname) FROM companion WHERE plantname=(SELECT plantname FROM plant WHERE plantname='lettuce');


/*
drop statements in the ase te database needs to be modified

DROP TABLE companion;
DROP TABLE plant;
DROP DATABASE companionplant;
*/

