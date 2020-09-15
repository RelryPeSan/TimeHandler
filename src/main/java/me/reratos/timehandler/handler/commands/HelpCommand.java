package me.reratos.timehandler.handler.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import me.reratos.timehandler.TimeHandler;

public class HelpCommand {
	
	public static boolean helpAll(CommandSender sender) {
		PluginCommand cmd = null;
		
//		TimeHandler.sendMessage(sender, "Adicione um '?' no comando para ter mais ajuda.");
		
		if(sender.hasPermission("timehandler.info.use")) {
			cmd = TimeHandler.plugin.getCommand("timehandler info");
			TimeHandler.sendMessage(sender, "/" + cmd.getAliases().get(0) + ChatColor.GREEN + " <world>");
		}

		if(sender.hasPermission("timehandler.list.use")) {
			cmd = TimeHandler.plugin.getCommand("timehandler list");
			TimeHandler.sendMessage(sender, "/" + cmd.getAliases().get(0));
		}

		if(sender.hasPermission("timehandler.set.use")) {
			cmd = TimeHandler.plugin.getCommand("timehandler set");
			TimeHandler.sendMessage(sender, "/" + cmd.getAliases().get(0) + ChatColor.GREEN + " <world> " + 
					ChatColor.LIGHT_PURPLE + "[<PROPERTY> <VALUE>]");
		}

		if(sender.hasPermission("timehandler.update.use")) {
			cmd = TimeHandler.plugin.getCommand("timehandler update");
			TimeHandler.sendMessage(sender, "/" + cmd.getAliases().get(0));
		}

		if(sender.hasPermission("timehandler.time.use")) {
			cmd = TimeHandler.plugin.getCommand("day");
			TimeHandler.sendMessage(sender, "/" + cmd.getName() + ChatColor.GREEN + " [world]");
			cmd = TimeHandler.plugin.getCommand("night");
			TimeHandler.sendMessage(sender, "/" + cmd.getName() + ChatColor.GREEN + " [world]");
		}

		if(sender.hasPermission("timehandler.weather.use")) {
			cmd = TimeHandler.plugin.getCommand("calm");
			TimeHandler.sendMessage(sender, "/" + cmd.getName() + ChatColor.GREEN + " [world]");
			cmd = TimeHandler.plugin.getCommand("rain");
			TimeHandler.sendMessage(sender, "/" + cmd.getName() + ChatColor.GREEN + " [world]");
			cmd = TimeHandler.plugin.getCommand("thundering");
			TimeHandler.sendMessage(sender, "/" + cmd.getName() + ChatColor.GREEN + " [world]");
		}
		
		return true;
	}
	
}
