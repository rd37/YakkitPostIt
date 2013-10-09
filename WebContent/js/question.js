var covHttpReq;
var maxchars=75;

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

function initialize(){
	covHttpReq=GetXmlHttpObject();
	document.getElementById('charleft').innerHTML=""+maxchars+" left";
	lastquestionsearch('');
}

function lastquestionsearch(searchvar){
	console.log("last question search to populate questions list");
	var url="PostIt";	
	url=url+"?op=searchquestions&searchkey="+searchvar+"&startindex=0";
	url=url+"&sid="+Math.random();
	covHttpReq.onreadystatechange=updatelistresponse;
	covHttpReq.open("GET",url,true);
	covHttpReq.send(null);
}

function updatelistresponse(){
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
			var divLi = createListNode(quesArray[i]);
		
			//var textNode=document.createTextNode(quesArray[i]);
			liElem.appendChild(divLi);
			liElem.appendChild(document.createElement("hr"));
			qList.appendChild(liElem);
		}
	}
}

function createListNode(content){
	var divElem = document.createElement("div");
	var tokens = content.question.split(" ");
	var innerContent="";
	var i=0;
	for(i=0;i<tokens.length;i++){
		innerContent+=geturlform(tokens[i])+" ";
	}
	
	divElem.innerHTML="<div class='dl' onclick='listanswers("+content.id+",\""+content.question+"\")'>"+innerContent+"</div>";
	return divElem;
}

function listanswers(id,ques){
	console.log("list answers  id "+id+" "+ques);
	window.location.href = "viewanswers.jsp?questionid="+id+"&question=Question: "+ques;
}

function searchquestion(){
	var sparam = document.getElementById("question").value;
	console.log("search with "+sparam+" "+sparam.length);
	if( (maxchars-sparam.length) < 0 ){
		sparam = sparam.substr(0,sparam.length-1);
		document.getElementById("question").value=sparam;
	}
	if(sparam.length>5){
		document.getElementById("sendquestion").removeAttribute("disabled");
	}else{
		document.getElementById("sendquestion").setAttribute("disabled", true);
	}
	document.getElementById('charleft').innerHTML=""+(maxchars-sparam.length)+" left";
	lastquestionsearch(sparam);
}

function viewquestions(){
	window.location.href = "viewquestions.jsp";
}

