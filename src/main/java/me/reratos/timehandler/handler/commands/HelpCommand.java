package me.reratos.timehandler.handler.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import me.reratos.timehandler.TimeHandler;

public class HelpCommand {
	
	public static boolean helpAll(CommandSender sender) {
		PluginCommand cmd = null;
		
		if(sender.hasPermission("timehandler.info.use")) {
			cmd = TimeHandler.plugin.getCommand("timehandler info");
			sender.sendMessage(commandHelp(cmd.getName(), cmd.getDescription()));
		}
		
		return false;
	}
	
	private static String commandHelp(String command, String description) {
		return ChatColor.GOLD + "/" + command + ": " + ChatColor.WHITE + description;
	}

}
