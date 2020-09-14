package me.reratos.timehandler.enums;

import java.util.HashMap;
import java.util.Map;

public enum ThunderEnum {
	DEFAULT	("default"),
	NONE	("none"),
	ALWAYS	("always");
	
	private final String thunder;
	private static final Map<String, ThunderEnum> map = new HashMap<>();
	
	static {
		for(ThunderEnum e : ThunderEnum.values()) {
			map.put(e.getValue(), e);
		}
	}

	ThunderEnum(String thunder) {
		this.thunder = thunder;
	}
	
	public String getValue() {
		return thunder;
	}
	
	public static ThunderEnum getEnumPorValue(String value) {
		return map.get(value);
	}
	
}
