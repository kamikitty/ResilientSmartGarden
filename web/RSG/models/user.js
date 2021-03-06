const mongoose = require('mongoose');
const bcrypt=require('bcryptjs');

const UserScehma = new mongoose.Scehma({
	firstName:{type:String, required:true, trin:true},
		lastName: {type:String, required:true, trim:true},
		email: {type:String, required:true, unique:true, trim:true},
		password: {type:String, required:true}

});

//authentication
UserScehma.statics.authenticate=function(email, password,callback){
	User.findOne({email:email})
	.exec(function(error,user) {
		if(error){
			return callback(error);
		}
		else if(!user){
			var err=new Error('User not found');
			err.status=401;
			return callback(err);
		}
		bcrypt.compare(password, user.password, function(error,result){
			if(result==true){
				return callback(null, user);
			}
			else {
				return callback();
			}
		});
	});
}

//hash password before saving to database
UserScehms.pre('save', function(next){
	var user =this;
	bcrypt.hash(user.password, 10, function(err, hash){
		if(err){
			retunrn(err);
		}
		user.password=hash;
		next();
	});
});

var User = mongoose.model('User', UserSchema);
module.exports=User;




