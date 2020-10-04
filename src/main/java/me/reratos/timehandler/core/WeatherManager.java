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
		return rain(sender, worldName, null);
	}
	
	public static boolean rain(CommandSender sender, String worldName, String duration) {
		World w = Bukkit.getWorld(worldName);
		
		if(w != null) {
			return rain(sender, w, duration);
		} else {
			TimeHandler.sendMessageLogo(sender, LocaleLoader.getString(Messages.WORLD_NOT_EXIST, worldName));
		}
		return true;
	}

	public static boolean rain(CommandSender sender, World world) {
		return rain(sender, world, null);
	}
	
	public static boolean rain(CommandSender sender, World world, String duration) {
		Integer d = null;
		
		if(duration != null) {
			try {
				d = Integer.parseInt(duration);;
			} catch (NumberFormatException e) {
				return false;
			}			
		}
		
		world.setStorm(true);
		world.setThundering(false);
		TimeHandler.sendMessageLogo(sender, LocaleLoader.getString(Messages.WEATHER_SET_FOR, getClimaAtual(world)));
		
		if(duration != null) {
			world.setWeatherDuration(d);
		}
		return true;
	}

	public static boolean thundering(CommandSender sender, String worldName) {
		return thundering(sender, worldName, null);
	}
	
	public static boolean thundering(CommandSender sender, String worldName, String duration) {
		World w = Bukkit.getWorld(worldName);
		
		if(w != null) {
			return thundering(sender, w, duration);
		} else {
			TimeHandler.sendMessageLogo(sender, LocaleLoader.getString(Messages.WORLD_NOT_EXIST, worldName));
		}
		return true;
	}

	public static boolean thundering(CommandSender sender, World world) {
		return thundering(sender, world, null);
	}

	public static boolean thundering(CommandSender sender, World world, String duration) {
		Integer d = null;
		
		if(duration != null) {
			try {
				d = Integer.parseInt(duration);;
			} catch (NumberFormatException e) {
				return false;
			}			
		}
		
		world.setStorm(true);
		world.setThundering(true);
		TimeHandler.sendMessageLogo(sender, LocaleLoader.getString(Messages.WEATHER_SET_FOR, getClimaAtual(world)));

		if(duration != null) {
			world.setThunderDuration(d);
			world.setWeatherDuration(d);
		}
		return true;
	}

	public static boolean calm(CommandSender sender, String worldName) {
		return calm(sender, worldName, null);
	}

	public static boolean calm(CommandSender sender, String worldName, String duration) {
		World w = Bukkit.getWorld(worldName);
		
		if(w != null) {
			return calm(sender, w, duration);
		} else {
			TimeHandler.sendMessageLogo(sender, LocaleLoader.getString(Messages.WORLD_NOT_EXIST, worldName));
		}
		return true;
	}

	public static boolean calm(CommandSender sender, World world) {
		return calm(sender, world, null);
	}

	public static boolean calm(CommandSender sender, World world, String duration) {
		Integer d = null;
		
		if(duration != null) {
			try {
				d = Integer.parseInt(duration);;
			} catch (NumberFormatException e) {
				return false;
			}			
		}
		
		world.setStorm(false);
		world.setThundering(false);
		TimeHandler.sendMessageLogo(sender, LocaleLoader.getString(Messages.WEATHER_SET_FOR, getClimaAtual(world)));

		if(duration != null) {
			world.setWeatherDuration(d);
		}
		return true;
	}
	
	public static String getClimaAtual(World world) {
		return (world.hasStorm() ? (world.isThundering() ? 
				ChatColor.YELLOW + LocaleLoader.getString(Messages.WEATHER_THUNDERSTORM) : 
					ChatColor.BLUE + LocaleLoader.getString(Messages.WEATHER_RAIN) ) : 
						ChatColor.WHITE + LocaleLoader.getString(Messages.WEATHER_CALM)) + ChatColor.RESET;
	}
}
