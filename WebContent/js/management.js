var map1;
var mapOptions;
var key;
var keyfound=0;
var bounds=null;
var covHttpReq;
var markersArray = [];
var selectArray = [];
var messagesArray = [];
var count=0;
var lat;
var lng;
var maxchars=75;
var questionid;
var answerid;

function GetXmlHttpObject()
{
if (window.XMLHttpRequest)
  {
  // code for IE7+, Firefox, Chrome, Opera, Safari
  return new XMLHttpRequest();
  }
if (window.ActiveXObject)
  {
  // code for IE6, IE5
  return new ActiveXObject("Microsoft.XMLHTTP");
  }
return null;
}

/*
 * 
 * Question Related Functions
 * 
 */

function removequestion(){
	var url="PostIt";	
	var un=document.getElementById("un").value;
	var pw=document.getElementById("pw").value;
	console.log("remove question "+un+" "+pw);
	url=url+"?op=removequestion&questionid="+questionid+"&username="+un+"&password="+pw;
	url=url+"&sid="+Math.random();
	covHttpReq.onreadystatechange=new function(){if(covHttpReq.readyState==4){console.log('question removed?'+covHttpReq.responseText);}};
	covHttpReq.open("GET",url,true);
	covHttpReq.send(null);	
}

function lastquestionsearch(searchvar){
	console.log("last question search to populate questions list");
	var url="PostIt";	
	url=url+"?op=searchquestions&searchkey="+searchvar+"&startindex=0";
	url=url+"&sid="+Math.random();
	covHttpReq.onreadystatechange=updatelistresponsequestion;
	covHttpReq.open("GET",url,true);
	covHttpReq.send(null);
}

function updatelistresponsequestion(){
	if(covHttpReq.readyState==4){
		var jsonusers=covHttpReq.responseText;
		console.log("update list response "+jsonusers);
		var jsonObj = JSON.parse(jsonusers);
		var quesArray=jsonObj.ques;
		//clear list
		var qList=document.getElementById("queslist");
		qList.innerHTML="";
		var i;
		for(i=0;i<quesArray.length;i++){
			console.log(quesArray[i]);
			var liElem = document.createElement("li");
			var divLi = createListNodeQuestion(quesArray[i]);
		
			//var textNode=document.createTextNode(quesArray[i]);
			liElem.appendChild(divLi);
			liElem.appendChild(document.createElement("hr"));
			qList.appendChild(liElem);
		}
	}
}

function setquestion(quesid){
	questionid=quesid;
	var divElem = document.getElementById(''+quesid);
	var divClass = divElem.getAttribute("class");
	console.log("div elem ques clicked "+divClass+" "+quesid);
	if(divClass == 'dl')
		divElem.setAttribute("class", "dlsel");
	else
		divElem.setAttribute("class", "dl");
	searchanswer();
}

function searchquestion(){
	var sparam = document.getElementById("question").value;
	console.log("search with "+sparam+" "+sparam.length);
	if( (maxchars-sparam.length) < 0 ){
		sparam = sparam.substr(0,sparam.length-1);
		document.getElementById("question").value=sparam;
	}
	document.getElementById('charleftques').innerHTML=""+(maxchars-sparam.length)+" left";
	lastquestionsearch(sparam);
}

function createListNodeQuestion(content){
	var divElem = document.createElement("div");
	divElem.innerHTML="<div class='dl' id='"+content.id+"' onclick='setquestion("+content.id+")'>"+content.question+"</div>";
	return divElem;
}

/*
 * 
 * 
 * Answer related functions
 * 
 * 
 */

function lastanswersearch(searchvar){
	console.log("last answer search to populate answer list");
	var url="PostIt";	
	url=url+"?op=searchanswers&searchkey="+searchvar+"&startindex=0&questionkey="+questionid;
	url=url+"&sid="+Math.random();
	covHttpReq.onreadystatechange=updatelistresponseanswer;
	covHttpReq.open("GET",url,true);
	covHttpReq.send(null);
}

function updatelistresponseanswer(){
	if(covHttpReq.readyState==4){
		var jsonusers=covHttpReq.responseText;
		//console.log("update answer list response "+jsonusers);
		var jsonObj = JSON.parse(jsonusers);
		var ansArray=jsonObj.answers;
		//clear list
		var qList=document.getElementById("anslist");
		qList.innerHTML="";
		var i;
		for(i=0;i<ansArray.length;i++){
			console.log(ansArray[i]);
			var liElem = document.createElement("li");
			var divLi = createListNodeAnswer(ansArray[i]);
		
			liElem.appendChild(divLi);
			liElem.appendChild(document.createElement("hr"));
			qList.appendChild(liElem);
		}
	}
}

