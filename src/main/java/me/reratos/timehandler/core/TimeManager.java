package me.reratos.timehandler.core;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

import me.reratos.timehandler.TimeHandler;
import me.reratos.timehandler.enums.MoonPhasesEnum;
import me.reratos.timehandler.enums.ThunderEnum;
import me.reratos.timehandler.enums.TimeEnum;
import me.reratos.timehandler.enums.WeatherEnum;
import me.reratos.timehandler.utils.Constants;
import me.reratos.timehandler.utils.ConstantsConfig;
import me.reratos.timehandler.utils.ConstantsWorldsConfig;
import me.reratos.timehandler.utils.LocaleLoader;
import me.reratos.timehandler.utils.Messages;

public class TimeManager {
	
	private static Map<String, WorldManager> runnablesWorld = new LinkedHashMap<String, WorldManager>();

	public static boolean day(CommandSender sender, String worldName) {
		World w = Bukkit.getWorld(worldName);
		
		if(w != null) {
			day(sender, w);
		} else {
			TimeHandler.sendMessageLogo(sender, LocaleLoader.getString(Messages.WORLD_NOT_EXIST, worldName));
		}
		
		return true;
	}

	public static boolean day(CommandSender sender, World world) {
		try {
			changeTime(sender, world, 500);		
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}

	public static boolean night(CommandSender sender, String worldName) {
		World w = Bukkit.getWorld(worldName);
		
		if(w != null) {
			night(sender, w);
		} else {
			TimeHandler.sendMessageLogo(sender, LocaleLoader.getString(Messages.WORLD_NOT_EXIST, worldName));
		}
		
		return true;
	}
	
	public static boolean night(CommandSender sender, World world) {
		try {
			changeTime(sender, world, 14000);			
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}

	public static boolean moonPhase(CommandSender sender, MoonPhasesEnum moonPhase, String worldName) {
		World w = Bukkit.getWorld(worldName);
		
		if(w != null) {
			moonPhase(sender, moonPhase, w);
		} else {
			TimeHandler.sendMessageLogo(sender, LocaleLoader.getString(Messages.WORLD_NOT_EXIST, worldName));
		}
		
		return true;
	}
	
	public static boolean moonPhase(CommandSender sender, MoonPhasesEnum moonPhase, World world) {
		try {
			if(moonPhase == MoonPhasesEnum.DEFAULT) return false;
			
			long fullTime = world.getFullTime();
			long days = fullTime / 24000;
        	int currentPhase = (int) (days % 8);
        	int targetPhase = moonPhase.ordinal();
        	int timeSkip = (targetPhase < currentPhase ? (targetPhase + 8) - currentPhase : targetPhase - currentPhase);
        	
        	if(timeSkip != 0) {
        		world.setFullTime(fullTime + (timeSkip * 24000));
        	}
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}


	private static void changeTime(CommandSender sender, World world, int time) {
		world.setTime(time);
		
		if(sender instanceof Player) {
			Player p = (Player) sender;
			TimeHandler.broadcastMessage(LocaleLoader.getString(Messages.WEATHER_PLAYER_SET_FOR, p.getDisplayName(), 
					world.getName(), time));
		} else {
			TimeHandler.broadcastMessage(LocaleLoader.getString(Messages.WEATHER_PLAYER_SET_FOR, world.getName(), time));
		}
	}
	
	public static void initTask(String worldName) {
		WorldManager wm = runnablesWorld.get(worldName);
		
		if(wm == null) {
			Object objDay = TimeHandler.config.get(ConstantsConfig.DAY_BEGIN_IN);
			Object objNight = TimeHandler.config.get(ConstantsConfig.NIGHT_BEGIN_IN);
			
			if(objDay != null && objNight != null && 
					(((int) objDay >= 22000 	&& (int) objDay <= 23999 ) || (int) objDay == 0 ) && 
					( (int) objNight >= 12000 	&& (int) objNight <= 14000 ) ) {
				int beginDay = (int) objDay;
				int beginNight = (int) objNight;
				
				wm = new WorldManager(worldName, beginDay, beginNight);
			} else {
				wm = new WorldManager(worldName);
			}
			
			runnablesWorld.put(worldName, wm);
			
			Bukkit.getScheduler().runTaskTimer(TimeHandler.plugin, wm, 
					TimeHandler.config.getInt(ConstantsConfig.TICKS_CHECK_UPDATE_WORLDS), 
					TimeHandler.config.getInt(ConstantsConfig.TICKS_CHECK_UPDATE_WORLDS));
		} else {
			TimeHandler.sendMessage(LocaleLoader.getString(Messages.WORLD_OPEN_INSTANCE_MANAGEMENT));
			return;
		}

		MemorySection worldsConfig = (MemorySection) TimeHandler.worldsConfig.get(Constants.WORLDS_DOT + worldName);
		LinkedHashMap<String, Object> list = (LinkedHashMap<String, Object>) worldsConfig.getValues(true);

		Object objAux;

		objAux = list.get(ConstantsWorldsConfig.ENABLED);
		wm.setEnabled(objAux == null ? false : objAux.equals(true));
		
		objAux = list.get(ConstantsWorldsConfig.THUNDER);
		try {
			wm.setThunder(ThunderEnum.getEnumPorValue((String) objAux));
		} catch (Exception e) {
			wm.setThunder(ThunderEnum.DEFAULT);
		}
		
		objAux = list.get(ConstantsWorldsConfig.TIME);
		try {
			wm.setTime(TimeEnum.getEnumPorValue(objAux.toString()));
		} catch (Exception e) {
			wm.setTime(TimeEnum.DEFAULT);
		}
		
		objAux = list.get(ConstantsWorldsConfig.TIME_FIXED);
		try {
			wm.setTimeFixed((int) objAux);
		} catch (Exception e) {
			wm.setTimeFixed(0);
		}
		
		objAux = list.get(ConstantsWorldsConfig.WEATHER);
		try {
			wm.setWeather(WeatherEnum.getEnumPorValue(objAux.toString()));
		} catch (Exception e) {
			wm.setWeather(WeatherEnum.DEFAULT);
		}
		
		objAux = list.get(ConstantsWorldsConfig.MOON_PHASE);
		try {
			wm.setMoonPhase(MoonPhasesEnum.DEFAULT);
			for(MoonPhasesEnum moon : MoonPhasesEnum.values()) {
				if(moon.getValue().toLowerCase().equals(((String) objAux).toLowerCase())) {
					wm.setMoonPhase(moon);
					break;
				}
			}
		} catch (Exception e) {
			wm.setMoonPhase(MoonPhasesEnum.DEFAULT);
		}
		
		objAux = list.get(ConstantsWorldsConfig.DURATION_DAY);
		try {
			wm.setDurationDay((int) objAux);
		} catch (Exception e) {
			wm.setDurationDay(14000);
		}
		
		objAux = list.get(ConstantsWorldsConfig.DURATION_NIGHT);
		try {
			wm.setDurationNight((int) objAux);
		} catch (Exception e) {
			wm.setDurationNight(10000);
		}
	}
	
	public static void finalizeTask() {
		runnablesWorld = new LinkedHashMap<String, WorldManager>();
	}
	
	public static Map<String, WorldManager> getRunablesWorld() {
		return runnablesWorld;
	}
	
	
}
