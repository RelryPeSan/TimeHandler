package me.reratos.timehandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Charsets;

import me.reratos.timehandler.core.TimeManager;
import me.reratos.timehandler.core.WeatherManager;
import me.reratos.timehandler.enums.MoonPhasesEnum;
import me.reratos.timehandler.events.WorldListener;
import me.reratos.timehandler.handler.CommandCompleter;
import me.reratos.timehandler.handler.CommandHandler;
import me.reratos.timehandler.utils.ConstantsConfig;
import me.reratos.timehandler.utils.UpdateChecker;

public class TimeHandler extends JavaPlugin {

	public static TimeHandler plugin;
	
	public static FileConfiguration config;
	public static YamlConfiguration worldsConfig;
	public File fileWorldsConfig;
	
	private final String nameFileWorldsConfig = "worldsConfig.yml";
	private final String resourceId = "83803";

	@Override
	public void onEnable() {
		plugin = this;
		fileWorldsConfig = new File(getDataFolder(), nameFileWorldsConfig);
		
		if(!fileWorldsConfig.exists()) {
			defineDefaultWorldsConfig();
			saveWorldsConfig();
		} else {
			worldsConfig = YamlConfiguration.loadConfiguration(fileWorldsConfig);
		}
		
		saveDefaultConfig();
        saveConfig();
        config = getConfig();

		if(config.getBoolean(ConstantsConfig.CHECK_UPDATE_PLUGIN)) {
			CommandHandler.update(resourceId);
			lastVersionPlugin();
		}

		Bukkit.getPluginManager().registerEvents(new WorldListener(), this);

		CommandCompleter tabCompletion = new CommandCompleter();
		getCommand("timehandler").setTabCompleter(tabCompletion);
		getCommand("th").setTabCompleter(tabCompletion);
		getCommand("moonphase").setTabCompleter(tabCompletion);
		
		initializeTasks();
		
		sendMessage(ChatColor.AQUA + "Time Handler is active.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		sender.sendMessage("");

		switch (command.getName().toLowerCase()) {
		case "timehandler":
		case "th":
			if (sender.hasPermission("timehandler.use")) {

				if (args.length == 0) {
					sendMessage(sender, "Use command: '/help th' for more help.");
					return false;
				}

				switch (args[0].toLowerCase()) {
				case "help":
					if (args.length == 1) {
						sendHeaderMessage(sender, args[0].toUpperCase());
						return CommandHandler.help(sender);
					}
//	                    	TimeHandler.sendMessage(sender, "Help em desenvolvimento!");
					command.setUsage(
							Bukkit.getPluginCommand(command.getName() + " " + args[0].toLowerCase()).getUsage());
					sendMessage(sender, "Use command: '/help th " + args[0].toLowerCase() + "' for more help.");
					return false;

				case "info":
					if(args.length == 1 && sender instanceof Player) {
						sendHeaderMessage(sender, args[0].toUpperCase());
						return CommandHandler.info(sender, ((Player)sender).getWorld());
					} else if (args.length == 2) {
						sendHeaderMessage(sender, args[0].toUpperCase());
						return CommandHandler.info(sender, args[1]);
					}
					command.setUsage(Bukkit.getPluginCommand(command.getName() + " " + args[0].toLowerCase()).getUsage());
					sendMessage(sender,"Use command: '/help th " + args[0].toLowerCase() + "' for more help.");
					return false;

				case "list":
					if (args.length != 1) {
						command.setUsage(
								Bukkit.getPluginCommand(command.getName() + " " + args[0].toLowerCase()).getUsage());
						sendMessage(sender,
								"Use command: '/help th " + args[0].toLowerCase() + "' for more help.");
						return false;
					}
					sendHeaderMessage(sender, args[0].toUpperCase());
					return CommandHandler.list(sender);

//						case "remove":
//							if(args.length != 2) {
////								command.setUsage(Bukkit.getPluginCommand(command.getName() + " " + args[0].toLowerCase()).getUsage());
//								sendMessage(sender, "Use command: '/help th " + args[0].toLowerCase() + "' for more help.");
//		                    	return false;
//							}
//							return CommandHandler.remove(sender, args[1]);

				case "set":
					if (args.length == 2) {
						sendHeaderMessage(sender, args[0].toUpperCase());
						return CommandHandler.set(sender, args[1]);
					} else if (args.length == 4) {
						sendHeaderMessage(sender, args[0].toUpperCase());
						return CommandHandler.set(sender, args[1], args[2], args[3]);
					}
					command.setUsage(
							Bukkit.getPluginCommand(command.getName() + " " + args[0].toLowerCase()).getUsage());
					sendMessage(sender, "Use command: '/help th " + args[0].toLowerCase() + "' for more help.");
					return false;

				case "update":
					sendHeaderMessage(sender, args[0].toUpperCase());
					return CommandHandler.update(sender, resourceId);

				default:
//							command.setUsage(Bukkit.getPluginCommand(command.getName()).getUsage());
					sendMessage(sender, "Use command: '/help th' for more help.");
					return true;
				}

			} else {
				sendMessage(sender, "You are not allowed to execute this command.");
			}
			break;

		case "day":
		case "thd":
			if (args.length == 0 && sender instanceof Player) {
				return TimeManager.day(sender, ((Player) sender).getWorld());
			} else if (args.length == 1) {
				return TimeManager.day(sender, args[0]);
			}
			sendMessage(sender, "Use command: '/help " + command.getName() + "' for more help.");
			return false;

		case "night":
		case "thn":
			if (args.length == 0 && sender instanceof Player) {
				return TimeManager.night(sender, ((Player) sender).getWorld());
			} else if (args.length == 1) {
				return TimeManager.night(sender, args[0]);
			}
			sendMessage(sender, "Use command: '/help " + command.getName() + "' for more help.");
			return false;

		case "moonphase":
		case "thmp":
			if (args.length == 1 && sender instanceof Player) {
				return TimeManager.moonPhase(sender, MoonPhasesEnum.getEnumPorValue(args[0]),
						((Player) sender).getWorld());
			} else if (args.length == 2) {
				return TimeManager.moonPhase(sender, MoonPhasesEnum.getEnumPorValue(args[0]), args[1]);
			}
			sendMessage(sender, "Use command: '/help " + command.getName() + "' for more help.");
			return false;

		case "rain":
		case "thr":
			if (args.length == 0 && sender instanceof Player) {
				return WeatherManager.rain(sender, ((Player) sender).getWorld());
			} else if (args.length == 1) {
				return WeatherManager.rain(sender, args[0]);
			}
			sendMessage(sender, "Use command: '/help " + command.getName() + "' for more help.");
			break;

		case "thundering":
		case "tht":
			if (args.length == 0 && sender instanceof Player) {
				return WeatherManager.thundering(sender, ((Player) sender).getWorld());
			} else if (args.length == 1) {
				return WeatherManager.thundering(sender, args[0]);
			}
			sendMessage(sender, "Use command: '/help " + command.getName() + "' for more help.");
			break;
		case "calm":
		case "thc":
			if (args.length == 0 && sender instanceof Player) {
				return WeatherManager.calm(sender, ((Player) sender).getWorld());
			} else if (args.length == 1) {
				return WeatherManager.calm(sender, args[0]);
			}
			sendMessage(sender, "Use command: '/help " + command.getName() + "' for more help.");
			break;
		}

		return false;
	}

	@Override
	public void onDisable() {
		HandlerList.unregisterAll(plugin);
		saveConfig();
		saveWorldsConfig();

		// finaliza as task de verificação de clima dos mundos
		Bukkit.getScheduler().cancelTasks(this);
		TimeManager.finalizeTask();

		plugin = null;
		worldsConfig = null;
		fileWorldsConfig = null;
		sendMessage(ChatColor.YELLOW + "Disabled");
	}

	public static boolean existWorld(String worldName) {
		return Bukkit.getWorld(worldName) != null;
	}

	public static void sendMessage(String message) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[TimeHandler] " + ChatColor.RESET + message);
	}

