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
			sender.sendMessage("/" + cmd.getAliases().get(0) + ChatColor.GREEN + " <world>");
		}

		if(sender.hasPermission("timehandler.list.use")) {
			cmd = TimeHandler.plugin.getCommand("timehandler list");
			sender.sendMessage("/" + cmd.getAliases().get(0));
		}

		if(sender.hasPermission("timehandler.set.use")) {
			cmd = TimeHandler.plugin.getCommand("timehandler set");
			sender.sendMessage("/" + cmd.getAliases().get(0) + ChatColor.GREEN + " <world> " + 
					ChatColor.LIGHT_PURPLE + "[<PROPERTY> <VALUE>]");
		}

		if(sender.hasPermission("timehandler.update.use")) {
			cmd = TimeHandler.plugin.getCommand("timehandler update");
			sender.sendMessage("/" + cmd.getAliases().get(0));
		}

		if(sender.hasPermission("timehandler.time.use")) {
			cmd = TimeHandler.plugin.getCommand("day");
			sender.sendMessage("/" + cmd.getName() + ChatColor.GREEN + " [world]");
			
			cmd = TimeHandler.plugin.getCommand("night");
			sender.sendMessage("/" + cmd.getName() + ChatColor.GREEN + " [world]");
			
			cmd = TimeHandler.plugin.getCommand("moonphase");
			sender.sendMessage("/" + cmd.getName() + ChatColor.LIGHT_PURPLE + " <phase>" + ChatColor.GREEN + " [world]");
		}

		if(sender.hasPermission("timehandler.weather.use")) {
			cmd = TimeHandler.plugin.getCommand("calm");
			sender.sendMessage("/" + cmd.getName() + ChatColor.GREEN + " [world] [duration]");
			
			cmd = TimeHandler.plugin.getCommand("rain");
			sender.sendMessage("/" + cmd.getName() + ChatColor.GREEN + " [world] [duration]");
			
			cmd = TimeHandler.plugin.getCommand("thundering");
			sender.sendMessage("/" + cmd.getName() + ChatColor.GREEN + " [world] [duration]");
		}
		
		return true;
	}
	
}
