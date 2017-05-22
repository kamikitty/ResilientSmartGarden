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
</style>
</head>

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

<!--COMPANION PLANTING Page -->
<div class="w3-container w3-content w3-center w3-padding-200" style="max-width:800px; margin-top:120px;">
<div class="transbox">
  <h1 class="w3-center">SMART ROOTS: Resilient Smart Garden</h>
  <p class="w3-center w3-large">What is the project about? <br><br>Smart Roots is a garden system that minimizes water 
	  usage while maintaining the habitat of the plants. The health of the garden will be monitored by a temperature 
	  sensor, humidity sensor, and moisture sensor.<br>
		<br>What are the features?<br>
	<br>A threshold limit is put in place for each sensor to automatically water the plant. The user can monitor the 
	garden through a web application, Android, and iOS. The user has the ability to change the predefined threshold 
	for a more suitable garden habitat.</p>	<br><br>
	
	<h3>Meet The Team</h3>
	
	<div class="w3-row-padding w3-padding-32" style="margin:0 -16px">
        <div class="w3-quarter w3-margin-bottom">
          <img src="images\ruben.png" alt="ruben" style="width:100%" class="w3-hover-opacity">
          <div class="w3-container w3-white">
            <p><b>Ruben Marin</b></p>
            <p class="w3-opacity">"RES"</p>
            <p>Doesn't have a soul.</p>
          </div>
        </div>
        <div class="w3-quarter w3-margin-bottom">
          <img src="images\jason.jpg" alt="jason" style="width:100%" class="w3-hover-opacity">
          <div class="w3-container w3-white">
            <p><b>Jason Plojo</b></p>
            <p class="w3-opacity">"JSON"</p>
            <p>Great at managing us and inserting appropriate GIFs.</p>
          </div>
        </div>
        <div class="w3-quarter w3-margin-bottom">
          <img src="images\mari.png" alt="mari" style="width:100%" class="w3-hover-opacity">
          <div class="w3-container w3-white">
            <p><b>Marinela Snachez</b></p>
            <p class="w3-opacity">"floder"</p>
            <p>MVP of the group.</p>
          </div>
        </div>
		<div class="w3-quarter w3-margin-bottom">
          <img src="images\alice.png" alt="alice" style="width:100%" class="w3-hover-opacity">
          <div class="w3-container w3-white">
            <p><b>Alicia Hernandez</b></p>
            <p class="w3-opacity">"shrug"</p>
            <p>Insert inspirational quote.</p>
          </div>
        </div>
      </div>
	

    </div>
</div>
<script>
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