function createListNodeAnswer(content){
	//console.log("retreive answer "+content);
	var divElem = document.createElement("div");
	divElem.innerHTML="<div class='dl' id='"+content.id+"'  onclick='showmap("+content.lat+","+content.lng+","+content.id+")'>"+content.answer+"</div>";
	return divElem;
}

function removeanswer(){
	
	var url="PostIt";	
	var un=document.getElementById("un").value;
	var pw=document.getElementById("pw").value;
	console.log("remove answer "+un+" "+pw);
	url=url+"?op=removeanswer&answerid="+answerid+"&username="+un+"&password="+pw;
	url=url+"&sid="+Math.random();
	covHttpReq.onreadystatechange=new function(){if(covHttpReq.readyState==4){console.log('answer removed?'+covHttpReq.responseText);}};
	covHttpReq.open("GET",url,true);
	covHttpReq.send(null);	
}

function setanswer(ansid){
	answerid=ansid;
	var divElem = document.getElementById(''+ansid);
	var divClass = divElem.getAttribute("class");
	console.log("div elem ans clicked "+divClass+" "+ansid);
	if(divClass == 'dl')
		divElem.setAttribute("class", "dlsel");
	else
		divElem.setAttribute("class", "dl");
}

function searchanswer(){
	var sparam = document.getElementById("answer").value;
	console.log("search with "+sparam+" "+sparam.length);
	if( (maxchars-sparam.length) < 0 ){
		sparam = sparam.substr(0,sparam.length-1);
		document.getElementById("answer").value=sparam;
	}
	document.getElementById('charleftans').innerHTML=""+(maxchars-sparam.length)+" left";
	lastanswersearch(sparam);
}


/*
 * 
 * Google Map Related functionality
 * 
 * 
 */

function showmap(lat,lng,contentid){
	setanswer(contentid);
	clearMarkers(0);
	if(lat!=0 && lng!=0){
		addMarker(new google.maps.LatLng(lat,lng),"ok",0);
		map1.setZoom(17);
	    map1.setCenter(new google.maps.LatLng(lat,lng));
	}
}

function initializemap() {
	var latlng = new google.maps.LatLng(48.4733, -123.3133);     
	mapOptions = {       zoom: 13,       center: latlng,       mapTypeId: google.maps.MapTypeId.ROADMAP     };     
	map1 = new google.maps.Map(document.getElementById("map_canvas"),         mapOptions); 
	google.maps.event.addListener(map1, 'bounds_changed', function() {
       bounds = map1.getBounds();
     });
	google.maps.event.addListener(map1, 'click', function(event) {
	   //map1.setCenter(event.latLng);
		clearMarkers(0);
		document.getElementById("lat").value=event.latLng.lat();
		document.getElementById("lng").value=event.latLng.lng();
		addMarker(new google.maps.LatLng(event.latLng.lat(),event.latLng.lng()),"ok",0);
	});
	key=-1;
	covHttpReq=GetXmlHttpObject();
	console.log("map intialized");
	searchquestion();
}  

function addMarker(location,status,i) {
	if(!markersArray[i]){
	  //console.log("Add new Marker "+i);
	  var marker = new google.maps.Marker({
	    position: location,
	    map: map1
	  });
	  markersArray[i]=marker;
	  setMarkerStatus(status,i);
	  count++;
	}else{
		markersArray[i].position=location;
	}
}

function setMarkerStatus(status,i){
	//console.log("SMS::"+i+":"+status)
	if(status=="ok"){
		//console.log("Set icon to green");
		markersArray[i].setIcon('icons/green-dot.png');
	}else if (status=="help"){
		//console.log("Set icon to red");
		markersArray[i].setIcon('icons/red-dot.png');
	}
}

function clearMarkers(j){
	//console.log("Remove after "+j+" of "+count);
	
	for(var i=0;i<count;i++){
		//console.log("clear this marker from map "+j);
		markersArray[i].setMap(null);
		markersArray[i]=null;
		
	}
	count=0;
	
}

