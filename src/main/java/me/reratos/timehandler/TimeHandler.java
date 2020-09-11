package me.reratos.timehandler;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import me.reratos.timehandler.core.TimeManager;
import me.reratos.timehandler.core.WeatherManager;
import me.reratos.timehandler.handler.CommandHandler;
import me.reratos.timehandler.handler.ListenerHandler;
import me.reratos.timehandler.handler.TabCompletion;

public class TimeHandler extends JavaPlugin {

	public static TimeHandler plugin;
	public static FileConfiguration config;
	public static Yaml configPlugin;

    @Override
    public void onEnable() {
    	plugin = this;
    	try {

    	    DumperOptions options = new DumperOptions();
    	    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    	    configPlugin = new Yaml(options);
    	    
    	    FileWriter arq = new FileWriter("plugins/TimeHandler/configWorlds.yml");
//			configPlugin.load((new FileInputStream(arq)));

			Map<String, Map<String, String>> arqYml = (Map<String, Map<String, String>>) new HashMap<String, Map<String, String>>();
			Map<String, String> worlds = (Map<String, String>) new HashMap<String, String>();

			worlds.put("world", "NORMAL");
			worlds.put("void", "NORMAL");
			worlds.put("world_nether", "NETHER");
			arqYml.put("mundos", worlds);

//			String ret = configPlugin.dump(arqYml);
			configPlugin.dump(arqYml, arq);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	Bukkit.getPluginManager().registerEvents(new ListenerHandler(), this);

    	TabCompletion tabCompletion = new TabCompletion();
    	getCommand("timehandler").setTabCompleter(tabCompletion);
    	getCommand("th").setTabCompleter(tabCompletion);
    	
        sendMessage(ChatColor.AQUA + "Manipulador de Tempo está ativo.");
        
        saveDefaultConfig();
        saveConfig();
        config = getConfig();
        
    	for(Player p : Bukkit.getOnlinePlayers()) {
    		if(p.isOp()) {
    			p.setGameMode(GameMode.CREATIVE);
    			TimeHandler.sendMessage(p, "Operador: alterado gamemode para criativo");
    		}
    	}
    	
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
//        if(!(sender instanceof Player)) {
//            sendMessage(config.getString(Config.STR_PLAYER_ONLY_COMMAND));
//            return false;
//        }
        
        switch(command.getName()) {
            case "timehandler":
            case "th":
                if(sender.hasPermission("timehandler.use")){
                	
//                    for(int i = 0; i < args.length; i++) {
//                    	sendMessage(ChatColor.GREEN + "args[" + i + "]: " + ChatColor.YELLOW + args[i]);
//                    }
                    
                    if(args.length == 0) {
                    	sendMessage(sender, "Utilize o comando: '/help th' para mais ajuda.");
                    	return false;
                    }
                    
                    switch (args[0].toLowerCase()) {
						case "info":
							if(args.length != 2) {
		                    	sendMessage(sender, "Utilize o comando: '/help th " + args[0].toLowerCase() + "' para mais ajuda.");
		                    	return false;
		                    }
							return CommandHandler.info(sender, args[1]);
							
						case "list":
							if(args.length != 1) {
								sendMessage(sender, "Utilize o comando: '/help th " + args[0].toLowerCase() + "' para mais ajuda.");
		                    	return false;
							}
							return CommandHandler.list(sender);

						case "remove":
							if(args.length != 2) {
								sendMessage(sender, "Utilize o comando: '/help th " + args[0].toLowerCase() + "' para mais ajuda.");
		                    	return false;
							}
							return CommandHandler.remove(sender, args[1]);

						case "set":
							if(args.length == 2) {
		                    	return CommandHandler.set(sender, args[1]);
		                    } else if(args.length == 4) {
		                    	return CommandHandler.set(sender, args[1], args[2], args[3]);
		                    }
							sendMessage(sender, "Utilize o comando: '/help th " + args[0].toLowerCase() + "' para mais ajuda.");
	                    	return false;
	                    	
						case "update":
							return CommandHandler.update(sender);
	
						default:
							sendMessage(sender, "Utilize o comando: '/help th' para mais ajuda.");
							return false;
					}
                    
                } else {
                    sendMessage(sender, config.getString(Config.STR_NOT_PERMISSION_COMMAND));
                }
                break;

            case "day":
            case "thd":
            	if(args.length == 0 && sender instanceof Player) {
            		return TimeManager.day(sender, ((Player)sender).getWorld());
            	} else if(args.length == 1) {
            		return TimeManager.day(sender, args[0]);
            	}
            	sendMessage(sender, "Utilize o comando: '/help " + command.getName() + "' para mais ajuda.");
            	return false;

            case "night":
            case "thn":
            	if(args.length == 0 && sender instanceof Player) {
            		return TimeManager.night(sender, ((Player)sender).getWorld());
            	} else if(args.length == 1) {
            		return TimeManager.night(sender, args[0]);
            	}
            	sendMessage(sender, "Utilize o comando: '/help " + command.getName() + "' para mais ajuda.");
            	return false;
            	
            case "rain":
            case "thr":
            	if(args.length == 0 && sender instanceof Player) {
            		return WeatherManager.rain(sender, ((Player)sender).getWorld());
            	} else if(args.length == 1) {
            		return WeatherManager.rain(sender, args[0]);
            	}
            	sendMessage(sender, "Utilize o comando: '/help " + command.getName() + "' para mais ajuda.");
            	break;

            case "thundering":
            case "tht":
            	if(args.length == 0 && sender instanceof Player) {
            		return WeatherManager.thundering(sender, ((Player)sender).getWorld());
            	} else if(args.length == 1) {
            		return WeatherManager.thundering(sender, args[0]);
            	}
            	sendMessage(sender, "Utilize o comando: '/help " + command.getName() + "' para mais ajuda.");
            	break;
            case "calm":
            case "thc":
            	if(args.length == 0 && sender instanceof Player) {
            		return WeatherManager.calm(sender, ((Player)sender).getWorld());
            	} else if(args.length == 1) {
            		return WeatherManager.calm(sender, args[0]);
            	}
            	sendMessage(sender, "Utilize o comando: '/help " + command.getName() + "' ppara mais ajuda.");
            	break;
        }
        
        return false;
    }
    
    @Override
    public void onDisable() {
    	HandlerList.unregisterAll(plugin);
    	sendMessage(ChatColor.YELLOW + "Desabilitado");
    	saveConfig();
    	
    	plugin = null;
    	config = null;
    	configPlugin = null;
    }
    
    public static boolean existWorld(String worldName) {
    	return Bukkit.getWorld(worldName) != null;
    }

    private static void sendMessage(String message) {
    	Bukkit.getConsoleSender()
    		.sendMessage(ChatColor.GOLD + "[TimeHandler] " + ChatColor.RESET + message);
    }

    public static void sendMessage(CommandSender sender, String message) {
    	sender.sendMessage(ChatColor.GOLD + "[TimeHandler] " + ChatColor.RESET + message);
    }
    
    public static void broadcastMessage(String message) {
    	Bukkit.broadcastMessage(ChatColor.GOLD + "[TimeHandler] " + ChatColor.RESET + message);
    }
			
}