	public static void sendMessage(CommandSender sender, String message) {
		sender.sendMessage(ChatColor.GOLD + "[TimeHandler] " + ChatColor.RESET + message);
	}

	public static void sendHeaderMessage(CommandSender sender, String message) {
		sendMessage(sender,
				ChatColor.YELLOW + "---========== " + ChatColor.GREEN + message + ChatColor.YELLOW + " ==========---");
	}

	public static void broadcastMessage(String message) {
		Bukkit.broadcastMessage(ChatColor.GOLD + "[TimeHandler] " + ChatColor.RESET + message);
	}

	private static void initializeTasks() {
		for (String worldName : CommandHandler.getWorldsTimeHandler()) {
			TimeManager.initTask(worldName);
		}
	}

	private void lastVersionPlugin() {
		new UpdateChecker(plugin, resourceId).getVersionConsumer(version -> {
			if (!plugin.getDescription().getVersion().equals(version)) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p.isOp()) {
						sendMessage(p, "New update available.");
						sendMessage(p, "Current version: " + plugin.getDescription().getVersion());
						sendMessage(p, "Last version: " + ChatColor.BOLD + ChatColor.GREEN + version);
					}
				}
			}
		});
	}

	public void saveWorldsConfig() {
		try {
			worldsConfig.save(fileWorldsConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void defineDefaultWorldsConfig() {
        worldsConfig = YamlConfiguration.loadConfiguration(fileWorldsConfig);

        final InputStream inputStream = getResource(nameFileWorldsConfig);
        if (inputStream == null) {
            return;
        }

        worldsConfig.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream, Charsets.UTF_8)));
	}

}
