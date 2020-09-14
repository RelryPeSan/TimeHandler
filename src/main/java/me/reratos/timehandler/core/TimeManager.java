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
import me.reratos.timehandler.enums.ThunderEnum;
import me.reratos.timehandler.enums.TimeEnum;
import me.reratos.timehandler.enums.WeatherEnum;

public class TimeManager {
	
	private static Map<String, WorldManager> runnablesWorld = new LinkedHashMap<String, WorldManager>();

	public static boolean day(CommandSender sender, String worldName) {
		World w = Bukkit.getWorld(worldName);
		
		if(w != null) {
			day(sender, w);
		} else {
			TimeHandler.sendMessage(sender, "Este mundo não existe!");
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
			TimeHandler.sendMessage(sender, "Este mundo não existe!");
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


	private static void changeTime(CommandSender sender, World world, int time) {
		world.setTime(time);
		
		if(sender instanceof Player) {
			Player p = (Player) sender;
			TimeHandler.broadcastMessage("O player " + ChatColor.BLUE + p.getDisplayName() + 
					ChatColor.RESET + " alterou o tempo do mundo " + ChatColor.GREEN + world.getName() +
					ChatColor.RESET + " para " + time);
		} else {
			TimeHandler.broadcastMessage("O server alterou o tempo do mundo " + ChatColor.GREEN + 
					world.getName() + ChatColor.RESET + " para " + time);
		}
	}
	
	public static void initTask(World world) {
		WorldManager wm = runnablesWorld.get(world.getName());
		
		if(wm == null) {
			wm = new WorldManager(world);
			runnablesWorld.put(world.getName(), wm);
			
			Bukkit.getScheduler()
				.scheduleSyncRepeatingTask(TimeHandler.plugin, wm, 5 * 20, 10 * 20);
		} else {
			TimeHandler.sendMessage("Este mundo já possui uma instancia de gerenciamento aberta.");
			return;
		}
		
		MemorySection worldConfig = (MemorySection) TimeHandler.config.get("configWorld." + world.getName());
		LinkedHashMap<String, Object> list = (LinkedHashMap<String, Object>) worldConfig.getValues(true);

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
	}
	
	public static Map<String, WorldManager> getRunablesWorld() {
		return runnablesWorld;
	}
	
	
}
