const express = require('express');
const router = express.Router();

const user = require('../models/user');

//retrieving data need an api
//replace any instance of rsg with a call to the user in the api
router.get('/rsg', (req,res,next)=>{
	user.find(function(err, user){
		res.json(user);
	});
});

//add data
router.post('/user', (req,res,next)=>{
	let newUser = new user({
		firstName:req.body.firstName,
		lastName:req.body.lastName,
		userName:req.body.userName,
		email:req.body.email,
		password:req.body.password
	});
	newUser.save((err, user)=>{
		if(err)
		{
			res.json({msg:'Failed to add user'});
		}
		else{
			res.json({msg:'User added successfully'});
		}
	});
});

//delete
router.delete('/user/:id', (req,res,next)=>{
	//logic to add userf
	User.remove({_id: req.param.id},function(err,result){
		if(err){
			res.json(err);
		}
		else{
			res.json(result);
		}
	});
});


module.exports = router;