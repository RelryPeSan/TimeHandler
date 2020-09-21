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

public class InfoCommand {

	public static boolean commandInfoBase(CommandSender sender, World world) {
		String worldName = world.getName();
		
		MemorySection worldsConfigMS = (MemorySection) TimeHandler.worldsConfig.get("worlds." + worldName);
        
        if(worldsConfigMS == null) {
        	sender.sendMessage("This world has NOT yet been added to the handler.");
        } else {
        	WorldManager wm = TimeManager.getRunablesWorld().get(worldName);
        	
        	String status = ChatColor.RESET + "[" + (wm.getWorld() == null ? ChatColor.RED + "RUNNING ERROR" : (
        			wm.isEnabled() ? ChatColor.GREEN + "RUNNING" : ChatColor.RED + "OFF")) +
        			ChatColor.RESET + "]";
        	
        	sender.sendMessage(ChatColor.YELLOW + "World name: " + ChatColor.GREEN + worldName + 
        			ChatColor.RESET +" - " + status);
        	
        	sender.sendMessage("Current time: " + world.getTime() + ", FullTime: " + world.getFullTime());
        	
        	long days = world.getFullTime() / 24000;
        	int phase = (int) (days % 8);
        	sender.sendMessage("Current moon phase: " + ChatColor.BLUE + MoonPhasesEnum.values()[phase].name());
        	
        	LinkedHashMap<String, Object> list = (LinkedHashMap<String, Object>) worldsConfigMS.getValues(true);

        	String climaAtual = WeatherManager.getClimaAtual(world);
        	sender.sendMessage("Current weather: " + climaAtual + ", change in: " + world.getWeatherDuration());
        	
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
		
		sendMessage(sender, "enabled", wm.isEnabled());
		
		sendMessage(sender, "weather", wm.getWeather().getValue(), enabledProperty);
		
		if(wm.getWeather() != WeatherEnum.CALM) {
			sendMessage(sender, "thunder", wm.getThunder().getValue(), enabledProperty);
		} else {
			sendMessage(sender, "thunder", wm.getThunder().getValue(), false);
		}
		
		sendMessage(sender, "time", wm.getTime().getValue(), enabledProperty);
		
		if(wm.getTime() == TimeEnum.FIXED) {
			sendMessage(sender, "timeFixed", wm.getTimeFixed(), enabledProperty);
		} else {
			sendMessage(sender, "timeFixed", wm.getTimeFixed(), false);
		}
		
		if(wm.getTime() == TimeEnum.CONFIGURED) {
			sendMessage(sender, "durationDay", wm.getDurationDay(), enabledProperty);
			sendMessage(sender, "durationNight", wm.getDurationNight(), enabledProperty);
		} else {
			sendMessage(sender, "durationDay", wm.getDurationDay(), false);
			sendMessage(sender, "durationNight", wm.getDurationNight(), false);
		}
		
		if(wm.getTime() != TimeEnum.DAY) {
			sendMessage(sender, "moonPhase", wm.getMoonPhase().getValue(), enabledProperty);
		} else {
			sendMessage(sender, "moonPhase", wm.getMoonPhase().getValue(), false);
		}
	}

	private static void sendMessage(CommandSender sender, String property, Object value) {
		sendMessage(sender, property, value, true);
	}

	private static void sendMessage(CommandSender sender, String property, Object value, boolean enabled) {
		if(enabled) {
			sender.sendMessage("  " + ChatColor.YELLOW + property + ": " + (value != null ? ChatColor.WHITE + value.toString() : ChatColor.DARK_GRAY + "-" ));
		} else {
			sender.sendMessage("  " + ChatColor.DARK_GRAY + property + ": " + (value != null ? value.toString() : "-" ) );
		}
	}

}
