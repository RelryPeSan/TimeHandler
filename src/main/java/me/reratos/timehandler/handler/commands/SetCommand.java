package me.reratos.timehandler.handler.commands;

import org.bukkit.command.CommandSender;

import me.reratos.timehandler.TimeHandler;
import me.reratos.timehandler.core.TimeManager;
import me.reratos.timehandler.core.WorldManager;
import me.reratos.timehandler.enums.MoonPhasesEnum;
import me.reratos.timehandler.enums.ThunderEnum;
import me.reratos.timehandler.enums.TimeEnum;
import me.reratos.timehandler.enums.WeatherEnum;
import me.reratos.timehandler.utils.Constants;
import me.reratos.timehandler.utils.LocaleLoader;
import me.reratos.timehandler.utils.Messages;

public class SetCommand {

	private final static String enabled = 	"enabled";
	private final static String weather = 	"weather";
	private final static String thunder = 	"thunder";
	private final static String time = 		"time";
	private final static String timeFixed = "timeFixed";
	private final static String durationDay = 	"durationDay";
	private final static String durationNight = "durationNight";
	private final static String moonPhase = 	"moonPhase";

	private final static String optionDefault 	= "default";
	private final static String optionRain 		= "rain";
	private final static String optionCalm 		= "calm";
	private final static String optionNone 		= "none";
	private final static String optionAlways 	= "always";
	private final static String optionDay 		= "day";
	private final static String optionNight 	= "night";
	private final static String optionFixed 	= "fixed";
	private final static String optionConfigured 	= "configured";
	
//	public static boolean commandSetWeather(CommandSender sender, String worldName, String property, String value) {
//		return true;
//	}
	
	public static boolean commandSetDefault(CommandSender sender, String worldName) {

		TimeHandler.worldsConfig.set(Constants.WORLDS_DOT + worldName, null);
		TimeHandler.worldsConfig.set(Constants.WORLDS_DOT + worldName + ".enabled", true);
		TimeHandler.worldsConfig.set(Constants.WORLDS_DOT + worldName + ".weather", Constants.DEFAULT);
		TimeHandler.worldsConfig.set(Constants.WORLDS_DOT + worldName + ".thunder", Constants.DEFAULT);
		TimeHandler.worldsConfig.set(Constants.WORLDS_DOT + worldName + ".time", Constants.DEFAULT);
		TimeHandler.worldsConfig.set(Constants.WORLDS_DOT + worldName + ".timeFixed", 1000);
		TimeHandler.worldsConfig.set(Constants.WORLDS_DOT + worldName + ".durationDay", 14000);
		TimeHandler.worldsConfig.set(Constants.WORLDS_DOT + worldName + ".durationNight", 10000);
		TimeHandler.worldsConfig.set(Constants.WORLDS_DOT + worldName + ".moonPhase", Constants.DEFAULT);
		
		TimeHandler.plugin.saveWorldsConfig();
		
//		TimeHandler.sendMessage(sender, ChatColor.YELLOW + "Default configuration created for the world: " + 
//				ChatColor.GREEN + worldName);
		sender.sendMessage(LocaleLoader.getString(Messages.COMMAND_SET_DEFAULT_CONFIGURATION, worldName));
		TimeManager.initTask(worldName);
		return true;
	}
	
	public static boolean commandSetBase(CommandSender sender, WorldManager worldManager, String property, String value) {
		boolean ret = false;
		
//		weather: default/rain/calm
//		thunder: default/none/always
//		time: 	 default/day/night/fixed/configured
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
				
			case durationDay:
				ret = commandSetDurationDay(sender, worldManager, property, value);
				break;
				
			case durationNight:
				ret = commandSetDurationNight(sender, worldManager, property, value);
				break;
				
			case moonPhase:
				ret = commandSetMoonPhase(sender, worldManager, property, value);
				break;
	
			default:
				return false;
		}

		if(ret) {
			sender.sendMessage(LocaleLoader.getString(Messages.COMMAND_SET_PROPERTY_CHANGED, property, value));
			TimeHandler.plugin.saveWorldsConfig();
		}
		
		return true;
	}
	
	public static boolean commandSetTime(CommandSender sender, WorldManager worldManager, String property, String value) {
		switch (value) {
			case optionDefault:
			case optionDay:
			case optionNight:
			case optionFixed:
			case optionConfigured:
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

	public static boolean commandSetDurationDay(CommandSender sender, WorldManager worldManager, String property, String value) {
		try {
			int tempo = Integer.parseInt(value);
			if(tempo >= (worldManager.getDurationDefaultDay() / 10) && tempo <= (worldManager.getDurationDefaultDay() * 10)) {
				configSetValue(worldManager, property, Integer.parseInt(value));
				worldManager.setDurationDay(tempo);
			} else {
				throw new NumberFormatException();
			}
			return true;
			
		} catch (NumberFormatException e) {
			messageValorInvalido(sender, property, value, " (" + 
					(int)(worldManager.getDurationDefaultDay() / 10) + 
					" - " + (int)(worldManager.getDurationDefaultDay() * 10) + ")");
			return false;
		}
	}

	public static boolean commandSetDurationNight(CommandSender sender, WorldManager worldManager, String property, String value) {
		try {
			int tempo = Integer.parseInt(value);
			if(tempo >= (worldManager.getDurationDefaultNight() / 10) && tempo <= (worldManager.getDurationDefaultNight() * 10)) {
				configSetValue(worldManager, property, Integer.parseInt(value));
				worldManager.setDurationNight(tempo);
			} else {
				throw new NumberFormatException();
			}
			return true;
			
		} catch (NumberFormatException e) {
			messageValorInvalido(sender, property, value, " (" + 
					(int)(worldManager.getDurationDefaultNight() / 10) + 
					" - " + (int)(worldManager.getDurationDefaultNight() * 10) + ")");
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
		sender.sendMessage(LocaleLoader.getString(Messages.COMMAND_SET_INVALID_PROPERTY_VALUE, property, value, sufix)	);
	}
	
	private static void configSetValue(WorldManager worldManager, String property, Object value) {
		TimeHandler.worldsConfig.set(Constants.WORLDS_DOT + worldManager.getWorld().getName() + "." + property, value);
	}
}
