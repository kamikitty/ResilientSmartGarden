//importing modules
var express = require('express');
var mongoose = require('mongoose');
var bodyparser=require('body-parser');
var cors = require('cors');
var path = require('path');

var app = express();

const route = require('./routes/route')

//connect to mongodb
mongoose.connect('mongodb://localhost:27017/RSG');

//on connection
mongoose.connection.on('connected',()=>{
	console.log('connected database mongodb @ 27017');
});

mongoose.connection.on('error',(err)=>{
	if(err)
	{
		console.log('Error in Database conection'+err);
	}
});
//port no

const port=3000;


//addinf middleware
app.use(cors());

//body-parser
app.use(bodyparser.json());

//static files, dirname points towards current directory
app.use(express.static(path.join(__dirname, 'public')))
//route
app.use('/api', route);
//testing environment
app.get('/', (req, res)=>{
	res.send('foobar');
});

app.listen(port, ()=>{
	console.log('server sarted at port: ' + port);
});