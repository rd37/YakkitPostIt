package service;

public class Answers {
	public String content;
	public int id;
	public double lat;
	public double lng;
	public int count=0;
	
	public Answers( int id,String content, double lat, double lng){
		this.content=content;
		this.id=id;
		this.lat=lat;
		this.lng=lng;
	}
}
