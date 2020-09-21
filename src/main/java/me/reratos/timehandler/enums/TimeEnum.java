package me.reratos.timehandler.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum TimeEnum {
	DEFAULT	("default"),
	DAY		("day"),
	NIGHT	("night"),
	FIXED	("fixed"),
	CONFIGURED	("configured");
	
	private final String time;
	private static final Map<String, TimeEnum> map = new HashMap<>();
	
	static {
		for(TimeEnum e : TimeEnum.values()) {
			map.put(e.getValue(), e);
		}
	}

	TimeEnum(String time) {
		this.time = time;
	}
	
	public String getValue() {
		return time;
	}

	public static TimeEnum getEnumPorValue(String value) {
		return map.get(value);
	}

	public static Set<String> getList() {
		return map.keySet();
	}
	
}
