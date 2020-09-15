package me.reratos.timehandler.handler.completer.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// default/day/night/fixed
public enum SetTimeEnum {
	DEFAULT("default"),
	DAY("day"),
	NIGHT("night"),
	FIXED("fixed");

	private final String enabled;
	private static final Map<String, SetTimeEnum> map = new HashMap<>();

	static {
		for(SetTimeEnum e : SetTimeEnum.values()) {
			map.put(e.getValue(), e);
		}
	}

	SetTimeEnum(String enabled) {
		this.enabled = enabled;
	}

	public String getValue() {
		return enabled;
	}

	public static SetTimeEnum getEnumPorValue(String value) {
		return map.get(value);
	}
	
	public static Set<String> getList() {
		return map.keySet();
	}
	
}
