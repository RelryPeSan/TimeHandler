package me.reratos.timehandler.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum EnabledEnum {
	FALSE("false"),
	TRUE("true");
//	OFF("off"),
//	ON("on");

	private final String enabled;
	private static final Map<String, EnabledEnum> map = new HashMap<>();

	static {
		for(EnabledEnum e : EnabledEnum.values()) {
			map.put(e.getValue(), e);
		}
	}

	EnabledEnum(String enabled) {
		this.enabled = enabled;
	}

	public String getValue() {
		return enabled;
	}

	public static EnabledEnum getEnumPorValue(String value) {
		return map.get(value);
	}
	
	public static Set<String> getList() {
		return map.keySet();
	}
	
}
