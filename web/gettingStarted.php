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

<!-- Getting Started Page -->
<div class="w3-container w3-content w3-center w3-padding-200" style="max-width:800px; margin-top:120px;">
<div class="transbox">
  <h1 class="w3-center">GETTING STARTED</h1>
  <p class="w3-center w3-large">Here are the specifics on the software used and the hardware used.</p>
  <div class="w3-row-padding w3-center" style="margin-top:64px">
    <div class="w3-center">
      <i class="fa fa-file w3-margin-bottom w3-jumbo w3-center"></i>
      <h3 class="w3-large">Software Info</h3>
	<b><u>How to install?</u></b>
<p>In addition to the garden system hardware, there are 4 different software development environments: Arduino IDE for the garden, Web Development Environment, Android Studio for Android, and xCode for iOS.
</p>
<p><u>How to Install Arduino IDE</u></p>
<ul>
<li>Download <a href="https://www.arduino.cc/en/Main/Software">Arduino IDE</a> for your respective Operating System</li>
<li>Open Arduino IDE</li>
<li>Select Arduino UNO Board</li>
<ul>
<li>Go to Tools->Board</li>
<li>Select Arduino UNO Board</li>
</ul>
<li></li>
</ul>


<p><u>How to install Web Development Environment</u></p>
<ul>
<li>Install web server for your operating system(LAMP,WAMP,XAMPP)</li>
<li>Download programming languages listed below(if necessary)</li>
<li>Install sublime or editor of your choice</li>
</ul>

<p><u> How to install Android Studio</u></p>
<ul>
<li>Download Android Studio for you respective Operating System
	<ul>
<li>https://developer.android.com/studio/index.html#downloads
</li></ul></li>
<li>Start Android Studio</li>
<li>Download SDK Platform
	<ul>
			<li>Go to Tools->Android->SDK Manager</li>
			<li>Under SDK Platforms click the checkbox for Android 7.0 (Nougat) and click Apply</li>
			<img id="android1" src ="images\android1.png"width="80%" height="500">
</ul>
<li>Download SDK Tools
	<ul>
		<li>Click on the SDK Tools tab</li>
		<li>Click the checkbox for the following items</li>
			<ul>
					<li>Android Emulator</li>
					<li>Android SDK Tools</li>
			</ul>
		<li>Go to the bottom of the list and expand Support Repository</li>
		<li>Click on the checkbox for Android Support Repository</li>
		<li>Click Apply</li>
		<img id="android2" src ="images\android2.png"width="80%" height="500">
	</ul>

</ul>
<p><u> How to install xCode</u></p>
<ul>
<li>Go to apple and register as a developer using iCloud account(free)</li>
	<ul>
			<li>https://developer.apple.com/develop/</li>
	</ul>
<li>Download latest version of Xcode from apple’s website or the app store</li>
	<ul>
			<li>https://developer.apple.com/xcode/</li>
	</ul>
<li>Download update(if necessary)</li>
<li>Everything required for iOS is already included in Xcode, in the event that swift is not included, it can be downloaded from: </li>
	<ul>
			<li>https://swift.org/download/#releases</li>
	</ul>
</ul>

<p><u> Hardware Block Diagram</u></p>
<img src="images\gardenblockdiagram.jpg" alt="pie chart of supported platforms"width="80%" height="500">

<h3><b><u>What are the components?</u></b> </h3>

<p><u>Android Gradle Dependencies</u></p>
<ul>
<li><a href="https://github.com/PhilJay/MPAndroidChart">MPAndroidChart</a> depends on MPAndroidChart for the history dialog</li>
<li>appcompat-v7(For material design of activity)</li>
<li>cardview-v7(For cards displaying garden contents)</li>
<li>recyclerview-v7(To display the garden cards in a list using an adapter)</li>
</ul>

<p><u>Arduino UNO Dependencies</u></p>
<ul>
<li> <a href="https://github.com/RobTillaart/Arduino/tree/master/libraries/DHTlib">DHTlib</a>(For Digital Humidity & Temperature Sensor)

</ul>


	 </div>
    <div class="w3-center">
      <i class="fa fa-microchip w3-margin-bottom w3-jumbo"></i>
      <h3 class="w3-large">Hardware Info</h3>
    </div>
	 <ul>
<li>Arduino UNO</li>
<li>OctagonStar DHT11 Digital Humidity and Temperature Sensor</li>
<li>Phantom YoYo High Snesitivity Water Sensor</li>
<li>Tolako 5V Relay Module</li>
<li>DIGITEN 5/4" DC 12V Electric Solnoid Valve Normally Closed ater Interl Flow Switch</li>
<li>ESP8266 WiFi Module</li>
<li>12V 1A Power Supply</li>
</ul>
<p><u> Languages </u></p>
<ul>
<li>Java(Android)</li>
<li>XML(Android)</li>
<li>Swift(iOS)</li>
<li>Node js(RESTful server)</li>
<li>C++(Arduino)</li>
<li>HTML/CSS(website)</li>
<li>PHP(website/server)</li>
<li>angularJS(front-end framework)</li>
<li>MySQL(Database)</li>
</ul>
<br><br>


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