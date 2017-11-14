/// @file database_examples.js
/// Example code that interacts with the database. The functions are asynchronous,
/// so it is highly recommended to run each function individually to get the desired
/// results. Use callbacks if you intend to run the functions in sequence.

var db = require('./rsg_database');

///////////////////
// USER DATABASE //
///////////////////

// Adds a user into the user database. This will first check to make sure
// there is no duplicate user in the database. Success will be false if a duplicate
// user is found in the database. Success will be true if no duplicate is found
// and the user has been successfully inserted into the database.
db.user.addUser("User", "Password", function(success, message) {
  console.log("Success: " + success);
  console.log("Message: " + message);
});

// Adds a garden into the database. The garden will be associated with a user using
// the garden's MAC address. A name will be given to the garden so the user can
// easily identify it. Success will be false if a duplicate garden is found or
// if the user does not exist. Success will be true if no duplicate garden is found,
// the user exists, and the garden has been successfully inserted into the database.
db.user.addGarden("User", "MAC13", "Garden", function(success, message) {
  console.log("Success: " + success);
  console.log("Message: " + message);
});

// Adds a sensor reading associated with a garden into the database. The sensor readings
// to insert is temperature, humidity, and moisture. Success will be true if inserting
// the sensor readings into the database is successful, false otherwiese.
db.user.addSensorReadings("MAC13", 23, 45, 67, function(success, message){
  console.log("Success: " + success);
  console.log("Message: " + message);
});

// Validates a user's username and password. Success will be true if the user's
// credentials were found and the credentials are valid, false otherwise.
db.user.validateUser("User", "Password", function(success) {
  console.log("Success: " + success);
});

// Finds all the users in the database.
db.user.findUsers(function(docs) {
  for (i = 0; i < docs.length; i++) {
    console.log(docs[i].username);
  }
});

// Finds all the user's gardens in the database.
db.user.findUserGardens("User", function(docs){
  for (i = 0; i < docs.length; i++) {
    console.log(docs[i]);
  }
});

// Finds the latest sensor readings of a garden. The MAC address of the garden
// is used as a reference to find the latest sensor readings.
db.user.findLatestGardenReading("MAC13", function(docs) {
  console.log(docs);
});

// Finds all the sensor readings of a garden. The MAC address of the garden
// is used as a reference to find all the sensor readings.
db.user.findGardenReadings("MAC", function(docs) {
  for (i = 0; i < docs.length; i++) {
    console.log(docs[i]);
  }
});

// Purges all collections in the user database. For testing purposes.
db.user.purgeCollections();

/////////////////////////////////
// COMPANION PLANTING DATABASE //
/////////////////////////////////

// Finds a plant in the database
db.companion.findPlant('Onion', function(docs) {
  console.log(docs);
});

// Finds all the plants in the database
db.companion.findAllPlants(function(docs) {
  console.log(docs);
});

// Finds alll the names of the plants in the database
db.companion.findAllPlantNames(function(docs) {
  console.log(docs);
});

// Finds a plant's companion plants
db.companion.findCompanion('Onion', function(docs) {
  console.log(docs);
});

// Finds a plant's enemy plants
db.companion.findEnemy('Onion', function(docs) {
  console.log(docs);
});
