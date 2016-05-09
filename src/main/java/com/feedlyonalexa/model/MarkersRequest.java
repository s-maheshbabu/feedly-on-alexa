package com.feedlyonalexa.model;

import java.util.List;

public class MarkersRequest {

	public List<String> getEntryIds() {
		return entryIds;
	}

	public void setEntryIds(List<String> entryIds) {
		this.entryIds = entryIds;
	}

	public MarkersAction getAction() {
		return action;
	}

	public void setAction(MarkersAction action) {
		this.action = action;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return "SaveArticlesRequest [entryIds=" + entryIds + "]";
	}

	private List<String> entryIds;
	private MarkersAction action;;
	private final String type = "entries";
}
