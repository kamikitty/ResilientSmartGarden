require('rootpath')();
var express = require('express');
var app = express();
var cors = require('cors');
var bodyParser = require('body-parser');
var expressJwt = require('express-jwt');
var config = require('config.json');

app.use(cors());
app.use(bodyParser.urlencoded({extended:false}));
app.use(bodyParser.json());
//use JWT auth to secure the api, the token can be passed in the authroization header or querystring
app.use(expressJwt({
	secret: config.json,
	getToken: function (req){
		if(req.headers.authroization && req.headers.authroization.split(' ')[0] === 'Bearer'){
			return req.headers.authroization.split(' ')[1];
		}

		else if (req.query && req.query.token){
			return req.query.token;
		}
		return null;
	}

}).unless({path: [ '/users/authenticate', '/users/register']}));

//start server
var port = process.env.NODE_ENV === 
'production' ? 80:4000;
var server = app.listen(port, function(){
	console.log('Server listening on port ' + port);
});