package me.reratos.timehandler.handler.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;

import me.reratos.timehandler.TimeHandler;
import me.reratos.timehandler.core.TimeManager;
import me.reratos.timehandler.core.WeatherManager;
import me.reratos.timehandler.core.WorldManager;
import me.reratos.timehandler.enums.MoonPhasesEnum;
import me.reratos.timehandler.enums.TimeEnum;
import me.reratos.timehandler.enums.WeatherEnum;
import me.reratos.timehandler.utils.Constants;
import me.reratos.timehandler.utils.LocaleLoader;
import me.reratos.timehandler.utils.Messages;

public class InfoCommand {

	public static boolean commandInfoBase(CommandSender sender, World world) {
		String worldName = world.getName();
		
		MemorySection worldsConfigMS = (MemorySection) TimeHandler.worldsConfig.get(Constants.WORLDS_DOT + worldName);
        
        if(worldsConfigMS == null) {
        	sender.sendMessage(LocaleLoader.getString(Messages.COMMAND_INFO_WORLD_NOT_ADDED_CONFIG));
        } else {
        	WorldManager wm = TimeManager.getRunablesWorld().get(worldName);
        	
        	String status = (wm.getWorld() == null ? 
        			ChatColor.RED + LocaleLoader.getString(Messages.STATUS_ERROR_RUNNING) : 
        				(wm.isEnabled() ? ChatColor.GREEN + LocaleLoader.getString(Messages.STATUS_RUNNING) : 
        					ChatColor.RED + LocaleLoader.getString(Messages.STATUS_OFF))); 
        	
        	sender.sendMessage(LocaleLoader.getString(Messages.COMMAND_INFO_WORLD_NAME_STATUS, worldName, status));
        	
        	sender.sendMessage(LocaleLoader.getString(Messages.COMMAND_INFO_CURRENT_TIME_FULL_TIME, 
        			world.getTime(), world.getFullTime()));
        	
        	long days = world.getFullTime() / 24000;
        	int phase = (int) (days % 8);
        	sender.sendMessage(LocaleLoader.getString(Messages.COMMAND_INFO_CURRENT_MOON_PHASE, 
        			MoonPhasesEnum.values()[phase].name()));
        	
        	LinkedHashMap<String, Object> list = (LinkedHashMap<String, Object>) worldsConfigMS.getValues(true);

        	String climaAtual = WeatherManager.getClimaAtual(world);
        	sender.sendMessage(LocaleLoader.getString(Messages.COMMAND_INFO_CURRENT_WEATHER_CHANGED_IN, climaAtual, 
        			( world.isThundering() ? world.getThunderDuration() : world.getWeatherDuration() )));
        	
        	// Lista as informações de ambiente do mundo
        	listInfo(sender, worldName, list);
        }
        
		return true;
	}

	private static void listInfo(CommandSender sender, String worldName, Map<String, Object> list) {
		boolean enabledProperty = true;
		
		if(list == null) {
			return;
		}
		
		WorldManager wm = TimeManager.getRunablesWorld().get(worldName);

		enabledProperty = wm.isEnabled();
		
		sendMessage(sender, LocaleLoader.getString(Messages.INFO_ENABLED), wm.isEnabled());
		
		sendMessage(sender, LocaleLoader.getString(Messages.INFO_WEATHER), wm.getWeather().getValue(), enabledProperty);
		
		if(wm.getWeather() != WeatherEnum.CALM) {
			sendMessage(sender, LocaleLoader.getString(Messages.INFO_THUNDER), wm.getThunder().getValue(), enabledProperty);
		} else {
			sendMessage(sender, LocaleLoader.getString(Messages.INFO_THUNDER), wm.getThunder().getValue(), false);
		}
		
		sendMessage(sender, LocaleLoader.getString(Messages.INFO_TIME), wm.getTime().getValue(), enabledProperty);
		
		if(wm.getTime() == TimeEnum.FIXED) {
			sendMessage(sender, LocaleLoader.getString(Messages.INFO_TIME_FIXED_IN), wm.getTimeFixed(), enabledProperty);
		} else {
			sendMessage(sender, LocaleLoader.getString(Messages.INFO_TIME_FIXED_IN), wm.getTimeFixed(), false);
		}
		
		if(wm.getTime() == TimeEnum.CONFIGURED) {
			sendMessage(sender, LocaleLoader.getString(Messages.INFO_DURATION_DAY), wm.getDurationDay(), enabledProperty);
			sendMessage(sender, LocaleLoader.getString(Messages.INFO_DURATION_NIGHT), wm.getDurationNight(), enabledProperty);
		} else {
			sendMessage(sender, LocaleLoader.getString(Messages.INFO_DURATION_DAY), wm.getDurationDay(), false);
			sendMessage(sender, LocaleLoader.getString(Messages.INFO_DURATION_NIGHT), wm.getDurationNight(), false);
		}
		
		if(wm.getTime() != TimeEnum.DAY) {
			sendMessage(sender, LocaleLoader.getString(Messages.INFO_MOON_PHASE), wm.getMoonPhase().getValue(), enabledProperty);
		} else {
			sendMessage(sender, LocaleLoader.getString(Messages.INFO_MOON_PHASE), wm.getMoonPhase().getValue(), false);
		}
	}

	private static void sendMessage(CommandSender sender, String property, Object value) {
		sendMessage(sender, property, value, true);
	}

	private static void sendMessage(CommandSender sender, String property, Object value, boolean enabled) {
		if(enabled) {
			sender.sendMessage("  " + LocaleLoader.getString(Messages.COMMAND_INFO_PROPERTY_VALUE, property, 
					(value != null ? value.toString() : ChatColor.DARK_GRAY + "-" )));
		} else {
			sender.sendMessage("  " + LocaleLoader.getString(Messages.COMMAND_INFO_PROPERTY_VALUE, ChatColor.DARK_GRAY + property,
					ChatColor.DARK_GRAY + (value != null ? value.toString() : "-" )));
		}
	}

}
