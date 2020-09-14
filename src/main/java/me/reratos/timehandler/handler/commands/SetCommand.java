package me.reratos.timehandler.handler.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import me.reratos.timehandler.TimeHandler;
import me.reratos.timehandler.core.TimeManager;
import me.reratos.timehandler.core.WorldManager;
import me.reratos.timehandler.enums.ThunderEnum;
import me.reratos.timehandler.enums.TimeEnum;
import me.reratos.timehandler.enums.WeatherEnum;

public class SetCommand {

	private final static String enabled = "enabled";
	private final static String weather = "weather";
	private final static String thunder = "thunder";
	private final static String time = "time";
	private final static String timeFixed = "timeFixed";

	private final static String optionDefault 	= "default";
	private final static String optionRain 		= "rain";
	private final static String optionCalm 		= "calm";
	private final static String optionNone 		= "none";
	private final static String optionAlways 	= "always";
	private final static String optionDay 		= "day";
	private final static String optionNight 	= "night";
	private final static String optionFixed 	= "fixed";
	
//	public static boolean commandSetWeather(CommandSender sender, String worldName, String property, String value) {
//		return true;
//	}
	
	public static boolean commandSetDefault(CommandSender sender, String worldName) {

		TimeHandler.config.set("configWorld." + worldName, null);
		TimeHandler.config.set("configWorld." + worldName + ".enabled", true);
		TimeHandler.config.set("configWorld." + worldName + ".weather", "default");
		TimeHandler.config.set("configWorld." + worldName + ".thunder", "default");
		TimeHandler.config.set("configWorld." + worldName + ".time", "default");
		TimeHandler.config.set("configWorld." + worldName + ".timeFixed", 1000);
		
		TimeHandler.plugin.saveConfig();
		
		TimeHandler.sendMessage(sender, ChatColor.YELLOW + "Configuração padrão criada para o mundo: " + 
				ChatColor.GREEN + worldName);
		TimeManager.initTask(Bukkit.getWorld(worldName));
		return true;
	}
	
	public static boolean commandSetBase(CommandSender sender, WorldManager worldManager, String property, String value) {
		String worldName = worldManager.getWorld().getName();
		
//		weather: default/rain/calm
//		thunder: default/none/always
//		time: 	 default/day/night/fixed
//		timeFixed: 	 0 - 23999
		switch (property) {
			case enabled:
				switch (value) {
				case "true":
				case "false":
					TimeHandler.config.set("configWorld." + worldName + "." + property, Boolean.parseBoolean(value));
					worldManager.setEnabled(Boolean.parseBoolean(value));
					break;
				default:
					TimeHandler.sendMessage(sender, "Valor '" + ChatColor.RED + value + ChatColor.RESET + 
							"' para a propriedade " + ChatColor.AQUA + property + ChatColor.RESET + " é invalido.");
					return false;
				}
			break;
			
			case weather:
				switch (value) {
					case optionDefault:
					case optionRain:
					case optionCalm:
						TimeHandler.config.set("configWorld." + worldName + "." + property, value);
						worldManager.setWeather(WeatherEnum.getEnumPorValue(value));
						break;
					default:
						TimeHandler.sendMessage(sender, "Valor '" + ChatColor.RED + value + ChatColor.RESET + 
								"' para a propriedade " + ChatColor.AQUA + property + ChatColor.RESET + " é invalido.");
						return false;
				}
				break;
				
			case thunder:
				switch (value) {
					case optionDefault:
					case optionNone:
					case optionAlways:
						TimeHandler.config.set("configWorld." + worldName + "." + property, value);
						worldManager.setThunder(ThunderEnum.getEnumPorValue(value));
						break;
					default:
						TimeHandler.sendMessage(sender, "Valor '" + ChatColor.RED + value + ChatColor.RESET + 
								"' para a propriedade " + ChatColor.AQUA + property + ChatColor.RESET + " é invalido.");
						return false;
				}
				break;
				
			case time:
				switch (value) {
					case optionDefault:
					case optionDay:
					case optionNight:
					case optionFixed:
						TimeHandler.config.set("configWorld." + worldName + "." + property, value);
						worldManager.setTime(TimeEnum.getEnumPorValue(value));
						break;
					default:
						TimeHandler.sendMessage(sender, "Valor '" + ChatColor.RED + value + ChatColor.RESET + 
								"' para a propriedade " + ChatColor.AQUA + property + ChatColor.RESET + " é invalido.");
						return false;
				}
				break;
				
			case timeFixed:
				try {
					int tempo = Integer.parseInt(value);
					if(tempo >= 0 && tempo < 24000) {
						TimeHandler.config.set("configWorld." + worldName + "." + property, tempo);
						worldManager.setTimeFixed(tempo);
					} else {
						throw new NumberFormatException();
					}
					
				} catch (NumberFormatException e) {
					TimeHandler.sendMessage(sender, "Valor '" + ChatColor.RED + value + 
							ChatColor.RESET + "' não é um tempo válido(0 - 23999) para a propriedade " + 
							ChatColor.AQUA + property + ChatColor.RESET + "");
					return false;
				}
				break;
	
			default:
				return false;
		}

		TimeHandler.sendMessage(sender, "Alterado a propriedade '" + ChatColor.AQUA + property + ChatColor.RESET + 
				"' para o valor: '" + ChatColor.LIGHT_PURPLE + value + ChatColor.RESET + "'");
		TimeHandler.plugin.saveConfig();
		
		return true;
	}
	
}
