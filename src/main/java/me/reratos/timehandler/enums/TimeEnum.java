package me.reratos.timehandler.enums;

public enum TimeEnum {
	DEFAULT	("default"),
	DAY		("day"),
	NIGHT	("night"),
	FIXED	("fixed");
	
	private final String time;

	TimeEnum(String time) {
		this.time = time;
	}
	
	public String getValue() {
		return time;
	}
	
}
