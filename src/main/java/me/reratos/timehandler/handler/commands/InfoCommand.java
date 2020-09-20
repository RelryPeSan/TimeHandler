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
        	TimeHandler.sendMessage(sender, "This world has NOT yet been added to the handler.");
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
		if(list == null) {
			return;
		}
		
		WorldManager wm = TimeManager.getRunablesWorld().get(worldName);

		sendMessage(sender, "enabled: " + getValueMessageInfo(list, "enabled"));
		
		sendMessage(sender, "weather: " + getValueMessageInfo(list, "weather"));
		
		if(wm.getWeather() != WeatherEnum.CALM) {
			sendMessage(sender, "thunder: " + getValueMessageInfo(list, "thunder"));
		} else {
			sendMessage(sender, ChatColor.DARK_GRAY + "thunder: " + getValueMessage(list, "thunder"));
		}
		
		sendMessage(sender, "time: " 	+ getValueMessageInfo(list, "time"));
		
		if(wm.getTime() == TimeEnum.FIXED) {
			sendMessage(sender, "timeFixed: " + getValueMessageInfo(list, "timeFixed"));
		} else {
			sendMessage(sender, ChatColor.DARK_GRAY + "timeFixed: " + getValueMessage(list, "timeFixed"));
		}
		
		if(wm.getTime() == TimeEnum.CONFIGURED) {
			sendMessage(sender, "durationDay: " + getValueMessageInfo(list, "durationDay"));
			sendMessage(sender, "durationNight: " + getValueMessageInfo(list, "durationNight"));
		} else {
			sendMessage(sender, ChatColor.DARK_GRAY + "durationDay: " + getValueMessage(list, "durationDay"));
			sendMessage(sender, ChatColor.DARK_GRAY + "durationNight: " + getValueMessage(list, "durationNight"));
		}
		
		if(wm.getTime() != TimeEnum.DAY) {
			sendMessage(sender, "moonPhase: " + getValueMessageInfo(list, "moonPhase"));
		} else {
			sendMessage(sender, ChatColor.DARK_GRAY + "moonPhase: " + getValueMessage(list, "moonPhase"));
		}
	}

	private static void sendMessage(CommandSender sender, String message) {
		sender.sendMessage("  " + ChatColor.YELLOW + message);
	}
	
	private static String getValueMessage(Map<String, Object> list, String key) {
		Object obj = list.get(key);
		return obj != null ? obj.toString() : "-" ;
	}
	
	private static String getValueMessageInfo(Map<String, Object> list, String key) {
		Object obj = list.get(key);
		return obj != null ? ChatColor.WHITE + obj.toString() : ChatColor.DARK_GRAY + "-" ;
	}
	
}
