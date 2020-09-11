package me.reratos.timehandler.core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.reratos.timehandler.TimeHandler;

public class TimeManager {

	public static boolean day(CommandSender sender, String worldName) {
		World w = Bukkit.getWorld(worldName);
		
		if(w != null) {
			day(sender, w);
		} else {
			TimeHandler.sendMessage(sender, "Este mundo não existe!");
		}
		
		return true;
	}

	public static boolean day(CommandSender sender, World world) {
		try {
			changeTime(sender, world, 500);		
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}

	public static boolean night(CommandSender sender, String worldName) {
		World w = Bukkit.getWorld(worldName);
		
		if(w != null) {
			night(sender, w);
		} else {
			TimeHandler.sendMessage(sender, "Este mundo não existe!");
		}
		
		return true;
	}
	
	public static boolean night(CommandSender sender, World world) {
		try {
			changeTime(sender, world, 14000);			
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}


	private static void changeTime(CommandSender sender, World world, int time) {
		world.setTime(time);
		
		if(sender instanceof Player) {
			Player p = (Player) sender;
			TimeHandler.broadcastMessage("O player " + ChatColor.BLUE + p.getDisplayName() + 
					ChatColor.RESET + " alterou o tempo do mundo " + ChatColor.GREEN + world.getName() +
					ChatColor.RESET + " para " + time);
		} else {
			TimeHandler.broadcastMessage("O server alterou o tempo do mundo " + ChatColor.GREEN + 
					world.getName() + ChatColor.RESET + " para " + time);
		}
	}
	
}
