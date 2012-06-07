package edu.osu.geogame;

/**
 * Container class for comment info retrieved from the server
 * 
 */
public class CommentThreadTuple {

	/**
	 * The id of the comment
	 */
	private int id;

	/**
	 * The message itself
	 */
	private String message;

	/**
	 * The family who posted the comment
	 */
	private String family;

	/**
	 * Timestamp
	 */
	private String timestamp;

	/**
	 * Constructor
	 */
	public CommentThreadTuple() {
	}

	/**
	 * Set the comment id
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Set the comment
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Set the family name
	 * 
	 * @param family
	 */
	public void setFamily(String family) {
		this.family = family;
	}

	/**
	 * Set the timestamp
	 * 
	 * @param timestamp
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Return the comment id
	 * 
	 * @return int the comment id
	 */
	public int id() {
		return this.id;
	}

	/**
	 * Return the comment
	 * 
	 * @return String the comment
	 */
	public String message() {
		return this.message;
	}

	/**
	 * Return the family name
	 * 
	 * @return String the family name
	 */
	public String family() {
		return this.family;
	}

	/**
	 * Return the timestamp
	 * 
	 * @return String the timestamp
	 */
	public String timestamp() {
		return this.timestamp;
	}

	/**
	 * Used by CommentPageActivity to display this comment on the UI
	 */
	public String toString() {
		return family() + ": " + message() + "\n" + timestamp();
	}

}
