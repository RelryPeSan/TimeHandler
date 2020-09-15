package me.reratos.timehandler.handler.completer.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// default/rain/calm
public enum SetWeatherEnum {
	DEFAULT("default"),
	RAIN("rain"),
	CALM("calm");

	private final String enabled;
	private static final Map<String, SetWeatherEnum> map = new HashMap<>();

	static {
		for(SetWeatherEnum e : SetWeatherEnum.values()) {
			map.put(e.getValue(), e);
		}
	}

	SetWeatherEnum(String enabled) {
		this.enabled = enabled;
	}

	public String getValue() {
		return enabled;
	}

	public static SetWeatherEnum getEnumPorValue(String value) {
		return map.get(value);
	}
	
	public static Set<String> getList() {
		return map.keySet();
	}
	
}
