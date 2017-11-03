/// @file rsg_database.js
/// Main module that references all database modules. This is the module
/// to import into the server.

exports.user  = require('./user_database');
exports.companion = require('./companion_database');
