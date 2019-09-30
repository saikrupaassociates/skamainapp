package com.saikrupa.app.dto;

import java.util.Map;

public class FilterParameter {
	private String filterName;
	private Map<String, Object> parameters;

	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	public Map<String, Object> getParameters() {		
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

}
