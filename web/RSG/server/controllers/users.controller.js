/* handles all routes/endpoints for the api that relate to users, this includes authentication, registration and standard CRUD operations.
each controller calls the user service to perform the action required
this enables controllwe to staly 'lean' and completely separated from the database/persistence code

Top of the file contains all the route definitions so it's easy to see all routes
at a glance the rest of the file contains the route implementations*/

var config = require('config.json');
var express = require('express');
var router = express.Router();
var userService = require('../services/user.service');

//routes

router.post('/authenticate', authenticate);
router.post('/register', register);
router.get('/',getAll);
router.get('current',getCurrent);
router.put('/:_id', update);
router.delete('/:_id', _delete);

module.exports = router;

function authenticate(req, res){
	userService.authenticate(req.body.username, req.body.password).then(function(user){
		if (user){
			//authentication successful
			res.send(user);
		}

		else{
			//authentication failed
			res.status(400).send('Username or password is inccorrect');
		}
	})
	.catch(function (err){
		res.status(400).send(err);
	});
}

function register(req, res){
	userService.create(req.body)
	.then(function() {
		res.sendStatus(200);
	})
	.catch(function (err){
		res.status(400).send(err);
	});
}

function getAll(req,res){
	userService.getAll()
	.then(function (users){
		res.send(users);
	})
	.catch(function (err){
		res.status(400).send(err);
	});
}

function getCurrent(req, res){
	userService.getById(req.user.sub)
	.then(function (user){
		if(user){
			res.send(user);
		}
		else{
			res.sendStatus(404);
		}

	})
	.catch(function (err){
		res.status(400).send(err);
	});
}

function update(req,res){
	userService.update(req.params._id, req.body)
	.then(function (){
		res.sendStatus(200);
	})
	.catch(function (err){
		res.status(400).send(err);
	});
}

function _delete(req,res){
	userService.delete(req.params._id)
	.then(function (){
		res.sendStatus(200);
	})
	.catch(function (err){
		res.status(400).send(err);
	});
}
