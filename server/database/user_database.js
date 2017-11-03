/// @file user_database.js
/// Module for interacting with the Resilient Smart Garden's user database.
/// The user database will contain the user's account, the user's gardens, and
/// the sensor readings for the user's garden. Most functions are callbacks of
/// each other to maintain synchronous operations.

var MongoClient = require('mongodb').MongoClient
  , assert = require('assert');

// Connection URL
var dbUser = 'mongodb://localhost:27017/rsg_users';

// Collection names
var cUsers = 'users';
var cGardens = 'gardens';

// Export functions. This will be the functions to interact with the user database.
exports.addUser = addUser;
exports.addGarden = addGarden;
exports.addSensorReadings = addSensorReadings;

exports.findUsers = findUsers;
exports.findUserGardens = findUserGardens;
exports.findLatestGardenReading = findLatestGardenReading;
exports.findGardenReadings = findGardenReadings;

exports.validateUser = validateUser;
exports.purgeCollections = purgeCollections;

////////////////////////
// DATABASE FUNCTIONS //
////////////////////////

/// @function connectDB
/// Makes a connection to database. This is intended for testing purposes.
function connectDB() {
  MongoClient.connect(dbUser, function(err, db) {
    assert.equal(null, err);
    console.log("Connected to " + dbUser);

    db.close();
  });
}

/// @function purgeCollections
/// Purges all collections in the database.
function purgeCollections() {
  MongoClient.connect(dbUser, function(err, db) {
    assert.equal(null, err);

    db.dropDatabase();
    db.close();
  });
}

////////////////////
// USER FUNCTIONS //
////////////////////

/// @function addUser
/// Adds a user into the user collection. This is the main function to call
/// when adding a user to the database. It will check to see if there is a duplicate
/// in the database first before adding a user.
/// @param {String} username The user's username to add into the user collection.
/// This will be the unique identifier for the user in the database.
/// @param {String} password The user's password to add into the database.
/// @param {Function} Callback function that contains the result of inserting
/// a user into the database. The first parameter will return true if the user
/// insertion is successful, false if it is not. The second parameter contains
/// a message of the result.
function addUser(username, password, result) {
  // Call checkuser to check for any duplicates on the database. Callback
  // function to insert the user into the users collection if no duplicate
  // is present.
  checkUser(username, password, insertUser, result);
}

/// @function insertUser
/// Inserts a user into the users collection.
/// @param {String} username The user's username to insert into the database. This
/// will be the unique identifier for the user in the database.
/// @param {String} password The user's password to insert into the database.
/// @param {Function} result Callback function that contains the result of inserting
/// a user into the database. The first parameter will return true if the user
/// insertion is successful, false if it is not. The second parameter contains
/// a message of the result.
function insertUser(username, password, result) {
  MongoClient.connect(dbUser, function(err, db){

    // Get the collection of users.
    var collection = db.collection(cUsers);

    // Insert the user into the collection.
    collection.insertOne({"username" : username, "password" : password}, function(err, result) {

      assert.equal(err, null);
      assert.equal(1, result.result.n);
      assert.equal(1, result.ops.length);
      console.log("Inserted 1 user into " + collection.collectionName);
    });

    db.close();
    // Return the result of the insertion through a callback function.
    result(true, "Inserted 1 user into " + collection.collectionName);
  });
}

/// @function checkUser
/// Checks username to see if there is a duplicate on the database. If no duplicates
/// are found, a callback function called "callback" will handle inserting the
/// user into the database. If a duplicate is found, a callback function called
/// "result" will return the result of adding a user.
/// @param {String} username The user's username to insert into the database.
/// @param {String} password The user's password to insert into the database.
/// @param {Function} callback The callback function that will handle inserting the
/// user into the database.
/// @param {Function} result The callback function that will return the result
/// of inserting a user into the database.
function checkUser(username, password, callback, result) {
  MongoClient.connect(dbUser, function(err, db) {
    console.log("Connected to " + dbUser);

    // Get the collection of users.
    var collection = db.collection(cUsers);

    // Find any instance of same username, regardless if password is the same
    // or not.
    collection.find({"username":username}).toArray(function(err, docs) {
      assert.equal(err, null);

      // If there is no instance of the username...
      if (docs.length == 0) {
        //... callback function to insert user into the database. Pass username
        // and password to Callback function.
        callback(username, password, result);
      } else {
        //... otherwise a duplicate was found. Callback result function to return
        // the result of inserting the user into the database.
        db.close();
        result(false, "Duplicate found!");
      }

      db.close();
    });
  });
}

