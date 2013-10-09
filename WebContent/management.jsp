<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"> </script> 
<script type="text/javascript" src="js/management.js"></script>
<LINK href="css/management.css" rel="stylesheet" type="text/css">
<title>Manage PostIt</title>
</head>
<body onload="initializemap()">

<table class="header">
<tr>
<td class="col1"><img class="icon" alt="yakkit.com" src="images/yakicon.png"></td>
<td class="col2">PostIt Management Console</td>
<td class="col3"><img class="qrcode" alt="ask question qr code" src="images/postit.png"></td>
</tr>
<tr><td class="col1"></td><td class="col2"> <hr> </td><td class="col3"></td></tr>
</table>

<table class="header">
<form action="PostIt" method="GET">
<tr><td>search questions 
	<input type="text" name="question" id="question" onkeyup="searchquestion()"><div id="charleftques"></div></td>
	<td>search answers 
	<input type="text" name="answer" id="answer" onkeyup="searchanswer()"><div id="charleftans"></div></td></tr>
</form>
</table>

<hr>
un: <input type="text" id="un"> pw: <input type="text" id="pw">
<hr>
<table class="header">
<tr><td class="col4">
<div id="main">
  <ul class="flip" id="queslist">
  </ul>
</div>
</td>
<td class="col5">
<div id="main">
  <ul class="flip" id="anslist">
  </ul>
</div>
</td>
</tr>
<tr><td>
<input type="button" value="remove" onclick="removequestion()">
</td>
<td>
<input type="button" value="remove" onclick="removeanswer()">
</td></tr>
</table>

<hr>

<center><div class="gmap" id="map_canvas" ></div></center>

<hr>
</body>
</html>