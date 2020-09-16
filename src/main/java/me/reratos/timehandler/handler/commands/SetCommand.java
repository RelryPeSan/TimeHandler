package me.reratos.timehandler.handler.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import me.reratos.timehandler.TimeHandler;
import me.reratos.timehandler.core.TimeManager;
import me.reratos.timehandler.core.WorldManager;
import me.reratos.timehandler.enums.MoonPhasesEnum;
import me.reratos.timehandler.enums.ThunderEnum;
import me.reratos.timehandler.enums.TimeEnum;
import me.reratos.timehandler.enums.WeatherEnum;

public class SetCommand {

	private final static String enabled = "enabled";
	private final static String weather = "weather";
	private final static String thunder = "thunder";
	private final static String time = "time";
	private final static String timeFixed = "timeFixed";
	private final static String moonPhase = "moonPhase";

	private final static String optionDefault 	= "default";
	private final static String optionRain 		= "rain";
	private final static String optionCalm 		= "calm";
	private final static String optionNone 		= "none";
	private final static String optionAlways 	= "always";
	private final static String optionDay 		= "day";
	private final static String optionNight 	= "night";
	private final static String optionFixed 	= "fixed";
	
//	public static boolean commandSetWeather(CommandSender sender, String worldName, String property, String value) {
//		return true;
//	}
	
	public static boolean commandSetDefault(CommandSender sender, String worldName) {

		TimeHandler.config.set("configWorld." + worldName, null);
		TimeHandler.config.set("configWorld." + worldName + ".enabled", true);
		TimeHandler.config.set("configWorld." + worldName + ".weather", "default");
		TimeHandler.config.set("configWorld." + worldName + ".thunder", "default");
		TimeHandler.config.set("configWorld." + worldName + ".time", "default");
		TimeHandler.config.set("configWorld." + worldName + ".timeFixed", 1000);
		TimeHandler.config.set("configWorld." + worldName + ".moonPhase", "default");
		
		TimeHandler.plugin.saveConfig();
		
		TimeHandler.sendMessage(sender, ChatColor.YELLOW + "Default configuration created for the world: " + 
				ChatColor.GREEN + worldName);
		TimeManager.initTask(worldName);
		return true;
	}
	
	public static boolean commandSetBase(CommandSender sender, WorldManager worldManager, String property, String value) {
		boolean ret = false;
		
//		weather: default/rain/calm
//		thunder: default/none/always
//		time: 	 default/day/night/fixed
//		timeFixed: 	 0 - 24000
		switch (property) {
			case enabled:
				ret = commandSetEnabled(sender, worldManager, property, value);
				break;
			
			case weather:
				ret = commandSetWeather(sender, worldManager, property, value);
				break;
				
			case thunder:
				ret = commandSetThunder(sender, worldManager, property, value);
				break;
				
			case time:
				ret = commandSetTime(sender, worldManager, property, value);
				break;
				
			case timeFixed:
				ret = commandSetTimeFixed(sender, worldManager, property, value);
				break;
				
			case moonPhase:
				ret = commandSetMoonPhase(sender, worldManager, property, value);
				break;
	
			default:
				return false;
		}

		if(ret) {
			TimeHandler.sendMessage(sender, "The '" + ChatColor.AQUA + property + ChatColor.RESET + 
					"' property changed to: '" + ChatColor.LIGHT_PURPLE + value + ChatColor.RESET + "'");
			TimeHandler.plugin.saveConfig();
		}
		
		return ret;
	}
	
	public static boolean commandSetTime(CommandSender sender, WorldManager worldManager, String property, String value) {
		switch (value) {
			case optionDefault:
			case optionDay:
			case optionNight:
			case optionFixed:
				configSetValue(worldManager, property, value);
				worldManager.setTime(TimeEnum.getEnumPorValue(value));
				return true;
			default:
				messageValorInvalido(sender, property, value);
				return false;
		}
	}

	public static boolean commandSetTimeFixed(CommandSender sender, WorldManager worldManager, String property, String value) {
		try {
			int tempo = Integer.parseInt(value);
			if(tempo >= 0 && tempo <= 24000) {
				configSetValue(worldManager, property, Integer.parseInt(value));
				worldManager.setTimeFixed(tempo);
			} else {
				throw new NumberFormatException();
			}
			return true;
			
		} catch (NumberFormatException e) {
			messageValorInvalido(sender, property, value, " (0 - 24000)");
			return false;
		}
	}

	public static boolean commandSetMoonPhase(CommandSender sender, WorldManager worldManager, String property, String value) {
		for(String phase : MoonPhasesEnum.getList()) {
			if(phase.toLowerCase().equals(value.toLowerCase())) {
				configSetValue(worldManager, property, phase);
				worldManager.setMoonPhase(MoonPhasesEnum.getEnumPorValue(phase));
				return true;
			}
		}
		messageValorInvalido(sender, property, value);
		return false;
	}
	
	public static boolean commandSetThunder(CommandSender sender, WorldManager worldManager, String property, String value) {
		switch (value) {
			case optionDefault:
			case optionNone:
			case optionAlways:
				configSetValue(worldManager, property, value);
				worldManager.setThunder(ThunderEnum.getEnumPorValue(value));
				return true;
			default:
				messageValorInvalido(sender, property, value);
				return false;
		}
	}
	
	public static boolean commandSetWeather(CommandSender sender, WorldManager worldManager, String property, String value) {
		switch (value) {
			case optionDefault:
			case optionRain:
			case optionCalm:
				configSetValue(worldManager, property, value);
				worldManager.setWeather(WeatherEnum.getEnumPorValue(value));
				return true;
			default:
				messageValorInvalido(sender, property, value);
				return false;
		}
	}
	
	public static boolean commandSetEnabled(CommandSender sender, WorldManager worldManager, String property, String value) {
		switch (value) {
			case "true":
			case "false":
				configSetValue(worldManager, property, Boolean.parseBoolean(value));
				worldManager.setEnabled(Boolean.parseBoolean(value));
				return true;
			default:
				messageValorInvalido(sender, property, value);
				return false;
		}
	}

	private static void messageValorInvalido(CommandSender sender, String property, String value) {
		messageValorInvalido(sender, property, value, "");
	}
	
	private static void messageValorInvalido(CommandSender sender, String property, String value, String sufix) {
		TimeHandler.sendMessage(sender, "The value '" + ChatColor.RED + value + ChatColor.RESET + 
				"' is invalid for property '" + ChatColor.AQUA + property + ChatColor.RESET + "'. " + 
				ChatColor.YELLOW + sufix + ChatColor.RESET);
	}
	
	private static void configSetValue(WorldManager worldManager, String property, Object value) {
		TimeHandler.config.set("configWorld." + worldManager.getWorld().getName() + "." + property, value);
	}
}
