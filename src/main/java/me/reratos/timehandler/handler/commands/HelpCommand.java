package me.reratos.timehandler.handler.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import me.reratos.timehandler.TimeHandler;

import java.util.List;

public class HelpCommand {
	
	public static boolean helpAll(CommandSender sender) {
		PluginCommand cmd = null;
		
//		TimeHandler.sendMessage(sender, "Adicione um '?' no comando para ter mais ajuda.");
		
		if(sender.hasPermission("timehandler.info.use")) {
			cmd = TimeHandler.plugin.getCommand("timehandler info");
			String nameCommand = getAliaseOrName(cmd);
			sender.sendMessage("/" + nameCommand + ChatColor.GREEN + " <world>");
		}

		if(sender.hasPermission("timehandler.list.use")) {
			cmd = TimeHandler.plugin.getCommand("timehandler list");
			String nameCommand = getAliaseOrName(cmd);
			sender.sendMessage("/" + nameCommand);
		}

		if(sender.hasPermission("timehandler.set.use")) {
			cmd = TimeHandler.plugin.getCommand("timehandler set");
			String nameCommand = getAliaseOrName(cmd);
			sender.sendMessage("/" + nameCommand + ChatColor.GREEN + " <world> " +
					ChatColor.LIGHT_PURPLE + "[<PROPERTY> <VALUE>]");
		}

		if(sender.hasPermission("timehandler.update.use")) {
			cmd = TimeHandler.plugin.getCommand("timehandler update");
			String nameCommand = getAliaseOrName(cmd);
			sender.sendMessage("/" + nameCommand);
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

	private static String getAliaseOrName(PluginCommand pluginCommand) {
		return pluginCommand.getAliases().isEmpty() ? pluginCommand.getName() : pluginCommand.getAliases().get(0);
	}
	
}
