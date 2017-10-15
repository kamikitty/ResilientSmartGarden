var MongoClient = require('mongodb').MongoClient
  , assert = require('assert');

// Connection URL
var url = 'mongodb://localhost:27017/rsg_test';

// Collection names
var cUsers = 'users'
var cGardens = 'gardens'

// Export functions
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

/// @function Test connection to database
function connectDB() {
  MongoClient.connect(url, function(err, db) {
    assert.equal(null, err);
    console.log("Connected to " + url);

    db.close();
  });
}

/// @function purges all collections in the database
function purgeCollections() {
  MongoClient.connect(url, function(err, db) {
    assert.equal(null, err);

    db.dropDatabase();
    db.close();
  })
}

////////////////////
// USER FUNCTIONS //
////////////////////

/// @function Adds a user into the user collection. This is the main function
/// to call when adding a user to the database
function addUser(username, password, result) {
  // Call checkuser to check for any duplicates on the database. Callback
  // function to insert the user into the users collection if no duplicate
  // is present.
  checkUser(username, password, insertUser, result);
}

/// @function Inserts a user into the users collection
function insertUser(username, password, result) {
  MongoClient.connect(url, function(err, db){

    var collection = db.collection(cUsers);

    collection.insertOne({"username" : username, "password" : password}, function(err, result) {

      assert.equal(err, null);
      assert.equal(1, result.result.n);
      assert.equal(1, result.ops.length);
      console.log("Inserted 1 user into " + collection.collectionName);
    });

    db.close();
    result(true, "Inserted 1 user into " + collection.collectionName);
  });
}

/// @function Checks username to see if there is a duplicate on the database.
/// Callback function to insert user into collection if no duplicate is found.
function checkUser(username, password, callback, result) {
  MongoClient.connect(url, function(err, db) {
    console.log("Connected to " + url);

    var collection = db.collection(cUsers);

    // Find any instance of same username, regardless if password is the same
    // or not.
    collection.find({"username":username}).toArray(function(err, docs) {
      assert.equal(err, null);

      // If there is no instance of the username...
      if (docs.length == 0) {
        // Callback function to insert user into the database. Pass username
        // and password to Callback function.
        callback(username, password, result);
      } else {
        db.close();
        result(false, "Duplicate found!");
        assert.equal(0, docs.length, "Duplicate found!");
      }

      db.close();
    });
  });
}

/// @function Finds all users in the user colleciton.
function findUsers(callback) {
  MongoClient.connect(url, function(err, db) {
    assert.equal(null, err);
    console.log("Connected to " + url);

    var collection = db.collection(cUsers);

    collection.find({}).toArray(function(err, docs) {
      assert.equal(err, null);
      callback(docs);
    });

    db.close();
  });
}

/// @function Validates the username and password. Callback will pass true
/// if credentials are found. False otherwise.
function validateUser(username, password, callback) {
  MongoClient.connect(url, function(err,db) {
    assert.equal(null, err);
    console.log("Connected to " + url);

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
        //...pass true to callback function.
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

/// @function Adds a garden to a user.
function addGarden(username, mac, gardenname, result) {
  // Calls checkGarden to check for any duplicate on the database. Callback
  // function to insert a garden is called when no duplicate is found.
  checkUserGarden(username, mac, gardenname, checkGarden, result);
}

/// @function Inserts a garden into the gardens collection.
function insertGarden(username, mac, gardenname, result) {
  MongoClient.connect(url, function(err, db) {
    var collection = db.collection(cGardens);

    collection.insertOne({"mac":mac, "username":username, "gardenname":gardenname}, function(err, results) {

      assert.equal(err, null);
      assert.equal(1, results.result.n);
      assert.equal(1, results.ops.length);
      console.log("Inserted 1 garden to user " + username);
      result(true, "Inserted 1 garden to user " + username);
    });

    db.close();
  })
}

/// @function Checks to see if the username exist in the database. Check to see
/// if there is a duplicate garden after user check.
function checkUserGarden(username, mac, gardenname, callback, result) {
  MongoClient.connect(url, function(err, db) {
    assert.equal(null, err);
    console.log("Connected to " + url);
    var collection = db.collection(cUsers);

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

/// @function Checks garden to see if there is a duplicate in the database.
/// Callback function  to insert garden if no duplicate is present.
function checkGarden(username, mac, gardenname, callback, result) {
  MongoClient.connect(url, function(err, db) {
    assert.equal(null, err);
    console.log("Connected to " + url);
    var collection = db.collection(cGardens);

    collection.find({"mac":mac}).toArray(function(err, docs) {
      assert.equal(err, null);

      // If there is not existing garden...
      if (docs.length == 0) {
        //...insert it into the database
        callback(username, mac, gardenname, result);
      } else {
        //... otherwise send notification that duplicate was found.
        db.close();
        result(false, "Duplicate Found!");
        assert.equal(0, docs.length, "Duplicate Found!");
      }

      db.close();
    });
  });
}

/// @function Finds all the user's gardens
function findUserGardens(username, callback){
  MongoClient.connect(url, function(err, db) {
    assert.equal(null, err);
    console.log("Connected to " + url);
    var collection = db.collection(cGardens);

    collection.find({"username":username}).toArray(function(err, docs) {
      assert.equal(err, null);

      callback(docs);
    });

    db.close();
  });
}

//////////////////////
// SENSOR FUNCTIONS //
//////////////////////

/// @function Adds a sensor reading to a garden. This is the main function
/// to insert a sensor reading to a garden.
function addSensorReadings(mac, temperature, humidity, moisture, result) {
  // Call checkGardenMac to see if the garden exists in the database. callback
  // function is called when the garden exists in the database.
  checkGardenMac(mac, temperature, humidity, moisture, insertSensorReadings, result);
}

/// @function Checks the database to see if garden exists by looking up mac
/// address.
function checkGardenMac(mac, temperature, humidity, moisture, callback, result) {
  MongoClient.connect(url, function(err, db) {

    assert.equal(null, err);
    console.log("Connected to " + url);
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

/// @function adds a sensor reading to a garden
function insertSensorReadings(mac, temperature, humidity, moisture, result) {
  MongoClient.connect(url, function(err, db) {
    assert.equal(null, err);
    console.log("Connected to " + url);

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

/// @function Finds the latest sensor readings from a garden
function findLatestGardenReading(mac, callback) {
  MongoClient.connect(url, function(err, db) {
    assert.equal(null, err);
    console.log("Connected to " + url);

    var collection = db.collection(mac);

    collection.findOne({}, function(err, docs) {
      assert.equal(err, null);

      callback(docs);
    });

    db.close();
  })

}

/// @function Finds all the sensor readings from a garden
function findGardenReadings(mac, callback) {
  MongoClient.connect(url, function(err, db) {
    assert.equal(null, err);
    console.log("Connected to " + url);

    var collection = db.collection(mac);

    collection.find({}).toArray(function(err, docs) {
      assert.equal(err, null);

      callback(docs);
    });

    db.close();
  })

}
