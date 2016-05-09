package com.feedlyonalexa.model;

import java.util.List;

public class StreamContents {
	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContinuation() {
		return continuation;
	}

	public void setContinuation(String continuation) {
		this.continuation = continuation;
	}

	@Override
	public String toString() {
		return "StreamContents [items=" + items + ", continuation=" + continuation + ", id=" + id + "]";
	}

	private List<Item> items;
	private String continuation;
	private String id;
}
