/// @file companion_database.js
/// Module for interacting with the Resilient Smart Garden's companion planting
/// database. The companion planting database will contain plants and their
/// characteristics, plants they are compatiable with, and plants they are not
/// compatiable with.

var MongoClient = require ('mongodb').MongoClient
  , assert = require('assert');

// Connection URL
var dbCompanion = 'mongodb://localhost:27017/rsg_companion';

// Collection names
var cCompanions = 'companion';

// Export functions. This will be the functions to interact with the user database.
exports.findPlant = findPlant;
exports.findAllPlants = findAllPlants;
exports.findCompanion = findCompanion;
exports.findEnemy = findEnemy;

////////////////////////
// DATABASE FUNCTIONS //
////////////////////////

/// @function connectDB
/// Makes a connection to database. This is intended for testing purposes.
function connectDB() {
  MongoClient.connect(dbCompanion, function(err, db) {
    assert.equal(null, err);
    console.log("Connected to " + dbCompanion);

    db.close();
  });
}

/// @function purgeCollections
/// Purges all collections in the database.
function purgeCollections() {
  MongoClient.connect(dbCompanion, function(err, db) {
    assert.equal(null, err);

    db.dropDatabase();
    db.close();
  });
}

/////////////////////////
// COMPANION FUNCTIONS //
/////////////////////////

/// @function findPlant
/// Finds the specified plant and returns all the plant information.
/// @param {String} plant The plant to find in the database.
/// @param {Function} callback The callback function that contains all the plant
/// information
function findPlant(plant, callback) {
  MongoClient.connect(dbCompanion, function(err, db){
    assert.equal(null, err);
    console.log("Connected to " + dbCompanion);

    var collection = db.collection(cCompanions);

    collection.findOne({"_id":plant}, function(err, docs) {
      assert.equal(err, null);

      callback(docs);
    });

    db.close();
  })
}

/// @function findAllPlants
/// Finds all the plants in the database and returns it in an array.
/// @param {Function} callback The callback function that contains all the plants
/// in an array
function findAllPlants(callback) {
  MongoClient.connect(dbCompanion, function(err, db){
    assert.equal(null, err);
    console.log("Connected to " + dbCompanion);

    var collection = db.collection(cCompanions);

    collection.find({}).toArray(function(err, docs) {
      assert.equal(err,null);
      callback(docs);
    });

    db.close();
  });
}

/// @function findCompanion
/// Finds the companion plants of the specified plant.
/// @param {String} plant The plant to find companions for.
/// @param {Function} callback The callback function that contains an array
/// of companions for the specified plant.
function findCompanion(plant, callback) {
  MongoClient.connect(dbCompanion, function(err, db) {
    assert.equal(null, err);
    console.log("Connected to " + dbCompanion);

    var collection = db.collection(cCompanions);

    collection.findOne({"_id":plant}, function(err, docs) {
      assert.equal(err, null);

      callback(docs['companion']);
    });

    db.close();
  })
}

/// @function findEnemy
/// Finds the enemy plants of the specified plant.
/// @param {String} plant The plant to find enemies for.
/// @param {Function} callback The callback function that contains an array
/// of enemies for the specified plant.
function findEnemy(plant, callback) {
  MongoClient.connect(dbCompanion, function(err, db) {
    assert.equal(null, err);
    console.log("Connected to " + dbCompanion);

    var collection = db.collection(cCompanions);

    collection.findOne({"_id":plant}, function(err, docs) {
      assert.equal(err, null);

      callback(docs['enemy']);
    });

    db.close();
  })
}
