package me.reratos.timehandler.enums;

public enum ThunderEnum {
	DEFAULT	("default"),
	NONE	("none"),
	ALWAYS	("always");
	
	private final String thunder;

	ThunderEnum(String thunder) {
		this.thunder = thunder;
	}
	
	public String getValue() {
		return thunder;
	}
	
}
