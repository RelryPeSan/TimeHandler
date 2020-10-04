package me.reratos.timehandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import com.google.common.base.Charsets;

import me.reratos.timehandler.core.TimeManager;
import me.reratos.timehandler.core.WeatherManager;
import me.reratos.timehandler.enums.MoonPhasesEnum;
import me.reratos.timehandler.events.WorldListener;
import me.reratos.timehandler.handler.CommandCompleter;
import me.reratos.timehandler.handler.CommandHandler;
import me.reratos.timehandler.utils.Constants;
import me.reratos.timehandler.utils.ConstantsConfig;
import me.reratos.timehandler.utils.LocaleLoader;
import me.reratos.timehandler.utils.Messages;
import me.reratos.timehandler.utils.UpdateChecker;

public class TimeHandler extends JavaPlugin {

	public static TimeHandler plugin;
	
	public static FileConfiguration config;
	public static YamlConfiguration worldsConfig;
	public File fileWorldsConfig;
	
	// Construtores para teste
    public TimeHandler() {
        super();
    }

    protected TimeHandler(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }
	
	@Override
	public void onEnable() {
		plugin = this;
		fileWorldsConfig = new File(getDataFolder(), Constants.FILE_NAME_WORLDS_CONFIG_YML);
		
		if(!fileWorldsConfig.exists()) {
			defineDefaultWorldsConfig();
			saveWorldsConfig();
		} else {
			worldsConfig = YamlConfiguration.loadConfiguration(fileWorldsConfig);
		}
		
		saveDefaultConfig();
        config = getConfig().options().copyDefaults(true).configuration();
        saveConfig();
        
        LocaleLoader.initialize();

		if(config.getBoolean(ConstantsConfig.CHECK_UPDATE_PLUGIN)) {
			CommandHandler.update(Constants.RESOURCE_ID);
			lastVersionPlugin();
		}

		Bukkit.getPluginManager().registerEvents(new WorldListener(), this);

		initializeTabCompleter();
		
		initializeTasks();
		
		sendMessage(LocaleLoader.getString(Messages.TIMEHANDLER_ENABLED));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		switch (command.getName().toLowerCase()) {
		case Constants.COMMAND_TIMEHANDLER:
		case Constants.COMMAND_TH:
//			if (sender.hasPermission(Constants.PERMISSION_TIMEHANDLER_USE)) {

				if (args.length == 0) {
					sendMessageLogo(sender, LocaleLoader.getString(Messages.HELP_DEFAULT_TH));
					return false;
				}

				switch (args[0].toLowerCase()) {
				case Constants.COMMAND_TH_HELP:
					if (args.length == 1) {
						sendHeaderMessage(sender, args[0].toUpperCase());
						return CommandHandler.help(sender);
					}
					command.setUsage(
							Bukkit.getPluginCommand(command.getName() + " " + args[0].toLowerCase()).getUsage());
					sendMessageLogo(sender, LocaleLoader.getString(Messages.HELP_DEFAULT_TH_FOR_COMMAND, args[0].toLowerCase()));
					return false;

				case Constants.COMMAND_TH_INFO:
					if(args.length == 1 && sender instanceof Player) {
						sendHeaderMessage(sender, args[0].toUpperCase());
						return CommandHandler.info(sender, ((Player)sender).getWorld());
					} else if (args.length == 2) {
						sendHeaderMessage(sender, args[0].toUpperCase());
						return CommandHandler.info(sender, args[1]);
					}
					command.setUsage(Bukkit.getPluginCommand(command.getName() + " " + args[0].toLowerCase()).getUsage());
					sendMessageLogo(sender, LocaleLoader.getString(Messages.HELP_DEFAULT_TH_FOR_COMMAND, args[0].toLowerCase()));
					return false;

				case Constants.COMMAND_TH_LIST:
					if (args.length != 1) {
						command.setUsage(
								Bukkit.getPluginCommand(command.getName() + " " + args[0].toLowerCase()).getUsage());
						sendMessageLogo(sender, LocaleLoader.getString(Messages.HELP_DEFAULT_TH_FOR_COMMAND, args[0].toLowerCase()));
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

				case Constants.COMMAND_TH_SET:
					if (args.length == 2) {
						sendHeaderMessage(sender, args[0].toUpperCase());
						return CommandHandler.set(sender, args[1]);
					} else if (args.length == 4) {
						sendHeaderMessage(sender, args[0].toUpperCase());
						return CommandHandler.set(sender, args[1], args[2], args[3]);
					}
					command.setUsage(
							Bukkit.getPluginCommand(command.getName() + " " + args[0].toLowerCase()).getUsage());
					sendMessageLogo(sender, LocaleLoader.getString(Messages.HELP_DEFAULT_TH_FOR_COMMAND, args[0].toLowerCase()));
					return false;

				case Constants.COMMAND_TH_UPDATE:
					sendHeaderMessage(sender, args[0].toUpperCase());
					return CommandHandler.update(sender, Constants.RESOURCE_ID);

				default:
					sendMessageLogo(sender, LocaleLoader.getString(Messages.HELP_DEFAULT_TH));
					return false;
				}

//			} else {
//				sendMessageLogo(sender, LocaleLoader.getString(Messages.NOT_PERMISSION_DEFAULT));
//			}
//			break;

		case Constants.COMMAND_DAY:
		case Constants.COMMAND_THD:
			if (args.length == 0 && sender instanceof Player) {
				return TimeManager.day(sender, ((Player) sender).getWorld());
			} else if (args.length == 1) {
				return TimeManager.day(sender, args[0]);
			}
			sendMessageLogo(sender, LocaleLoader.getString(Messages.HELP_DEFAULT_FOR_COMMAND, command.getName()));
			break;

		case Constants.COMMAND_NIGHT:
		case Constants.COMMAND_THN:
			if (args.length == 0 && sender instanceof Player) {
				return TimeManager.night(sender, ((Player) sender).getWorld());
			} else if (args.length == 1) {
				return TimeManager.night(sender, args[0]);
			}
			sendMessageLogo(sender, LocaleLoader.getString(Messages.HELP_DEFAULT_FOR_COMMAND, command.getName()));
			break;

		case Constants.COMMAND_MOON_PHASE:
		case Constants.COMMAND_THMP:
			if (args.length == 1 && sender instanceof Player) {
				return TimeManager.moonPhase(sender, MoonPhasesEnum.getEnumPorValue(args[0]),
						((Player) sender).getWorld());
			} else if (args.length == 2) {
				return TimeManager.moonPhase(sender, MoonPhasesEnum.getEnumPorValue(args[0]), args[1]);
			}
			sendMessageLogo(sender, LocaleLoader.getString(Messages.HELP_DEFAULT_FOR_COMMAND, command.getName()));
			break;

		case Constants.COMMAND_RAIN:
		case Constants.COMMAND_THR:
			if (args.length == 0 && sender instanceof Player) {
				return WeatherManager.rain(sender, ((Player) sender).getWorld());
			} else if (args.length == 1) {
				return WeatherManager.rain(sender, args[0]);
			} else if (args.length == 2) {
				return WeatherManager.rain(sender, args[0], args[1]);
			}
			sendMessageLogo(sender, LocaleLoader.getString(Messages.HELP_DEFAULT_FOR_COMMAND, command.getName()));
			break;

		case Constants.COMMAND_THUNDERING:
		case Constants.COMMAND_THT:
			if (args.length == 0 && sender instanceof Player) {
				return WeatherManager.thundering(sender, ((Player) sender).getWorld());
			} else if (args.length == 1) {
				return WeatherManager.thundering(sender, args[0]);
			} else if (args.length == 2) {
				return WeatherManager.thundering(sender, args[0], args[1]);
			}
			sendMessageLogo(sender, LocaleLoader.getString(Messages.HELP_DEFAULT_FOR_COMMAND, command.getName()));
			break;
			
		case Constants.COMMAND_CALM:
		case Constants.COMMAND_THC:
			if (args.length == 0 && sender instanceof Player) {
				return WeatherManager.calm(sender, ((Player) sender).getWorld());
			} else if (args.length == 1) {
				return WeatherManager.calm(sender, args[0]);
			} else if (args.length == 2) {
				return WeatherManager.calm(sender, args[0], args[1]);
			}
			sendMessageLogo(sender, LocaleLoader.getString(Messages.HELP_DEFAULT_FOR_COMMAND, command.getName()));
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
		sendMessage(LocaleLoader.getString(Messages.TIMEHANDLER_DISABLED));
	}

	public static boolean existWorld(String worldName) {
		return Bukkit.getWorld(worldName) != null;
	}

	public static void sendMessage(String message) {
		Bukkit.getConsoleSender().sendMessage(LocaleLoader.getString(Messages.TIMEHANDLER_LOGO, message));
	}

	public static void sendMessageLogo(CommandSender sender, String message) {
		sender.sendMessage(LocaleLoader.getString(Messages.TIMEHANDLER_LOGO, message));
	}

	public static void sendHeaderMessage(CommandSender sender, String message) {
		sendMessageLogo(sender, LocaleLoader.getString(Messages.TIMEHANDLER_HEADER, message));
	}

	public static void broadcastMessage(String message) {
		Bukkit.broadcastMessage(LocaleLoader.getString(Messages.TIMEHANDLER_LOGO, message));
	}
	
	private void initializeTabCompleter() {
		CommandCompleter tabCompletion = new CommandCompleter();

		getCommand(Constants.COMMAND_TIMEHANDLER).setTabCompleter(tabCompletion);
		getCommand(Constants.COMMAND_MOON_PHASE).setTabCompleter(tabCompletion);
		getCommand(Constants.COMMAND_RAIN).setTabCompleter(tabCompletion);
		getCommand(Constants.COMMAND_CALM).setTabCompleter(tabCompletion);
		getCommand(Constants.COMMAND_THUNDERING).setTabCompleter(tabCompletion);
		
//		try {
//			getCommand(Constants.COMMAND_TH).setTabCompleter(tabCompletion);
//		} catch (Exception e) {
//		}
	}

	private static void initializeTasks() {
		for (String worldName : CommandHandler.getWorldsTimeHandler()) {
			TimeManager.initTask(worldName);
		}
	}

	private void lastVersionPlugin() {
		new UpdateChecker(plugin, Constants.RESOURCE_ID).getVersionConsumer(version -> {
			if (!plugin.getDescription().getVersion().equals(version)) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p.isOp()) {
						sendMessageLogo(p, LocaleLoader.getString(Messages.COMMAND_UPDATE_INIT_FOR_OP, 
								plugin.getDescription().getVersion(), version));
						p.sendMessage(LocaleLoader.getString(Messages.ACCESS_URL_PLUGIN, Constants.URL_PLUGIN));
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

        final InputStream inputStream = getResource(Constants.FILE_NAME_WORLDS_CONFIG_YML);
        if (inputStream == null) {
            return;
        }

        worldsConfig.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream, Charsets.UTF_8)));
	}
	
}
