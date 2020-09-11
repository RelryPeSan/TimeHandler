package me.reratos.timehandler.handler;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

public class TabCompletion implements TabCompleter{
	private static final List<String> ACTIONS = new ArrayList<String>();

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		final List<String> completions = new ArrayList<String>();
		ACTIONS.add("info");
		ACTIONS.add("help");
		ACTIONS.add("list");
		ACTIONS.add("remove");
		ACTIONS.add("set");
		final Iterable<String> it = ACTIONS;
		
		// arg2 é o comando
		// arg3[i] são os parametros
		switch (arg2.toLowerCase()) {
		case "timehandler":
		case "th":
			if(arg3.length == 1) {
				StringUtil.copyPartialMatches(arg3[0], it, completions);				
			} else if(arg3.length == 2) {
				if(arg3[0].equalsIgnoreCase("info")) {
					StringUtil.copyPartialMatches(arg3[1], CommandHandler.getWorldsTimeHandler(), completions);
				}
			}
			return completions;

		default:
			break;
		}
		return null;
	}

}
