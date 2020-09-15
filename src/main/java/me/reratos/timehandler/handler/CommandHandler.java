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
import org.bukkit.entity.Player;

import me.reratos.timehandler.TimeHandler;
import me.reratos.timehandler.WorldConfig;
import me.reratos.timehandler.core.TimeManager;
import me.reratos.timehandler.core.WeatherManager;
import me.reratos.timehandler.core.WorldManager;
import me.reratos.timehandler.enums.MoonPhasesEnum;
import me.reratos.timehandler.handler.commands.HelpCommand;
import me.reratos.timehandler.handler.commands.SetCommand;
import me.reratos.timehandler.utils.UpdateChecker;

public class CommandHandler {
	
	public static boolean info(CommandSender sender, String worldName) {
		// verifica existencia do mundo
		World world = Bukkit.getWorld(worldName);
        if(world == null) {
        	return false;
        }

        MemorySection worldConfig = (MemorySection) TimeHandler.config.get("configWorld." + worldName);
        
        if(worldConfig == null) {
        	TimeHandler.sendMessage(sender, "Este mundo ainda NÃO foi adicionado no manipulador.");
        } else {
        	WorldManager wm = TimeManager.getRunablesWorld().get(worldName);
        	String status = ChatColor.RESET + "[" + (wm.getWorld() == null ? ChatColor.RED + "RUNNING ERROR" : (
        			wm.isEnabled() ? ChatColor.GREEN + "RUNNING" : ChatColor.RED + "OFF")) +
        			ChatColor.RESET + "]";
        	TimeHandler.sendMessage(sender, ChatColor.YELLOW + "Mundo: " + ChatColor.GREEN + worldName + 
        			ChatColor.RESET +" - " + status);
        	TimeHandler.sendMessage(sender, "Tempo atual: " + world.getTime() + ", FullTime: " + world.getFullTime());
        	long days = world.getFullTime() / 24000;
        	int phase = (int) (days % 8);
        	TimeHandler.sendMessage(sender, "Fase da lua: " + ChatColor.BLUE + MoonPhasesEnum.values()[phase].name());
        	
        	LinkedHashMap<String, Object> list = (LinkedHashMap<String, Object>) worldConfig.getValues(true);

        	String climaAtual = WeatherManager.getClimaAtual(world);
        	TimeHandler.sendMessage(sender, "Clima atual: " + climaAtual);
        	
        	// Lista as informações de ambiente do mundo
        	WorldConfig.info(sender, list);
        }
        
		return true;
	}
	
	public static boolean help(CommandSender sender) {
		return HelpCommand.helpAll(sender);
	}
	
	public static boolean list(CommandSender sender) {
		MemorySection worldsMS = (MemorySection) TimeHandler.config.get("configWorld");

		
		if(worldsMS == null) {
			TimeHandler.sendMessage(sender, "Nenhum mundo foi configurado no plugin TimeHandler.");
		} else {
			LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) worldsMS.getValues(false);
			List<String> lista = new ArrayList<>(map.keySet());
			
			Collections.sort(lista);
			
			TimeHandler.sendMessage(sender, "Lista de mundos configurados.");
			for(String worldName : lista) {
				WorldManager wm = TimeManager.getRunablesWorld().get(worldName);
				World world = wm != null ? wm.getWorld() : null;
				StringBuilder message = new StringBuilder();
				message.append(ChatColor.YELLOW);
				message.append(" - ");
				message.append(worldName);
				
				if(sender instanceof Player) {
					message.append(ChatColor.RESET);
					message.append(" - ");
				} else {
					message.append("\t\t");
				}
				
				message.append(ChatColor.RESET);
				message.append("[");
				if(wm != null) {
					if(wm.isEnabled()) {
						message.append(ChatColor.GREEN);
						message.append("RUNNING");
					} else {
						message.append(ChatColor.RED);
						message.append("OFF");
					}
				} else {
					message.append(ChatColor.RED);
					message.append("NOT RUNNING");
				}
				message.append(ChatColor.RESET);
				message.append(", ");
				
				if(world != null) {
					message.append(ChatColor.GREEN);
					message.append("LOADED");
				} else {
					message.append(ChatColor.RED);
					message.append("UNLOADED");
				}
				message.append(ChatColor.RESET);
				message.append("]");
				
				TimeHandler.sendMessage(sender, message.toString());
//				TimeHandler.sendMessage(sender, ChatColor.YELLOW + " - " + worldName);
			}
		}
		
		return true;
	}

	public static boolean remove(CommandSender sender, String worldName) {
		Object obj = TimeHandler.config.get("configWorld." + worldName);
		
		if(obj != null) {
			TimeHandler.config.set("configWorld." + worldName, null);
			TimeHandler.sendMessage(sender, "O mundo " + ChatColor.GREEN + worldName + 
					" foi removido das configurações do TimeHandler");
			TimeHandler.plugin.saveConfig();
		} else {
			TimeHandler.sendMessage(sender, "Este mundo não existe nas configurações do TimeHandler");
		}
		
		return true;
	}

	public static boolean set(CommandSender sender, String worldName) {
		
		if(!TimeHandler.existWorld(worldName)) {
			TimeHandler.sendMessage(sender, ChatColor.RED + "Este mundo não existe: " + ChatColor.UNDERLINE + worldName);
			return false;
		} else if(TimeHandler.config.get("configWorld." + worldName) != null) {
			TimeHandler.sendMessage(sender, "Este mundo já esta configurado: " + ChatColor.RED + ChatColor.UNDERLINE + worldName);
			return true;
		}

		return SetCommand.commandSetDefault(sender, worldName);
	}

	public static boolean set(CommandSender sender, String worldName, String property, String value) {
		WorldManager wm = TimeManager.getRunablesWorld().get(worldName);
		
		if(!TimeHandler.existWorld(worldName)) {
			TimeHandler.sendMessage(sender, ChatColor.RED + "Este mundo não existe: " + ChatColor.UNDERLINE + worldName);
			return false;
		} else if(wm == null) {
			TimeHandler.sendMessage(sender, "Este mundo ainda não foi configurado no plugin, utilize " + 
					ChatColor.GREEN + "/th set " + worldName + ChatColor.RESET + " para configura-ló.");
			return false;
		}
		
		return SetCommand.commandSetBase(sender, wm, property, value);
	}
	
	public static boolean update(String resourceId) {
		return update(TimeHandler.plugin.getServer().getConsoleSender(), resourceId);
	}

	public static boolean update(CommandSender sender, String resourceId) {
        new UpdateChecker(TimeHandler.plugin, resourceId).getVersionConsumer(version -> {
//        	TimeHandler.sendMessage(sender, "version: " + version + ", server: " + TimeHandler.plugin.getDescription().getVersion());
        	if (TimeHandler.plugin.getDescription().getVersion().equalsIgnoreCase(version)) {
    			TimeHandler.sendMessage(sender, "The plugin is updated. " + ChatColor.LIGHT_PURPLE + version);
            } else {
    			TimeHandler.sendMessage(sender, "There is a new version available: " + ChatColor.GREEN + version);
            }
        });
		return true;
	}

	public static List<String> getWorldsTimeHandler() {
		List<String> list = new ArrayList<String>();
		try {
			list.addAll(((LinkedHashMap<String, Object>)((MemorySection) TimeHandler.config.get("configWorld")).getValues(false)).keySet());			
			Collections.sort(list);
		} catch (Exception e) {
			// nothing
		}
		return list;
	}
	
}
