package edu.osu.geogame;

import java.util.ArrayList;
import java.util.Iterator;

public class ForumThreadTuple {
	
	private String title;
	private String message;
	private String family;
	private String timestamp;
	private String count;
	private ArrayList<CommentThreadTuple> comments;
	
	public ForumThreadTuple() {
		comments = new ArrayList<CommentThreadTuple>();
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
	
	public void addComment( CommentThreadTuple comment ) {
		this.comments.add(comment);
	}
	
	public String title() {
		return this.title;
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
	
	public String count() {
		return this.count;
	}
	
	public Iterator<CommentThreadTuple> comments() {
		return this.comments.iterator();
	}
	
	
	public String toString() {
		return "<h4>" + title() + "</h4><br>" +
				family() + ": " + message() + "<br>" +
				timestamp() + "<br>" +
				"comments: " + count();
	
	}
	

}
