package me.reratos.timehandler.core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import me.reratos.timehandler.TimeHandler;
import me.reratos.timehandler.utils.LocaleLoader;
import me.reratos.timehandler.utils.Messages;

public class WeatherManager {

	public static boolean rain(CommandSender sender, String worldName) {
		World w = Bukkit.getWorld(worldName);
		
		if(w != null) {
			rain(sender, w);
		} else {
			TimeHandler.sendMessageLogo(sender, LocaleLoader.getString(Messages.WORLD_NOT_EXIST, worldName));
		}
		return true;
	}

	public static boolean rain(CommandSender sender, World world) {
		world.setStorm(true);
		world.setThundering(false);
		TimeHandler.sendMessageLogo(sender, LocaleLoader.getString(Messages.WEATHER_SET_FOR, getClimaAtual(world)));
		
		return true;
	}

	public static boolean thundering(CommandSender sender, String worldName) {
		World w = Bukkit.getWorld(worldName);
		
		if(w != null) {
			rain(sender, w);
		} else {
			TimeHandler.sendMessageLogo(sender, LocaleLoader.getString(Messages.WORLD_NOT_EXIST, worldName));
		}
		return true;
	}

	public static boolean thundering(CommandSender sender, World world) {
		world.setStorm(true);
		world.setThundering(true);
		TimeHandler.sendMessageLogo(sender, LocaleLoader.getString(Messages.WEATHER_SET_FOR, getClimaAtual(world)));
		
		return true;
	}

	public static boolean calm(CommandSender sender, String worldName) {
		World w = Bukkit.getWorld(worldName);
		
		if(w != null) {
			calm(sender, w);
		} else {
			TimeHandler.sendMessageLogo(sender, LocaleLoader.getString(Messages.WORLD_NOT_EXIST, worldName));
		}
		return true;
	}

	public static boolean calm(CommandSender sender, World world) {
		world.setStorm(false);
		world.setThundering(false);
		TimeHandler.sendMessageLogo(sender, LocaleLoader.getString(Messages.WEATHER_SET_FOR, getClimaAtual(world)));
		
		return true;
	}
	
	public static String getClimaAtual(World world) {
		return (world.hasStorm() ? (world.isThundering() ? 
				ChatColor.YELLOW + LocaleLoader.getString(Messages.WEATHER_THUNDERSTORM) : 
					ChatColor.BLUE + LocaleLoader.getString(Messages.WEATHER_RAIN) ) : 
						ChatColor.WHITE + LocaleLoader.getString(Messages.WEATHER_CALM)) + ChatColor.RESET;
	}
}
