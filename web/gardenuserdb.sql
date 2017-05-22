/*This database stores information of the user accounts
This database contains information from name, user name and e-mail 
to the current gardens registered to the user and the plants 
contained in each garden
Several test queries have been created to simiulate different users andd their gardens.
*/
/*Create database*/
CREATE DATABASE smartgarden;
USE smartgarden;

/*tale containing information pertaining to the user*/
CREATE TABLE userinfo (
	username varchar(20),
    name varchar(30),
	email varchar(60),
    password varchar(20),
    experiencelevel varchar(10),
    PRIMARY KEY (username),
    UNIQUE KEY username (username)
);

/*Table containing information pertaining to the garden*/
CREATE TABLE garden(
    username varchar(20),
	gardenid varchar(3),
    gardenname varchar(40),
    moisturelevel int,
	temperature int,
    wateringday varchar(12),
    wateringtime TIME,
    numofplants int,
    PRIMARY KEY (gardenid),
    UNIQUE KEY (gardenid),
    FOREIGN KEY (username) REFERENCES userinfo (username)
);

/*Information about the plant housed in a garden*/
CREATE TABLE plant (
	plantid varchar(4),
    melon varchar(20),
	plantspecies varchar(20),
    vegetable varchar(20),
    givenname varchar(20),
    growth int,
    PRIMARY KEY (plantid),
    UNIQUE KEY (plantid)
);

/*Because garden and plant have a many to many relationship
this table helps connect the two tables and keeps track of when a plant is planted*/
CREATE TABLE gardenbed (
	gardenid varchar(3),
    plantid varchar(4),
	dateplanted DATE,
    timeplanted TIME,
    PRIMARY KEY (timeplanted),
    FOREIGN KEY (gardenid) REFERENCES garden (gardenid),
    FOREIGN KEY (plantid) REFERENCES plant (plantid)
);

/*
To be implemented
Keeps track of any companion plants that are in the garden
CREATE TABLE companionplant (
	plantid varchar(3),
    compname varchar(20),
    compspecies varchar(20),
    comptype varchar(20),
    description varchar(200),
    PRIMARY KEY(compname),
    UNIQUE KEY(compname),
    FOREIGN KEY (plantid) REFERENCES plant (plantid)
);*/

/*Test queries for users */
INSERT INTO userinfo VALUE( 'Gina_L', 'Gina Lane', 'ginalane8@domain.com', 'password1', 'Botanist');
INSERT INTO userinfo VALUE('Mikey', 'Michael Smith', 'msmith23@domain.com', 'password2', 'Hobbiest');
INSERT INTO userinfo VALUE('Sunshine_Steph', 'Stephanie Toledo', 'stephiet@domain.com', 'password3', 'Beginner');
INSERT INTO userinfo VALUE('MaryGardens', 'Marianne Cruz', 'marycruz12@domain.com', 'password4', 'Hobbiest');
INSERT INTO userinfo VALUE('Tyler_R', 'Tyler Reeve', 'tylerreeve31@domain.com', 'password5', 'Botanist');

/*Gardens registered to Gina Lane*/
INSERT INTO garden VALUE( 'Gina_L','001', 'heerb', 89, 78, 'tuesday','13:30', 8);
INSERT INTO garden VALUE( 'Gina_L', '002', 'fruit', 76, 78, 'wednesday', '14:50', 8);
INSERT INTO garden VALUE( 'Gina_L','003', 'vegetable', 90, 78, 'friday','9:45', 8);

/* Gardens registered to Michael Smith*/
INSERT INTO garden VALUE( 'Mikey', '004', 'outside garden', 93, 86, 'Wednesday','17:32', 10);
INSERT INTO garden VALUE( 'Mikey', '005', 'window garden', 64, 86, 'satruday', '8:20', 5);

/*Gardens registered to Stephanie Toledo*/
INSERT INTO garden VALUE( 'Sunshine_Steph', '006', 'Sunshine Land', 79, 74, 'tuesday', '9:15', 5);
/*garden registered to MArianne Cruz*/
INSERT INTO garden VALUE( 'MaryGardens', '007', 'Herb',  74, 89, 'tuesday','6:13', 8);
INSERT INTO garden VALUE( 'MaryGardens', '008', 'Vegetable', 76, 89, 'tuesday','6:13', 8);

