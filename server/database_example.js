var database = require('./database');

database.addUser("User", "Password", function(success, message) {
  console.log("Success: " + success);
  console.log("Message: " + message);
});

database.addGarden("User", "MAC", function(success, message) {
  console.log("Success: " + success);
  console.log("Message: " + message);
});

database.addSensorReadings("MAC", 23, 45, 67, function(success, message){
  console.log("Success: " + success);
  console.log("Message: " + message);
});

database.findUsers(function(docs) {
  for (i = 0; i < docs.length; i++) {
    console.log(docs[i].username);
  }
});

database.findUserGardens("User", function(docs){
  for (i = 0; i < docs.length; i++) {
    console.log(docs[i]);
  }
});

database.findLatestGardenReading("MAC", function(docs) {
  console.log(docs);
});

database.findGardenReadings("MAC", function(docs) {
  for (i = 0; i < docs.length; i++) {
    console.log(docs[i]);
  }
});
