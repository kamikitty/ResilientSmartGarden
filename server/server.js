'use strict';

const express = require('express');
const bodyparser = require('body-parser');
//const scrypt = require('scrypt-for-humans');
//const levelup = require('levelup');
const app = express();
const db = require('./database');

let port = process.env.PORT || 3001

app.use(bodyparser.urlencoded({extended: false}));
app.use(bodyparser.json());


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
		db.addUser(uname,passw,function(success,message){
			res.json({'username': uname, 'password':passw});
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
		
		db.findUser(username,function(success,message){
		if(err) {
			if(err.notFound){
				res.json({success: false});
			}
		}
		else {
			res.json({success: true});
		}});
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
		db.addGarden(uname,mac,gardeName, function(success,message) {
			res.json({success: true});
		})
	}
});

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
		db.addSensorReadings(mac,temperature,humidity,moisture,function(success,message){
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
		db.findLatestGardenReading(mac,function(success,message){
			//need json response here
		})
	}
});

app.listen(port, () => {
	console.log(`server listening on port ${port}`);
})
