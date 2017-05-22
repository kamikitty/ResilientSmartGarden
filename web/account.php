<html>
<head>
<title>SMART ROOTS</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Lato">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="shortcut icon" href="images\fav_icon.png">
<style>

body {
	font-family: "Lato", sans-serif;
	background-image: url('http://bgfons.com/upload/leaves_texture1258.jpg');
    background-repeat: no-repeat;
    background-position: right top;
    background-attachment: fixed;

	}
	
div.transbox {
    margin: 30px;
    background-color: #ffffff;

    opacity: 0.7;
    filter: alpha(opacity=70); /* For IE8 and earlier */
}

div.transbox p {
    margin: 5%;
    font-weight: bold;
    color: #000000;
}
		
.mySlides {display: none}

.w3-bar{
	float:right;
	}
	
/* Full-width input fields */
input[type=text], input[type=password] {
    width: 100%;
    padding: 12px 20px;
    margin: 8px 0;
    display: inline-block;
    border: 1px solid #ccc;
    box-sizing: border-box;
}

/* Set a style for all buttons */
button {
    background-color: #4CAF50;
    color: white;
    padding: 14px 20px;
    border: none;
    cursor: pointer;
    width: 100%;
}

/* Extra styles for the cancel button */
.cancelbtn {
    padding: 14px 20px;
    background-color: #f44336;
}

/* Float cancel and signup buttons and add an equal width */
.cancelbtn,.signupbtn {float:left;width:50%}

/* Add padding to container elements */
.container {
    padding: 16px;
}

/* The Modal (background) */
.modal {
    display: none; /* Hidden by default */
    position: fixed; /* Stay in place */
    z-index: 1; /* Sit on top */
    left: 0;
    top: 0;
    width: 100%; /* Full width */
    height: 100%; /* Full height */
    overflow: auto; /* Enable scroll if needed */
    background-color: rgb(0,0,0); /* Fallback color */
    background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
    padding-top: 60px;
}

/* Modal Content/Box */
.modal-content {
    background-color: #fefefe;
    margin: 5% auto 15% auto; /* 5% from the top, 15% from the bottom and centered */
    border: 1px solid #888;
    width: 80%; /* Could be more or less, depending on screen size */
}

/* The Close Button (x) */
.close {
    position: absolute;
    right: 35px;
    top: 15px;
    color: #000;
    font-size: 40px;
    font-weight: bold;
}

.close:hover,
.close:focus {
    color: red;
    cursor: pointer;
}

/* Clear floats */
.clearfix::after {
    content: "";
    clear: both;
    display: table;
}

/* Change styles for cancel button and signup button on extra small screens */
@media screen and (max-width: 300px) {
    .cancelbtn, .signupbtn {
       width: 100%;
    }
}
</style>
</head>

<!--Account button-->
<body>
<!--Navbar-->
<div class="w3-top">
  <div class="w3-bar w3-green w3-card-2 w3-padding-32 w3-center w3-large">
  <img style="float:left; width:5%; padding-left:15px;" src="images\RootsMonkey.png" alt="logo" class="w3-hide-small"> 
    <a class="w3-bar-item w3-button w3-padding-large w3-hide-medium w3-hide-large w3-right" href="javascript:void(0)" onclick="myFunction()" title="Toggle Navigation Menu"><i class="fa fa-bars"></i></a>
    <a href="index.php" class="w3-bar-item w3-button w3-padding-large w3-hide-small">HOME</a>
    <a href="gettingStarted.php" class="w3-bar-item w3-button w3-padding-large w3-hide-small">GETTING STARTED</a>
    <a href="appFeatures.php" class="w3-bar-item w3-button w3-padding-large w3-hide-small">APP FEATURES</a>
    <a href="compPlanting.php" class="w3-bar-item w3-button w3-padding-large w3-hide-small">COMPANION PLANTING</a>
	<a href="account.php" class="w3-bar-item w3-button w3-padding-large w3-hide-small">ACCOUNT</a>
    <a href="login.php" class="w3-bar-item w3-button w3-padding-large w3-hide-small">LOGIN</a>
	<a href="about.php" class="w3-bar-item w3-button w3-padding-large w3-hide-small">ABOUT</a>
	<a href="contact.php" class="w3-bar-item w3-button w3-padding-large w3-hide-small">CONTACT</a>
  </div>
</div>

<!-- Navbar on small screens -->
<div id="navDemo" class="w3-bar-block w3-green w3-hide w3-hide-large w3-hide-medium w3-top" style="margin-top:46px">
<img style="float:center; width:20%; padding-left:30px; padding-bottom:10px;" src="images\RootsMonkey.png" alt="logo">
  <a href="index.php" class="w3-bar-item w3-button w3-padding-large">HOME</a>
  <a href="gettingStarted.php" class="w3-bar-item w3-button w3-padding-large">GETTING STARTED</a>
  <a href="appFeatures.php" class="w3-bar-item w3-button w3-padding-large">APP FEATURES</a>
  <a href="compPlanting.php" class="w3-bar-item w3-button w3-padding-large">COMPANION PLANTING</a>
  <a href="account.php" class="w3-bar-item w3-button w3-padding-large">ACCOUNT</a>
  <a href="login.php" class="w3-bar-item w3-button w3-padding-large">LOGIN</a>
  <a href="about.php" class="w3-bar-item w3-button w3-padding-large">ABOUT</a>
  <a href="contact.php" class="w3-bar-item w3-button w3-padding-large">CONTACT</a>
</div>
<div class="w3-container w3-content w3-center w3-padding-200" style="max-width:800px; margin-top:120px;">
<div class="transbox">
<h2 class="w3-center" id="account">ACCOUNT SIGNUP</h2>

<p>Fill out information below to create a new account. <br><br> If trying to login please click here. (add link)</p>

<button onclick="document.getElementById('id01').style.display='block'" style="width:auto;">Sign Up</button><br><br><br>

<div id="id01" class="modal">
  <span onclick="document.getElementById('id01').style.display='none'" class="close" title="Close Modal">×</span>
  <form class="modal-content animate" action="/action_page.php">
    <div class="container" style="margin-top:30px;">
      <label><b>Email</b></label>
      <input type="text" placeholder="Enter Email" name="email" required>

      <label><b>Password</b></label>
      <input type="password" placeholder="Enter Password" name="psw" required>

      <label><b>Repeat Password</b></label>
      <input type="password" placeholder="Repeat Password" name="psw-repeat" required>
      <input type="checkbox" checked="checked"> Remember me
      <p>By creating an account you agree to our <a href="#">Terms & Privacy</a>.</p>

      <div class="clearfix">
        <button type="button" onclick="document.getElementById('id01').style.display='none'" class="cancelbtn">Cancel</button>
        <button type="submit" class="signupbtn">Sign Up</button>
      </div>
    </div>
  </form><br><br><br>
</div>
</div>
</div>

<script>
// Get the modal
var modal = document.getElementById('id01');

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}

// Used to toggle the menu on small screens when clicking on the menu button
function myFunction() {
    var x = document.getElementById("navDemo");
    if (x.className.indexOf("w3-show") == -1) {
        x.className += " w3-show";
    } else { 
        x.className = x.className.replace(" w3-show", "");
    }
}
</script>

</body>
</html>