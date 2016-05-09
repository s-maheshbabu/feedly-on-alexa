package com.feedlyonalexa.model;

public class Origin {
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStreamId() {
		return streamId;
	}

	public void setStreamId(String streamId) {
		this.streamId = streamId;
	}

	@Override
	public String toString() {
		return "Source [title=" + title + ", streamId=" + streamId + "]";
	}

	private String title;
	private String streamId;
}
