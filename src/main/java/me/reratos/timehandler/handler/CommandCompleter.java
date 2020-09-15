package me.reratos.timehandler.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import me.reratos.timehandler.handler.completer.SetCompleter;

public class CommandCompleter implements TabCompleter{
	private static final Set<String> ACTIONS = new HashSet<String>();

	@Override 
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] params) {
		List<String> completions = new ArrayList<String>();
		ACTIONS.add("help");
		ACTIONS.add("info");
		ACTIONS.add("list");
//		ACTIONS.add("remove");
		ACTIONS.add("set");
		ACTIONS.add("update");

		final Iterable<String> ac = ACTIONS;
		
		// arg2 é o comando
		// arg3[i] são os parametros
		switch (label.toLowerCase()) {
		case "timehandler":
		case "th":
			if(params.length == 1) {
				StringUtil.copyPartialMatches(params[0], ac, completions);				
			} else {
				switch (params[0].toLowerCase()) {
				case "info":
					StringUtil.copyPartialMatches(params[1], CommandHandler.getWorldsTimeHandler(), completions);
					break;
					
				case "set":
					SetCompleter.completer(command, label, params, completions);
//					StringUtil.copyPartialMatches(params[1], CommandHandler.getWorldsTimeHandler(), completions);
					break;

				default:
					break;
				}
			}
			Collections.sort(completions);
			return completions;

		default:
			break;
		}
		return null;
	}

}
