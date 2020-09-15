package me.reratos.timehandler.handler.completer;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.util.StringUtil;

import me.reratos.timehandler.handler.CommandHandler;
import me.reratos.timehandler.handler.completer.enums.OptionsSetEnum;
import me.reratos.timehandler.handler.completer.enums.SetEnabledEnum;
import me.reratos.timehandler.handler.completer.enums.SetThunderEnum;
import me.reratos.timehandler.handler.completer.enums.SetTimeEnum;
import me.reratos.timehandler.handler.completer.enums.SetWeatherEnum;

public class SetCompleter {
	
	public static void completer(Command command, String label, String[] params, List<String> collection) {
		
		switch (params.length) {
		case 1:
			break;
			
		case 2: // th set {WORLD}
			StringUtil.copyPartialMatches(params[1], CommandHandler.getWorldsTimeHandler(), collection);
			break;
			
		case 3:  // th set world {PROPERTY}
			StringUtil.copyPartialMatches(params[2], OptionsSetEnum.getList(), collection);
			break;
			
		case 4:  // th set world property {VALUE}
			if(OptionsSetEnum.getEnumPorValue(params[2].toLowerCase()) != null) {
				switch (OptionsSetEnum.getEnumPorValue(params[2].toLowerCase())) {
				case ENABLED:
					StringUtil.copyPartialMatches(params[3], SetEnabledEnum.getList(), collection);
					break;
				case TIME:
					StringUtil.copyPartialMatches(params[3], SetTimeEnum.getList(), collection);
					break;
					
				case TIME_FIXED:
					for(int i = 0; i <= 24000; i++) {
						collection.add(Integer.toString(i));
					}
					break;
					
				case THUNDER:
					StringUtil.copyPartialMatches(params[3], SetThunderEnum.getList(), collection);
					break;
					
				case WEATHER:
					StringUtil.copyPartialMatches(params[3], SetWeatherEnum.getList(), collection);
					break;

				default:
					break;
				}
			}
			break;

		default:
			break;
		}
		
		return;
	}
}
