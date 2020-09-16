package me.reratos.timehandler.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum OptionsSetEnum {
	ENABLED("enabled"),
	MOON_PHASE("moonPhase"),
	TIME("time"),
	TIME_FIXED("timeFixed"),
	THUNDER("thunder"),
	WEATHER("weather");

	private final String optionSet;
	private static final Map<String, OptionsSetEnum> map = new HashMap<>();
	
	static {
		for(OptionsSetEnum e : OptionsSetEnum.values()) {
			map.put(e.getValue(), e);
		}
	}

	OptionsSetEnum(String optionSet) {
		this.optionSet = optionSet;
	}

	public String getValue() {
		return optionSet;
	}

	public static OptionsSetEnum getEnumPorValue(String value) {
		return map.get(value);
	}
	
	public static Set<String> getList() {
		return map.keySet();
	}
	
}