/*gardens registered to Tyler Reeves*/
INSERT INTO garden VALUE( 'Tyler_R', '009', 'greenhouse vegetables', 91, 87, 'thursday', '16:25', 12);
INSERT INTO garden VALUE( 'Tyler_R', '010', 'outside vegetables', 65, 84, 'tuesday','9:34', 16);
INSERT INTO garden VALUE( 'Tyler_R', '011', 'fruit', 76, 84, 'tuesday','9:34', 6);

/*plants registered to each of the gardens*/
/* plants in Gina's garden*/
INSERT INTO plant VALUE('0001', 'mint', 'plantspecies', 'herb', 'givenname', 50);
INSERT INTO plant VALUE('0002', 'mint', 'plantspecies', 'herb', 'givenname', 50);
INSERT INTO plant VALUE('0003', 'parsley', 'plantspecies', 'herb', 'givenname', 50);
INSERT INTO plant VALUE('0004', 'parsley', 'plantspecies', 'herb', 'givenname', 50);
INSERT INTO plant VALUE('0005', 'thyme', 'plantspecies', 'herb', 'givenname', 50);
INSERT INTO plant VALUE('0006', 'rosemary', 'plantspecies', 'herb', 'givenname', 50);
INSERT INTO plant VALUE('0007', 'cilantro', 'plantspecies', 'herb', 'givenname', 50);
INSERT INTO plant VALUE('0008', 'cilantro', 'plantspecies', 'herb', 'givenname', 50);
INSERT INTO plant VALUE('0009', 'Strawberry', 'plantspecies', 'fruit', 'givenname', 50);
INSERT INTO plant VALUE('0010', 'strawberry', 'plantspecies', 'fruit', 'givenname', 50);
INSERT INTO plant VALUE('0011', 'strawberry', 'plantspecies', 'fruit', 'givenname', 50);
INSERT INTO plant VALUE('0012', 'melon', 'plantspecies', 'fruit', 'givenname', 50);
INSERT INTO plant VALUE('0013', 'blueberry', 'plantspecies', 'fruit', 'givenname', 50);
INSERT INTO plant VALUE('0014', 'blueberry', 'plantspecies', 'fruit', 'givenname', 50);
INSERT INTO plant VALUE('0015', 'blueberry', 'plantspecies', 'fruit', 'givenname', 50);
INSERT INTO plant VALUE('0016', 'lettuce', 'plantspecies', 'fruit', 'givenname', 50);
INSERT INTO plant VALUE('0017', 'lettuce', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0018', 'lettuce', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0019', 'onion', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0020', 'onion', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0021', 'onion', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0022', 'onion', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0023', 'leek', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0024', 'leek', 'plantspecies', 'vegetable', 'givenname', 50);

/*plants in Mikey's gardens*/
INSERT INTO plant VALUE('0025', 'leek', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0026', 'leek', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0027', 'leek', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0028', 'melon', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0029', 'melon', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0030', 'onion', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0031', 'onion', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0032', 'onion', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0033', 'onion', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0034', 'leek', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0035', 'thyme', 'plantspecies', 'herb', 'givenname', 50);
INSERT INTO plant VALUE('0036', 'rosemary', 'plantspecies', 'herb', 'givenname', 50);
INSERT INTO plant VALUE('0037', 'chives', 'plantspecies', 'herb', 'givenname', 50);
INSERT INTO plant VALUE('0038', 'cilantro', 'plantspecies', 'herb', 'givenname', 50);
INSERT INTO plant VALUE('0039', 'mint', 'plantspecies', 'herb', 'givenname', 50);

/*plants in Stephanie's garden*/
INSERT INTO plant VALUE('0040', 'strawberry', 'plantspecies', 'fruit', 'givenname', 50);
INSERT INTO plant VALUE('0041', 'strawberry', 'plantspecies', 'fruit', 'givenname', 50);
INSERT INTO plant VALUE('0042', 'strawberry', 'plantspecies', 'fruit', 'givenname', 50);
INSERT INTO plant VALUE('0043', 'cucumber', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0044', 'lettuce', 'plantspecies', 'vegetable', 'givenname', 50);

/*plants in Marrianne's garden*/
INSERT INTO plant VALUE('0045', 'rosemary', 'plantspecies', 'herb', 'givenname', 50);
INSERT INTO plant VALUE('0046', 'thyme', 'plantspecies', 'herb', 'givenname', 50);
INSERT INTO plant VALUE('0047', 'rosemary', 'plantspecies', 'herb', 'givenname', 50);
INSERT INTO plant VALUE('0048', 'mint', 'plantspecies', 'herb', 'givenname', 50);
INSERT INTO plant VALUE('0049', 'oregano', 'plantspecies', 'herb', 'givenname', 50);
INSERT INTO plant VALUE('0050', 'cilantro', 'plantspecies', 'herb', 'givenname', 50);
INSERT INTO plant VALUE('0051', 'mint', 'plantspecies', 'herb', 'givenname', 50);
INSERT INTO plant VALUE('0052', 'thyme', 'plantspecies', 'herb', 'givenname', 50);
INSERT INTO plant VALUE('0053', 'lettuce', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0054', 'lettuce', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0055', 'lettuce', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0056', 'lettuce', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0057', 'lettuce', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0058', 'onion', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0059', 'onion', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0060', 'onion', 'plantspecies', 'vegetable', 'givenname', 50);

/*plants in tyler's garden*/
INSERT INTO plant VALUE('0061', 'onion', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0062', 'onion', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0063', 'leek', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0064', 'leek', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0065', 'leek', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0066', 'parsnip', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0067', 'parsnip', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0068', 'parsnip', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0069', 'broccoli', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0070', 'lettuce', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0071', 'lettuce', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0072', 'lettuce', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0073', 'lettuce', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0074', 'lettuce', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0075', 'lettuce', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0076', 'broccoli', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0077', 'broccoli', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0078', 'broccoli', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0079', 'lettuce', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0080', 'lettuce', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0081', 'lettuce', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0082', 'lettuce', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0083', 'broccoli', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0084', 'carrot', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0085', 'carrot', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0086', 'carrot', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0087', 'carrot', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0088', 'potato', 'plantspecies', 'vegetable', 'givenname', 50);
INSERT INTO plant VALUE('0089', 'potato', 'plantspecies', 'fruit', 'givenname', 50);
INSERT INTO plant VALUE('0090', 'strawberry', 'plantspecies', 'fruit', 'givenname', 50);
INSERT INTO plant VALUE('0091', 'strawberry', 'plantspecies', 'fruit', 'givenname', 50);
INSERT INTO plant VALUE('0092', 'potato', 'plantspecies', 'fruit', 'givenname', 50);
INSERT INTO plant VALUE('0093', 'blueberry', 'plantspecies', 'fruit', 'givenname', 50);
INSERT INTO plant VALUE('0094', 'blueberry', 'plantspecies', 'fruit', 'givenname', 50);

/*Gina's gardens with plants*/
INSERT INTO gardenbed VALUE(  '001', '0001', '2017-1-12', '6:30:45');
INSERT INTO gardenbed VALUE(  '001', '0002', '2017-1-12', '6:35:20');
INSERT INTO gardenbed VALUE(  '001', '0003', '2017-1-12', '6:38:25');
INSERT INTO gardenbed VALUE(  '001', '0004', '2017-1-12', '6:42:34');
INSERT INTO gardenbed VALUE(  '001', '0005', '2017-1-12', '6:48:18');
INSERT INTO gardenbed VALUE(  '001', '0006', '2017-1-12', '6:50:21');
INSERT INTO gardenbed VALUE(  '001', '0007', '2017-1-12', '6:50:58');
INSERT INTO gardenbed VALUE(  '001', '0008', '2017-1-12', '6:51:34');
INSERT INTO gardenbed VALUE(  '002', '0009', '2017-1-13', '6:52:59');
INSERT INTO gardenbed VALUE(  '002', '0010', '2017-1-13', '6:54:10');
INSERT INTO gardenbed VALUE(  '002', '0011', '2017-1-13', '6:56:04');
INSERT INTO gardenbed VALUE(  '002', '0012', '2017-1-13', '6:58:24');
INSERT INTO gardenbed VALUE(  '002', '0013', '2017-1-13', '6:01:01');
INSERT INTO gardenbed VALUE(  '002', '0014', '2017-1-13', '6:03:03');
INSERT INTO gardenbed VALUE(  '002', '0015', '2017-1-13', '6:03:051');
INSERT INTO gardenbed VALUE(  '002', '0016', '2017-1-13', '6:05:51');
INSERT INTO gardenbed VALUE(  '003', '0017', '2017-1-20', '6:06:23');
INSERT INTO gardenbed VALUE(  '003', '0018', '2017-1-20', '6:07:07');
INSERT INTO gardenbed VALUE(  '003', '0019', '2017-1-20', '6:08:32');
INSERT INTO gardenbed VALUE(  '003', '0020', '2017-1-20', '6:09:43');
INSERT INTO gardenbed VALUE(  '003', '0021', '2017-1-20', '6:10:04');
INSERT INTO gardenbed VALUE(  '003', '0022', '2017-1-20', '6:10:58');
INSERT INTO gardenbed VALUE(  '003', '0023', '2017-1-20', '6:11:14');
INSERT INTO gardenbed VALUE(  '003', '0024', '2017-1-20', '6:12:20');

/*Mikey's Gardens and plants*/
INSERT INTO gardenbed VALUE(  '004', '0025', '2017-1-23', '6:13:24');
INSERT INTO gardenbed VALUE(  '004', '0026', '2017-1-23', '6:14:56');
INSERT INTO gardenbed VALUE(  '004', '0027', '2017-1-23', '6:15:16');
INSERT INTO gardenbed VALUE(  '004', '0028', '2017-1-23', '6:16:49');
INSERT INTO gardenbed VALUE(  '004', '0029', '2017-1-23', '6:17:18');
INSERT INTO gardenbed VALUE(  '004', '0030', '2017-1-23', '6:17:59');
INSERT INTO gardenbed VALUE(  '004', '0031', '2017-1-23', '6:20:00');
INSERT INTO gardenbed VALUE(  '004', '0032', '2017-1-23', '6:20:54');
INSERT INTO gardenbed VALUE(  '004', '0034', '2017-1-23', '6:21:03');
INSERT INTO gardenbed VALUE(  '005', '0035', '2017-1-23', '7:45:23');
INSERT INTO gardenbed VALUE(  '005', '0036', '2017-1-23', '7:52:32');
INSERT INTO gardenbed VALUE(  '005', '0037', '2017-1-23', '7:53:03');
INSERT INTO gardenbed VALUE(  '005', '0038', '2017-1-23', '7:53:59');
INSERT INTO gardenbed VALUE(  '005', '0039', '2017-1-23', '7:54:32');

/*Stephanie's garden and plants*/
INSERT INTO gardenbed VALUE(  '006', '0040', '2017-1-23', '7:55:59');
INSERT INTO gardenbed VALUE(  '006', '0041', '2017-1-23', '7:56:12');
INSERT INTO gardenbed VALUE(  '006', '0042', '2017-1-23', '7:57:29');
INSERT INTO gardenbed VALUE(  '006', '0043', '2017-1-23', '7:58:58');
INSERT INTO gardenbed VALUE(  '006', '0044', '2017-1-23', '8:00:34');

/*Marianne's gardens and plants*/
INSERT INTO gardenbed VALUE(  '007', '0045', '2017-3-17', '8:01:45');
INSERT INTO gardenbed VALUE(  '007', '0046', '2017-3-17', '8:03:13');
INSERT INTO gardenbed VALUE(  '007', '0047', '2017-3-17', '8:04:04');
INSERT INTO gardenbed VALUE(  '007', '0048', '2017-3-17', '8:05:01');
INSERT INTO gardenbed VALUE(  '007', '0049', '2017-3-17', '8:06:12');
INSERT INTO gardenbed VALUE(  '007', '0050', '2017-3-17', '8:07:49');
INSERT INTO gardenbed VALUE(  '007', '0051', '2017-3-17', '8:08:13');
INSERT INTO gardenbed VALUE(  '007', '0052', '2017-3-17', '8:08:59');
INSERT INTO gardenbed VALUE(  '008', '0053', '2017-5-20', '8:09:34');
INSERT INTO gardenbed VALUE(  '008', '0054', '2017-5-20', '8:10:58');
INSERT INTO gardenbed VALUE(  '008', '0055', '2017-5-20', '8:11:47');
INSERT INTO gardenbed VALUE(  '008', '0056', '2017-5-20', '8:12:54');
INSERT INTO gardenbed VALUE(  '008', '0057', '2017-5-20', '8:13:01');
INSERT INTO gardenbed VALUE(  '008', '0058', '2017-5-20', '8:14:23');
INSERT INTO gardenbed VALUE(  '008', '0059', '2017-5-20', '8:15:56');
INSERT INTO gardenbed VALUE(  '008', '0060', '2017-5-20', '8:16:56');

/* Tyler's gardens and plants*/
INSERT INTO gardenbed VALUE(  '009', '0061', '2017-5-21', '8:17:23');
INSERT INTO gardenbed VALUE(  '009', '0062', '2017-5-21', '8:18:52');
INSERT INTO gardenbed VALUE(  '009', '0063', '2017-5-21', '8:19:07');
INSERT INTO gardenbed VALUE(  '009', '0064', '2017-5-21', '8:20:34');
INSERT INTO gardenbed VALUE(  '009', '0065', '2017-5-21', '8:21:48');
INSERT INTO gardenbed VALUE(  '009', '0066', '2017-5-21', '8:22:12');
INSERT INTO gardenbed VALUE(  '009', '0067', '2017-5-21', '8:23:47');
INSERT INTO gardenbed VALUE(  '009', '0068', '2017-5-21', '8:24:51');
INSERT INTO gardenbed VALUE(  '009', '0069', '2017-5-21', '8:25:39');
INSERT INTO gardenbed VALUE(  '009', '0070', '2017-5-21', '8:26:11');
INSERT INTO gardenbed VALUE(  '009', '0071', '2017-5-21', '8:27:58');
INSERT INTO gardenbed VALUE(  '009', '0072', '2017-5-21', '8:28:12');
INSERT INTO gardenbed VALUE(  '010', '0073', '2017-7-16', '3:14:50');
INSERT INTO gardenbed VALUE(  '010', '0074', '2017-7-16', '3:15:23');
INSERT INTO gardenbed VALUE(  '010', '0075', '2017-7-16', '3:17:01');
INSERT INTO gardenbed VALUE(  '010', '0076', '2017-7-16', '3:18:23');
INSERT INTO gardenbed VALUE(  '010', '0077', '2017-7-16', '3:19:38');
INSERT INTO gardenbed VALUE(  '010', '0078', '2017-7-16', '3:21:45');
INSERT INTO gardenbed VALUE(  '010', '0079', '2017-7-16', '3:22:34');
INSERT INTO gardenbed VALUE(  '010', '0080', '2017-7-16', '3:23:01');
INSERT INTO gardenbed VALUE(  '010', '0081', '2017-7-16', '3:24:21');
INSERT INTO gardenbed VALUE(  '010', '0082', '2017-7-16', '3:25:21');
INSERT INTO gardenbed VALUE(  '010', '0083', '2017-7-16', '3:26:49');
INSERT INTO gardenbed VALUE(  '010', '0084', '2017-7-16', '3:27:49');
INSERT INTO gardenbed VALUE(  '010', '0085', '2017-7-16', '3:28:50');
INSERT INTO gardenbed VALUE(  '010', '0086', '2017-7-16', '3:29:50');
INSERT INTO gardenbed VALUE(  '010', '0087', '2017-7-16', '3:30:38');
INSERT INTO gardenbed VALUE(  '010', '0088', '2017-7-16', '3:31:01');
INSERT INTO gardenbed VALUE(  '011', '0089', '2017-7-29', '3:32:23');
INSERT INTO gardenbed VALUE(  '011', '0090', '2017-7-29', '3:33:40');
INSERT INTO gardenbed VALUE(  '011', '0091', '2017-7-29', '3:34:07');
INSERT INTO gardenbed VALUE(  '011', '0092', '2017-7-29', '3:35:23');
INSERT INTO gardenbed VALUE(  '011', '0093', '2017-7-29', '3:37:54');
INSERT INTO gardenbed VALUE(  '011', '0094', '2017-7-29', '3:40:03');


/* Generic select statements to make sure databases are storing information*/
SELECT * FROM userinfo;
SELECT * FROM garden;
SELECT * FROM gardenbed;
SELECT * FROM plant;
SELECT * FROM companionplant;
/*
Drop commands in the case of needing to remake the database*

DROP DATABASE smartgarden;
DROP TABLE userinfo;
DROP TABLE garden;
DROP TABLE plant;
DROP TABLE gardenbed;
DROP TABLE companionplant;
DROP DATABASE smartgarden;
*/
