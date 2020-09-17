package me.reratos.timehandler.handler.completer;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.util.StringUtil;

import me.reratos.timehandler.enums.EnabledEnum;
import me.reratos.timehandler.enums.MoonPhasesEnum;
import me.reratos.timehandler.enums.OptionsSetEnum;
import me.reratos.timehandler.enums.ThunderEnum;
import me.reratos.timehandler.enums.TimeEnum;
import me.reratos.timehandler.enums.WeatherEnum;
import me.reratos.timehandler.handler.CommandHandler;

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
			if(OptionsSetEnum.getEnumPorValue(params[2]) != null) {
				switch (OptionsSetEnum.getEnumPorValue(params[2])) {
				case ENABLED:
					StringUtil.copyPartialMatches(params[3], EnabledEnum.getList(), collection);
					break;
					
				case MOON_PHASE:
					StringUtil.copyPartialMatches(params[3], MoonPhasesEnum.getList(), collection);
					break;
					
				case TIME:
					StringUtil.copyPartialMatches(params[3], TimeEnum.getList(), collection);
					break;
					
				case TIME_FIXED:
//					for(int i = 0; i <= 24000; i += 2000) {
//						collection.add(Integer.toString(i));
//					}
					break;
					
				case THUNDER:
					StringUtil.copyPartialMatches(params[3], ThunderEnum.getList(), collection);
					break;
					
				case WEATHER:
					StringUtil.copyPartialMatches(params[3], WeatherEnum.getList(), collection);
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
