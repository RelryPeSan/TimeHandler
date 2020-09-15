package me.reratos.timehandler.core;

import org.bukkit.Bukkit;
import org.bukkit.World;

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
	
	public WorldManager(String nameWorld) {
		super();
		this.setNameWorld(nameWorld);
		this.world = Bukkit.getWorld(nameWorld);
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
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
		
		if(time == TimeEnum.DAY && (world.getTime() < 500 || world.getTime() > 11500)) {
			world.setTime(500);
		} else if(time == TimeEnum.NIGHT && (world.getTime() < 14000 || world.getTime() > 22000)) {
			world.setTime(14000);
		} else if(time == TimeEnum.FIXED) {
			long fullTime = world.getFullTime();
			long hoursDay = fullTime % 24000;
			long timeSet = (fullTime - hoursDay) + timeFixed;
			world.setFullTime(timeSet);
		}
		
		if(weather == WeatherEnum.CALM) {
			world.setStorm(false);
		} else if(weather == WeatherEnum.RAIN) {
			world.setStorm(true);
		}
		
		if(thunder == ThunderEnum.NONE) {
			world.setThundering(false);
		} else if(thunder == ThunderEnum.ALWAYS) {
			world.setThundering(true);
		}
	}
	
}
