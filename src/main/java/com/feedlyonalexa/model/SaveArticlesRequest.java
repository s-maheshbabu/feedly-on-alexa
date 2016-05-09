package com.feedlyonalexa.model;

import java.util.List;

public class SaveArticlesRequest {

	public List<String> getEntryIds() {
		return entryIds;
	}

	public void setEntryIds(List<String> entryIds) {
		this.entryIds = entryIds;
	}

	public String getAction() {
		return action;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return "SaveArticlesRequest [entryIds=" + entryIds + "]";
	}

	private List<String> entryIds;
	private final String action = "markAsSaved";
	private final String type = "entries";
}
