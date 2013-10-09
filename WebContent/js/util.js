String.prototype.contains = function(it) { return this.indexOf(it) != -1; };

function geturlform(addr){
	if(addr.contains('http') || addr.contains('https')){
		return "<a href='"+addr+"'>"+addr+"</a>";
	}else{
		return addr;
	}
}