/// @function findUsers
/// Finds all users in the user collection.
/// @param {Function} Callback function containing an array of all the users.
function findUsers(callback) {
  MongoClient.connect(dbUser, function(err, db) {
    assert.equal(null, err);
    console.log("Connected to " + dbUser);

    // Get the collection of users.
    var collection = db.collection(cUsers);

    collection.find({}).toArray(function(err, docs) {
      assert.equal(err, null);
      callback(docs);
    });

    db.close();
  });
}

/// @function validateUser
/// Checks to see if the user exists in the database, then check to see if the
/// credentials are correct. A callback function will contain the result of the
/// user validation.
/// @param {String} username The user's username to validate.
/// @param {String} password The user's password to validate.
/// @param {Function} callback The callback function that will contain the result
/// of the user validation. The first parameter will return true if the user
/// credentials are valid, otherwise false.
function validateUser(username, password, callback) {
  MongoClient.connect(dbUser, function(err,db) {
    assert.equal(null, err);
    console.log("Connected to " + dbUser);

    var collection = db.collection(cUsers);

    collection.find({"username":username, "password":password}).toArray(function(err, docs) {
      assert.equal(err, null);

      // If the user credential is not found...
      if (docs.length == 0) {
        //...pass false to callback function.
        console.log("Credentials not found!");
        db.close();
        callback(false);
      } else {
        //... otherwise, pass true to callback function since the credentials were found.
        console.log("Credentials found!");
        db.close();
        callback(true);
      }
    });
  });
}

/////////////////////
// GARDEN FUNCTION //
/////////////////////

/// @function addGarden
/// Adds a garden in the garden collection, which will be associated with the
/// user. It will first check to see if there is a duplicate in the database
/// before adding a garden.
/// @param {String} username The user's username to add into the garden collection. This
/// will relate the user to the garden.
/// @param {String} mac The MAC address of the garden to insert. This will be the
/// unique identifier for the garden in the database.
/// @param {String} gardenname The name of the garden to add into the garden collection.
/// @param {Function} result Callback function that contains the the result of
/// inserting a garden into the database. The first parameter will return true
/// if the garden insertion is successful, false if is not. The second parameter
/// contains a message of the result.
function addGarden(username, mac, gardenname, result) {
  // Calls checkGarden to check for any duplicate on the database. Callback
  // function to insert a garden is called when no duplicate is found.
  checkUserGarden(username, mac, gardenname, checkGarden, result);
}

/// @function insertGarden
/// Inserts a garden into the gardens collection.
/// @param {String} username The username of the user to associate the garden with.
/// This will be the relation between the user and the garden.
/// @param {String} mac The MAC address of the garden to insert. This will be the
/// unique identifier for the garden in the database.
/// @param {Function} result Callback function that contains the result of inserting
/// a garden into the database. The first parameter will return true if the user
/// insertion is successful, false if it is not. The second parameter contains
/// a message of the result.
function insertGarden(username, mac, gardenname, result) {
  MongoClient.connect(dbUser, function(err, db) {

    // Get the collection of gardens.
    var collection = db.collection(cGardens);

    // Insert the garden into the collection.
    collection.insertOne({"mac":mac, "username":username, "gardenname":gardenname}, function(err, results) {

      assert.equal(err, null);
      assert.equal(1, results.result.n);
      assert.equal(1, results.ops.length);
      console.log("Inserted 1 garden to user " + username);
    });

    db.close();
    // Return the result of the insertion through a callback function.
    result(true, "Inserted 1 garden to user " + username);
  });
}

