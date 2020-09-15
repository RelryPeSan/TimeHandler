package me.reratos.timehandler.handler.completer.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// default/none/always
public enum SetThunderEnum {
	DEFAULT("default"),
	NONE("none"),
	ALWAYS("always");

	private final String enabled;
	private static final Map<String, SetThunderEnum> map = new HashMap<>();

	static {
		for(SetThunderEnum e : SetThunderEnum.values()) {
			map.put(e.getValue(), e);
		}
	}

	SetThunderEnum(String enabled) {
		this.enabled = enabled;
	}

	public String getValue() {
		return enabled;
	}

	public static SetThunderEnum getEnumPorValue(String value) {
		return map.get(value);
	}
	
	public static Set<String> getList() {
		return map.keySet();
	}
	
}
