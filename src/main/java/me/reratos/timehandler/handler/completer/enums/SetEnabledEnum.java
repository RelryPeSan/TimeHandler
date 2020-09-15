package me.reratos.timehandler.handler.completer.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum SetEnabledEnum {
	FALSE("false"),
	TRUE("true");
//	OFF("off"),
//	ON("on");

	private final String enabled;
	private static final Map<String, SetEnabledEnum> map = new HashMap<>();

	static {
		for(SetEnabledEnum e : SetEnabledEnum.values()) {
			map.put(e.getValue(), e);
		}
	}

	SetEnabledEnum(String enabled) {
		this.enabled = enabled;
	}

	public String getValue() {
		return enabled;
	}

	public static SetEnabledEnum getEnumPorValue(String value) {
		return map.get(value);
	}
	
	public static Set<String> getList() {
		return map.keySet();
	}
	
}
