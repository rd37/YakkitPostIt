<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"> </script> 
<script type="text/javascript" src="js/answers.js"></script>
<script type="text/javascript" src="js/gpsscript.js"></script>
<script type="text/javascript" src="js/mdetect.js"></script>
<script type="text/javascript" src="js/util.js"></script>
<LINK href="css/answer.css" rel="stylesheet" type="text/css">
<title>View Answers</title>
</head>
<body onload="initializemap(<%= request.getParameter("questionid") %>)">

<table class="header">
<tr>
<td class="col1"><img class="icon" alt="yakkit.com" src="images/yakicon.png"></td>
<td class="col2">
<center>
<table><tr><td align="center">have an answer? ...</td></tr><tr><td align="center">
<% if( request.getAttribute("servletmessage") != null ) { %>
<%= request.getAttribute("servletmessage") %>
<% } %>
</td></tr>
<tr><td align="center">
<% if( request.getParameter("question") != null ) { %>
<%= request.getParameter("question") %>
<% } %>
</td></tr>
</table>
</center>
</td>
<td class="col3"><img class="qrcode" alt="ask question qr code" src="images/postit.png"></td>
</tr>
<tr><td class="col1"><a href="http://media.yakkit.com">yakkit.com</a></td><td class="col2"><hr></td><td class="col3"><a href="http://web.yakkit.com/YakkitPostIt">PostIt</a></td></tr>
</table>

<table class="header">
<form action="PostIt" method="GET">
<tr><td class="col1">search</td><td class="col2" align="center"><input type="text" name="answer" id="answer" onkeyup="searchanswer()"></td><td class="col3"><div id='charleft'></div></td></tr>
<tr><td class="col1"></td><td class="col2" align="center"><input type="submit" id="sendanswer" value="post new answer" disabled><input type="button" onclick="viewquestions()" value="view questions"></td><td class="col3"></td></tr>
<input type="hidden" id= "op" name="op" value="postanswer" /> 
<input type="hidden" id="lat" name="lat" value="0" />
<input type="hidden" id="lng" name="lng" value="0" />
<input type="hidden" id="questionid" name="questionid" value="0" />
</form>
</table>

<table class="header">
<tr><td class="col1"></td>
<td class="col2">
<div id="main">
  <ul class="flip" id="anslist">
  </ul>
</div>
</td>
<td class="col3"></td></tr>
</table>
<hr>

<center><div class="gmap" id="map_canvas" ></div></center>

<hr>
</body>
</html>