package me.reratos.timehandler.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum WeatherEnum {
	DEFAULT	("default"),
	RAIN	("rain"),
	CALM	("calm");
	
	private final String weather;
	private static final Map<String, WeatherEnum> map = new HashMap<>();
	
	static {
		for(WeatherEnum e : WeatherEnum.values()) {
			map.put(e.getValue(), e);
		}
	}

	WeatherEnum(String weather) {
		this.weather = weather;
	}
	
	public String getValue() {
		return weather;
	}

	public static WeatherEnum getEnumPorValue(String value) {
		return map.get(value);
	}

	public static Set<String> getList() {
		return map.keySet();
	}
	
}
