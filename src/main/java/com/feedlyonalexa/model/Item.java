package com.feedlyonalexa.model;

public class Item {
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Origin getOrigin() {
		return origin;
	}

	public void setOrigin(Origin origin) {
		this.origin = origin;
	}

	@Override
	public String toString() {
		return "Item [title=" + title + ", id=" + id + ", origin=" + origin + "]";
	}

	private String title;
	private String id;
	private Origin origin;
}
