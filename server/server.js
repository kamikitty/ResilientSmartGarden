/*
Updated code implementing login and register 
currently using levelDB, still needs to 
implement MongoDB for future use
*/


'use strict';

const express = require('express');
const bodyparser = require('body-parser');
//const mongoose = require('mongoose');
//const scrypt = require('scrypt-for-humans');
const levelup = require('levelup');
const app = express();

let port = process.env.PORT || 3001

//mongoose.connect('mongodb://localhost/mongo');
var db = levelup('./mydb')


app.use(bodyparser.urlencoded({extended: false}));
app.use(bodyparser.json());

//mongoose.model('users',{name:String});

//This is used for testing server connection
app.get('/', (req,res)=> {
	res.send('Smart Roots  ¯\\_(ツ)_/¯');
});

//HTTP request for registering a new user
app.post('/register', function(req,res) {
	var uname = req.body.username;
	var passw = req.body.password;
	
	if(!uname || !passw ) {
		res.json({success: false, msg: 'Please pass name and password'})
	} else {
		

		db.put(uname,passw);
		res.json({'username': uname, 'password': passw });
	}
});


//HTTP request for login 
app.post('/login', function(req,res) {
	var username = req.body.username;
	var password = req.body.password;
	
	if(!username || !password) {
		res.json({success: false});
	}
	else { 
		
		db.get(username,function(err,username){
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

//For collection of data over summer

// app.post('/sendData', function(req,res) {
// 	//JSON DATA COLLECTION
// 	var jsonData = req.body.data;
	
// 	if(!jsonData) {
// 		res.json({success: false});
// 	} else {
// 		console.log("in here I go to die")
// 		db.put('data',jsonData, function(err) {
// 			if (err) return console.log('Ooops!',err)

// 		db.get('data',function(err,value){
// 			if(err)return console.log('Ooops!',err)

// 			console.log('data=' + value)
// 		})
// 	})
// 		res.json({success: true});
// 		//Insert MongoDB 
// 	}
// })



//--------------Future Work Below-------------------

//Need to add logic for adding new board/garden
//dont worry about this for now
app.post('/addGarden', function(req,res) {
	const board = req.body.boardID;
	var username = req.body.username;
	if(!board || !username) {
		res.json({success: false, msg: "Invalid input"});
	} else {
		res.json({success: true})
	}
});

app.post('/test', function(req,res) {
	
	
	const data = req.body.board;
	if(!board) {
		res.json({success: false, msg: "Invalid input"});
	} else {
		res.json({success: true})
	}
});


app.post('/addData', function(req,res) {
		
	
	var data = {
		"temperature" : 0.00,
		"humidity" : 0.00,
		"moisture" : 0.00 
	}
	if(!data) {
		res.json({success: false, msg: "no data"});
	} else {
		res.send('data sent');
	}
});


//Need to add logic for removing garden
app.post('/removeGarden', function(req,res) {
	var board = req.body.boardID;
	var username = req.body.username;
	if(!board || !username) {
		res.json({success: false, msg: 'Invalid input'});
	} else {
		res.json({success:true});
	}
});

//need to add logic for adding a new sensor
app.post('/addSensor',function(req,res) {
	const sensor = req.body.sensorID;
});

//need to add logic for removing a sensor
app.post('/removeSensor', function(req,res) {
	const sensor = req.body.sensorID;
	var username = req.body.username;
	if(!sensor || !username) {
		res.json({success: false, msg: 'Invalid input'});
	} else {
		res.json({success: true});
	}
});

//hard coded json data for testing purposes
//used to communicate with android app
app.get('/data',function(req,res) {

	res.json({"temperature":71.60,"humidity":14.00,"moisture":1.42});
});

app.listen(port, () => {
	console.log(`server listening on port ${port}`);
})
