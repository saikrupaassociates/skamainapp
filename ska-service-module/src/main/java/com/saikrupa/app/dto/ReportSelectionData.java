package com.saikrupa.app.dto;

import java.util.List;

public class ReportSelectionData {
	private int selectionType;
	private List<FilterParameter> filters;

	public int getSelectionType() {
		return selectionType;
	}

	public void setSelectionType(int selectionType) {
		this.selectionType = selectionType;
	}

	public List<FilterParameter> getFilters() {
		return filters;
	}

	public void setFilters(List<FilterParameter> filters) {
		this.filters = filters;
	}
}
