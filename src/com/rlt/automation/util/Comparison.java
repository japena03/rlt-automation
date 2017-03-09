package com.rlt.automation.util;

public enum Comparison {
	
	EQUAL_TO("is equal to"),
	DOES_NOT_EQUAL("does not equal"),
	GREATER_THAN("greater than"),
	GREATER_THAN_OR_EQUAL_TO("greater than or equal to"),
	LESS_THAN("less than"),
	LESS_THAN_OR_EQUAL_TO("less than or equal to"),
	IS_SET("is set"),
	IS_NOT_SET("is not set");
	
	private String key;

	Comparison() {}
	
	Comparison(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
}
