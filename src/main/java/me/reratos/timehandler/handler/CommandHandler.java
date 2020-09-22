package me.reratos.timehandler.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;

import me.reratos.timehandler.TimeHandler;
import me.reratos.timehandler.core.TimeManager;
import me.reratos.timehandler.core.WorldManager;
import me.reratos.timehandler.handler.commands.HelpCommand;
import me.reratos.timehandler.handler.commands.InfoCommand;
import me.reratos.timehandler.handler.commands.SetCommand;
import me.reratos.timehandler.utils.Constants;
import me.reratos.timehandler.utils.LocaleLoader;
import me.reratos.timehandler.utils.Messages;
import me.reratos.timehandler.utils.UpdateChecker;

public class CommandHandler {
	
	public static boolean info(CommandSender sender, String worldName) {
		// verifica existencia do mundo
		World world = Bukkit.getWorld(worldName);
        if(world == null) {
        	sender.sendMessage(LocaleLoader.getString(Messages.WORLD_NOT_EXIST, worldName));
        	return true;
        }

        return info(sender, world);
	}
	
	public static boolean info(CommandSender sender, World world) {
		return InfoCommand.commandInfoBase(sender, world);
	}
	
	public static boolean help(CommandSender sender) {
		return HelpCommand.helpAll(sender);
	}
	
	public static boolean list(CommandSender sender) {
		MemorySection worldsMS = (MemorySection) TimeHandler.worldsConfig.get(Constants.WORLDS);

		
		if(worldsMS == null) {
			sender.sendMessage(LocaleLoader.getString(Messages.COMMAND_LIST_NO_WORLD_CONFIGURED));
		} else {
			LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) worldsMS.getValues(false);
			List<String> lista = new ArrayList<>(map.keySet());
			
			Collections.sort(lista);
			
			sender.sendMessage(LocaleLoader.getString(Messages.COMMAND_LIST_CONFIGURED_WORLDS));
			for(String worldName : lista) {
				WorldManager wm = TimeManager.getRunablesWorld().get(worldName);
				World world = (wm != null ? wm.getWorld() : null);

				String running = "";
				String loaded = "";

				if(wm != null) {
					if(wm.isEnabled()) {
						running = ChatColor.GREEN + LocaleLoader.getString(Messages.STATUS_RUNNING);
					} else {
						running = ChatColor.RED + LocaleLoader.getString(Messages.STATUS_OFF);
					}
				} else {
					running = ChatColor.RED + LocaleLoader.getString(Messages.STATUS_NOT_RUNNING);
				}
				
				if(world != null) {
					loaded = ChatColor.GREEN + LocaleLoader.getString(Messages.STATUS_LOADED);
				} else {
					loaded = ChatColor.RED + LocaleLoader.getString(Messages.STATUS_UNLOADED);
				}
				
				sender.sendMessage("  " + LocaleLoader.getString(Messages.COMMAND_LIST_WORLD_INFO, worldName, running, loaded));
			}
		}
		
		return true;
	}

//	public static boolean remove(CommandSender sender, String worldName) {
//		Object obj = TimeHandler.config.get("configWorld." + worldName);
//		
//		if(obj != null) {
//			TimeHandler.config.set("configWorld." + worldName, null);
//			TimeHandler.sendMessage(sender, "The world " + ChatColor.GREEN + worldName + 
//					" has been removed from the TimeHandler settings");
//			TimeHandler.plugin.saveConfig();
//		} else {
//			TimeHandler.sendMessage(sender, "This world does not exist in TimeHandler settings.");
//		}
//		
//		return true;
//	}

	public static boolean set(CommandSender sender, String worldName) {
		
		if(!TimeHandler.existWorld(worldName)) {
			sender.sendMessage(LocaleLoader.getString(Messages.WORLD_NOT_EXIST, worldName));
			return true;
		} else if(TimeHandler.worldsConfig.get(Constants.WORLDS_DOT + worldName) != null) {
			sender.sendMessage(LocaleLoader.getString(Messages.COMMAND_SET_WORLD_CONFIGURED, worldName));
			return true;
		}

		return SetCommand.commandSetDefault(sender, worldName);
	}

	public static boolean set(CommandSender sender, String worldName, String property, String value) {
		WorldManager wm = TimeManager.getRunablesWorld().get(worldName);
		
		if(!TimeHandler.existWorld(worldName)) {
			sender.sendMessage(LocaleLoader.getString(Messages.WORLD_NOT_EXIST, worldName));
			return true;
		} else if(wm == null) {
			sender.sendMessage(LocaleLoader.getString(Messages.COMMAND_SET_WORLD_NOT_CONFIGURED, worldName));
			return false;
		}
		
		return SetCommand.commandSetBase(sender, wm, property, value);
	}
	
	public static boolean update(String resourceId) {
		return update(TimeHandler.plugin.getServer().getConsoleSender(), resourceId);
	}

	public static boolean update(CommandSender sender, String resourceId) {
        new UpdateChecker(TimeHandler.plugin, resourceId).getVersionConsumer(version -> {
        	if (TimeHandler.plugin.getDescription().getVersion().equalsIgnoreCase(version)) {
        		sender.sendMessage(LocaleLoader.getString(Messages.COMMAND_PLUGIN_IS_UPDATED, version));
            } else {
            	sender.sendMessage(LocaleLoader.getString(Messages.COMMAND_NEW_VERSION_AVAILABLE, version));
    			sender.sendMessage(LocaleLoader.getString(Messages.ACCESS_URL_PLUGIN, Constants.URL_PLUGIN));
            }
        });
		return true;
	}

	public static List<String> getWorldsTimeHandler() {
		List<String> list = new ArrayList<String>();
		try {
			list.addAll(((LinkedHashMap<String, Object>)((MemorySection) TimeHandler
					.worldsConfig.get(Constants.WORLDS)).getValues(false)).keySet());			
			Collections.sort(list);
		} catch (Exception e) {
			// nothing
		}
		return list;
	}

}
