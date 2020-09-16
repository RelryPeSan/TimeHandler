package me.reratos.timehandler;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class WorldConfig {
	public static void info(CommandSender sender, Map<String, Object> list) {
		if(list == null) {
			return;
		}

		sendMessage(sender, "enabled: " + getValue(list, "enabled"));
		sendMessage(sender, "weather: " + getValue(list, "weather"));
		sendMessage(sender, "thunder: " + getValue(list, "thunder"));
		sendMessage(sender, "time: " + getValue(list, "time"));
		sendMessage(sender, "timeFixed: " + getValue(list, "timeFixed"));
		sendMessage(sender, "moonPhase: " + getValue(list, "moonPhase"));
	}
	
	private static void sendMessage(CommandSender sender, String message) {
		sender.sendMessage("   " + ChatColor.YELLOW + message);
	}
	
	private static String getValue(Map<String, Object> list, String key) {
		Object obj = list.get(key);
		return obj != null ? ChatColor.WHITE + obj.toString() : ChatColor.DARK_GRAY + "-" ;
	}
}