/// @function checkUserGarden
/// Checks to see if the username exist in the database. If the username exists,
/// a callback function called "callback" will check to see if there is a
/// duplicate garden. If the username does not exist, a callback function called
/// "result" will return the result of adding a garden.
/// @param {String} username The username of the user to associate the garden with.
/// This will be the relation between the user and the garden.
/// @param {String} mac The MAC address of the garden to insert into the database.
/// This will be the unique identifier for the garden in the database.
/// @param {Function} callback The callback function that will check for duplicate
/// garden.
/// @param {Function} result The callback function that will return the results
/// of adding a garden into the database.
function checkUserGarden(username, mac, gardenname, callback, result) {
  MongoClient.connect(dbUser, function(err, db) {
    assert.equal(null, err);
    console.log("Connected to " + dbUser);

    // Get the collection of users.
    var collection = db.collection(cUsers);

    // Find an instance of the username, regardless of password.
    collection.find({"username":username}).toArray(function(err, docs) {
      assert.equal(err, null);

      // If the username exists...
      if (docs.length != 0) {
        //... check to see if there is a duplicate garden
        callback(username, mac, gardenname, insertGarden, result);
      } else {
        //... otherwise send message that username does not exist
        db.close();
        result(false, "Username not found!");
      }

      db.close();
    });
  });
}

/// @function checkGarden
/// Checks garden to see if there is a duplicate in the database. If no duplicates
/// are found, a callback function called "callback" will handle inserting the
/// garden into the database. If a duplicate is found, a callback function called
/// "result" will return the result of adding a garden.
/// @param {String} username The username of the user to assoicate the garden with.
/// This will be the relation between the user and the garden.
/// @param {String} mac The MAC address of the garden to insert into the database.
/// This will be the unique identifier for the garden in the database.
/// @param {Function} callback The callback function that will handle inserting
/// the garden into the database.
/// @param {Function} result The callback function that will return the result
/// of inserting a user into the database.
function checkGarden(username, mac, gardenname, callback, result) {
  MongoClient.connect(dbUser, function(err, db) {
    assert.equal(null, err);
    console.log("Connected to " + dbUser);
    var collection = db.collection(cGardens);

    // Find any instance of same garden
    collection.find({"mac":mac}).toArray(function(err, docs) {
      assert.equal(err, null);

      // If there is no existing garden...
      if (docs.length == 0) {
        //... insert it into the database
        callback(username, mac, gardenname, result);
      } else {
        //... otherwise send notification that duplicate was found.
        db.close();
        result(false, "Duplicate Found!");
      }

      db.close();
    });
  });
}

/// @function findUserGardens
/// Finds all the user's gardens.
/// @param {String} username The username of the user to retrieve all the associated
/// gardens.
/// @param {Function} callback The callback function that will contain an array
/// of all the user's gardens. The array will be null if there is no gardens for
/// the users.
function findUserGardens(username, callback){
  MongoClient.connect(dbUser, function(err, db) {
    assert.equal(null, err);
    console.log("Connected to " + dbUser);

    // Get the collection of gardens.
    var collection = db.collection(cGardens);

    // Find the user in the database.
    collection.find({"username":username}).toArray(function(err, docs) {
      assert.equal(err, null);

      //... callback function to return the user's garden.
      callback(docs);
    });

    db.close();
  });
}

//////////////////////
// SENSOR FUNCTIONS //
//////////////////////

/// @function addSensorReadings
/// Adds a sensor reading to a garden. This is the main function to insert a
/// sensor reading to a garden. It will add the sensor readings to the assoicated
/// garden.
/// @param {String} mac The MAC address of garden to associate the sensor readings
/// to. This will be the unique identifier for the sensor readings. This will also
/// associate the sensor readings to the garden.
/// @param {Number} temperature The temperature to add into the database.
/// @param {Number} humidity The humidity to add into the database.
/// @param {Number} moisture The moisture to add into the database.
/// @param {Function} result The callback function that will return the result
/// of inserting a sensor reading into the database.
function addSensorReadings(mac, temperature, humidity, moisture, result) {
  // Call checkGardenMac to see if the garden exists in the database. callback
  // function is called when the garden exists in the database.
  checkGardenMac(mac, temperature, humidity, moisture, insertSensorReadings, result);
}

