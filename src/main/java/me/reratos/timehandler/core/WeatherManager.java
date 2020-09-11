package me.reratos.timehandler.core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import me.reratos.timehandler.TimeHandler;

public class WeatherManager {

	public static boolean rain(CommandSender sender, String worldName) {
		World w = Bukkit.getWorld(worldName);
		
		if(w != null) {
			rain(sender, w);
		} else {
			TimeHandler.sendMessage(sender, "Este mundo não existe!");
		}
		return true;
	}

	public static boolean rain(CommandSender sender, World world) {
		world.setStorm(true);
		world.setThundering(false);
		TimeHandler.sendMessage(sender, "Clima definido para: " + getClimaAtual(world));
//		if(sender instanceof Player) {
//			Player p = (Player) sender;
//		} else {
//			
//		}
//		world.setWeatherDuration(Integer.MAX_VALUE);
		
		return true;
	}

	public static boolean thundering(CommandSender sender, String worldName) {
		World w = Bukkit.getWorld(worldName);
		
		if(w != null) {
			rain(sender, w);
		} else {
			TimeHandler.sendMessage(sender, "Este mundo não existe!");
		}
		return true;
	}

	public static boolean thundering(CommandSender sender, World world) {
		world.setStorm(true);
		world.setThundering(true);
		TimeHandler.sendMessage(sender, "Clima definido para: " + getClimaAtual(world));
//		if(sender instanceof Player) {
//			Player p = (Player) sender;
//		} else {
//			
//		}
//		world.setWeatherDuration(Integer.MAX_VALUE);
		
		return true;
	}

	public static boolean calm(CommandSender sender, String worldName) {
		World w = Bukkit.getWorld(worldName);
		
		if(w != null) {
			calm(sender, w);
		} else {
			TimeHandler.sendMessage(sender, "Este mundo não existe!");
		}
		return true;
	}

	public static boolean calm(CommandSender sender, World world) {
//		if(sender instanceof Player) {
//			Player p = (Player) sender;
//			p.setPlayerWeather(WeatherType.CLEAR);
//		}
		world.setStorm(false);
		world.setThundering(false);
		TimeHandler.sendMessage(sender, "Clima definido para: " + getClimaAtual(world));
		
		return true;
	}
	
	public static String getClimaAtual(World world) {
		return (world.isThundering() ? ChatColor.YELLOW + "Thundering" : 
    		(world.hasStorm() ? ChatColor.BLUE + "Storm" : ChatColor.WHITE + "Clean")) + ChatColor.RESET;
	}
}
