package edu.osu.geogame;

public class ForumThreadTuple {
	
	private String title;
	private String message;
	private String family;
	private String timestamp;
	private String count;
	
	public ForumThreadTuple() {
	}
	
	public void setTitle( String title ) {
		this.title = title;
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
	
	public void setCount( String count ) {
		this.count = count;
	}
	
	public String title() {
		return title;
	}
	
	public String message() {
		return message;
	}
	
	public String family() {
		return family;
	}
	
	public String timestamp() {
		return timestamp;
	}
	
	public String count() {
		return count;
	}

}
