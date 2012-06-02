package edu.osu.geogame;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Data container for forum posts
 *
 */
public class ForumThreadTuple {
	
	/*
	 * Title of this post
	 */
	private String title;
	
	/*
	 * Post content
	 */
	private String message;
	
	/*
	 * The family that created this post
	 */
	private String family;
	
	/*
	 * Timestamp of the post
	 */
	private String timestamp;
	
	/*
	 * Number of comments in this post
	 */
	private String count;
	
	/**
	 * Constructor
	 */
	public ForumThreadTuple() {
	}
	
	/**
	 * Set the title of the post
	 * @param title
	 */
	public void setTitle( String title ) {
		this.title = title;
	}
	
	/**
	 * Set the content of the post
	 * @param message
	 */
	public void setMessage( String message ) {
		this.message = message;
	}
	
	/**
	 * Set the family name
	 * @param family
	 */
	public void setFamily( String family ) {
		this.family = family;
	}
	
	/**
	 * Set the timestamp
	 * @param timestamp
	 */
	public void setTimestamp( String timestamp ) {
		this.timestamp = timestamp;
	}
	
	/**
	 * Set the number of comments of this post
	 * @param count
	 */
	public void setCount( String count ) {
		this.count = count;
	}
	
	/**
	 * Get the post title
	 * @return String  the post title
	 */
	public String title() {
		return this.title;
	}
	
	/**
	 * Get the post content
	 * @return String  the post content
	 */
	public String message() {
		return this.message;
	}
	
	/**
	 * Get the family name
	 * @return String  the family name
	 */
	public String family() {
		return this.family;
	}
	
	/**
	 * Get the timestamp
	 * @return String  the timestamp
	 */
	public String timestamp() {
		return this.timestamp;
	}
	
	/**
	 * Get the number of comments of this post
	 * @return String  the number of comments of this post
	 */
	public String count() {
		return this.count;
	}
	
	
	/**
	 * Used by ForumTabActivity to display this post on the UI
	 */
	public String toString() {
		return "<h4>" + title() + "</h4><br>" +
				family() + ": " + message() + "<br>" +
				timestamp() + "<br>" +
				"comments: " + count();
	
	}
	

}
