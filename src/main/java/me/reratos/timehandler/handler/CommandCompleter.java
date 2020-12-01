package me.reratos.timehandler.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import me.reratos.timehandler.handler.completer.MoonPhaseCompleter;
import me.reratos.timehandler.handler.completer.SetCompleter;
import me.reratos.timehandler.handler.completer.ShortWeatherCompleter;
import me.reratos.timehandler.utils.Constants;

public class CommandCompleter implements TabCompleter{

	@Override 
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] params) {
		List<String> completions = new ArrayList<String>();

		final Iterable<String> ac = Arrays.asList(Constants.COMMAND_TH_HELP, Constants.COMMAND_TH_INFO,
				Constants.COMMAND_TH_LIST, Constants.COMMAND_TH_SET, Constants.COMMAND_TH_UPDATE);
		
		// arg2 é o comando
		// arg3[i] são os parametros
		switch (label.toLowerCase()) {
		case Constants.COMMAND_TIMEHANDLER:
		case Constants.COMMAND_TH:
			if(params.length == 1) {
				StringUtil.copyPartialMatches(params[0], ac, completions);				
			} else {
				switch (params[0].toLowerCase()) {
				case Constants.COMMAND_TH_INFO:
					if(params.length == 2) {
						StringUtil.copyPartialMatches(params[1], CommandHandler.getWorldsTimeHandler(), completions);
					}
					break;
					
				case Constants.COMMAND_TH_SET:
					SetCompleter.completer(command, label, params, completions);
					break;

				default:
					break;
				}
			}
			Collections.sort(completions);
			return completions;

		case Constants.COMMAND_MOON_PHASE:
			MoonPhaseCompleter.completer(command, label, params, completions);
			break;
			
		case Constants.COMMAND_RAIN:
		case Constants.COMMAND_CALM:
		case Constants.COMMAND_THUNDERING:
			ShortWeatherCompleter.completer(command, label, params, completions);
			break;
			
		default:
			break;
		}
		
		Collections.sort(completions);
		return completions;
	}

}
