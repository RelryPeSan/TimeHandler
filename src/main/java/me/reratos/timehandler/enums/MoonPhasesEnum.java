package me.reratos.timehandler.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum MoonPhasesEnum {
	FULL_MOON("fullMoon"),
	WANING_GIBBOUS("waningGibbous"),
	THIRD_QUARTER("thirdQuarter"),
	WANING_CRESCENT("waningCrescent"),
	NEW_MOON("newMoon"),
	WAXING_CRESCENT("waxingCrescent"),
	FIRST_QUARTER("firstQuarter"),
	WAXING_GIBBOUS("waxingGibbous"),
	DEFAULT("default");

	private final String moonPhase;
	private static final Map<String, MoonPhasesEnum> map = new HashMap<>();
	
	static {
		for(MoonPhasesEnum e : MoonPhasesEnum.values()) {
			map.put(e.getValue(), e);
		}
	}

	MoonPhasesEnum(String moonPhase) {
		this.moonPhase = moonPhase;
	}
	
	public String getValue() {
		return moonPhase;
	}
	
	public static MoonPhasesEnum getEnumPorValue(String value) {
		return map.get(value);
	}

	public static Set<String> getList() {
		return map.keySet();
	}
	
}
