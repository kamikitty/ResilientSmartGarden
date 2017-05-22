<html>
<head>
<title>SMART ROOTS</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Lato">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="shortcut icon" href="\images\fav_icon.png">
<style>

body {
	font-family: "Lato", sans-serif;
	background-image: url('http://bgfons.com/upload/leaves_texture1258.jpg');
    background-repeat: no-repeat;
    background-position: right top;
    background-attachment: fixed;

	}
	
.pic{
	 opacity: 1.0;
    filter: alpha(opacity=100);
}
div.transbox {
    margin: 30px;
    background-color: #ffffff;

    opacity: 0.8;
    filter: alpha(opacity=80); /* For IE8 and earlier */
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
</div>

<!--android app info-->
<div class="w3-container w3-content w3-center w3-padding-200" style="max-width:800px; margin-top:120px;">
<div class="transbox">
  <h1 class="w3-center">APP FEATURES: ANDROID</h1>

<p>The SmartRoot Android application lets you check with your garden remotely. Your Android
  device must be running Android 4.4 KitKat or above.<br><br>
	
  Opening the SmartRoots App will bring you to the welcome screen where you can
  login to your existing account, or create a new one. <br><br>

<img id="android3" src= "images\android3.png" width=50% height=60%> <br><br>


  Logging in to SmartRoots will give you access to your gardens. Tapping “Login” will prompt you
with a dialog to enter your username and password. Tap “Login” after entering your credentials to access your gardens,
 or tap cancel to stop the login process.<br><br>
 
<img id="android4" src= "images\android4.png" width=50% height=60%><br><br>

 If you do not have a SmartRoots account, you can create one by registering. Tapping “Register”
  will prompt you will a dialog to enter your desired username for SmartRoots. You will be asked
  to provide an email address and create a desired password. Tap “Create” after entering your desired
  credentials to create your account with SmartRoots, or tap cancel to stop the account creation.<br><br>

<img id="android5" src= "images\android5.png" width=50% height=60%><br><br>

 After logging in to SmartRoots you will be greeted to a list of your gardens represented as a card.
  Each garden card contains your garden name, temperature, humidity, and moisture. You can update the
  sensor readings by tapping on “Update” on the respective garden. You also have the ability to manually
  water your garden by tapping on “Water Now”. You can logout of your account by tapping the exit button
  located on the upper right.<br><br>

<img id="android6" src= "images\android6.png" width=50% height=60%><br><br>

 You can view the history of the sensor readings. Tapping “History” from the garden card will display a
  dialog with history chart of the sensor readings. Tap “Done” to go back to the list of gardens.
  (This feature will be in a future update)<br><br>

<img id="android7" src= "images\android7.png" width=50% height=60%><br><br>


  You can set the moisture limit to begin the automatic watering. Tapping “Setup” from the garden card will
  display a dialog where you can set the moisture limit. Tap “Done” to apply the new moisture limit and
  return back to the list of gardens<br><br>

<img id="android8" src= "images\android8.png" width=50% height=60%><br><br>


  Each garden card has an overflow menu on the upper right that reveals push notification settings and the ability
  to shut down the garden. Tapping “Shut Down” will turn off the garden.<br><br>

  
<img id="android9" src= "images\android9.png" width=50% height=60%><br><br>


  You can be notified when a sensor reaches a minimum or maximum limit through your Android Device. Tapping
  “Push Notification” from the overflow menu in the garden card will open a dialog showing all of the push
  notification settings. There is a main switch for Push Notification which will either turn all of the warning
  notifications off or on. Each sensor have their own minimum and maximum limit. Enter the desired value for each
  sensor and tap “Done” to apply the changes.<br><br>

<img id="android9" src= "images\android9.png" width=50% height=60%><br><br>


<h3 class="w3-center"><b><u>Hardware - Setting up the Sensors</u></b></h3>
<p>The Arduino UNO will be the main controller of the garden system. The Arduino UNO will be powered by USB.</p>
<img id="uno1" src= "images\Uno1.png" width=50% height=60%>>

<p>The DHT11 is a digital humidity and temperature sensor. The pins from left to right is VCC, DATA, Not Used, and GND.
</p>
<img id="uno2" src= "images\Uno2.png" width=50% height=60%>

<p>The Phantom YoYo High Sensitivity Water Sensor will detect the moisture levels. The pins from left to right is SIGNAL, +, and -.</p>
<img id="uno3" src= "images\Uno3.png" width=50% height=60%>

<p>Place the DHT11 into a breadboard.</p>
<img id="uno4" src= "images\Uno4.png" width=50% height=60%>

<p>Get a jumper cable and plug it into the GND of the Arduino UNO.
  Get the other end of the jumper cable and place it on the far edge of the beardboard on the - row.</p>
<img id="uno5" src= "images\Uno5.png" width=50% height=60%>
<img id="uno6" src= "images\Uno6.png" width=50% height=60%>

<p>Get a jumper cable and plug it into the 5V of the Arduino.
  Plug the other end of the jumper cable into the far edge of the beardboard in the + row.</p>
<img id="uno7" src= "images\Uno7.png" width=50% height=60%>
<img id="uno8" src= "images\Uno8.png" width=50% height=60%>

<p>Get a jumper cable and place it in the - row on the beardboard lined up with the GND on the DHT11.
  Then place the other end of the jumper cable in a slot matching the DHT11 GND. Get another jumper cable
  and place it in the + row on the beardboard lined up with the GND jumper cable. Now place the other end
  lined up with the DHT11 VCC.</p>
<img id="uno9" src= "images\Uno9.png" width=50% height=60%>
<img id="uno10" src= "images\Uno10.png" width=50% height=60%>


<p>Get a jumper cable and place it in Pin 2 of the Arduino UNO. Place the other end of the jumper cable matching DATA in the DHT11.</p>
<img id="uno11" src= "images\Uno11.png" width=50% height=60%>
<img id="uno12" src= "images\Uno12.png" width=50% height=60%>


<p>Get female jumper cables and slide them in the pins on the YoYo Phantom High Sensitivity Water Sensor.</p>
<img id="uno13" src= "images\Uno13.png" width=50% height=60%>

<p>Get a jumper cable and place it in pin A0 on the Arduino UNO.</p>
<img id="uno14" src= "images\Uno14.png" width=50% height=60%>

<p>Get a jumper cable and place it in the + row on the breadboard and attach the other end to the + of the YoYo
  Phantom High Sensitive Sensor. Do the same thing but for -. Get the jumper cable from A0 and slide the other end
  to the Signal.</p>
<img id="uno15" src= "Uno15.png" width=50% height=60%>
<br><br><br><br>


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

</body><br><br><br><br>
</html>