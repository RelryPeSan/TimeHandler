package me.reratos.timehandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import me.reratos.timehandler.core.TimeManager;
import me.reratos.timehandler.core.WeatherManager;
import me.reratos.timehandler.events.WorldListener;
import me.reratos.timehandler.handler.CommandCompleter;
import me.reratos.timehandler.handler.CommandHandler;
import me.reratos.timehandler.utils.UpdateChecker;

public class TimeHandler extends JavaPlugin {

	public static TimeHandler plugin;
	public static FileConfiguration config;
	public static YamlConfiguration configWorlds;
	static String resourceId = "83803";

    @Override
    public void onEnable() {
    	plugin = this;

    	CommandHandler.update(resourceId);
    	lastVersionPlugin();
    	
    	Bukkit.getPluginManager().registerEvents(new WorldListener(), this);

    	CommandCompleter tabCompletion = new CommandCompleter();
    	getCommand("timehandler").setTabCompleter(tabCompletion);
    	getCommand("th").setTabCompleter(tabCompletion);
    	
        sendMessage(ChatColor.AQUA + "Time Handler is active.");
        
        saveDefaultConfig();
        saveConfig();
        config = getConfig();
    	
    	initializeTasks();
    }
    
    private void lastVersionPlugin() {
		new UpdateChecker(plugin, resourceId).getVersionConsumer(version -> {
			if(!plugin.getDescription().getVersion().equals(version)) {
				for(Player p : Bukkit.getOnlinePlayers()) {
					if(p.isOp()) {
						sendMessage(p, "New update available.");
						sendMessage(p, "Current version: " + plugin.getDescription().getVersion());
						sendMessage(p, "Last version: \t" + version);
					}
				}
			}
		});
	}

	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	
    	sender.sendMessage("");
    	
    	switch(command.getName()) {
            case "timehandler":
            case "th":
                if(sender.hasPermission("timehandler.use")){
                	
                    if(args.length == 0) {
                    	sendMessage(sender, "Utilize o comando: '/help th' para mais ajuda.");
                    	return false;
                    }
                    
                    switch (args[0].toLowerCase()) {
	                    case "help":
	                    	if(args.length == 1) {
	                    		sendHeaderMessage(sender, args[0].toUpperCase());
	                    		return CommandHandler.help(sender);
	                    	}
//	                    	TimeHandler.sendMessage(sender, "Help em desenvolvimento!");
	                    	command.setUsage(Bukkit.getPluginCommand(command.getName() + " " + args[0].toLowerCase()).getUsage());
	                    	sendMessage(sender, "Utilize o comando: '/help th " + args[0].toLowerCase() + "' para mais ajuda.");
	                    	return false;
                    
						case "info":
							if(args.length != 2) {
								command.setUsage(Bukkit.getPluginCommand(command.getName() + " " + args[0].toLowerCase()).getUsage());
								sendMessage(sender, "Utilize o comando: '/help th " + args[0].toLowerCase() + "' para mais ajuda.");
		                    	return false;
		                    }
							sendHeaderMessage(sender, args[0].toUpperCase());
							return CommandHandler.info(sender, args[1]);
							
						case "list":
							if(args.length != 1) {
								command.setUsage(Bukkit.getPluginCommand(command.getName() + " " + args[0].toLowerCase()).getUsage());
								sendMessage(sender, "Utilize o comando: '/help th " + args[0].toLowerCase() + "' para mais ajuda.");
		                    	return false;
							}
							sendHeaderMessage(sender, args[0].toUpperCase());
							return CommandHandler.list(sender);

//						case "remove":
//							if(args.length != 2) {
////								command.setUsage(Bukkit.getPluginCommand(command.getName() + " " + args[0].toLowerCase()).getUsage());
//								sendMessage(sender, "Utilize o comando: '/help th " + args[0].toLowerCase() + "' para mais ajuda.");
//		                    	return false;
//							}
//							return CommandHandler.remove(sender, args[1]);

						case "set":
							if(args.length == 2) {
		                    	return CommandHandler.set(sender, args[1]);
		                    } else if(args.length == 4) {
		                    	return CommandHandler.set(sender, args[1], args[2], args[3]);
		                    }
							command.setUsage(Bukkit.getPluginCommand(command.getName() + " " + args[0].toLowerCase()).getUsage());
							sendMessage(sender, "Utilize o comando: '/help th " + args[0].toLowerCase() + "' para mais ajuda.");
	                    	return false;
	                    	
						case "update":
							return CommandHandler.update(sender, resourceId);
	
						default:
//							command.setUsage(Bukkit.getPluginCommand(command.getName()).getUsage());
							sendMessage(sender, "Utilize o comando: '/help th' para mais ajuda.");
							return true;
					}
                    
                } else {
                    sendMessage(sender, "Você não tem permissão para executar este comando.");
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
            	sendMessage(sender, "Utilize o comando: '/help " + command.getName() + "' para mais ajuda.");
            	break;
        }
        
        return false;
    }
    
    @Override
    public void onDisable() {
    	HandlerList.unregisterAll(plugin);
    	saveConfig();
    	
    	// finaliza as task de verificação de clima dos mundos
    	Bukkit.getScheduler().cancelTasks(this);
    	TimeManager.finalizeTask();
    	
    	plugin = null;
    	config = null;
    	configWorlds = null;
    	sendMessage(ChatColor.YELLOW + "Desabilitado");
    }
    
    public static boolean existWorld(String worldName) {
    	return Bukkit.getWorld(worldName) != null;
    }

    public static void sendMessage(String message) {
    	Bukkit.getConsoleSender()
    		.sendMessage(ChatColor.GOLD + "[TimeHandler] " + ChatColor.RESET + message);
    }

    public static void sendMessage(CommandSender sender, String message) {
    	sender.sendMessage(ChatColor.GOLD + "[TimeHandler] " + ChatColor.RESET + message);
    }

    public static void sendHeaderMessage(CommandSender sender, String message) {
    	sendMessage(sender, ChatColor.YELLOW + "---========== " + ChatColor.GREEN + message + 
    			ChatColor.YELLOW + " ==========---");
    }
    
    public static void broadcastMessage(String message) {
    	Bukkit.broadcastMessage(ChatColor.GOLD + "[TimeHandler] " + ChatColor.RESET + message);
    }
    
    private static void initializeTasks() {
    	for(String worldName: CommandHandler.getWorldsTimeHandler()) {
//    		World w = Bukkit.getWorld(nameWorld);
//    		if(w != null) {
//    			TimeManager.initTask(w);
//    		}
    		TimeManager.initTask(worldName);
    	}
    }
			
}
