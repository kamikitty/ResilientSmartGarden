<?php
$hostname = "localhost";
$username = "root";
$password = "kiwi";
$databaseName = "companionplant";

// connect to mysql
$connect = mysqli_connect($hostname, $username, $password, $databaseName);

// mysql select query

$query = "SELECT companionname FROM companion WHERE plantname=(SELECT plantname FROM plant WHERE plantname='beetroot'); ";
$query2 = "SELECT plantname,type,season, species FROM plant";


// result for method one
$result1 = mysqli_query($connect, $query);
$result2= mysqli_query($connect, $query2);



?>
<html>
<head>
<title>SMART ROOTS</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Lato">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="shortcut icon" href="images\fav_icon.png">
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

<!--Comp planting info-->
<div class="w3-container w3-content w3-center w3-padding-200" style="max-width:800px; margin-top:120px;">
<div class="transbox">
<h2 class="w3-center" id="comPlant">COMPANION PLANTING</h2>
  <p class="w3-center w3-large">Companion planting can help you create a more resilient garden in your home or community. By defintion companion planting is the establishment
  of two or more plant species in close proximity so that some culture benefit is derived. The scientific aspect of it defines it to embrace a number of strategies that increase
  the biodiversity of agricultural ecosystems.
  <br>While companion planting can potentially help your garden thrive, it can be hard to find information about it that is easy to read
  and understand. The companion planting feature found here will help you lessen your research. Companion planting is all about one plant helping another, well allow us to help
  you build a better garden. Click on our link below to explore our companion planting aid.</br></p><br><br>
<div class="dropdown w3-center">
<form>

  <button class="dropbtn">SELECT PRIMARY PLANT</button>
  <div class="dropdown-content">

<?

while ($row = mysqli_fetch_array($result2)) 
{
    $rows[] = $row;
	echo $row['plantname']."<br>";
}


?>

</div><br><br>
</div>

<!-- Table One 1  -->
        <table style="background-color: green;" class="table w3-center">
			<tr> <th>Companion Plant for: beetroot</th>  			
            </tr>

            <?php while($row1 = mysqli_fetch_array($result1)):;?>
            <tr>
                <td><?php echo $row1[0];?></td>
            </tr>
            <?php endwhile;?>

        </table>
        <br><br><br><br><br><br><br><br><br><br><br><br>
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

table,th,tr,td
{
    border: 1px solid black;
}
			
			/* Style The Dropdown Button */
.dropbtn {
    background-color: #4CAF50;
    color: white;
    padding: 16px;
    font-size: 16px;
    border: none;
    cursor: pointer;
}

/* The container- needed to position the dropdown content */
.dropdown {
    position: relative;
    display: inline-block;
}

/* Dropdown Content (Hidden by Default) */
.dropdown-content {
    display: none;
	color: white;
    position: absolute;
    background-color: black;
    min-width: 215px;
    box-shadow: 0px 8px 20px 0px rgba(0,0,0,0);
    z-index: 1;
}

/* Links inside the dropdown */
.dropdown-content a {
    color: white;
    padding: 50px 50px;
    text-decoration: none;
    display: block;
}

/* Change color of dropdown links on hover */
.dropdown-content a:hover {background-color: green}

/* Show the dropdown menu on hover */
.dropdown:hover .dropdown-content {
    display: block;
}

/* Change the background color of the dropdown button when the dropdown content is shown */
.dropdown:hover .dropbtn {
    background-color: #3e8e41;
}

</style>

</body>
</html>