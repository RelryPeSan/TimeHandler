package me.reratos.timehandler.core;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.reratos.timehandler.TimeHandler;
import me.reratos.timehandler.enums.MoonPhasesEnum;
import me.reratos.timehandler.enums.ThunderEnum;
import me.reratos.timehandler.enums.TimeEnum;
import me.reratos.timehandler.enums.WeatherEnum;

public class WorldManager implements Runnable {
	
	private String nameWorld;
	private World world;

	private Runnable runnableTask;
	private BukkitTask bukkitTask;
	
	private boolean enabled;
	private WeatherEnum weather;
	private ThunderEnum thunder;
	private TimeEnum time;
	private int timeFixed;
	private int durationDay;
	private int durationNight;
	private MoonPhasesEnum moonPhase;

	private final int BEGIN_DAY;
	private final int BEGIN_NIGHT;
	private final int DURATION_DEFAULT_DAY;
	private final int DURATION_DEFAULT_NIGHT;

	private final int DURATION_DAY_MIN;
	private final int DURATION_DAY_MAX;
	private final int DURATION_NIGHT_MIN;
	private final int DURATION_NIGHT_MAX;

	// variaveis para auxiliar na duração do dia e da noite
	static double offset = 0;
	private float auxTicksDay;
	private float auxTicksNight;

	public WorldManager(String worldName) {
		this(worldName, 23000, 13000);
	}

	public WorldManager(String worldName, int beginDay, int beginNight) {
		super();
		this.setNameWorld(worldName);
		this.world = Bukkit.getWorld(worldName);
		
		if(((beginDay >= 22000 && beginDay <= 23999 ) || beginDay == 0 ) &&
				(beginNight >= 12000 && beginNight <= 14000 )) {
			this.BEGIN_DAY = beginDay;
			this.BEGIN_NIGHT = beginNight;
		} else {
			this.BEGIN_DAY = 23000;
			this.BEGIN_NIGHT = 13000;
		}
		
		this.durationDay = this.DURATION_DEFAULT_DAY = (beginDay == 0 ? beginNight : (24000 - beginDay) + beginNight);
		this.durationNight = this.DURATION_DEFAULT_NIGHT = 24000 - DURATION_DEFAULT_DAY;

		this.DURATION_DAY_MIN = this.DURATION_DEFAULT_DAY / 10;
		this.DURATION_DAY_MAX = this.DURATION_DEFAULT_DAY * 10;
		this.DURATION_NIGHT_MIN = this.DURATION_DEFAULT_NIGHT / 10;
		this.DURATION_NIGHT_MAX = this.DURATION_DEFAULT_NIGHT * 10;

		this.enabled = true;
		this.thunder = ThunderEnum.DEFAULT;
		this.weather = WeatherEnum.DEFAULT;
		this.moonPhase = MoonPhasesEnum.DEFAULT;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		if(!enabled) {
			if(bukkitTask != null) {
				bukkitTask.cancel();
				bukkitTask = null;
			}
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

	public int getDurationDay() {
		return durationDay;
	}

	public void setDurationDay(int durationDay) {
		if(durationDay < DURATION_DAY_MIN) {
			this.durationDay = DURATION_DAY_MIN;
		} else this.durationDay = Math.min(durationDay, DURATION_DAY_MAX);
	}

	public int getDurationNight() {
		return durationNight;
	}

	public void setDurationNight(int durationNight) {
		if(durationNight < DURATION_NIGHT_MIN) {
			this.durationNight = DURATION_NIGHT_MIN;
		} else this.durationNight = Math.min(durationNight, DURATION_NIGHT_MAX);
	}

	public MoonPhasesEnum getMoonPhase() {
		return moonPhase;
	}

	public void setMoonPhase(MoonPhasesEnum moonPhase) {
		this.moonPhase = moonPhase;
	}

//	public String getNameWorld() {
//		return nameWorld;
//	}

	public void setNameWorld(String nameWorld) {
		this.nameWorld = nameWorld;
	}

	public World getWorld() {
		return world;
	}

	public float getDurationDefaultDay() {
		return DURATION_DEFAULT_DAY;
	}

	public float getDurationDefaultNight() {
		return DURATION_DEFAULT_NIGHT;
	}

	public Runnable getRunnableTask() {
		return runnableTask;
	}

	@Override
	public void run() {
		if(!enabled) return;
		
		if(world == null) {
			world = Bukkit.getWorld(nameWorld);
			if(world == null) return;
		}

		if(time == TimeEnum.CONFIGURED) {
			auxTicksDay = (float) DURATION_DEFAULT_DAY / (float) durationDay;
			auxTicksNight = (float) DURATION_DEFAULT_NIGHT / (float) durationNight;
			initializeBukkitTask();
		} else {
			if(bukkitTask != null) {
				bukkitTask.cancel();
				bukkitTask = null;
				runnableTask = null;
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

	private void initializeBukkitTask() {
		if(bukkitTask == null) {
			runnableTask = () -> {
				world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
				long fullTime = world.getFullTime();
				int hoursDay = (int) (fullTime % 24000);

				// if night
				if(hoursDay >= BEGIN_NIGHT && hoursDay < (BEGIN_DAY == 0 ? 24000 : BEGIN_DAY)) {
					offset += auxTicksNight;
				} else { // else day
					offset += auxTicksDay;
				}

				if(offset >= 1) {
					world.setFullTime(fullTime + (int)offset);
					offset %= 1; // deixa apenas o float na variavel
				}
			};

			bukkitTask = Bukkit.getScheduler().runTaskTimer(TimeHandler.plugin, runnableTask, 0, 1);
		}
	}
	
}
