package edu.osu.geogame;

public class CommentThreadTuple {

	private int id;
	private String message;
	private String family;
	private String timestamp;
	
	public CommentThreadTuple() {
	}
	
	
	public void setId( int id ) {
		this.id = id;
	}
	
	public void setMessage( String message ) {
		this.message = message;
	}
	
	public void setFamily( String family ) {
		this.family = family;
	}
	
	public void setTimestamp( String timestamp ) {
		this.timestamp = timestamp;
	}
	
	
	public int id() {
		return this.id;
	}
	
	public String message() {
		return this.message;
	}
	
	public String family() {
		return this.family;
	}
	
	public String timestamp() {
		return this.timestamp;
	}
	
	

}
