package me.reratos.timehandler.handler.commands;

import org.bukkit.command.CommandSender;

import me.reratos.timehandler.TimeHandler;
import me.reratos.timehandler.core.TimeManager;
import me.reratos.timehandler.core.WorldManager;
import me.reratos.timehandler.enums.EnabledEnum;
import me.reratos.timehandler.enums.MoonPhasesEnum;
import me.reratos.timehandler.enums.ThunderEnum;
import me.reratos.timehandler.enums.TimeEnum;
import me.reratos.timehandler.enums.WeatherEnum;
import me.reratos.timehandler.utils.Constants;
import me.reratos.timehandler.utils.ConstantsWorldsConfig;
import me.reratos.timehandler.utils.LocaleLoader;
import me.reratos.timehandler.utils.Messages;

public class SetCommand {

	public static boolean commandSetDefault(CommandSender sender, String worldName) {

		TimeHandler.worldsConfig.set(Constants.WORLDS_DOT + worldName, null);
		TimeHandler.worldsConfig.set(Constants.WORLDS_DOT + worldName + Constants.DOT_ENABLED, true);
		TimeHandler.worldsConfig.set(Constants.WORLDS_DOT + worldName + Constants.DOT_WEATHER, Constants.DEFAULT);
		TimeHandler.worldsConfig.set(Constants.WORLDS_DOT + worldName + Constants.DOT_THUNDER, Constants.DEFAULT);
		TimeHandler.worldsConfig.set(Constants.WORLDS_DOT + worldName + Constants.DOT_TIME, Constants.DEFAULT);
		TimeHandler.worldsConfig.set(Constants.WORLDS_DOT + worldName + Constants.DOT_TIME_FIXED, 1000);
		TimeHandler.worldsConfig.set(Constants.WORLDS_DOT + worldName + Constants.DOT_DURATION_DAY, 14000);
		TimeHandler.worldsConfig.set(Constants.WORLDS_DOT + worldName + Constants.DOT_DURATION_NIGHT, 10000);
		TimeHandler.worldsConfig.set(Constants.WORLDS_DOT + worldName + Constants.DOT_MOON_PHASE, Constants.DEFAULT);
		
		TimeHandler.plugin.saveWorldsConfig();
		
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
			case ConstantsWorldsConfig.ENABLED:
				ret = commandSetEnabled(sender, worldManager, value);
				break;
			
			case ConstantsWorldsConfig.WEATHER:
				ret = commandSetWeather(sender, worldManager, value);
				break;
				
			case ConstantsWorldsConfig.THUNDER:
				ret = commandSetThunder(sender, worldManager, value);
				break;
				
			case ConstantsWorldsConfig.TIME:
				ret = commandSetTime(sender, worldManager, value);
				break;
				
			case ConstantsWorldsConfig.TIME_FIXED:
				ret = commandSetTimeFixed(sender, worldManager, value);
				break;
				
			case ConstantsWorldsConfig.DURATION_DAY:
				ret = commandSetDurationDay(sender, worldManager, value);
				break;
				
			case ConstantsWorldsConfig.DURATION_NIGHT:
				ret = commandSetDurationNight(sender, worldManager, value);
				break;
				
			case ConstantsWorldsConfig.MOON_PHASE:
				ret = commandSetMoonPhase(sender, worldManager, value);
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
	
	public static boolean commandSetTime(CommandSender sender, WorldManager worldManager, String value) {
		TimeEnum t = TimeEnum.getEnumPorValue(value);
		
		if(t != null) {
			configSetValue(worldManager, ConstantsWorldsConfig.TIME, value);
			worldManager.setTime(t);
			return true;
		} else {
			messageValorInvalido(sender, ConstantsWorldsConfig.TIME, value);
			return false;
		}
		
	}

	public static boolean commandSetTimeFixed(CommandSender sender, WorldManager worldManager, String value) {
		try {
			int tempo = Integer.parseInt(value);
			if(tempo >= 0 && tempo <= 24000) {
				configSetValue(worldManager, ConstantsWorldsConfig.TIME_FIXED, Integer.parseInt(value));
				worldManager.setTimeFixed(tempo);
			} else {
				throw new NumberFormatException();
			}
			return true;
			
		} catch (NumberFormatException e) {
			messageValorInvalido(sender, ConstantsWorldsConfig.TIME_FIXED, value, " (0 - 24000)");
			return false;
		}
	}

	public static boolean commandSetDurationDay(CommandSender sender, WorldManager worldManager, String value) {
		try {
			int tempo = Integer.parseInt(value);
			if(tempo >= (worldManager.getDurationDefaultDay() / 10) && tempo <= (worldManager.getDurationDefaultDay() * 10)) {
				configSetValue(worldManager, ConstantsWorldsConfig.DURATION_DAY, Integer.parseInt(value));
				worldManager.setDurationDay(tempo);
			} else {
				throw new NumberFormatException();
			}
			return true;
			
		} catch (NumberFormatException e) {
			messageValorInvalido(sender, ConstantsWorldsConfig.DURATION_DAY, value, " (" +
					(int)(worldManager.getDurationDefaultDay() / 10) + 
					" - " + (int)(worldManager.getDurationDefaultDay() * 10) + ")");
			return false;
		}
	}

	public static boolean commandSetDurationNight(CommandSender sender, WorldManager worldManager, String value) {
		try {
			int tempo = Integer.parseInt(value);
			if(tempo >= (worldManager.getDurationDefaultNight() / 10) && tempo <= (worldManager.getDurationDefaultNight() * 10)) {
				configSetValue(worldManager, ConstantsWorldsConfig.DURATION_NIGHT, Integer.parseInt(value));
				worldManager.setDurationNight(tempo);
			} else {
				throw new NumberFormatException();
			}
			return true;
			
		} catch (NumberFormatException e) {
			messageValorInvalido(sender, ConstantsWorldsConfig.DURATION_NIGHT, value, " (" +
					(int)(worldManager.getDurationDefaultNight() / 10) + 
					" - " + (int)(worldManager.getDurationDefaultNight() * 10) + ")");
			return false;
		}
	}

	public static boolean commandSetMoonPhase(CommandSender sender, WorldManager worldManager, String value) {
		MoonPhasesEnum mp = MoonPhasesEnum.getEnumPorValue(value);
		
		if(mp != null) {
			configSetValue(worldManager, ConstantsWorldsConfig.MOON_PHASE, value);
			worldManager.setMoonPhase(mp);
			return true;
		} else {
			messageValorInvalido(sender, ConstantsWorldsConfig.MOON_PHASE, value);
			return false;
		}
	}
	
	public static boolean commandSetThunder(CommandSender sender, WorldManager worldManager, String value) {
		ThunderEnum t = ThunderEnum.getEnumPorValue(value);
		
		if(t != null) {
			configSetValue(worldManager, ConstantsWorldsConfig.THUNDER, value);
			worldManager.setThunder(t);
			return true;
		} else {
			messageValorInvalido(sender, ConstantsWorldsConfig.THUNDER, value);
			return false;
		}
	}
	
	public static boolean commandSetWeather(CommandSender sender, WorldManager worldManager, String value) {
		WeatherEnum w = WeatherEnum.getEnumPorValue(value);
		
		if(w != null) {
			configSetValue(worldManager, ConstantsWorldsConfig.WEATHER, value);
			worldManager.setWeather(w);
			return true;
		} else {
			messageValorInvalido(sender, ConstantsWorldsConfig.WEATHER, value);
			return false;
		}
	}
	
	public static boolean commandSetEnabled(CommandSender sender, WorldManager worldManager, String value) {
		EnabledEnum e = EnabledEnum.getEnumPorValue(value);
		
		if(e != null) {
			configSetValue(worldManager, ConstantsWorldsConfig.ENABLED, Boolean.parseBoolean(value));
			worldManager.setEnabled(Boolean.parseBoolean(value));
			return true;
		} else {
			messageValorInvalido(sender, ConstantsWorldsConfig.ENABLED, value);
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
