package me.reratos.timehandler.core;

import org.bukkit.World;

import me.reratos.timehandler.enums.ThunderEnum;
import me.reratos.timehandler.enums.TimeEnum;
import me.reratos.timehandler.enums.WeatherEnum;

public class WorldManager implements Runnable {
	
	private World world;
	
	private boolean enabled;
	private WeatherEnum weather;
	private ThunderEnum thunder;
	private TimeEnum time;
	private int timeFixed;
	
	public WorldManager(World world) {
		super();
		this.world = world;
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

	public World getWorld() {
		return world;
	}

	@Override
	public void run() {
		if(!enabled) return;
		
		if(time == TimeEnum.DAY && world.getTime() > 11500) {
			world.setTime(500);
		} else if(time == TimeEnum.NIGHT && (world.getTime() < 14000 || world.getTime() > 22000)) {
			world.setTime(14000);
		} else if(time == TimeEnum.FIXED) {
			world.setTime(timeFixed);
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
