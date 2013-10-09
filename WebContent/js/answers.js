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
var questionid;
var lat;
var lng;
var maxchars=75;
var timer;

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

function lastanswersearch(searchvar){
	console.log("last answer search to populate answer list");
	var url="PostIt";	
	url=url+"?op=searchanswers&searchkey="+searchvar+"&startindex=0&questionkey="+questionid;
	url=url+"&sid="+Math.random();
	covHttpReq.onreadystatechange=updatelistresponse;
	covHttpReq.open("GET",url,true);
	covHttpReq.send(null);
}

function updatelistresponse(){
	if(covHttpReq.readyState==4){
		var jsonusers=covHttpReq.responseText;
		console.log("update answer list response "+jsonusers);
		var jsonObj = JSON.parse(jsonusers);
		var ansArray=jsonObj.answers;
		//clear list
		var qList=document.getElementById("anslist");
		qList.innerHTML="";
		var i;
		for(i=0;i<ansArray.length;i++){
			console.log(ansArray[i]);
			var liElem = document.createElement("li");
			var divLi = createListNode(ansArray[i]);
		
			//var textNode=document.createTextNode(quesArray[i]);
			liElem.appendChild(divLi);
			liElem.appendChild(document.createElement("hr"));
			qList.appendChild(liElem);
		}
	}
}

function createListNode(content){
	var divElem = document.createElement("div");
	var tokens = content.answer.split(" ");
	var innerContent="";
	var i=0;
	for(i=0;i<tokens.length;i++){
		innerContent+=geturlform(tokens[i])+" ";
	}
	divElem.innerHTML="<div class='dl' onclick='showmap("+content.lat+","+content.lng+","+content.id+")'>"+innerContent+"</div>";
	return divElem;
}

function showmap(lat,lng,contentid){
	clearMarkers(0);
	if(lat!=0 && lng!=0){
		addMarker(new google.maps.LatLng(lat,lng),"help",0);
		map1.setZoom(17);
	    map1.setCenter(new google.maps.LatLng(lat,lng));
	}
}

function viewquestions(){
	console.log("goto questions page ");
	window.location.href = "viewquestions.jsp";
}

function searchanswer(){
	var sparam = document.getElementById("answer").value;
	
	console.log("search with "+sparam+" "+sparam.length);
	if( (maxchars-sparam.length) < 0 ){
		sparam = sparam.substr(0,sparam.length-1);
		document.getElementById("answer").value=sparam;
	}
	if(sparam.length>5){
		document.getElementById("sendanswer").removeAttribute("disabled");
	}else{
		document.getElementById("sendanswer").setAttribute("disabled", true);
	}
	document.getElementById('charleft').innerHTML=""+(maxchars-sparam.length)+" left";
	
	lastanswersearch(sparam);
}

function initializemap(quesid) {
	console.log("qeustion id is "+quesid);
	document.getElementById('charleft').innerHTML=""+maxchars+" left";
	document.getElementById("questionid").value=quesid;
	questionid=quesid;
	var latlng = new google.maps.LatLng(48.4670, -123.3133);     
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
		addMarker(new google.maps.LatLng(event.latLng.lat(),event.latLng.lng()),"help",0);
	});
	key=-1;
	covHttpReq=GetXmlHttpObject();
	console.log("map intialized");
	initializeGPS();
	lastanswersearch('');
	gpsobject.cb=updatemaploc;
}  

function updatemaploc(position){
	console.log("update pos");
	//addMarker(position,"ok",1);
	clearMarkers(1);
	if(position){
		addMarker(new google.maps.LatLng(position.coords.latitude,position.coords.longitude),"ok",1);
		if(count==0){
			map1.setZoom(17);
		    map1.setCenter(new google.maps.LatLng(position.coords.latitude,position.coords.longitude));
		    count++;
		}
	}
}

function viewquestions(){
	window.location.href = "viewquestions.jsp";
}


function addMarker(location,status,i) {
	if(!markersArray[i]){
	  console.log("Add new Marker "+i);
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
		markersArray[i].setIcon('icons/walking_scaled.png');
	}else if (status=="help"){
		//console.log("Set icon to red");
		markersArray[i].setIcon('icons/red-dot.png');
	}
}

function clearMarkers(i){
	//console.log("Remove after "+j+" of "+count);
	
	if(markersArray[i]!=null){
		//console.log("clear this marker from map "+j);
		markersArray[i].setMap(null);
		markersArray[i]=null;
		
	}
	//count=0;
	
}