/// @function checkGardenMac
/// Checks the database to see if garden exists. If the garden exist, a callback function
/// called "callback" will handle inserting the sensor readings into the database. If the
/// garden does not exist, a callback function called "result" will return the result
/// adding a sensor readings.
/// @param {String} mac The MAC address of the garden to associate the sensor readings
/// to. This will be the unique identifier for the sensor readings. This will also
/// assocate the sensor readings to the garden.
/// @param {Number} temperature The temperature to add into the database.
/// @param {Number} humidity The humidity to add into the database.
/// @param {Number} moisture The mositure to add into the database.
/// @param {Function} callback The callback function that will handle inserting
/// the sensor readings into the database.
/// @param {Function} result The callback function that will return the result of
/// inserting a sensor readings into the database.
function checkGardenMac(mac, temperature, humidity, moisture, callback, result) {
  MongoClient.connect(dbUser, function(err, db) {

    assert.equal(null, err);
    console.log("Connected to " + dbUser);

    // Get the collections of gardens.
    var collection = db.collection(cGardens);

    collection.find({"mac":mac}).toArray(function(err, docs) {
      assert.equal(err, null);

      // If there is an instance of the garden in the database...
      if (docs.length != 0){
        //... insert the sensor readings into the garden's collection
        callback(mac, temperature, humidity, moisture, result);
      } else {
        //... otherwise send a message that the garden does not exists.
        db.close();
        result(false, "Garden not found!");
      }
    });

    db.close();
  });
}

/// @function insertSensorReadings
/// Inserts a sensor reading into the database.
/// @param {String} The MAC address of the garden to associate the sensor readings
/// to. A new collection will be created using the MAC address of the garden,
/// therefore, this will be the unique identifier for the sensor readings. This
/// will also associate the sensor readings to the garden.
/// @param {Number} temperature The temperature to add into the database.
/// @param {Number} humidity The humidity to add into the database.
/// @param {Number} moisture The moisture to add into the database.
/// @param {Function} result The callback function that will return the result
/// of inserting a sensor reading into the database.
function insertSensorReadings(mac, temperature, humidity, moisture, result) {
  MongoClient.connect(dbUser, function(err, db) {
    assert.equal(null, err);
    console.log("Connected to " + dbUser);

    // Get the collections of sensor readings associated with a garden's MAC
    // address.
    var collection = db.collection(mac);

    collection.insertOne({"temperature":temperature,"humidity":humidity,"moisture":moisture}, function(err, results) {
      assert.equal(err, null);
      assert.equal(1, results.result.n);
      assert.equal(1, results.ops.length);
      result(true, "Inserted 1 sensor readings to garden " + mac);
    });

    db.close();
  });
}

/// @function findLatestGardenReading
/// Finds the latest sensor readings from a garden. This will return the latest
/// sensor reading in a callback function. The result will be null if the latest
/// sensor reading does not exist.
/// @param {String} mac The MAC address of the garden to get the latest sensor
/// readings from.
/// @param {Function} callback The callback function that will contain the latest
/// sensor readings of a garden.
function findLatestGardenReading(mac, callback) {
  MongoClient.connect(dbUser, function(err, db) {
    assert.equal(null, err);
    console.log("Connected to " + dbUser);

    // Get the collection of sensor readings associated with a garden's MAC
    // address
    var collection = db.collection(mac);

    collection.findOne({}, function(err, docs) {
      assert.equal(err, null);

      callback(docs);
    });

    db.close();
  })

}

/// @function findGardenReadings
/// Finds all the sensor readings from a garden. This will return an array of all
/// the sensor readings.
/// @param {String} mac The MAC address of the garden to retreive the sensor readings
/// from.
/// @param {Function} callback The callback function that will contain an array of
/// all the sensor readings of a garden.
function findGardenReadings(mac, callback) {
  MongoClient.connect(dbUser, function(err, db) {
    assert.equal(null, err);
    console.log("Connected to " + dbUser);

    // Get the collection of sensor readings associated with a garden's MAC
    // address
    var collection = db.collection(mac);

    collection.find({}).toArray(function(err, docs) {
      assert.equal(err, null);

      callback(docs);
    });

    db.close();
  })

}
