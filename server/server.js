'use strict';

const express = require('express');
const bodyparser = require('body-parser');
//const scrypt = require('scrypt-for-humans');
//const levelup = require('levelup');
const app = express();
const db = require('./database/rsg_database');
const cors = require('cors');

let port = process.env.PORT || 3001

app.use(bodyparser.urlencoded({extended: false}));
app.use(bodyparser.json());
app.use(cors());


//This is used for testing server connection
app.get('/', (req,res)=> {
	res.send('Smart Roots  ¯\\_(ツ)_/¯');
});

//HTTP request for registering a new user
//Still need to add encryption for storing passwords
app.post('/register', function(req,res) {
	var uname = req.body.username;
	var passw = req.body.password;

	if(!uname || !passw ) {
		res.json({success: false, msg: 'Please pass name and password'})
	} else {
		db.user.addUser(uname,passw,function(success,message){
			res.json({'success': 'true'});
	})
}});


//HTTP request for login
app.post('/login', function(req,res) {
	var username = req.body.username;
	var password = req.body.password;

	if(!username || !password) {
		res.json({success: false});
	}
	else {

		db.user.validateUser(username, password, function(success,message){
			res.json({"success": success});
		});
		}
	});

//Request to add a garden
app.post('/addGarden', function(req,res) {
	var uname = req.body.username;
	var mac = req.body.mac;
	var gardenName = req.body.gardenName;
	var result = req.body.result;
	if(!uname || !mac) {
		res.json({success: false, msg: "Invalid input"});
	} else {
		db.user.addGarden(uname,mac,gardenName, function(success,message) {
			res.json({success: true});
		})
	}
});

app.post('/getGardens', function(req, res) {
	var uname = req.body.username;

	db.user.findUserGardens(uname, function(docs) {
		res.json({'gardens': docs });
	})
})

//Need to add logic for removing garden

//Request to add sensor readings
app.post('/addSensor',function(req,res) {
	var mac = req.body.mac;
	var temperature = req.body.temperature;
	var humidity = req.body.humidity;
	var moisture = req.body.moisture;

	if(!mac){
		res.json({success: false, msg: 'Invalid input'});
	}
	else {
		db.user.addSensorReadings(mac,temperature,humidity,moisture,function(success,message){
			res.json({success:'true'});
		})
	}
});

//need to add logic for removing a sensor


app.post('/getSensor',function(req,res){
	var mac = req.body.mac;

	if(!mac) {
		res.json({success: 'false', msg: 'Invalid'});
	}
	else{
		db.user.findLatestGardenReading(mac,function(success,message){
			//need json response here
		})
	}
});

app.get('/get_plant_name', (req, res)=> {
  db.companion.findAllPlantNames((docs) => {
    res.json({'data' : docs});
  });
});

app.post('/get_companion', (req, res) => {
  db.companion.findCompanion(req.body.plant, (docs) => {
    res.json({'data' : docs});
  });
});

app.post('/get_enemy', (req, res) => {
  db.companion.findEnemy(req.body.plant, (docs) => {
    res.json({'data': docs});
  });
});

app.listen(port, () => {
	console.log(`server listening on port ${port}`);
})
