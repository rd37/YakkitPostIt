var gpsobject = new Object();

function initializeGPS(){
	//determine phone type
	if( DetectIphone()){
		gpsobject.phonetype= "iphone";
		//alert("this is a iphone");
	}else if(DetectAndroidPhone()){
		gpsobject.phonetype= "android";
		//alert("this is a android");
	}else if(DetectIpad()){
		gpsobject.phonetype= "ipad";
		//alert("this is a ipad");
	}else{
		gpsobject.phonetype="psorlaptop";
		//alert("this is a different");
	}
	
	if(navigator.geolocation && ( (gpsobject.phonetype == "android") || (gpsobject.phonetype == "iphone")  )){             
		gpsobject.settimeout = setTimeout("updategps()",5000);
		gpsobject.gpssupported="true";
		gpsobject.gpsavailable="false";
	} else {  
		//console.log("try navigator only");
		if(navigator.geolocation){
			gpsobject.lat="";
			gpsobject.lng="";
			gpsobject.settimeout = setTimeout("updategps()",5000);
			gpsobject.gpssupported="true";
			gpsobject.gpsavailable="false";
		}
	}

}
 
function gps_callback(position) 
{ 
	var lati = position.coords.latitude; 
    var lngi = position.coords.longitude; 
    gpsobject.lat=lati;
    gpsobject.lng=lngi;
    gpsobject.available="true";
    gpsobject.position=position;
    gpsobject.cb(position);
}

function error_callback(errori){
	//console.log("GPS error using local network");
	gpsobject.available="false";
}

function updategps(){
	console.log("Attempt find GPS");
	navigator.geolocation.getCurrentPosition( gps_callback, error_callback,{ maximumAge: 3000, timeout: 25000, enableHighAccuracy: true }); 
	//document.getElementById("login_name").innerHTML=gpsobject.phonetype+" GPS Avail: "+gpsobject.available;
	gpsobject.settimeout = setTimeout("updategps()",15000);
}