package me.reratos.timehandler.enums;

public enum WeatherEnum {
	DEFAULT	("default"),
	RAIN	("rain"),
	CALM	("calm");
	
	private final String weather;

	WeatherEnum(String weather) {
		this.weather = weather;
	}
	
	public String getValue() {
		return weather;
	}
	
}
