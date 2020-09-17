package me.reratos.timehandler.core;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;

import me.reratos.timehandler.enums.MoonPhasesEnum;
import me.reratos.timehandler.enums.ThunderEnum;
import me.reratos.timehandler.enums.TimeEnum;
import me.reratos.timehandler.enums.WeatherEnum;

public class WorldManager implements Runnable {
	
	private String nameWorld;
	private World world;
	
	private boolean enabled;
	private WeatherEnum weather;
	private ThunderEnum thunder;
	private TimeEnum time;
	private int timeFixed;
	private MoonPhasesEnum moonPhase;
	
	public WorldManager(String nameWorld) {
		super();
		this.setNameWorld(nameWorld);
		this.world = Bukkit.getWorld(nameWorld);
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		if(!enabled) {
			world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
			world.setGameRule(GameRule.DO_WEATHER_CYCLE, true);
		}
		this.enabled = enabled;
	}

	public WeatherEnum getWeather() {
		return weather;
	}

	public void setWeather(WeatherEnum weather) {
		this.weather = weather;
	}

	public ThunderEnum getThunder() {
		return thunder;
	}

	public void setThunder(ThunderEnum thunder) {
		this.thunder = thunder;
	}

	public TimeEnum getTime() {
		return time;
	}

	public void setTime(TimeEnum time) {
		this.time = time;
	}

	public int getTimeFixed() {
		return timeFixed;
	}

	public void setTimeFixed(int timeFixed) {
		this.timeFixed = timeFixed;
	}

	public MoonPhasesEnum getMoonPhase() {
		return moonPhase;
	}

	public void setMoonPhase(MoonPhasesEnum moonPhase) {
		this.moonPhase = moonPhase;
	}

	public String getNameWorld() {
		return nameWorld;
	}

	public void setNameWorld(String nameWorld) {
		this.nameWorld = nameWorld;
	}

	public World getWorld() {
		return world;
	}

	@Override
	public void run() {
		if(!enabled) return;
		
		if(world == null) {
			world = Bukkit.getWorld(nameWorld);
			if(world == null) return;
		}

		if(time == TimeEnum.FIXED) {
			world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
			long fullTime = world.getFullTime();
			long hoursDay = fullTime % 24000;
			long timeSet = (fullTime - hoursDay) + timeFixed;
			world.setFullTime(timeSet);
		} else {
			world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
			
			if(time == TimeEnum.DAY && (world.getTime() < 500 || world.getTime() > 11500)) {
				world.setTime(500);
			} else if(time == TimeEnum.NIGHT && (world.getTime() < 14000 || world.getTime() > 22000)) {
				world.setTime(14000);
			}
		}		
		
		if(weather == WeatherEnum.DEFAULT) {
			world.setGameRule(GameRule.DO_WEATHER_CYCLE, true);
		} else {
			world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
			
			if(weather == WeatherEnum.CALM) {
				world.setStorm(false);
			} else if(weather == WeatherEnum.RAIN) {
				world.setStorm(true);
			}
		}
		
		if(thunder == ThunderEnum.NONE) {
			world.setThundering(false);
		} else if(thunder == ThunderEnum.ALWAYS) {
			world.setThundering(true);
		}
		
		if(moonPhase != MoonPhasesEnum.DEFAULT) {
			long fullTime = world.getFullTime();
			long days = fullTime / 24000;
        	int currentPhase = (int) (days % 8);
        	int targetPhase = moonPhase.ordinal();
        	int timeSkip = (targetPhase < currentPhase ? (targetPhase + 8) - currentPhase : targetPhase - currentPhase);
        	
        	if(timeSkip != 0) {
        		world.setFullTime(fullTime + (timeSkip * 24000));
        	}
		}
	}
	
}
