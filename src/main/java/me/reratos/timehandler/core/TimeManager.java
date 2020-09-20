package me.reratos.timehandler.core;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

import me.reratos.timehandler.TimeHandler;
import me.reratos.timehandler.enums.MoonPhasesEnum;
import me.reratos.timehandler.enums.ThunderEnum;
import me.reratos.timehandler.enums.TimeEnum;
import me.reratos.timehandler.enums.WeatherEnum;
import me.reratos.timehandler.utils.ConstantsConfig;

public class TimeManager {
	
	private static Map<String, WorldManager> runnablesWorld = new LinkedHashMap<String, WorldManager>();

	public static boolean day(CommandSender sender, String worldName) {
		World w = Bukkit.getWorld(worldName);
		
		if(w != null) {
			day(sender, w);
		} else {
			TimeHandler.sendMessage(sender, "This world does not exist!");
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
			TimeHandler.sendMessage(sender, "This world does not exist!");
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
			TimeHandler.sendMessage(sender, "This world does not exist!");
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
			TimeHandler.broadcastMessage("The player " + ChatColor.BLUE + p.getDisplayName() + 
					ChatColor.RESET + " changed the world time " + ChatColor.GREEN + world.getName() +
					ChatColor.RESET + " to " + time);
		} else {
			TimeHandler.broadcastMessage("The server changed the world time " + ChatColor.GREEN + 
					world.getName() + ChatColor.RESET + " to " + time);
		}
	}
	
	public static void initTask(String worldName) {
		WorldManager wm = runnablesWorld.get(worldName);
		
		if(wm == null) {
			wm = new WorldManager(worldName);
			runnablesWorld.put(worldName, wm);
			
			Bukkit.getScheduler()
				.scheduleSyncRepeatingTask(TimeHandler.plugin, wm, 
						TimeHandler.config.getInt(ConstantsConfig.TICKS_CHECK_UPDATE_WORLDS), 
						TimeHandler.config.getInt(ConstantsConfig.TICKS_CHECK_UPDATE_WORLDS));
		} else {
			TimeHandler.sendMessage("This world already has an open management instance.");
			return;
		}

		MemorySection worldsConfig = (MemorySection) TimeHandler.worldsConfig.get("worlds." + worldName);
		LinkedHashMap<String, Object> list = (LinkedHashMap<String, Object>) worldsConfig.getValues(true);

		Object objAux;
		
		objAux = list.get("enabled");
		wm.setEnabled(objAux == null ? false : objAux.equals(true));
		
		objAux = list.get("thunder");
		try {
			wm.setThunder(ThunderEnum.getEnumPorValue((String) objAux));
		} catch (Exception e) {
			wm.setThunder(ThunderEnum.DEFAULT);
		}
		
		objAux = list.get("time");
		try {
			wm.setTime(TimeEnum.getEnumPorValue(objAux.toString()));
		} catch (Exception e) {
			wm.setTime(TimeEnum.DEFAULT);
		}
		
		objAux = list.get("timeFixed");
		try {
			wm.setTimeFixed((int) objAux);
		} catch (Exception e) {
			wm.setTimeFixed(0);
		}
		
		objAux = list.get("weather");
		try {
			wm.setWeather(WeatherEnum.getEnumPorValue(objAux.toString()));
		} catch (Exception e) {
			wm.setWeather(WeatherEnum.DEFAULT);
		}
		
		objAux = list.get("moonPhase");
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
		
		objAux = list.get("durationDay");
		try {
			wm.setDurationDay((int) objAux);
		} catch (Exception e) {
			wm.setDurationDay(14000);
		}
		
		objAux = list.get("durationNight");
		try {
			wm.setDurationNight((int) objAux);
		} catch (Exception e) {
			wm.setDurationNight(8000);
		}
	}
	
	public static void finalizeTask() {
		runnablesWorld = new LinkedHashMap<String, WorldManager>();
	}
	
	public static Map<String, WorldManager> getRunablesWorld() {
		return runnablesWorld;
	}
	
	
}
