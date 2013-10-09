<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<script type="text/javascript" src="js/question.js"></script>
<script type="text/javascript" src="js/util.js"></script>
<LINK href="css/question.css" rel="stylesheet" type="text/css">
<title>View Questions</title>
</head>
<body onload="initialize()">

<table class="header">
<tr>
<td class="col1"><img class="icon" alt="yakkit.com" src="images/yakicon.png"></td>
<td class="col2">
<center>
<table><tr><td align="center">have a question? ...</td></tr><tr><td align="center">
<% if( request.getAttribute("servletmessage") != null ) { %>
<%= request.getAttribute("servletmessage") %>
<% } %>
</td></tr></table>
</center>
</td>
<td class="col3"><img class="qrcode" alt="ask question qr code" src="images/postit.png"></td>
</tr>
<tr><td class="col1"><a href="http://media.yakkit.com">yakkit.com</a></td><td class="col2"><hr></td><td class="col3"><a href="http://web.yakkit.com/YakkitPostIt">PostIt</a></td></tr>

</table>

<table class="header">
<form action="PostIt" method="GET">
<tr><td class="col1">search</td><td class="col2" align="center"><input type="text" name="question" id="question" onkeyup="searchquestion()"></td><td class="col3"><div id='charleft'></div></td></tr>
<tr><td class="col1"></td><td class="col2" align="center"><input type="submit" id="sendquestion" value="post new question" disabled></td><td class="col3"></td></tr>
<tr><td class="col1"></td><td class="col3"><input type="hidden" name="op" value="postquestion" /> </td><td class="col3"></td></tr>
</form>

</table>

<table class="header">
<tr><td class="col1"></td><td class="col2">

<div id="main" align="left">
  <ul class="flip" id="queslist">   
  </ul>
</div>
</td><td class="col3"></td></tr>
</table>


<table class="header">
<tr><td class="col1"></td><td class="col2"><hr></td><td class="col3"></td></tr>
<tr><td class="col1"></td><td class="col2">
please select a question
</td><td class="col3"></td></tr>
<tr><td class="col1"></td><td class="col2">
to post or view answers 
</td><td class="col3"></td></tr>
</table>

</body>
</html>